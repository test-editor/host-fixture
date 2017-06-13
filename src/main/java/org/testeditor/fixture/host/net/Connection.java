package org.testeditor.fixture.host.net;

import org.testeditor.fixture.host.Locator;
import org.testeditor.fixture.host.s3270.Result;
import org.testeditor.fixture.host.s3270.Status;
import org.testeditor.fixture.host.s3270.options.CharacterSet;
import org.testeditor.fixture.host.s3270.options.TerminalMode;
import org.testeditor.fixture.host.s3270.options.TerminalType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Connection {

    private static final Logger logger = LoggerFactory.getLogger(Connection.class);

    private String hostname;

    /**
     * The process that does the actual communication with the host.
     */
    private Process s3270Process = null;

    /**
     * Used to send commands to the s3270 process.
     */
    private PrintWriter out = null;

    /**
     * Used for reading input from the s3270 process.
     */
    private BufferedReader in = null;

    /**
     * Connects to a mainframe. The s3270 subprocess (which does the
     * communication with the mainframe) is immediately started and connected to
     * the target mainframe.
     *
     * @param s3270Path
     *            the path to your x3270 installation of the s3270 or ws3270.exe
     *            application.
     * @param hostname
     *            the name of the host to connect to
     * @param port
     *            the port of the mainframe to connnect to
     * @param type
     *            the type e.g 3278 or 23279 see {@link TerminalType}
     * @param mode
     *            this is number from 2 to 5 see {@link TerminalMode}
     *
     */
    public Connection connect(String s3270Path, String hostname, int port, TerminalType type, TerminalMode mode,
            CharacterSet charSet) {
        this.hostname = hostname;
        String commandLine = String.format("%s -charset %s -model %s-%d %s:%d -utf8", s3270Path, charSet.getCharSet(),
                type.getType(), mode.getMode(), hostname, port);
        try {
            logger.debug("executing " + commandLine);
            s3270Process = Runtime.getRuntime().exec(commandLine);

            out = new PrintWriter(new OutputStreamWriter(s3270Process.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(s3270Process.getInputStream()));

            return this;

        } catch (final IOException ex) {
            throw new RuntimeException("IO Exception while starting " + s3270Path, ex);
        }

    }

    /**
     * Disconnect from mainframe. Destroys all opened Input- and OutputStreams.
     * 
     * @return true if disconnected succesfull, false otherwise.
     */
    public boolean disconnect() {
        assertProcessAvailable();
        doQuit();
        stop3270Process();
        try {
            s3270Process.waitFor(5, TimeUnit.SECONDS);
        } catch (final InterruptedException ex) {
            logger.error("Something went wrong during termination of s2370 process");
        }
        try {
            in.close();
        } catch (final IOException ex) {
            logger.error("Something went wrong during closing InputStreamreader of s2370 process");
        }
        cleanup();
        boolean success = !isConnected();
        if (success) {
            logger.info("Disconnected successfully from host : {} ", hostname);
        } else {
            logger.info("Discoonection failed");
        }
        return success;
    }

    /**
     *
     * @return true if connection is established to mainframe through
     *         s3270/ws3270 emulation and process is still running, false
     *         otherwise.
     */
    public boolean isConnected() {
        if (s3270Process == null || in == null || out == null) {
            return false;
        } else {
            Result r = doCommand("");
            createStatus(r);
            if (r.getStatusString().matches(". . . C.*")) {
                return true;
            } else {
                doQuit();
                s3270Process.destroy();
                s3270Process = null;
                try {
                    in.close();
                } catch (final IOException ex) {
                    logger.error("Something went wrong during closing InputStreamreader of s2370 process");
                }
                cleanup();
                boolean success = !isConnected();
                if (success) {
                    logger.info("Disconnected successfully from host : {} ", hostname);
                } else {
                    logger.info("Discoonection failed");
                }
                return success;
            }
        }
    }

    /**
     * reset all opened streams
     */
    private void cleanup() {
        out.close();
        in = null;
        out = null;
        s3270Process = null;
    }

    /**
     * perform a quit statement
     */
    private void doQuit() {
        out.println("quit");
        out.flush();
    }

    /**
     * stop the s3270 process
     */
    private void stop3270Process() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    if (s3270Process != null) {
                        s3270Process.destroy(); // May run into TimeOut
                    }
                } catch (final InterruptedException ex) {
                    if (s3270Process != null) {
                        s3270Process.destroy();
                    }
                }
            }
        }).start();
    }

    private Status createStatus(Result r) {
        String statusCharacters = r.getStatusString();
        Status status = new Status(statusCharacters);
        r.setStatus(status);
        logger.debug(r.getStatus().toString());
        return status;
    }

    /**
     * A s3270 command will be executed with this method. The whole
     * communication with s3270 will be accessed through this method.
     */
    public Result doCommand(final String command) {
        assertProcessAvailable();
        try {
            out.println(command);
            out.flush();
            logger.debug("*****************************************************************************************");
            logger.debug("---> Command sent: \"{}\"", command);
            List<String> lines = readOutput();
            int size = lines.size();
            if (size > 0) {
                Result result = new Result(lines.subList(0, size - 1), lines.get(size - 1));
                logger.debug(
                        "*****************************************************************************************");
                return result;
            } else {
                throw new RuntimeException("no status received in command: " + command);
            }
        } catch (final IOException ex) {
            throw new RuntimeException("IOException during command: " + command, ex);
        }
    }

    private List<String> readOutput() throws IOException {
        List<String> lines = new ArrayList<String>();
        while (true) {
            String line = in.readLine();
            if (line == null) {
                logger.error("Line is null");
                // if we get here, it's a more obscure error
                throw new RuntimeException("s3270 process not responding");
            }
            logger.debug("<--- {}", line);
            if (line.equals("ok")) {
                break;
            }
            lines.add(line);
        }
        return lines;
    }

    public Status getStatus() {
        return requestStatus();
    }

    private void assertProcessAvailable() {
        if (s3270Process == null) {
            throw new RuntimeException("No Connection available!");
        }
    }

    private Status requestStatus() {
        if (s3270Process == null || in == null || out == null) {
            return null;
        } else {
            return createStatus(doCommand(""));
        }
    }

    public void typeInto(String value, Locator locator) {
        // TODO is coming next ;O)

    }

}
