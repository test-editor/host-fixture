package org.testeditor.fixture.host.util;

import java.util.List;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.testeditor.fixture.host.locators.LocatorByStartStop;
import org.testeditor.fixture.host.locators.LocatorByWidth;
import org.testeditor.fixture.host.s3270.Status;

public class LineReaderTest {

    private static Status status = new Status("U F U C(google.de) I 2 24 80 2 11 0x0 -");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void readThreeLinesTest() {
        // given
        String expected = " 01/01/2000 14:27:36        AMD MENUE FOR ABCDEFGHIJ          Panelid  - PAN1234"
                + " Coroneradmin = TROLL                                         Terminal - TM12345"
                + " Menukey     = KANONE";
        String elementLocator = "0;0;2;20";
        LocatorByStartStop locator = new LocatorByStartStop(elementLocator, status);
        HostScreen hostScreen = new HostScreen();
        List<String> lines = hostScreen.createHostScreen();

        // when
        LineReader lineReader = new LineReader();
        String actual = lineReader.readMultilines(lines, locator);

        // then
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void readTwoLinesTest() {
        String expected = "_ ABC         PF 1      ABC                                                     "
                + "_ DEF         PF 2      DEF";
        String elementLocator = "9;0;10;26";
        LocatorByStartStop locator = new LocatorByStartStop(elementLocator, status);
        HostScreen hostScreen = new HostScreen();
        List<String> lines = hostScreen.createHostScreen();

        // when
        LineReader lineReader = new LineReader();
        String actual = lineReader.readMultilines(lines, locator);

        // then
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void readMultipleLinesTest() {

        // given
        String expected = "ABC         PF 1      ABC                                                     "
                + "_ DEF         PF 2      DEF                                                     "
                + "_ GHI         PF 3      GHI                                                     "
                + "_ JKL         PF 4      JKL                                                     "
                + "_ MNO         PF 5      MNO                                                     "
                + "_ PQR         PF 6      PQR                                                     "
                + "_ STY         PF 7      STY                                                     "
                + "_ ZKI         PF 8      ZKI                                                     "
                + "_ CBA         PF 9      CBA";

        int startRow = 9;
        int startColumn = 2;
        int endRow = 17;
        int endColumn = 26;

        LocatorByStartStop locator = new LocatorByStartStop(startRow, startColumn, endRow, endColumn, status);
        HostScreen hostScreen = new HostScreen();
        List<String> lines = hostScreen.createHostScreen();

        // when
        LineReader lineReader = new LineReader();
        String actual = lineReader.readMultilines(lines, locator);

        // then
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void readMultipleLinesColumnFailureTest() {

        // given
        int startRow = 0;
        int startColumn = 0;
        int endRow = 23;
        int endColumn = 80;

        LocatorByStartStop locator = new LocatorByStartStop(startRow, startColumn, endRow, endColumn, status);
        HostScreen hostScreen = new HostScreen();
        List<String> lines = hostScreen.createHostScreen();

        // when
        LineReader lineReader = new LineReader();

        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Your chosen column '80' is greater than the maximum column '79'");
        lineReader.readMultilines(lines, locator);
    }

    @Test
    public void readMultipleLinesStartColumnFailureTest() {

        // given
        int startRow = 0;
        int startColumn = 80;
        int endRow = 23;
        int endColumn = 79;

        LocatorByStartStop locator = new LocatorByStartStop(startRow, startColumn, endRow, endColumn, status);
        HostScreen hostScreen = new HostScreen();
        List<String> lines = hostScreen.createHostScreen();

        // when
        LineReader lineReader = new LineReader();

        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Your chosen column '80' is greater than the maximum column '79'");
        lineReader.readMultilines(lines, locator);
    }

    @Test
    public void readMultipleLinesRowFailureTest() {

        // given
        int startRow = 0;
        int startColumn = 0;
        int endRow = 24;
        int endColumn = 79;

        LocatorByStartStop locator = new LocatorByStartStop(startRow, startColumn, endRow, endColumn, status);
        HostScreen hostScreen = new HostScreen();
        List<String> lines = hostScreen.createHostScreen();

        // when
        LineReader lineReader = new LineReader();

        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Your chosen row '24' is greater than the maximum row '23'");
        lineReader.readMultilines(lines, locator);
    }

    @Test
    public void readMultipleLinesStartRowFailureTest() {

        // given
        int startRow = 24;
        int startColumn = 0;
        int endRow = 23;
        int endColumn = 79;

        LocatorByStartStop locator = new LocatorByStartStop(startRow, startColumn, endRow, endColumn, status);
        HostScreen hostScreen = new HostScreen();
        List<String> lines = hostScreen.createHostScreen();

        // when
        LineReader lineReader = new LineReader();

        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Your chosen row '24' is greater than the maximum row '23'");
        lineReader.readMultilines(lines, locator);
    }

    @Test
    public void readSingleLineStartStopTest() {
        // given
        String expected = "Printer";
        int startRow = 3;
        int startColumn = 28;
        int endRow = 3;
        int endColumn = 34;
        LocatorByStartStop locator = new LocatorByStartStop(startRow, startColumn, endRow, endColumn, status);
        HostScreen hostScreen = new HostScreen();
        List<String> lines = hostScreen.createHostScreen();
        String line = lines.get(startRow);

        // when
        LineReader lineReader = new LineReader();
        String actual = lineReader.readSingleLine(line, locator);

        // then
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void readSingleLineWidthTest() {

        // given
        String expected = "Description";
        int startRow = 7;
        int startColumn = 26;
        int width = 11;
        LocatorByWidth locator = new LocatorByWidth(startRow, startColumn, width, status);
        HostScreen hostScreen = new HostScreen();
        List<String> lines = hostScreen.createHostScreen();
        String line = lines.get(startRow);

        // when
        LineReader lineReader = new LineReader();
        String actual = lineReader.readSingleLineWidth(line, locator);

        // then
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void readSingleRowFailureTest() throws RuntimeException {

        // given

        int startRow = 24;
        int startColumn = 0;
        int width = 8;
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Your chosen row width '24' is greater than the actual maximum row width '23'");
        LocatorByWidth locator = new LocatorByWidth(startRow, startColumn, width, status);

        HostScreen hostScreen = new HostScreen();
        List<String> lines = hostScreen.createHostScreen();
        String line = lines.get(startRow);

        // when
        // String line = "data: Menukey";
        LineReader lineReader = new LineReader();
        lineReader.readSingleLineWidth(line, locator);
    }

    @Test
    public void readSingleColumnFailureTest() throws RuntimeException {

        // given
        int startRow = 3;
        int startColumn = 0;
        int width = 8;
        LocatorByWidth locator = new LocatorByWidth(startRow, startColumn, width, status);

        // when
        String line = "data: Menukey";
        LineReader lineReader = new LineReader();
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Your chosen argument width '8' is greater than the actual max column width '7'");
        lineReader.readSingleLineWidth(line, locator);
    }

    @Test
    public void readSingleWidthFailureTest() throws RuntimeException {

        // given
        int startRow = 1;
        int startColumn = 74;
        int width = 8;
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Your chosen start column plus width '82' is greater than the maximum column size'79'");
        LocatorByWidth locator = new LocatorByWidth(startRow, startColumn, width, status);
        HostScreen hostScreen = new HostScreen();
        List<String> lines = hostScreen.createHostScreen();
        String line = lines.get(startRow);

        // when
        LineReader lineReader = new LineReader();
        lineReader.readSingleLineWidth(line, locator);
    }

}
