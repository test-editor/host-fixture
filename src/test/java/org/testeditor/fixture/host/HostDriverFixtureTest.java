package org.testeditor.fixture.host;

import org.junit.Assert;
import org.junit.Test;
import org.testeditor.fixture.host.s3270.Status;
import org.testeditor.fixture.host.s3270.options.TerminalMode;
import org.testeditor.fixture.host.s3270.statusformat.ConnectionState;
import org.testeditor.fixture.host.s3270.statusformat.EmulatorMode;
import org.testeditor.fixture.host.s3270.statusformat.FieldProtection;
import org.testeditor.fixture.host.s3270.statusformat.KeyboardState;
import org.testeditor.fixture.host.s3270.statusformat.ScreenFormatting;

public class HostDriverFixtureTest {

  private final int STANDARD_ROW = Integer.parseInt(System.getenv("STANDARD_ROW"));
  private final int STANDARD_COLUMN = Integer.parseInt(System.getenv("STANDARD_COLUMN"));
  private final int MAX_ROWS = 24;
  private final int MAX_COLUMNS = 80;
  private final String STANDARD_WINDOW_ID = "0x0";

  @Test
  public void connectionTest() {
    String S2370_PATH = System.getenv("S3270_PATH");
    String hostUrl = System.getenv("HOST_URL");
    int hostPort = Integer.parseInt(System.getenv("HOST_PORT"));
    HostDriverFixture hdf = new HostDriverFixture();
    hdf.connect(S2370_PATH, hostUrl, hostPort);
    hdf.disconnect();

  }

  @Test
  public void statusTest() {

    // given
    String S2370_PATH = System.getenv("S3270_PATH");
    String hostUrl = System.getenv("HOST_URL");
    int hostPort = Integer.parseInt(System.getenv("HOST_PORT"));

    HostDriverFixture hdf = new HostDriverFixture();
    hdf.connect(S2370_PATH, hostUrl, hostPort);

    // when
    Status status = hdf.getStatus();

    // then
    Assert.assertTrue(status.getKeyboardState().name().equals(KeyboardState.UNLOCKED.name()));
    Assert
        .assertTrue(status.getScreenFormatting().name().equals(ScreenFormatting.FORMATTED.name()));
    Assert
        .assertTrue(status.getFieldProtection().name().equals(FieldProtection.UNPROTECTED.name()));
    Assert.assertTrue(status.getConnectionState().name().equals(ConnectionState.CONNECTED.name()));
    Assert.assertTrue(status.getEmulatorMode().name().equals(EmulatorMode.M3270_MODE.name()));
    Assert.assertTrue(status.getMode().name().equals(TerminalMode.MODE_24x80.name()));
    Assert.assertTrue(status.getNumberRows() == MAX_ROWS);
    Assert.assertTrue(status.getNumberColumns() == MAX_COLUMNS);
    Assert.assertTrue(status.getNumberColumns() == MAX_COLUMNS);
    Assert.assertTrue(status.getCurrentCursorRow() == STANDARD_ROW);
    Assert.assertTrue(status.getCurrentCursorColumn() == STANDARD_COLUMN);
    Assert.assertTrue(status.getWindowId().equals(STANDARD_WINDOW_ID));
    Assert.assertTrue(status.getCommanExecutionTime().equals("-"));
    hdf.disconnect();

  }

}
