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
package org.testeditor.fixture.host;

import org.testeditor.fixture.core.interaction.FixtureMethod;
import org.testeditor.fixture.host.locators.LocatorByStart;
import org.testeditor.fixture.host.locators.LocatorByStartStop;
import org.testeditor.fixture.host.locators.LocatorByWidth;
import org.testeditor.fixture.host.locators.LocatorStrategy;
import org.testeditor.fixture.host.net.Connection;
import org.testeditor.fixture.host.s3270.Result;
import org.testeditor.fixture.host.s3270.Status;
import org.testeditor.fixture.host.s3270.actions.ControlCommand;
import org.testeditor.fixture.host.s3270.options.CharacterSet;
import org.testeditor.fixture.host.s3270.options.TerminalMode;
import org.testeditor.fixture.host.s3270.options.TerminalType;
import org.testeditor.fixture.host.s3270.statusformat.FieldProtection;
import org.testeditor.fixture.host.s3270.statusformat.ScreenFormatting;
import org.testeditor.fixture.host.screen.Field;
import org.testeditor.fixture.host.screen.TerminalScreen;
import org.testeditor.fixture.host.util.LineReader;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HostDriverFixture provides convenience methods for automating mainframe
 * functional tests. It consists of an API to deal with mainframe fields in
 * screens. As a test driver, we use the X3270 framework to communicate with the
 * mainframe which should be tested.
 *
 * @see <a href="http://x3270.bgp.nu/">http://x3270.bgp.nu/</a>
 */
public class HostDriverFixture {
    private static final Logger logger = LoggerFactory.getLogger(HostDriverFixture.class);
    private Connection connection;
    private TerminalMode mode;

    public HostDriverFixture() {
        this.connection = new Connection();
    }

    protected HostDriverFixture(Connection connection) {
        this.connection = connection;
    }

    /**
     * The Connection is responsible for the subprocess of the s3270/ws3270
     * application. The whole communication will be done through this
     * subprocess.
     *
     * @param s3270Path
     *            The path to the s3270/ws3270 application.
     * @param hostname
     *            The hostname to be connected to.
     * @param port
     *            The port of the host to be connected to.
     */
    @FixtureMethod
    public boolean connect(String s3270Path, String hostname, int port) {
        logger.info("Host-Fixture connecting ...");
        TerminalType type = TerminalType.TYPE_3279;
        this.mode = TerminalMode.MODE_24x80;
        CharacterSet charSet = CharacterSet.CHAR_GERMAN_EURO;
        connection.connect(s3270Path, hostname, port, type, this.mode, charSet);
        if (connection.isConnected()) {
            logger.info("successfully connected to host='{}', port='{}'", hostname, port);
            return true;
        } else {
            logger.info("The connection to host '" + hostname + "' on port '" + port + "' could not be established.");
            return false;
        }
    }

    /**
     * Disconnects from host.
     * {@link org.testeditor.fixture.host.net.Connection#disconnect()}
     *
     */
    @FixtureMethod
    public boolean disconnect() {
        logger.info("Disconnecting ...");
        return connection.disconnect();
    }

    /**
     * Provides the Status Objekt which will be returned when an input or action
     * is executed.
     *
     * @return {@link org.testeditor.fixture.host.s3270.Status}
     */
    @FixtureMethod
    public Status getStatus() {
        logger.info("get Status ...");
        return connection.getStatus();
    }

    /**
     * Provides a possibility to type a value into a specified field through the
     * parameters row and column.
     * <p>
     * Attention! The input field has to be unprotected and not hidden. If they
     * are, the s3270 emulation will lock further actions.
     *
     * @param value
     *            the value to be typed into the input field of a mainframe
     *            window.
     * @param row
     *            the row of the input field
     * @param col
     *            the column of the input field.
     */
    private void typeAtPosition(String value, int row, int col) {
        setCursorPosition(row, col);
        waiting(100);
        connection.doCommand("String(\"" + value + "\")");
        connection.doCommand("ascii"); // just to see if typed in successfully.
    }

    /**
     * 
     * @param elementLocator
     *            A String representation of a point to set the cursor on. <br>
     *            Example: elementLocator = "1;2" (for the
     *            {@link LocatorStrategy#START}<br>
     *            1 is the ROW representation and 2 is the COLUMN
     *            representation.
     * @param locatorType
     *            see {@link LocatorStrategy}
     * @param value
     *            The String which should be entered at the given position under
     *            elementLocator
     */
    @FixtureMethod
    public void typeAt(String elementLocator, LocatorStrategy locatorType, String value) {
        Result result = moveCursor(elementLocator, locatorType);
        result.logStatus();
        Status status = result.getStatus();
        if (status.getFieldProtection() == FieldProtection.UNPROTECTED) {
            waiting(100);
            connection.doCommand("String(\"" + value + "\")");
            // just to see if typed in successfully.
            connection.doCommand("ascii");
        } else {
            throw new RuntimeException("The field at the position x = '" + status.getCurrentCursorColumn()
                    + "' and y = '" + status.getCurrentCursorRow() + "' is protected.");
        }
    }

