package org.testeditor.fixture.host.options;

import org.junit.Assert;
import org.junit.Test;
import org.testeditor.fixture.host.s3270.options.TerminalMode;

public class TerminalModeTest {

    @Test
    public void terminalModeTest24x80() {
        TerminalMode mode = TerminalMode.getTerminalMode(2);

        int maxColumn = mode.getMaxColumn();
        int maxRow = mode.getMaxRow();

        Assert.assertEquals(80, maxColumn);
        Assert.assertEquals(24, maxRow);
    }

    @Test
    public void terminalModeTest32x80() {
        TerminalMode mode = TerminalMode.getTerminalMode(3);

        int maxColumn = mode.getMaxColumn();
        int maxRow = mode.getMaxRow();

        Assert.assertEquals(80, maxColumn);
        Assert.assertEquals(32, maxRow);
    }

    @Test
    public void terminalModeTest43x80() {
        TerminalMode mode = TerminalMode.getTerminalMode(4);

        int maxColumn = mode.getMaxColumn();
        int maxRow = mode.getMaxRow();

        Assert.assertEquals(80, maxColumn);
        Assert.assertEquals(43, maxRow);
    }

    @Test
    public void terminalModeTest27x132() {
        TerminalMode mode = TerminalMode.getTerminalMode(5);

        int maxColumn = mode.getMaxColumn();
        int maxRow = mode.getMaxRow();

        Assert.assertEquals(132, maxColumn);
        Assert.assertEquals(27, maxRow);
    }

}
