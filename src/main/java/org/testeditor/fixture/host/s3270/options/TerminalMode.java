package org.testeditor.fixture.host.s3270.options;

/**
 * The model number, which specifies the number of rows and columns.
 * <ul>
 * <li>Model 2 specifies a mainframe Terminal Emulator window with 24 rows and
 * 80 columns.</li>
 * <li>Model 3 specifies a mainframe Terminal Emulator window with 32 rows and
 * 80 columns.</li>
 * <li>Model 4 specifies a mainframe Terminal Emulator window with 43 rows and
 * 80 columns.</li>
 * <li>Model 5 specifies a mainframe Terminal Emulator window with 27 rows and
 * 132 columns.</li>
 * </ul>
 * <p>
 * Model 4 is the default when nothing will be specified under the application
 * S3270.
 *
 */
public enum TerminalMode {
  MODE_24x80(2), MODE_32x80(3), MODE_43x80(4), MODE_27x132(5);
  private int mode;

  private TerminalMode(int mode) {
    this.mode = mode;
  }

  public int getMode() {
    return mode;
  }
}
