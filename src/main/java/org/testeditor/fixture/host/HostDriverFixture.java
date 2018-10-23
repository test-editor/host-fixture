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
package org.testeditor.fixture.host;

import com.google.common.annotations.VisibleForTesting;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testeditor.fixture.core.FixtureException;
import org.testeditor.fixture.core.TestRunListener;
import org.testeditor.fixture.core.TestRunReportable;
import org.testeditor.fixture.core.TestRunReporter;
import org.testeditor.fixture.core.TestRunReporter.Action;
import org.testeditor.fixture.core.TestRunReporter.SemanticUnit;
import org.testeditor.fixture.core.interaction.FixtureMethod;
import org.testeditor.fixture.core.logging.FilenameHelper;
import org.testeditor.fixture.host.locators.LocatorByStart;
import org.testeditor.fixture.host.locators.LocatorByStartStop;
import org.testeditor.fixture.host.locators.LocatorByWidth;
import org.testeditor.fixture.host.locators.LocatorStrategy;
import org.testeditor.fixture.host.net.Connection;
import org.testeditor.fixture.host.s3270.Result;
import org.testeditor.fixture.host.s3270.Status;
import org.testeditor.fixture.host.s3270.actions.Command;
import org.testeditor.fixture.host.s3270.actions.ControlCommand;
import org.testeditor.fixture.host.s3270.options.CharacterSet;
import org.testeditor.fixture.host.s3270.options.TerminalMode;
import org.testeditor.fixture.host.s3270.options.TerminalType;
import org.testeditor.fixture.host.s3270.statusformat.FieldProtection;
import org.testeditor.fixture.host.s3270.statusformat.ScreenFormatting;
import org.testeditor.fixture.host.screen.Field;
import org.testeditor.fixture.host.screen.Offset;
import org.testeditor.fixture.host.screen.TerminalScreen;
import org.testeditor.fixture.host.util.LineReader;
import org.testeditor.fixture.host.util.Timer;

/**
 * HostDriverFixture provides convenience methods for automating mainframe
 * functional tests. It consists of an API to deal with mainframe fields in
 * screens. As a test driver, we use the X3270 framework to communicate with the
 * mainframe which should be tested.
 *
 * @see <a href="http://x3270.bgp.nu/">http://x3270.bgp.nu/</a>
 */
public class HostDriverFixture implements TestRunListener, TestRunReportable {

    private static final int THREAD_SLEEP_IN_MILLIS = 100;

    // Maximum amount of time x 100 ms -> 100 x 100ms = 10.000ms = 10 Seconds
    private static final int MAX_TIME_TO_WAIT = 100;

    private static final Logger logger = LoggerFactory.getLogger(HostDriverFixture.class);

    private Connection connection;
    private TerminalMode terminalMode = TerminalMode.MODE_24x80;
    private String pathName = "./screenshots";
    private String type = "html";
    private FilenameHelper filenameHelper = new FilenameHelper();
    private String runningTest = null;
    private Offset offset;
    private Command commandAscii = new Command("Ascii", "Ascii");
    private Timer timer = new Timer(TimeUnit.MILLISECONDS);

    public HostDriverFixture() {
        this.connection = new Connection();
    }

