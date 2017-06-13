package org.testeditor.fixture.host.s3279;

import org.testeditor.fixture.host.exceptions.StatusNotFoundException;
import org.testeditor.fixture.host.s3270.Status;
import org.testeditor.fixture.host.s3270.options.TerminalMode;
import org.testeditor.fixture.host.s3270.statusformat.ConnectionState;
import org.testeditor.fixture.host.s3270.statusformat.EmulatorMode;
import org.testeditor.fixture.host.s3270.statusformat.FieldProtection;
import org.testeditor.fixture.host.s3270.statusformat.KeyboardState;
import org.testeditor.fixture.host.s3270.statusformat.ScreenFormatting;

import org.junit.Assert;
import org.junit.Test;

public class StatusTest {

    String defaultStatusString = "U F U C(abcdefg.hi.google-mainframe.com) I 2 24 80 6 44 0x0 -";

    @Test
    public void keyboardStateSuccessfullTest() {
        String statusString = defaultStatusString;
        Status status = new Status(statusString);
        Assert.assertTrue(status.getKeyboardState().name().equals(KeyboardState.UNLOCKED.name()));
    }

    @Test(expected = StatusNotFoundException.class)
    public void keyboardStateUnsuccessfullTest() {
        String statusString = "A F U C"; // <--- keyboard state A is illegal
        new Status(statusString);
    }

    @Test
    public void screenFormattingSuccessfulTest() {
        String statusString = defaultStatusString;
        Status status = new Status(statusString);
        Assert.assertTrue(status.getScreenFormatting().name().equals(ScreenFormatting.FORMATTED.name()));
    }

    @Test(expected = StatusNotFoundException.class)
    public void screenFormattingUnSuccessfullTest() {
        String statusString = "U A U C"; // <--- screen formatting A is illegal
        new Status(statusString);
    }

    @Test
    public void fieldProtectionSuccessfulTest() {
        String statusString = defaultStatusString;
        Status status = new Status(statusString);
        Assert.assertTrue(status.getFieldProtection().name().equals(FieldProtection.UNPROTECTED.name()));
    }

    @Test(expected = StatusNotFoundException.class)
    public void fieldProtectionUnSuccessfullTest() {
        String statusString = "U F A C"; // <--- field protection A is illegal
        new Status(statusString);
    }

    @Test
    public void connectionStateSuccessfulTest() {
        String statusString = defaultStatusString;
        Status status = new Status(statusString);
        Assert.assertTrue(status.getConnectionState().name().equals(ConnectionState.CONNECTED.name()));
    }

    @Test(expected = StatusNotFoundException.class)
    public void connectionStateUnSuccessfullTest() {
        String statusString = "U F U A"; // <--- connection state A is illegal
        new Status(statusString);
    }

    @Test
    public void emulatorModeSuccessfulTest() {
        String statusString = defaultStatusString;
        Status status = new Status(statusString);
        Assert.assertTrue(status.getEmulatorMode().name().equals(EmulatorMode.M3270_MODE.name()));
    }

    @Test(expected = StatusNotFoundException.class)
    public void emulatorModeUnSuccessfullTest() {
        String statusString = "U F U C(abcdefg.hi.google-mainframe.com) A 2 24 80 6 44 0x0 -"; // <---
                                                                                               // emulator
                                                                                               // mode
                                                                                               // A
                                                                                               // is
                                                                                               // illegal
        new Status(statusString);
    }

    @Test
    public void terminalModeSuccessfulTest() {
        String statusString = defaultStatusString;
        Status status = new Status(statusString);
        Assert.assertTrue(status.getMode().name().equals(TerminalMode.MODE_24x80.name()));
    }

    @Test(expected = StatusNotFoundException.class)
    public void terminalModeUnSuccessfullTest() {
        String statusString = "U F U C(abcdefg.hi.google-mainframe.com) I 8 24 80 6 44 0x0 -"; // <---
                                                                                               // terminal
                                                                                               // mode
                                                                                               // 8
                                                                                               // is
                                                                                               // illegal
        new Status(statusString);
    }

    @Test
    public void numberOfRowsSuccessfulTest() {
        String statusString = defaultStatusString;
        Status status = new Status(statusString);
        Assert.assertTrue(status.getNumberRows() == 24);
    }

    @Test
    public void numberOfRowsUnSuccessfulTest() {
        String statusString = defaultStatusString;
        Status status = new Status(statusString);
        Assert.assertFalse(status.getNumberRows() == 32);
    }

    @Test
    public void numberOfColumnsSuccessfulTest() {
        String statusString = defaultStatusString;
        Status status = new Status(statusString);
        Assert.assertTrue(status.getNumberColumns() == 80);
    }

    @Test
    public void numberOfColumnsUnSuccessfulTest() {
        String statusString = defaultStatusString;
        Status status = new Status(statusString);
        Assert.assertFalse(status.getNumberColumns() == 81);
    }

    @Test
    public void currentColumnSuccessfulTest() {
        String statusString = defaultStatusString;
        Status status = new Status(statusString);
        Assert.assertTrue(status.getCurrentCursorColumn() == 44);
    }

    @Test
    public void currentColumnUnSuccessfulTest() {
        String statusString = defaultStatusString;
        Status status = new Status(statusString);
        Assert.assertFalse(status.getCurrentCursorColumn() == 45);
    }

    @Test
    public void currentRowSuccessfulTest() {
        String statusString = defaultStatusString;
        Status status = new Status(statusString);
        Assert.assertTrue(status.getCurrentCursorRow() == 6);
    }

    @Test
    public void currentRowUnSuccessfulTest() {
        String statusString = defaultStatusString;
        Status status = new Status(statusString);
        Assert.assertFalse(status.getCurrentCursorRow() == 7);
    }

    @Test
    public void windowIdSuccessfulTest() {
        String statusString = defaultStatusString;
        Status status = new Status(statusString);
        Assert.assertTrue(status.getWindowId().equals("0x0"));
    }

    @Test
    public void windowIdUnSuccessfulTest() {
        String statusString = defaultStatusString;
        Status status = new Status(statusString);
        Assert.assertFalse(status.getWindowId().equals("WATT"));
    }

    @Test
    public void commandExecutionTimeSuccessfulTest() {
        String statusString = defaultStatusString;
        Status status = new Status(statusString);
        Assert.assertTrue(status.getCommanExecutionTime().equals("-"));
    }

    @Test
    public void commandExecutionUnSuccessfulTest() {
        String statusString = defaultStatusString;
        new Status(statusString);
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
