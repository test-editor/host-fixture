package org.testeditor.fixture.host.options;

import org.testeditor.fixture.host.s3270.options.TerminalMode;

import org.junit.Assert;
import org.junit.Test;

public class TerminalModeTest {

    @Test
    public void terminalModeTest24x80() {
        TerminalMode mode = TerminalMode.getTerminalMode(2);

        int maxColumn = mode.getMaxColumn();
        int maxRow = mode.getMaxRow();

        Assert.assertTrue(maxColumn == 80);
        Assert.assertTrue(maxRow == 24);
    }

    @Test
    public void terminalModeTest32x80() {
        TerminalMode mode = TerminalMode.getTerminalMode(3);

        int maxColumn = mode.getMaxColumn();
        int maxRow = mode.getMaxRow();

        Assert.assertTrue(maxColumn == 80);
        Assert.assertTrue(maxRow == 32);
    }

    @Test
    public void terminalModeTest43x80() {
        TerminalMode mode = TerminalMode.getTerminalMode(4);

        int maxColumn = mode.getMaxColumn();
        int maxRow = mode.getMaxRow();

        Assert.assertTrue(maxColumn == 80);
        Assert.assertTrue(maxRow == 43);
    }

    @Test
    public void terminalModeTest27x132() {
        TerminalMode mode = TerminalMode.getTerminalMode(5);

        int maxColumn = mode.getMaxColumn();
        int maxRow = mode.getMaxRow();

        Assert.assertTrue(maxColumn == 132);
        Assert.assertTrue(maxRow == 27);
    }

}
