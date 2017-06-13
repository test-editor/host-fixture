package org.testeditor.fixture.host;

<<<<<<< HEAD
=======
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testeditor.fixture.host.s3270.options.CharacterSet.CHAR_GERMAN_EURO;
import static org.testeditor.fixture.host.s3270.options.TerminalMode.MODE_24x80;
import static org.testeditor.fixture.host.s3270.options.TerminalType.TYPE_3279;

import org.testeditor.fixture.host.net.Connection;

>>>>>>> feature/newStructure
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

<<<<<<< HEAD
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
=======
    @Test
    public void connectionSuccessfulTest() {

        // given
        String S3270_PATH = "S3270_PATH";
        String hostUrl = "HOST_URL";
        int hostPort = 1234;

        Connection con = mock(Connection.class);
        HostDriverFixture hdf = new HostDriverFixture(con);
        when(con.isConnected()).thenReturn(true);

        // when
        boolean connected = hdf.connect(S3270_PATH, hostUrl, hostPort);

        // then
        verify(con).connect(S3270_PATH, hostUrl, hostPort, TYPE_3279, MODE_24x80, CHAR_GERMAN_EURO);
        Assert.assertTrue(connected);
    }

    @Test
    public void connectionUnsuccessfulTest() {

        // given
        String S3270_PATH = "S3270_PATH";
        String hostUrl = "HOST_URL";
        int hostPort = 1234;

        Connection con = mock(Connection.class);
        HostDriverFixture hdf = new HostDriverFixture(con);
        when(con.isConnected()).thenReturn(false);

        // when
        boolean connected = hdf.connect(S3270_PATH, hostUrl, hostPort);

        // then
        verify(con).connect(S3270_PATH, hostUrl, hostPort, TYPE_3279, MODE_24x80, CHAR_GERMAN_EURO);
        Assert.assertFalse(connected);
    }

    @Test
    public void diconnectionSuccessfulTest() {

        // given
        Connection con = mock(Connection.class);
        HostDriverFixture hdf = new HostDriverFixture(con);
        when(con.disconnect()).thenReturn(true);

        // when
        boolean disconnected = hdf.disconnect();

        // then
        Assert.assertTrue(disconnected);
    }

    @Test(expected = RuntimeException.class)
    public void diconnectionUnsuccessfulTest() {

        // given
        Connection con = new Connection();
        HostDriverFixture hdf = new HostDriverFixture(con);

        // when
        boolean disconnected = hdf.disconnect();
>>>>>>> feature/newStructure

        // then
        Assert.assertTrue(disconnected);
    }

}