    public HostDriverFixture(Connection connection) {
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
     * @param offsetRow
     *            The offsetRow for the screen origin. Default for the x3270
     *            driver is that the row and column are zero-origin (0;0), that
     *            means the begin point is at row 0 and column 0. The offsetRow
     *            will rearrange the startpoint around the value for the row .
     * @param offsetColumn
     *            The offsetColumn for the screen origin. Default for the x3270
     *            driver is that the row and column are zero-origin (0;0), that
     *            means the begin point is at row 0 and column 0. The
     *            offsetColumn will rearrange the startpoint around the value
     *            for the column.
     */
    @FixtureMethod
    public boolean connect(String s3270Path, String hostname, int port, int offsetRow, int offsetColumn) throws FixtureException {
        logger.info("Host-Fixture connecting ...");
        this.offset = new Offset(-offsetRow, -offsetColumn);
        TerminalType type = TerminalType.TYPE_3279;
        CharacterSet charSet = CharacterSet.CHAR_GERMAN_EURO;
        timer.startTimer();
        connection.connect(s3270Path, hostname, port, type, terminalMode, charSet, offset);
        if (connection.isConnected()) {
            waitUntilScreenIsFormatted(MAX_TIME_TO_WAIT);
            timer.stopTimer();
            logger.debug("Elapsed time in millis after connect: {}", timer.getElapsedTime());
            logger.info("successfully connected to host='{}', port='{}'", hostname, port);
            return true;
        } else {
            logger.info("The connection to host '" + hostname + "' on port '" + port + "' could not be established.");
            return false;
        }
    }

    @VisibleForTesting
    void waitUntilScreenIsFormatted(int maxIterationThreadSleeping) {
        Status status = connection.getStatus();
        ScreenFormatting screenFormatting = status.getScreenFormatting();
        logger.debug("Screenformatting : {}", screenFormatting.getFormatting());
        Integer i = 0;
        while (status.getScreenFormatting() != ScreenFormatting.FORMATTED && i++ < maxIterationThreadSleeping) {
            performWaits(status);
        }
    }
    
    protected void performWaits(Status status) {
        try {
            TimeUnit.MILLISECONDS.sleep(THREAD_SLEEP_IN_MILLIS);
        } catch (InterruptedException e) {
            logger.error("Something went wrong during wait period: {}", e.getMessage());
        }
        logger.debug("waited {} ms", THREAD_SLEEP_IN_MILLIS);
        status = connection.getStatus();
    }

    /**
     * Disconnects from host.
     * {@link org.testeditor.fixture.host.net.Connection#disconnect()}
     *
     */
    @FixtureMethod
    public boolean disconnect() throws FixtureException {
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
    public Status getStatus() throws FixtureException {
        logger.info("get Status ...");
        return connection.getStatus();
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
    public void typeAt(String elementLocator, LocatorStrategy locatorType, String value) throws FixtureException {
        Result result = moveCursor(elementLocator, locatorType);
        result.logStatus();
        Status status = result.getStatus();
        if (status.getFieldProtection() == FieldProtection.UNPROTECTED) {
            waiting(100);
            Command commandType = new Command("String", "String(\"" + value + "\")");
            connection.doCommand("String(\"" + value + "\")", commandType);
            // just to see if typed in successfully.
            connection.doCommand("ascii", commandAscii);
        } else {
            throw new RuntimeException("The field at the position x = '" + status.getCurrentCursorColumn() + "' and y = '"
                    + status.getCurrentCursorRow() + "' is protected.");
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
    public void sendCommand(ControlCommand command) throws FixtureException {
        waiting(100);
        Command commandString = new Command(command.getCommand(), command.getCommand());
        connection.doCommand(command.getCommand(), commandString);
        connection.doCommand("ascii", commandAscii);
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
    public String readValueAt(String elementLocator, LocatorStrategy locatorType) throws FixtureException {
        return getElement(elementLocator, locatorType);
    }

    protected String getElement(String elementLocator, LocatorStrategy locatorType) throws FixtureException {
        logger.info("Lookup element {} type {}", elementLocator, locatorType.name());
        String result = null;
        Status status = getStatus();
        switch (locatorType) {
        case START_STOP:
            result = getValueByStartStop(new LocatorByStartStop(elementLocator, status, offset));
            break;
        case WIDTH:
            result = getValueByWidth(new LocatorByWidth(elementLocator, status, offset));
            break;
        default:
            result = getValueByStartStop(new LocatorByStartStop(elementLocator, status, offset));
            break;
        }
        return result;
    }

    private String getValueByStartStop(LocatorByStartStop locator) {
        Command commandAscii = new Command("Ascii", "Ascii");
        Result result = connection.doCommand("ascii", commandAscii);
        List<String> dataLines = result.getDataLines();
        Status status = result.getStatus();
        StringBuffer sb = new StringBuffer();
        // When only one row is available
        if (locator.getStartRow() == locator.getEndRow()) {
            String line = dataLines.get(locator.getStartRowWithOffset());
            line = LineReader.extracted(line);
            if (LineReader.extracted(line).length() > status.getNumberColumns()) {
                throw new RuntimeException("Row: " + line + " is greater than the specified max column size " + status.getNumberColumns());
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
        Command commandAscii = new Command("Ascii", "Ascii");
        Result result = connection.doCommand("ascii", commandAscii);
        Status status = result.getStatus();
        List<String> dataLines = result.getDataLines();
        String line = dataLines.get(locator.getStartRow() + offset.getOffsetRow());
        if (LineReader.extracted(line).length() > status.getNumberColumns()) {
            throw new RuntimeException("Row: " + line + " is greater than the specified max column size " + status.getNumberColumns());
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
        Command command = new Command("MoveCursor", "MoveCursor(" + (row - offset.getOffsetRow()) + "," + (col - offset.getOffsetColumn()) + ")", row,
                col);
        return connection.doCommand("MoveCursor(" + row + "," + col + ")", command);
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
     * @return {@link Result} of the action.
     */
    @FixtureMethod
    public Result moveCursor(String elementLocator, LocatorStrategy locatorType) throws FixtureException {
        Result result = null;
        Status status = getStatus();
        switch (locatorType) {
        case START:
            LocatorByStart locatorByStart = new LocatorByStart(elementLocator, status, offset);
            result = setCursorPosition(locatorByStart.getStartRowWithOffset(), locatorByStart.getStartColumnWithOffset());
            break;
        case START_STOP:
            LocatorByStartStop locatorByStartStop = new LocatorByStartStop(elementLocator, status, offset);
            result = setCursorPosition(locatorByStartStop.getStartRowWithOffset(), locatorByStartStop.getStartColumnWithOffset());
            break;
        case WIDTH:
            LocatorByWidth locatorByWidth = new LocatorByWidth(elementLocator, status, offset);
            result = setCursorPosition(locatorByWidth.getStartRowWithOffset(), locatorByWidth.getStartColumnWithOffset());
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
    public String buildAllFieldsAsString() throws FixtureException {
        String allFieldAsString = null;
        Command command = new Command("ReadBuffer", "ReadBuffer(Ascii)");
        Result result = connection.doCommand("ReadBuffer(Ascii)", command);
        Status status = result.getStatus();
        ScreenFormatting screenFormatting = status.getScreenFormatting();
        // Check if ScreenBuffer is formatted !
        int maxWaitCounter = 0;
        while (screenFormatting != ScreenFormatting.FORMATTED) {
            waiting(500);
            maxWaitCounter++;
            logger.debug("waiting 500 ms ...");
            result = connection.doCommand("ReadBuffer(Ascii)", command);
            status = result.getStatus();
            screenFormatting = status.getScreenFormatting();
            // Wait maximal 30 seconds that screen is formatted
            if (maxWaitCounter > 60) {
                break;
            }
        }
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

    private void takeScreenshot(String filenameBase) {
        String testcase = getCurrentTestCase();
        String filename = filenameHelper.constructFilename(pathName, testcase, filenameBase, type);
        String filePath = StringUtils.substringBeforeLast(filename, "/");
        try {
            FileUtils.mkdir(new File(filePath), true);
        } catch (IOException e) {
            logger.error("Something went wrong while creating test files: ", e);
        }
        Command command = new Command("PrintText", "PrintText html modi " + filename);
        Result result = connection.doCommand("PrintText html modi " + filename, command);
        if (result.getResultOfCommand().equals("ok")) {
            logger.info("Wrote screenshot to file='{}'.", filename);
        } else {
            logger.warn("An Error occured while taking screenshots. Could not write screenshot to file='{}'.", filename);
        }
    }

    @Override
    public void initWithReporter(TestRunReporter reporter) {
        reporter.addListener(this);
    }

    private String getCurrentTestCase() {
        return runningTest != null ? runningTest : "UNKNOWN_TEST";
    }

    private boolean screenshotShouldBeMade(SemanticUnit unit, Action action, String msg) {
        // configurable through maven build?
        return ((action == Action.LEAVE) || unit == SemanticUnit.TEST) && connection.isConnected();
    }

	@Override
	public void reported(SemanticUnit unit, Action action, String message, String id,
			org.testeditor.fixture.core.TestRunReporter.Status status, Map<String, String> variables) {
        if (unit == SemanticUnit.TEST && action == Action.ENTER) {
            runningTest = message;
        }
        if (screenshotShouldBeMade(unit, action, message)) {
            takeScreenshot(message + '.' + action.name());
        }
	}

	@Override
	public void reportFixtureExit(FixtureException fixtureException) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reportExceptionExit(Exception exception) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reportAssertionExit(AssertionError assertionError) {
		// TODO Auto-generated method stub
		
	}

}
