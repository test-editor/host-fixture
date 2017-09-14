/*******************************************************************************
 * Copyright (c) 2012 - 2017 Signal Iduna Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Signal Iduna Corporation - initial API and implementation
 * akquinet AG
 * itemis AG
 *******************************************************************************/

package org.testeditor.fixture.host.logging;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class LoggingTest {

    /**
     * Creates a typical output result of a 3270 command call.
     */
    private List<String> createDatalines(int row, int column) {
        List<String> datalines = new ArrayList<String>();
        datalines.add("test1");
        datalines.add("test2");
        datalines.add("test3");
        datalines.add("test4");
        datalines.add("test5");
        datalines.add("test6");
        datalines.add("U F U C(abcdefg.google.mainframe.de) I 2 24 80 " + row + " " + column + " 0x0 -");
        datalines.add("ok");
        return datalines;
    }

    @Test
    public void LogTransitionSameLength() {
        // given
        int row = 3;
        int column = 8;
        int rowWithOffset = 4;
        int columnWithOffset = 9;
        List<String> datalines = createDatalines(row, column);

        int offsetRow = -1;
        int offsetColumn = -1;

        // when
        List<String> considerPositionOffset = Logging.considerPositionOffset(datalines, offsetRow, offsetColumn);

        // then
        Assert.assertEquals(considerPositionOffset.get(6),
                "U F U C(abcdefg.google.mainframe.de) I 2 24 80 " + rowWithOffset + " " + columnWithOffset + " 0x0 -");
    }

    @Test
    public void LogRowTransitionReductionLength() {
        // given
        int row = 30;
        int column = 8;
        int rowWithOffset = 4;
        int columnWithOffset = 9;
        List<String> datalines = createDatalines(row, column);

        int offsetRow = 26;
        int offsetColumn = -1;

        // when
        List<String> considerPositionOffset = Logging.considerPositionOffset(datalines, offsetRow, offsetColumn);

        // then
        Assert.assertEquals(considerPositionOffset.get(6),
                "U F U C(abcdefg.google.mainframe.de) I 2 24 80 " + rowWithOffset + " " + columnWithOffset + " 0x0 -");
    }

    @Test
    public void LogColumnTransitionReductionLength() {
        // given
        int row = 1;
        int column = 80;
        int rowWithOffset = 2;
        int columnWithOffset = 1;
        List<String> datalines = createDatalines(row, column);

        int offsetRow = -1;
        int offsetColumn = 79;

        // when
        List<String> considerPositionOffset = Logging.considerPositionOffset(datalines, offsetRow, offsetColumn);

        // then
        Assert.assertEquals(considerPositionOffset.get(6),
                "U F U C(abcdefg.google.mainframe.de) I 2 24 80 " + rowWithOffset + " " + columnWithOffset + " 0x0 -");
    }

    @Test
    public void LogRowTransitionIncreaseLength() {
        // given
        int row = 1;
        int column = 8;
        int rowWithOffset = 11;
        int columnWithOffset = 9;
        List<String> datalines = createDatalines(row, column);

        int offsetRow = -10;
        int offsetColumn = -1;

        // when
        List<String> considerPositionOffset = Logging.considerPositionOffset(datalines, offsetRow, offsetColumn);

        // then
        Assert.assertEquals(considerPositionOffset.get(6),
                "U F U C(abcdefg.google.mainframe.de) I 2 24 80 " + rowWithOffset + " " + columnWithOffset + " 0x0 -");
    }

    @Test
    public void LogColumnTransitionIncreaseLength() {
        // given
        int row = 1;
        int column = 8;
        int rowWithOffset = 2;
        int columnWithOffset = 108;
        List<String> datalines = createDatalines(row, column);

        int offsetRow = -1;
        int offsetColumn = -100;

        // when
        List<String> considerPositionOffset = Logging.considerPositionOffset(datalines, offsetRow, offsetColumn);

        // then
        Assert.assertEquals(considerPositionOffset.get(6),
                "U F U C(abcdefg.google.mainframe.de) I 2 24 80 " + rowWithOffset + " " + columnWithOffset + " 0x0 -");
    }

}
