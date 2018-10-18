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
package org.testeditor.fixture.host.s3279;

import org.junit.Assert;
import org.junit.Test;
import org.testeditor.fixture.host.exceptions.StatusNotFoundException;
import org.testeditor.fixture.host.s3270.Status;
import org.testeditor.fixture.host.s3270.options.TerminalMode;
import org.testeditor.fixture.host.s3270.statusformat.ConnectionState;
import org.testeditor.fixture.host.s3270.statusformat.EmulatorMode;
import org.testeditor.fixture.host.s3270.statusformat.FieldProtection;
import org.testeditor.fixture.host.s3270.statusformat.KeyboardState;
import org.testeditor.fixture.host.s3270.statusformat.ScreenFormatting;
import org.testeditor.fixture.host.screen.Offset;

public class StatusTest {

    String defaultStatusString = "U F U C(abcdefg.hi.google-mainframe.com) I 2 24 80 6 44 0x0 -";

    @Test
    public void keyboardStateSuccessfulTest() {
        String statusString = defaultStatusString;
        Offset offset = new Offset(1, 1);
        Status status = new Status(statusString, offset);
        Assert.assertEquals(status.getKeyboardState().name(), KeyboardState.UNLOCKED.name());
    }

    @Test(expected = StatusNotFoundException.class)
    public void keyboardStateUnsuccessfulTest() {
        String statusString = "A F U C"; // <--- keyboard state A is illegal
        Offset offset = new Offset(1, 1);
        new Status(statusString, offset);
    }

    @Test
    public void screenFormattingSuccessfulTest() {
        String statusString = defaultStatusString;
        Offset offset = new Offset(1, 1);
        Status status = new Status(statusString, offset);
        Assert.assertEquals(status.getScreenFormatting().name(), ScreenFormatting.FORMATTED.name());
    }

    @Test(expected = StatusNotFoundException.class)
    public void screenFormattingUnSuccessfulTest() {
        String statusString = "U A U C"; // <--- screen formatting A is illegal
        Offset offset = new Offset(1, 1);
        new Status(statusString, offset);
    }

    @Test
    public void fieldProtectionSuccessfulTest() {
        String statusString = defaultStatusString;
        Offset offset = new Offset(1, 1);
        Status status = new Status(statusString, offset);
        Assert.assertEquals(status.getFieldProtection().name(), FieldProtection.UNPROTECTED.name());
    }

    @Test(expected = StatusNotFoundException.class)
    public void fieldProtectionUnSuccessfulTest() {
        String statusString = "U F A C"; // <--- field protection A is illegal
        Offset offset = new Offset(1, 1);
        new Status(statusString, offset);
    }

    @Test
    public void connectionStateSuccessfulTest() {
        String statusString = defaultStatusString;
        Offset offset = new Offset(1, 1);
        Status status = new Status(statusString, offset);
        Assert.assertEquals(status.getConnectionState().name(), ConnectionState.CONNECTED.name());
    }

    @Test(expected = RuntimeException.class)
    public void connectionStateUnSuccessfulTest() {
        String statusString = "U F U A"; // <--- connection state A is illegal
        Offset offset = new Offset(1, 1);
        new Status(statusString, offset);
    }

    @Test
    public void emulatorModeSuccessfulTest() {
        String statusString = defaultStatusString;
        Offset offset = new Offset(1, 1);
        Status status = new Status(statusString, offset);
        Assert.assertEquals(status.getEmulatorMode().name(), EmulatorMode.M3270_MODE.name());
    }

    @Test(expected = StatusNotFoundException.class)
    public void emulatorModeUnSuccessfulTest() {
        // emulator mode A is illegal
        String statusString = "U F U C(abcdefg.hi.google-mainframe.com) A 2 24 80 6 44 0x0 -";
        Offset offset = new Offset(1, 1);
        new Status(statusString, offset);
    }

    @Test
    public void terminalModeSuccessfulTest() {
        String statusString = defaultStatusString;
        Offset offset = new Offset(1, 1);
        Status status = new Status(statusString, offset);
        Assert.assertEquals(status.getMode().name(), TerminalMode.MODE_24x80.name());
    }

    @Test(expected = StatusNotFoundException.class)
    public void terminalModeUnSuccessfulTest() {
        // terminal mode 8 is illegal
        String statusString = "U F U C(abcdefg.hi.google-mainframe.com) I 8 24 80 6 44 0x0 -";
        Offset offset = new Offset(1, 1);
        new Status(statusString, offset);
    }

    @Test
    public void numberOfRowsSuccessfulTest() {
        String statusString = defaultStatusString;
        Offset offset = new Offset(1, 1);
        Status status = new Status(statusString, offset);
        Assert.assertTrue(status.getNumberRows() == 24);
    }

    @Test
    public void numberOfColumnsSuccessfulTest() {
        String statusString = defaultStatusString;
        Offset offset = new Offset(1, 1);
        Status status = new Status(statusString, offset);
        Assert.assertTrue(status.getNumberColumns() == 80);
    }

    @Test
    public void currentColumnSuccessfulTest() {
        String statusString = defaultStatusString;
        Offset offset = new Offset(1, 1);
        Status status = new Status(statusString, offset);
        Assert.assertTrue(status.getCurrentCursorColumn() == 44);
    }

    @Test
    public void currentRowSuccessfulTest() {
        String statusString = defaultStatusString;
        Offset offset = new Offset(1, 1);
        Status status = new Status(statusString, offset);
        Assert.assertTrue(status.getCurrentCursorRow() == 6);
    }

    @Test
    public void windowIdSuccessfulTest() {
        String statusString = defaultStatusString;
        Offset offset = new Offset(1, 1);
        Status status = new Status(statusString, offset);
        Assert.assertEquals(status.getWindowId(), "0x0");
    }

    @Test
    public void commandExecutionTimeSuccessfulTest() {
        String statusString = defaultStatusString;
        Offset offset = new Offset(1, 1);
        Status status = new Status(statusString, offset);
        Assert.assertEquals(status.getCommanExecutionTime(), "-");
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "StatusTest [defaultResult=" + defaultStatusString + ", toString()=" + super.toString() + "]";
    }

}
