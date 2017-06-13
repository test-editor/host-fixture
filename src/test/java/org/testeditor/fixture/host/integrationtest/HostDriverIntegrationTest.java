package org.testeditor.fixture.host.integrationtest;

import org.testeditor.fixture.host.HostDriverFixture;
import org.testeditor.fixture.host.s3270.Status;
import org.testeditor.fixture.host.s3270.options.TerminalMode;
import org.testeditor.fixture.host.s3270.statusformat.ConnectionState;
import org.testeditor.fixture.host.s3270.statusformat.EmulatorMode;
import org.testeditor.fixture.host.s3270.statusformat.FieldProtection;
import org.testeditor.fixture.host.s3270.statusformat.KeyboardState;
import org.testeditor.fixture.host.s3270.statusformat.ScreenFormatting;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HostDriverIntegrationTest {

    private final int STANDARD_ROW = Integer.parseInt(System.getenv("STANDARD_ROW"));
    private final int STANDARD_COLUMN = Integer.parseInt(System.getenv("STANDARD_COLUMN"));
    private final int MAX_ROWS = 24;
    private final int MAX_COLUMNS = 80;
    private final String STANDARD_WINDOW_ID = "0x0";
    String S2370_PATH;
    String hostUrl;
    int hostPort;

    @Before
    public void intialize() {
        S2370_PATH = System.getenv("S3270_PATH");
        hostUrl = System.getenv("HOST_URL");
        hostPort = Integer.parseInt(System.getenv("HOST_PORT"));
    }

    // @Ignore("manual execution only in special environments")
    @Test
    /**
     * This integrationtest is only for a special environment and not for
     * executing in a gradle test environment. You can uncomment the
     * annotation @Test to test this special environment. Please do not delete
     * this file.
     */
    public void connectionTest() {

        HostDriverFixture hdf = new HostDriverFixture();
        Assert.assertTrue(hdf.connect(S2370_PATH, hostUrl, hostPort));
        Status status = hdf.getStatus();
        Assert.assertNotNull(status);
        Assert.assertTrue(status.getFieldProtection() == FieldProtection.UNPROTECTED);
        Assert.assertTrue(status.getConnectionState() == ConnectionState.CONNECTED);
        Assert.assertTrue(status.getKeyboardState() == KeyboardState.UNLOCKED);
        Assert.assertTrue(status.getScreenFormatting() == ScreenFormatting.FORMATTED);
        Assert.assertTrue(hdf.disconnect());
    }

    // @Ignore("manual execution only in special environments")
    @Test
    public void statusTest() {

        // given
        HostDriverFixture hdf = new HostDriverFixture();
        hdf.connect(S2370_PATH, hostUrl, hostPort);

        // when
        Status status = hdf.getStatus();

        // then
        Assert.assertTrue(status.getKeyboardState().name().equals(KeyboardState.UNLOCKED.name()));
        Assert.assertTrue(status.getScreenFormatting().name().equals(ScreenFormatting.FORMATTED.name()));
        Assert.assertTrue(status.getFieldProtection().name().equals(FieldProtection.UNPROTECTED.name()));
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
