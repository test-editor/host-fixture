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
package org.testeditor.fixture.host.integrationtest;

import org.testeditor.fixture.host.HostDriverFixture;
import org.testeditor.fixture.host.locators.LocatorStrategy;
import org.testeditor.fixture.host.s3270.Status;
import org.testeditor.fixture.host.s3270.actions.ControlCommand;
import org.testeditor.fixture.host.s3270.options.TerminalMode;
import org.testeditor.fixture.host.s3270.statusformat.ConnectionState;
import org.testeditor.fixture.host.s3270.statusformat.EmulatorMode;
import org.testeditor.fixture.host.s3270.statusformat.FieldProtection;
import org.testeditor.fixture.host.s3270.statusformat.KeyboardState;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class HostDriverIntegrationTest {

    private int standardRow = 0;
    private int standardColumn = 0;
    private final int MAX_ROWS = 24;
    private final int MAX_COLUMNS = 80;
    private final String STANDARD_WINDOW_ID = "0x0";
    private String s3270Path = "S3270_PATH";
    private String hostUrl = "HOST_URL";
    private int hostPort = 0;

    private HostDriverFixture hostDriverFixture;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void intialize() {
        // manual execution only in special environments
        assumeWindowsAndS3270Accessible();
        standardRow = Integer.parseInt(System.getenv("STANDARD_ROW"));
        standardColumn = Integer.parseInt(System.getenv("STANDARD_COLUMN"));

        hostUrl = System.getenv("HOST_URL");
        hostPort = Integer.parseInt(System.getenv("HOST_PORT"));
        hostDriverFixture = new HostDriverFixture();
        Assert.assertTrue(hostDriverFixture.connect(s3270Path, hostUrl, hostPort));
    }

    private void assumeWindowsAndS3270Accessible() {
        Assume.assumeTrue("This is not a Windows OS - ignoring test", S3270Helper.isOsWindows());
        s3270Path = System.getenv("S3270_PATH");
        Assume.assumeTrue("The path to the s3270 driver is not present - ignoring test",
                S3270Helper.isS3270DriverPresent(s3270Path));
    }

    @Test
    /**
     * This integrationtest is only for a special environment and not for
     * executing in a gradle test environment. You can uncomment the
     * annotation @Test to test this special environment. Please do not delete
     * this file.
     */
    public void connectionTest() {
        assumeWindowsAndS3270Accessible();
        // given
        // hostDriverFixture in init

        // when
        Status status = hostDriverFixture.getStatus();

        // then
        Assert.assertNotNull(status);
        Assert.assertTrue(status.getFieldProtection() == FieldProtection.UNPROTECTED);
        Assert.assertTrue(status.getConnectionState() == ConnectionState.CONNECTED);
        Assert.assertTrue(status.getKeyboardState() == KeyboardState.UNLOCKED);
        hostDriverFixture.disconnect();
    }

    @Test
    public void statusTest() {
        assumeWindowsAndS3270Accessible();
        // given
        // hostDriverFixture in init

        // when
        Status status = hostDriverFixture.getStatus();

        // then
        Assert.assertTrue(status.getKeyboardState().name().equals(KeyboardState.UNLOCKED.name()));
        Assert.assertTrue(status.getFieldProtection().name().equals(FieldProtection.UNPROTECTED.name()));
        Assert.assertTrue(status.getConnectionState().name().equals(ConnectionState.CONNECTED.name()));
        Assert.assertTrue(status.getEmulatorMode().name().equals(EmulatorMode.M3270_MODE.name()));
        Assert.assertTrue(status.getMode().name().equals(TerminalMode.MODE_24x80.name()));
        Assert.assertTrue(status.getNumberRows() == MAX_ROWS);
        Assert.assertTrue(status.getNumberColumns() == MAX_COLUMNS);
        Assert.assertTrue(status.getNumberColumns() == MAX_COLUMNS);
        Assert.assertTrue(status.getWindowId().equals(STANDARD_WINDOW_ID));
        Assert.assertTrue(status.getCommanExecutionTime().equals("-"));
        hostDriverFixture.disconnect();
    }

    @Test
    public void screenTest() {
        assumeWindowsAndS3270Accessible();
        // given
        // hostDriverFixture in init

        // when

        String allFieldsAsString = hostDriverFixture.buildAllFieldsAsString();
        hostDriverFixture.disconnect();
    }

    @Test
    public void typeAtTest() {
        assumeWindowsAndS3270Accessible();
        // given
        // hostDriverFixture in init

        // when
        hostDriverFixture.typeAt((standardRow + ";" + standardColumn), LocatorStrategy.START, "äöüßabc");
        String actualValue = hostDriverFixture.readValueAt(
                standardRow + ";" + standardColumn + ";" + standardRow + ";" + (standardColumn + 6),
                LocatorStrategy.START_STOP);

        // then
        // on screen there will be typed some Umlaut characters
        Assert.assertEquals("äöüßabc", actualValue);
        hostDriverFixture.disconnect();
    }

    @Test
    public void typeAtTestFailure() {
        assumeWindowsAndS3270Accessible();
        // given
        // hostDriverFixture in init

        // when
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("The field at the position x = '0' and y = '0' is protected.");
        hostDriverFixture.typeAt("0;0", LocatorStrategy.START, "äöüßabcdefg");
        // then
        hostDriverFixture.disconnect();
    }

    @Test
    public void newTransactionTest() {
        assumeWindowsAndS3270Accessible();
        // given
        // hostDriverFixture in init
        String user = System.getenv("USER");
        String userPwd = System.getenv("USER_PWD");
        String testSystem = System.getenv("TEST_SYSTEM");
        String transaction = System.getenv("TRANSACTION");
        String stoptransaction = System.getenv("STOP_TRANSACTION");
        String stopSystem = System.getenv("STOP_SYSTEM");
        int testDefaultRow = Integer.parseInt(System.getenv("TEST_DEFAULT_ROW"));
        int testDefaultColumn = Integer.parseInt(System.getenv("TEST_DEFAULT_COLUMN"));
        int transactionRow = Integer.parseInt(System.getenv("TRANSACTION_ROW"));
        int transactionColumn = Integer.parseInt(System.getenv("TRANSACTION_COLUMN"));
        int userPwdRow = Integer.parseInt(System.getenv("USER_PWD_ROW"));
        int userPwdColumn = Integer.parseInt(System.getenv("USER_PWD_COLUMN"));

        // when
        hostDriverFixture.typeAt((standardRow + ";" + standardColumn), LocatorStrategy.START, user);
        hostDriverFixture.typeAt((userPwdRow + ";" + userPwdColumn), LocatorStrategy.START, userPwd);
        hostDriverFixture.sendControlCommand(ControlCommand.ENTER);
        hostDriverFixture.typeAt((testDefaultRow + ";" + testDefaultColumn), LocatorStrategy.START, testSystem);
        hostDriverFixture.sendControlCommand(ControlCommand.ENTER);
        hostDriverFixture.typeAt((transactionRow + ";" + transactionColumn), LocatorStrategy.START, transaction);
        hostDriverFixture.sendControlCommand(ControlCommand.ENTER);
        hostDriverFixture.sendControlCommand(ControlCommand.CLEAR);
        hostDriverFixture.typeAt((transactionRow + ";" + transactionColumn), LocatorStrategy.START, stoptransaction);
        hostDriverFixture.sendControlCommand(ControlCommand.ENTER);
        hostDriverFixture.typeAt((testDefaultRow + ";" + testDefaultColumn), LocatorStrategy.START, stopSystem);
        hostDriverFixture.sendControlCommand(ControlCommand.ENTER);

        // then
        // on screen there will be typed some Umlaut characters, the test can
        // only be verified not before there will be implemented a verification
        // method.

        hostDriverFixture.disconnect();
    }

    @Test
    public void readValueWithLocatorStrategyStartStopTest() {
        assumeWindowsAndS3270Accessible();
        // given
        // hostDriverFixture in init
        String user = System.getenv("USER");
        String userPwd = System.getenv("USER_PWD");
        String stopSystem = System.getenv("STOP_SYSTEM");
        String expectedValue = System.getenv("EXPECTED_VALUE");
        int testDefaultRow = Integer.parseInt(System.getenv("TEST_DEFAULT_ROW"));
        int testDefaultColumn = Integer.parseInt(System.getenv("TEST_DEFAULT_COLUMN"));
        int userPwdRow = Integer.parseInt(System.getenv("USER_PWD_ROW"));
        int userPwdColumn = Integer.parseInt(System.getenv("USER_PWD_COLUMN"));

        // when
        hostDriverFixture.typeAt((standardRow + ";" + standardColumn), LocatorStrategy.START, user);
        hostDriverFixture.typeAt((userPwdRow + ";" + userPwdColumn), LocatorStrategy.START, userPwd);
        hostDriverFixture.sendControlCommand(ControlCommand.ENTER);
        String actualValue = hostDriverFixture.readValueAt("2;1;2;10", LocatorStrategy.START_STOP);

        // then
        Assert.assertEquals(expectedValue, actualValue);

        // stopping the host transaction
        hostDriverFixture.typeAt((testDefaultRow + ";" + testDefaultColumn), LocatorStrategy.START, stopSystem);
        hostDriverFixture.sendControlCommand(ControlCommand.ENTER);
        hostDriverFixture.disconnect();
    }

    @Test
    public void readValueWithLocatorStrategyWidthTest() {
        assumeWindowsAndS3270Accessible();
        // given
        // hostDriverFixture in init
        String user = System.getenv("USER");
        String userPwd = System.getenv("USER_PWD");
        String stopSystem = System.getenv("STOP_SYSTEM");
        String expectedValue = System.getenv("EXPECTED_VALUE");
        int testDefaultRow = Integer.parseInt(System.getenv("TEST_DEFAULT_ROW"));
        int testDefaultColumn = Integer.parseInt(System.getenv("TEST_DEFAULT_COLUMN"));
        int userPwdRow = Integer.parseInt(System.getenv("USER_PWD_ROW"));
        int userPwdColumn = Integer.parseInt(System.getenv("USER_PWD_COLUMN"));

        // when
        hostDriverFixture.typeAt((standardRow + ";" + standardColumn), LocatorStrategy.START, user);
        hostDriverFixture.typeAt((userPwdRow + ";" + userPwdColumn), LocatorStrategy.START, userPwd);
        hostDriverFixture.sendControlCommand(ControlCommand.ENTER);
        String actualValue = hostDriverFixture.readValueAt("2;1;10", LocatorStrategy.WIDTH);

        // then
        Assert.assertEquals(expectedValue, actualValue);

        // stopping the host transaction
        hostDriverFixture.typeAt((testDefaultRow + ";" + testDefaultColumn), LocatorStrategy.START, stopSystem);
        hostDriverFixture.sendControlCommand(ControlCommand.ENTER);
        hostDriverFixture.disconnect();
    }

    @Test
    public void typeAtLocatorStrategyWidthTest() {
        assumeWindowsAndS3270Accessible();
        // given
        // hostDriverFixture in init
        String user = System.getenv("USER");
        String userPwd = System.getenv("USER_PWD");
        String stopSystem = System.getenv("STOP_SYSTEM");
        String expectedValue = System.getenv("EXPECTED_VALUE");
        int testDefaultRow = Integer.parseInt(System.getenv("TEST_DEFAULT_ROW"));
        int testDefaultColumn = Integer.parseInt(System.getenv("TEST_DEFAULT_COLUMN"));
        int userPwdRow = Integer.parseInt(System.getenv("USER_PWD_ROW"));
        int userPwdColumn = Integer.parseInt(System.getenv("USER_PWD_COLUMN"));

        // when
        String elementlocatorUser = standardRow + ";" + standardColumn + ";0";
        System.out.println(elementlocatorUser);
        hostDriverFixture.typeAt(elementlocatorUser, LocatorStrategy.WIDTH, user);
        hostDriverFixture.typeAt(userPwdRow + ";" + userPwdColumn + ";0", LocatorStrategy.WIDTH, userPwd);
        hostDriverFixture.sendControlCommand(ControlCommand.ENTER);
        String actualValue = hostDriverFixture.readValueAt("2;1;10", LocatorStrategy.WIDTH);

        // then
        Assert.assertEquals(expectedValue, actualValue);

        // stopping the host transaction
        hostDriverFixture.typeAt((testDefaultRow + ";" + testDefaultColumn), LocatorStrategy.START, stopSystem);
        hostDriverFixture.sendControlCommand(ControlCommand.ENTER);
        hostDriverFixture.disconnect();
    }

    @Test
    public void typeAtLocatorStrategyStartStopTest() {
        assumeWindowsAndS3270Accessible();
        // given
        // hostDriverFixture in init
        String user = System.getenv("USER");
        String userPwd = System.getenv("USER_PWD");
        String stopSystem = System.getenv("STOP_SYSTEM");
        String expectedValue = System.getenv("EXPECTED_VALUE");
        int testDefaultRow = Integer.parseInt(System.getenv("TEST_DEFAULT_ROW"));
        int testDefaultColumn = Integer.parseInt(System.getenv("TEST_DEFAULT_COLUMN"));
        int userPwdRow = Integer.parseInt(System.getenv("USER_PWD_ROW"));
        int userPwdColumn = Integer.parseInt(System.getenv("USER_PWD_COLUMN"));

        // when
        String elementlocatorUser = standardRow + ";" + standardColumn + ";0;0";
        System.out.println(elementlocatorUser);
        hostDriverFixture.typeAt(elementlocatorUser, LocatorStrategy.START_STOP, user);
        hostDriverFixture.typeAt(userPwdRow + ";" + userPwdColumn + ";0;0", LocatorStrategy.START_STOP, userPwd);
        hostDriverFixture.sendControlCommand(ControlCommand.ENTER);
        String actualValue = hostDriverFixture.readValueAt("2;1;10", LocatorStrategy.WIDTH);

        // then
        Assert.assertEquals(expectedValue, actualValue);

        // stopping the host transaction
        hostDriverFixture.typeAt((testDefaultRow + ";" + testDefaultColumn), LocatorStrategy.START, stopSystem);
        hostDriverFixture.sendControlCommand(ControlCommand.ENTER);
        hostDriverFixture.disconnect();
    }

}
