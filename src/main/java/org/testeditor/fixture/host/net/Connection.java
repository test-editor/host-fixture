/*******************************************************************************
 * Copyright (c) 2012 - 2017 Signal Iduna Corporation and others.
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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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

    public Connection() {
        // Default Constructor
    }

    /**
     * This Constructor is for test purposes
     */
    public Connection(Process s3270Process, String hostname, BufferedReader in, PrintWriter out) {
        this.s3270Process = s3270Process;
        this.hostname = hostname;
        this.in = in;
        this.out = out;
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
     * @return true if disconnected successful, false otherwise.
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
    public Result doCommand(final String command) {
        assertProcessAvailable();
        try {
            out.println(command);
            out.flush();
            logger.debug(
                    "************************************************************************************************");
            logger.debug("---> Command sent: '{}'", command);
            if (command.equals("ascii")) {
                logger.debug(
                        "-------------- 0----+----1----+----2----+----3----+----4----+----5----+----6----+----7----+----8");
            }
            List<String> lines = readOutput();
            int size = lines.size();
            if (size > 1) {
                Result result = new Result(lines.subList(0, size - 2), lines.get(size - 2), lines.get(size - 1));
                logger.debug(
                        "************************************************************************************************");
                return result;
            } else {
                throw new RuntimeException("no status received in command: " + command);
            }
        } catch (final IOException ex) {
            throw new RuntimeException("IOException during command: " + command, ex);
        }
    }

    // TODO this should be private
    public List<String> readOutput() throws IOException {
        List<String> lines = new ArrayList<String>();
        int lineNumber = 0;
        while (true) {
            String line = in.readLine();
            if (line == null) {
                logger.error("Line is null");
                // if we get here, it's a more obscure error
                throw new RuntimeException("s3270 process not responding");
            }
            String number = createNumber(lineNumber);
            logger.debug("<--- {} '{}'", number, line);
            lines.add(line);
            lineNumber++;
            if (line.equals("ok")) {
                break;
            }
        }
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
        return doCommand("");
    }

    private void assertProcessAvailable() {
        if (s3270Process == null || in == null || out == null) {
            throw new RuntimeException("No s3270 Process available");
        } // everything is fine, do nothing !
    }

}