    /**
     * Provides the possibility to access some special commands defined under
     * {@link ControlCommand}
     * 
     * 
     * @param value
     *            a ControlCommand like {@link ControllCommand#DELETE},
     *            {@link ControllCommand#TAB}, {@link ControllCommand#ENTER}
     *            etc.
     * 
     * @see ControlCommand
     */
    @FixtureMethod
    public void sendControlCommand(ControlCommand value) {
        waiting(100);
        connection.doCommand(value.getCommand());
        connection.doCommand("ascii"); // just to see if typed in successfully.
    }

    /**
     * Just for testing can be deleted when Enums working in Test-Editor .
     * 
     */
    @FixtureMethod
    public void send(String value) {
        waiting(100);
        connection.doCommand(value);
        connection.doCommand("ascii"); // just to see if typed in successfully.
    }

    /**
     * Sends a (see {@link ControlCommand}) BACKTAB command to the mainframe
     * screen .
     */
    @FixtureMethod
    public void sendBacktab() {
        sendEmulationCommand(ControlCommand.BACKTAB.getCommand());
    }

    /**
     * Sends a (see {@link ControlCommand}) CLEAR command to the mainframe
     * screen .
     */
    @FixtureMethod
    public void sendClear() {
        sendEmulationCommand(ControlCommand.CLEAR.getCommand());
    }

    /**
     * Sends a (see {@link ControlCommand}) ENTER command to the mainframe
     * screen .
     */
    @FixtureMethod
    public void sendEnter() {
        sendEmulationCommand(ControlCommand.ENTER.getCommand());
    }

    /**
     * Sends a (see {@link ControlCommand}) ERASE_EOF command to the mainframe
     * screen .
     */
    @FixtureMethod
    public void sendEraseEndOfField() {
        sendEmulationCommand(ControlCommand.ERASE_EOF.getCommand());
    }

    /**
     * Sends a (see {@link ControlCommand}) ERASE_INPUT command to the mainframe
     * screen .
     */
    @FixtureMethod
    public void sendEraseInput() {
        sendEmulationCommand(ControlCommand.ERASE_INPUT.getCommand());
    }

    /**
     * Sends a (see {@link ControlCommand}) RESET command to the mainframe
     * screen .
     */
    @FixtureMethod
    public void sendReset() {
        sendEmulationCommand(ControlCommand.RESET.getCommand());
    }

    /**
     * Sends a (see {@link ControlCommand}) TAB command to the mainframe screen
     * .
     */
    @FixtureMethod
    public void sendTab() {
        sendEmulationCommand(ControlCommand.TAB.getCommand());
    }

    /**
     * Reads a value on a given position range start(x,y) and end(x,y)
     * 
     * @param elementLocator
     *            The elementlocator exists of position definitions with a
     *            starting(row,column) and end(row,column) position in the form
     *            "2;12;5;16" which means the start position is at row = 2,
     *            column = 12 up to stop position row = 5, column 16.
     * @param locatorType
     *            see {@link LocatorStrategy} this is the type how the
     *            elementlocator is defined.
     * @return the String value which is read at the given position.
     */
    @FixtureMethod
    public String readValueAt(String elementLocator, LocatorStrategy locatorType) {
        return getElement(elementLocator, locatorType);
    }

    protected String getElement(String elementLocator, LocatorStrategy locatorType) {
        logger.info("Lookup element {} type {}", elementLocator, locatorType.name());
        String result = null;
        switch (locatorType) {
        case START_STOP:
            result = getValueByStartStop(new LocatorByStartStop(elementLocator, mode));
            break;
        case WIDTH:
            result = getValueByWidth(new LocatorByWidth(elementLocator, mode));
            break;
        default:
            result = getValueByStartStop(new LocatorByStartStop(elementLocator, mode));
            break;
        }
        return result;
    }

    private String getValueByStartStop(LocatorByStartStop locator) {
        Result result = connection.doCommand("ascii");
        List<String> dataLines = result.getDataLines();
        StringBuffer sb = new StringBuffer();
        // When only one row is available
        if (locator.getStartRow() == locator.getEndRow()) {
            String line = dataLines.get(locator.getStartRow());
            line = LineReader.extracted(line);
            if (LineReader.extracted(line).length() > mode.getMaxColumn()) {
                throw new RuntimeException(
                        "Row: " + line + " is greater than the specified max column size " + mode.getMaxColumn());
            }
            LineReader lineReader = new LineReader();
            sb.append(lineReader.readSingleLine(line, locator));
        } else {
            // When multiple lines are available
            LineReader lineReader = new LineReader();
            sb.append(lineReader.readMultilines(dataLines, locator));
        }
        return sb.toString();
    }

