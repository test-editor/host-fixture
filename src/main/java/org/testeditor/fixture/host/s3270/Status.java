package org.testeditor.fixture.host.s3270;

import org.testeditor.fixture.host.exceptions.StatusNotFoundException;
import org.testeditor.fixture.host.s3270.options.TerminalMode;
import org.testeditor.fixture.host.s3270.statusformat.ConnectionState;
import org.testeditor.fixture.host.s3270.statusformat.EmulatorMode;
import org.testeditor.fixture.host.s3270.statusformat.FieldProtection;
import org.testeditor.fixture.host.s3270.statusformat.KeyboardState;
import org.testeditor.fixture.host.s3270.statusformat.ScreenFormatting;

/**
 * The returned status Information from s3270/ws3270 application after execution
 * of a s3270 command. It consists of 2 lines. The first line represents the
 * status message and the second line a return code like ok or error. The status
 * message consists of 12 blank-separated fields. Here is an example for a
 * complete status:
 * <ul>
 * <li>U U U C(abcdefg.google.mainfraim.com) I 2 24 80 11 45 0x0 -</li>
 * <li>ok</li>
 *
 * The documentation about the fields can be found here: See <a href=
 * "http://x3270.bgp.nu/x3270-script.html#Status-Format">http://x3270.bgp.nu/x3270-script.html#Status-Format</a>
 */
public class Status {

  private KeyboardState keyboardState;
  private ScreenFormatting screenFormatting;
  private FieldProtection fieldProtection;
  private ConnectionState connectionState;
  private EmulatorMode emulatorMode;
  private TerminalMode mode;
  private int numberRows;
  private int numberColumns;
  private int currentCursorRow;
  private int currentCursorColumn;
  private String windowId;
  private String commanExecutionTime;

  public Status(String status) {
    createStatus(status);
  }

  private void createStatus(String status) {
    String[] splittedStatus = status.split(" ");
    createKeyBoardState(splittedStatus[0]);
    createScreenFormatting(splittedStatus[1]);
    createFieldProtection(splittedStatus[2]);
    createConnectionState(splittedStatus[3]);
    createEmulatorMode(splittedStatus[4]);
    createModelNumber(splittedStatus[5]);
    createNumberOfRows(splittedStatus[6]);
    createNumberOfColumns(splittedStatus[7]);
    createCursorRow(splittedStatus[8]);
    createCursorColumn(splittedStatus[9]);
    createWindowId(splittedStatus[10]);
    createCommandExecutionTime(splittedStatus[11]);
  }

  private void createKeyBoardState(String input) {
    try {
      setKeyboardState(KeyboardState.getKeyboardState(input));
    } catch (StatusNotFoundException e) {
      throw new StatusNotFoundException(e.getMessage());
    }
  }

  private void createScreenFormatting(String input) {
    try {
      setScreenFormatting(ScreenFormatting.getScreenFormatting(input));
    } catch (StatusNotFoundException e) {
      throw new StatusNotFoundException(e.getMessage());
    }
  }

  private void createFieldProtection(String input) {
    try {
      setFieldProtection(FieldProtection.getFieldProtection(input));
    } catch (StatusNotFoundException e) {
      throw new StatusNotFoundException(e.getMessage());
    }
  }

  private void createConnectionState(String input) {
    try {
      String a = input.substring(0, 1);
      setConnectionState(ConnectionState.getConnectionState(a));
    } catch (StatusNotFoundException e) {
      throw new StatusNotFoundException(e.getMessage());
    }
  }

  private void createEmulatorMode(String input) {
    try {
      setEmulatorMode(EmulatorMode.getEmulatorMode(input));
    } catch (StatusNotFoundException e) {
      throw new StatusNotFoundException(e.getMessage());
    }
  }

  private void createModelNumber(String input) {
    try {
      setMode(TerminalMode.getTerminalMode(Integer.parseInt(input)));
    } catch (StatusNotFoundException e) {
      throw new StatusNotFoundException(e.getMessage());
    }
  }

  private void createNumberOfRows(String input) {
    try {
      setNumberRows(Integer.parseInt(input));
    } catch (StatusNotFoundException e) {
      throw new StatusNotFoundException(e.getMessage());
    }
  }

  private void createNumberOfColumns(String input) {
    try {
      setNumberColumns(Integer.parseInt(input));
    } catch (StatusNotFoundException e) {
      throw new StatusNotFoundException(e.getMessage());
    }
  }

  private void createCursorRow(String input) {
    try {
      setCurrentCursorRow(Integer.parseInt(input));
    } catch (StatusNotFoundException e) {
      throw new StatusNotFoundException(e.getMessage());
    }
  }

