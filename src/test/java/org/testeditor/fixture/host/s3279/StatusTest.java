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

public class StatusTest {

  String defaultResult = "U F U C(abcdefg.hi.google-mainframe.com) I 2 24 80 6 44 0x0 -";

  @Test
  public void keyboardStateSuccessfullTest() {
    String result = defaultResult;
    Status status = new Status(result);
    Assert.assertTrue(status.getKeyboardState().name().equals(KeyboardState.UNLOCKED.name()));
  }

  @Test(expected = StatusNotFoundException.class)
  public void keyboardStateUnsuccessfullTest() {
    String result = "A F U C";
    new Status(result);
  }

  @Test
  public void screenFormattingSuccessfullTest() {
    String result = defaultResult;
    Status status = new Status(result);
    Assert
        .assertTrue(status.getScreenFormatting().name().equals(ScreenFormatting.FORMATTED.name()));
  }

  @Test(expected = StatusNotFoundException.class)
  public void screenFormattingUnSuccessfullTest() {
    String result = "U A U C";
    new Status(result);
  }

  @Test
  public void fieldProtectionSuccessfullTest() {
    String result = defaultResult;
    Status status = new Status(result);
    Assert
        .assertTrue(status.getFieldProtection().name().equals(FieldProtection.UNPROTECTED.name()));
  }

  @Test(expected = StatusNotFoundException.class)
  public void fieldProtectionUnSuccessfullTest() {
    String result = "U F A C";
    new Status(result);
  }

  @Test
  public void connectionStateSuccessfullTest() {
    String result = defaultResult;
    Status status = new Status(result);
    Assert.assertTrue(status.getConnectionState().name().equals(ConnectionState.CONNECTED.name()));
  }

  @Test(expected = StatusNotFoundException.class)
  public void connectionStateUnSuccessfullTest() {
    String result = "U F U A";
    new Status(result);
  }

  @Test
  public void emulatorModeSuccessfullTest() {
    String result = defaultResult;
    Status status = new Status(result);
    Assert.assertTrue(status.getEmulatorMode().name().equals(EmulatorMode.M3270_MODE.name()));
  }

  @Test(expected = StatusNotFoundException.class)
  public void emulatorModeUnSuccessfullTest() {
    String result = "U F U C(abcdefg.hi.google-mainframe.com) A 2 24 80 6 44 0x0 -";
    new Status(result);
  }

  @Test
  public void terminalModeSuccessfullTest() {
    String result = defaultResult;
    Status status = new Status(result);
    Assert.assertTrue(status.getMode().name().equals(TerminalMode.MODE_24x80.name()));
  }

  @Test(expected = StatusNotFoundException.class)
  public void terminalModeUnSuccessfullTest() {
    String result = "U F U C(abcdefg.hi.google-mainframe.com) I 8 24 80 6 44 0x0 -";
    new Status(result);
  }

  @Test
  public void numberOfRowsSuccessfullTest() {
    String result = defaultResult;
    Status status = new Status(result);
    Assert.assertTrue(status.getNumberRows() == 24);
  }

  @Test
  public void numberOfRowsUnSuccessfullTest() {
    String result = defaultResult;
    Status status = new Status(result);
    Assert.assertFalse(status.getNumberRows() == 32);
  }

  @Test
  public void numberOfColumnsSuccessfullTest() {
    String result = defaultResult;
    Status status = new Status(result);
    Assert.assertTrue(status.getNumberColumns() == 80);
  }

  @Test
  public void numberOfColumnsUnSuccessfullTest() {
    String result = defaultResult;
    Status status = new Status(result);
    Assert.assertFalse(status.getNumberColumns() == 81);
  }

  @Test
  public void currentColumnSuccessfullTest() {
    String result = defaultResult;
    Status status = new Status(result);
    Assert.assertTrue(status.getCurrentCursorColumn() == 44);
  }

  @Test
  public void currentColumnUnSuccessfullTest() {
    String result = defaultResult;
    Status status = new Status(result);
    Assert.assertFalse(status.getCurrentCursorColumn() == 45);
  }

  @Test
  public void currentRowSuccessfullTest() {
    String result = defaultResult;
    Status status = new Status(result);
    Assert.assertTrue(status.getCurrentCursorRow() == 6);
  }

  @Test
  public void currentRowUnSuccessfullTest() {
    String result = defaultResult;
    Status status = new Status(result);
    Assert.assertFalse(status.getCurrentCursorRow() == 7);
  }

  @Test
  public void windowIdSuccessfullTest() {
    String result = defaultResult;
    Status status = new Status(result);
    Assert.assertTrue(status.getWindowId().equals("0x0"));
  }

  @Test
  public void windowIdUnSuccessfullTest() {
    String result = defaultResult;
    Status status = new Status(result);
    Assert.assertFalse(status.getWindowId().equals("WATT"));
  }

  @Test
  public void commandExecutionTimeSuccessfullTest() {
    String result = defaultResult;
    Status status = new Status(result);
    Assert.assertTrue(status.getCommanExecutionTime().equals("-"));
  }

  @Test
  public void commandExecutionUnSuccessfullTest() {
    String result = defaultResult;
    new Status(result);
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "StatusTest [defaultResult=" + defaultResult + ", toString()=" + super.toString() + "]";
  }
}
