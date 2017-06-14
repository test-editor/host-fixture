package org.testeditor.fixture.host.integrationtest;

import org.testeditor.fixture.host.HostDriverFixture;
import org.testeditor.fixture.host.net.HelperTool;
import org.testeditor.fixture.host.s3270.Status;
import org.testeditor.fixture.host.s3270.options.TerminalMode;
import org.testeditor.fixture.host.s3270.statusformat.ConnectionState;
import org.testeditor.fixture.host.s3270.statusformat.EmulatorMode;
import org.testeditor.fixture.host.s3270.statusformat.FieldProtection;
import org.testeditor.fixture.host.s3270.statusformat.KeyboardState;
import org.testeditor.fixture.host.s3270.statusformat.ScreenFormatting;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

public class HostDriverIntegrationTest {

    private final int STANDARD_ROW = Integer.parseInt(System.getenv("STANDARD_ROW"));
    private final int STANDARD_COLUMN = Integer.parseInt(System.getenv("STANDARD_COLUMN"));
    private final int MAX_ROWS = 24;
    private final int MAX_COLUMNS = 80;
    private final String STANDARD_WINDOW_ID = "0x0";
    private final String s3270Path = System.getenv("S3270_PATH");
    private final String hostUrl = System.getenv("HOST_URL");
    private final int hostPort = Integer.parseInt(System.getenv("HOST_PORT"));

    private HostDriverFixture hdf;

    @Before
    public void intialize() {
        // manual execution only in special environments
        Assume.assumeTrue("This is not a Windows OS - ignoring test", HelperTool.isOsWindows());
        Assume.assumeTrue("The path to the s3270 driver is not present - ignoring test",
                HelperTool.isS3270DriverPresent(s3270Path));
        hdf = new HostDriverFixture();
        Assert.assertTrue(hdf.connect(s3270Path, hostUrl, hostPort));
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
        Status status = hdf.getStatus();

        // then
        Assert.assertNotNull(status);
        Assert.assertTrue(status.getFieldProtection() == FieldProtection.UNPROTECTED);
        Assert.assertTrue(status.getConnectionState() == ConnectionState.CONNECTED);
        Assert.assertTrue(status.getKeyboardState() == KeyboardState.UNLOCKED);
        hdf.disconnect();
    }

    @Test
    public void statusTest() {

        // given
        // hostDriverFixture in init

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

    @Test
    public void typeIntoTest() {
        // given
        // hostDriverFixture in init

        // when
        hdf.typeAt("äöüßabcdefg", STANDARD_ROW, STANDARD_COLUMN);

        // then
        // on screen there will be typed some Umlaut characters, the test can
        // only be verified not before there will be implemented a verification
        // method.

        hdf.disconnect();
    }
}