  private void createCursorColumn(String input) {
    try {
      setCurrentCursorColumn(Integer.parseInt(input));
    } catch (StatusNotFoundException e) {
      throw new StatusNotFoundException(e.getMessage());
    }
  }

  private void createWindowId(String input) {
    try {
      setWindowId(input);
    } catch (StatusNotFoundException e) {
      throw new StatusNotFoundException(e.getMessage());
    }
  }

  private void createCommandExecutionTime(String input) {
    try {
      setCommanExecutionTime(input);
    } catch (StatusNotFoundException e) {
      throw new StatusNotFoundException(e.getMessage());
    }
  }

  /**
   * @return the keyboardState
   */
  public KeyboardState getKeyboardState() {
    return keyboardState;
  }

  /**
   * @param keyboardState
   *          the keyboardState to set
   */
  public void setKeyboardState(KeyboardState keyboardState) {
    this.keyboardState = keyboardState;
  }

  /**
   * @return the screenFormatting
   */
  public ScreenFormatting getScreenFormatting() {
    return screenFormatting;
  }

  /**
   * @param screenFormatting
   *          the screenFormatting to set
   */
  public void setScreenFormatting(ScreenFormatting screenFormatting) {
    this.screenFormatting = screenFormatting;
  }

  /**
   * @return the fieldProtection
   */
  public FieldProtection getFieldProtection() {
    return fieldProtection;
  }

  /**
   * @param fieldProtection
   *          the fieldProtection to set
   */
  public void setFieldProtection(FieldProtection fieldProtection) {
    this.fieldProtection = fieldProtection;
  }

  /**
   * @return the connectionState
   */
  public ConnectionState getConnectionState() {
    return connectionState;
  }

  /**
   * @param connectionState
   *          the connectionState to set
   */
  public void setConnectionState(ConnectionState connectionState) {
    this.connectionState = connectionState;
  }

  /**
   * @return the emulatorMode
   */
  public EmulatorMode getEmulatorMode() {
    return emulatorMode;
  }

  /**
   * @param emulatorMode
   *          the emulatorMode to set
   */
  public void setEmulatorMode(EmulatorMode emulatorMode) {
    this.emulatorMode = emulatorMode;
  }

  /**
   * @return the mode
   */
  public TerminalMode getMode() {
    return mode;
  }

  /**
   * @param mode
   *          the mode to set
   */
  public void setMode(TerminalMode mode) {
    this.mode = mode;
  }

  /**
   * @return the numberRows
   */
  public int getNumberRows() {
    return numberRows;
  }

  /**
   * @param numberRows
   *          the numberRows to set
   */
  public void setNumberRows(int numberRows) {
    this.numberRows = numberRows;
  }

  /**
   * @return the numberColumns
   */
  public int getNumberColumns() {
    return numberColumns;
  }

  /**
   * @param numberColumns
   *          the numberColumns to set
   */
  public void setNumberColumns(int numberColumns) {
    this.numberColumns = numberColumns;
  }

  /**
   * @return the currentCursorRow
   */
  public int getCurrentCursorRow() {
    return currentCursorRow;
  }

  /**
   * @param currentCursorRow
   *          the currentCursorRow to set
   */
  public void setCurrentCursorRow(int currentCursorRow) {
    this.currentCursorRow = currentCursorRow;
  }

  /**
   * @return the currentCursorColumn
   */
  public int getCurrentCursorColumn() {
    return currentCursorColumn;
  }

  /**
   * @param currentCursorColumn
   *          the currentCursorColumn to set
   */
  public void setCurrentCursorColumn(int currentCursorColumn) {
    this.currentCursorColumn = currentCursorColumn;
  }

  /**
   * @return the windowId
   */
  public String getWindowId() {
    return windowId;
  }

  /**
   * @param windowId
   *          the windowId to set
   */
  public void setWindowId(String windowId) {
    this.windowId = windowId;
  }

  /**
   * @return the commanExecutionTime
   */
  public String getCommanExecutionTime() {
    return commanExecutionTime;
  }

  /**
   * @param commanExecutionTime
   *          the commanExecutionTime to set
   */
  public void setCommanExecutionTime(String commanExecutionTime) {
    this.commanExecutionTime = commanExecutionTime;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Status [keyboardState=\"" + keyboardState + "\", screenFormatting=\"" + screenFormatting
        + "\", fieldProtection=\"" + fieldProtection + "\", connectionState=\"" + connectionState
        + "\", emulatorMode=\"" + emulatorMode + "\", mode=\"" + mode + "\", numberRows=\""
        + numberRows + "\", numberColumns=\"" + numberColumns + "\", currentCursorRow=\""
        + currentCursorRow + "\", currentCursorColumn=\"" + currentCursorColumn + "\", windowId=\""
        + windowId + "\", commanExecutionTime=\"" + commanExecutionTime + "\"]";
  }
}