    private String getValueByWidth(LocatorByWidth locator) {
        Result result = connection.doCommand("ascii");
        List<String> dataLines = result.getDataLines();
        String line = dataLines.get(locator.getStartRow());
        if (LineReader.extracted(line).length() > mode.getMaxColumn()) {
            throw new RuntimeException(
                    "Row: " + line + " is greater than the specified max column size " + mode.getMaxColumn());
        }

        LineReader lineReader = new LineReader();
        return lineReader.readSingleLineWidth(line, locator);
    }

    private void waiting(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ex) {
            logger.error(ex.getMessage());
        }
    }

    private Result setCursorPosition(int row, int col) {
        return connection.doCommand("MoveCursor(" + row + "," + col + ")");
    }

    /**
     * @param elementLocator
     *            A String representation of a point to set the cursor on. <br>
     *            Example: elementLocator = "1;2" (for the
     *            {@link LocatorStrategy#START}<br>
     *            1 is the ROW representation and 2 is the COLUMN
     *            representation.
     * @param locatorType
     *            see {@link LocatorStrategy}
     * @return {@link Result} of
     */
    @FixtureMethod
    public Result moveCursor(String elementLocator, LocatorStrategy locatorType) {
        Result result = null;
        switch (locatorType) {
        case START:
            LocatorByStart locatorByStart = new LocatorByStart(elementLocator, mode);
            result = setCursorPosition(locatorByStart.getStartRow(), locatorByStart.getStartColumn());
            break;
        case START_STOP:
            LocatorByStartStop locatorByStartStop = new LocatorByStartStop(elementLocator, mode);
            result = setCursorPosition(locatorByStartStop.getStartRow(), locatorByStartStop.getStartColumn());
            break;
        case WIDTH:
            LocatorByWidth locatorByWidth = new LocatorByWidth(elementLocator, mode);
            result = setCursorPosition(locatorByWidth.getStartRow(), locatorByWidth.getStartColumn());
            break;
        }
        return result;
    }

    /**
     * Prints all {@link Field}s of a mainframe ScreenBuffer in the form:
     * 
     * <pre>
     * |----------------------------------------------------------------------------------------------------------------------------------------------|
     * |Nr |row|field|width|colStart|colEnd|numeric|protected|hidden|value                                                                           
     * |----------------------------------------------------------------------------------------------------------------------------------------------|
     * |1  |0  |1    |4    |1       |4     |       |true     |false | 'z/OS'                                                                         
     * |2  |0  |2    |10   |6       |15    |       |false    |false | ' Z18 Level'                                                                   
     * |3  |0  |3    |5    |17      |21    |true   |false    |false | ' 0609'                                                                        
     * |4  |0  |4    |31   |23      |53    |       |false    |true  | '                               '                                              
     * |5  |0  |5    |24   |55      |78    |       |false    |false | 'IP Address = 78.51.59.98'                                                     
     * |6  |0  |6    |6    |80      |85    |       |false    |false | '
     * ...
     * </pre>
     * 
     * <strong>Caution:</strong> But before it will be verified, if the result
     * of ScreenBuffer is formatted. If the application program uses field
     * attributes to define fields on the screen, then the screen is formatted.
     * If there are no fields defined on the screen, then the screen is
     * unformatted, and the operator uses it in free-form manner.
     * 
     * @return The String reprentation of all found {@link Field}s in
     *         ScreenBuffer.
     */
    @FixtureMethod
    public String buildAllFieldsAsString() {
        String allFieldAsString = null;
        Result result = connection.doCommand("ReadBuffer(Ascii)");
        Status status = result.getStatus();
        ScreenFormatting screenFormatting = status.getScreenFormatting();
        // Check if ScreenBuffer is formatted !
        if (screenFormatting == ScreenFormatting.FORMATTED) {
            TerminalScreen screen = new TerminalScreen();
            allFieldAsString = screen.printAllFieldAsString(result.getDataLines());
            logger.info(allFieldAsString);

        } else {
            throw new RuntimeException(
                    "The result of delivered Host-Screen is not formatted, so it is not possible to get any information about fields!");
        }
        return allFieldAsString;
    }

    private void sendEmulationCommand(String command) {
        waiting(100);
        connection.doCommand(command);
        connection.doCommand("ascii"); // just to see if typed in successfully.
    }

}
