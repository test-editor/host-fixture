/*******************************************************************************
 * Copyright (c) 2012 - 2018 Signal Iduna Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Signal Iduna Corporation - initial API and implementation
 * akquinet AG
 * itemis AG
 *******************************************************************************/
package org.testeditor.fixture.host.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testeditor.fixture.core.FixtureException;
import org.testeditor.fixture.host.logging.Logging;
import org.testeditor.fixture.host.s3270.Result;
import org.testeditor.fixture.host.s3270.Status;
import org.testeditor.fixture.host.s3270.actions.Command;
import org.testeditor.fixture.host.s3270.options.CharacterSet;
import org.testeditor.fixture.host.s3270.options.TerminalMode;
import org.testeditor.fixture.host.s3270.options.TerminalType;
import org.testeditor.fixture.host.screen.Offset;

import com.github.jknack.handlebars.Options.Buffer;

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

    private Offset offset;

    public Connection() {
        // Default Constructor
    }

    /**
     * This Constructor is for test purposes
     */
    public Connection(Process s3270Process, String hostname, BufferedReader in, PrintWriter out, Offset offset) {
        this.s3270Process = s3270Process;
        this.hostname = hostname;
        this.in = in;
        this.out = out;
        this.offset = offset;
    }

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
     * @param charSet 
     *            the character set "german-euro" means character set ISO8859-15.is used 
     *            For further information about character sets to use, see <a href="http://x3270.bgp.nu/Unix/s3270-man.html">Character Set for X3270 </a>
     * @param offset 
     *            the offset to the original host screen. 
     * @param veryfyCertification
     *           
     * @return
     */
    public Connection connect(String s3270Path, String hostname, int port, TerminalType type, TerminalMode mode,
            CharacterSet charSet, Offset offset, boolean verifyCertificate) {
        this.hostname = hostname;
        this.offset = offset;
        String commandLine = String.format("%s -charset %s -model %s-%d %s:%d -utf8", s3270Path, charSet.getCharSet(),
                type.getType(), mode.getMode(), hostname, port);
        commandLine = expandCommandLineForCertificate(verifyCertificate, commandLine);
        
        try {
            logger.trace("executing '" + commandLine + "'");
            s3270Process = Runtime.getRuntime().exec(commandLine);

            out = new PrintWriter(new OutputStreamWriter(s3270Process.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(s3270Process.getInputStream()));

            return this;

        } catch (final IOException ex) {
            throw new RuntimeException("IO Exception while starting " + s3270Path, ex);
        }

    }

    protected String expandCommandLineForCertificate(boolean verifyCertificate, String commandLine) {
        // This part is only necessary when host certification should be used 
        if (verifyCertificate) {
            StringBuffer buffer = new StringBuffer(commandLine);
            final String CADIR = System.getProperty("HOSTFIXTURE_CADIR"); 
            final String CAFILE = System.getProperty("HOSTFIXTURE_CAFILE");
            final String CERTFILETYPE = System.getProperty("HOSTFIXTURE_CERTFILETYPE");
            final String CERTFILE = System.getProperty("HOSTFIXTURE_CERTFILE");
            final String CHAINFILE = System.getProperty("HOSTFIXTURE_CHAINFILE");
            final String CLIENTCERT = System.getProperty("HOSTFIXTURE_CLIENTCERT");
            final String KEYFILE = System.getProperty("HOSTFIXTURE_KEYFILE");
            final String KEYFILETYPE = System.getProperty("HOSTFIXTURE_KEYFILETYPE");
            final String KEYPASSWD = System.getProperty("HOSTFIXTURE_KEYPASSWD");
            
            if (CADIR != null && CADIR.length() > 0) {
                buffer.append(" -cadir" + CADIR);
            } else if (CAFILE != null && CAFILE.length() > 0) {
                buffer.append(" -cafile" + CAFILE);
            } else if (CERTFILETYPE != null && CERTFILETYPE.length() > 0) {
                buffer.append(" -certfiletype" + CERTFILETYPE);
            } else if (CERTFILE != null && CERTFILE.length() > 0) {
                buffer.append(" -certfile" + CERTFILE);
            } else if (CHAINFILE != null && CHAINFILE.length() > 0) {
                buffer.append(" -chainfile" + CHAINFILE);
            } else if (CLIENTCERT != null && CLIENTCERT.length() > 0) {
                buffer.append(" -clientcert" + CLIENTCERT);
            } else if (KEYFILE != null && KEYFILE.length() > 0) {
                buffer.append(" -keyfile" + KEYFILE);
            } else if (KEYFILETYPE != null && KEYFILETYPE.length() > 0) {
                buffer.append(" -keyfiletype" + KEYFILETYPE);
            } else if (KEYPASSWD != null && KEYPASSWD.length() > 0) {
                buffer.append(" -keypasswd" + KEYPASSWD);
            } 
              return buffer.toString();
            }else {
            return commandLine + " -noverifycert";
        }
    }

    /**
     * Disconnect from mainframe. Destroys all opened Input- and OutputStreams.
     * 
     * @return true if disconnected successful, false otherwise.
     * @throws FixtureException 
     */
    public boolean disconnect() throws FixtureException {
        assertProcessAvailable();
        doQuit();
        stop3270Process();
        try {
            s3270Process.waitFor(5, TimeUnit.SECONDS);
        } catch (final InterruptedException ex) {
            logger.warn("Something went wrong during termination of s3270 process");
        }
        try {
            in.close();
        } catch (final IOException ex) {
            logger.warn("Something went wrong during closing InputStreamreader of s3270 process");
        }
        cleanup();
        boolean success = !isConnected();
        if (success) {
            logger.debug("Disconnected successfully from host : {} ", hostname);
        } else {
            throw new FixtureException("Disconnection failed from host '" + hostname + "'", FixtureException.keyValues("hostname", hostname) );
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
            // when process is available check if commands are possible :O)
            Result r = doEmptyCommand();
            r.logStatus();
            if (r.getStatusString().matches(". . . C.*")) {
                return true;
            } else {
                return false;
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

    /**
     * A s3270 command will be executed with this method. The whole
     * communication with s3270 will be accessed through this method.
     */
    public Result doCommand(final String commandString, Command command) {
        assertProcessAvailable();
        try {
            out.println(commandString);
            out.flush();
            logger.trace(
                    "************************************************************************************************");
            logger.trace("---> Command sent: '{}'", command.getActionForLog());
            if (commandString.equals("ascii")) {
                logger.trace(
                        "--------------0----+----1----+----2----+----3----+----4----+----5----+----6----+----7----+----8");
            }
            List<String> lines = readOutput();
            int size = lines.size();
            if (size > 1) {
                Result result = new Result(lines.subList(0, size - 2), lines.get(size - 2), lines.get(size - 1),
                        offset);
                logger.trace(
                        "************************************************************************************************");
                return result;
            } else {
                throw new RuntimeException("no status received in command: " + commandString);
            }
        } catch (final IOException ex) {
            throw new RuntimeException("IOException during command: " + commandString, ex);
        }
    }

    // TODO this should be private
    public List<String> readOutput() throws IOException {
        List<String> lines = new ArrayList<String>();
        ArrayList<String> linesForLogging = new ArrayList<String>();
        int lineNumber = 1;
        while (true) {
            String line = in.readLine();
            if (line == null) {
                logger.error("Line is null");
                // if we get here, it's a more obscure error
                throw new RuntimeException("s3270 process not responding");
            }
            String number = createNumber(lineNumber);
            linesForLogging.add("<--- " + number + " '" + line + "'");
            lines.add(line);
            lineNumber++;
            if (line.equals("ok") || line.equals("error")) {
                break;
            }
        }
        Logging.logOutput(linesForLogging, this.offset.getOffsetRow(), this.offset.getOffsetColumn());
        return lines;
    }

    private String createNumber(int lineNumber) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMAN);
        ((DecimalFormat) numberFormat).applyPattern("00");
        return numberFormat.format(new Integer(lineNumber));
    }

    /**
     * 
     * @return throws RuntimeException if no s3270 process is available,
     *         otherwise the status of the Connection
     * @throws RuntimeException
     *             when no s3270 process is available
     */
    public Status getStatus() throws RuntimeException {
        assertProcessAvailable();
        Result r = doEmptyCommand();
        r.logStatus();
        return r.getStatus();
    }

    /**
     * Special command for receiving just the status
     * 
     * @return Result of the command.
     */
    private Result doEmptyCommand() {
        Command command = new Command("", "");
        return doCommand("", command);
    }

    private void assertProcessAvailable() {
        if (s3270Process == null || in == null || out == null) {
            throw new RuntimeException("No s3270 Process available");
        } // everything is fine, do nothing !
    }

}
