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
import org.testeditor.fixture.host.s3270.Status;
import org.testeditor.fixture.host.s3270.options.TerminalMode;
import org.testeditor.fixture.host.s3270.statusformat.ConnectionState;
import org.testeditor.fixture.host.s3270.statusformat.EmulatorMode;
import org.testeditor.fixture.host.s3270.statusformat.FieldProtection;
import org.testeditor.fixture.host.s3270.statusformat.KeyboardState;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

public class HostDriverIntegrationTest {

    private final int standarRow = Integer.parseInt(System.getenv("STANDARD_ROW"));
    private final int standardColumn = Integer.parseInt(System.getenv("STANDARD_COLUMN"));
    private final int MAX_ROWS = 24;
    private final int MAX_COLUMNS = 80;
    private final String STANDARD_WINDOW_ID = "0x0";
    private final String s3270Path = System.getenv("S3270_PATH");
    private final String hostUrl = System.getenv("HOST_URL");
    private final int hostPort = Integer.parseInt(System.getenv("HOST_PORT"));

    private HostDriverFixture hostDriverFixture;

    @Before
    public void intialize() {
        // manual execution only in special environments
        Assume.assumeTrue("This is not a Windows OS - ignoring test", S3270Helper.isOsWindows());
        Assume.assumeTrue("The path to the s3270 driver is not present - ignoring test",
                S3270Helper.isS3270DriverPresent(s3270Path));
        hostDriverFixture = new HostDriverFixture();
        Assert.assertTrue(hostDriverFixture.connect(s3270Path, hostUrl, hostPort));
    }

    @Test
    /**
     * This integrationtest is only for a special environment and not for
     * executing in a gradle test environment. You can uncomment the
     * annotation @Test to test this special environment. Please do not delete
     * this file.
     */
    public void connectionTest() {

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
    public void typeAtTest() {
        // given
        // hostDriverFixture in init

        // when
        hostDriverFixture.typeAt("äöüßabcdefg", standarRow, standardColumn);

        // then
        // on screen there will be typed some Umlaut characters, the test can
        // only be verified not before there will be implemented a verification
        // method.

        hostDriverFixture.disconnect();
    }

}
