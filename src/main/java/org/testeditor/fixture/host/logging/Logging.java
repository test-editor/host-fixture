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

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testeditor.fixture.host.net.Connection;

public class Logging {

    private static final Logger logger = LoggerFactory.getLogger(Connection.class);

    public static void logOutput(List<String> lines, int offsetRow, int offsetColumn) {
        List<String> considerPositionOffsetLines = considerPositionOffset(lines, offsetRow, offsetColumn);

        for (String line : considerPositionOffsetLines) {
            logger.debug(line);
        }
    }

    public static List<String> considerPositionOffset(List<String> lines, int offsetRow, int offsetColumn) {
        int sizeOfLines = lines.size();
        int statusStringLine = sizeOfLines - 2;
        String statusStringOriginal = lines.get(statusStringLine);
        lines.set(statusStringLine, substituteRowAndColumnWithOffset(statusStringOriginal, offsetRow, offsetColumn));
        return lines;
    }

    private static String substituteRowAndColumnWithOffset(String statusStringOriginal, int offsetRow,
            int offsetColumn) {
        Pattern pattern = Pattern.compile(".*\\(.*\\)\\s(\\w+\\s){4}(?<row>\\d+)\\s(?<column>\\d+).*");
        Matcher matcher = pattern.matcher(statusStringOriginal);
        int originalRow = 0;
        int originalColumn = 0;
        int rowWithOffset = 0;
        int columnWithOffset = 0;
        if (matcher.matches()) {
            originalRow = Integer.parseInt(matcher.group("row"));
            originalColumn = Integer.parseInt(matcher.group("column"));
            rowWithOffset = originalRow - offsetRow;
            columnWithOffset = originalColumn - offsetColumn;
        } else {
            logger.error("The given row and column does not match. Got: {}", statusStringOriginal);
        }
        String replacedRowInStatusString = replaceStatusString(statusStringOriginal, "row", pattern,
                String.valueOf(rowWithOffset));
        return replaceStatusString(replacedRowInStatusString, "column", pattern, String.valueOf(columnWithOffset));
    }

    private static String replaceStatusString(String statusStringOriginal, String groupName, Pattern pattern,
            String valueToChange) {
        StringBuilder b = new StringBuilder();
        Matcher matcher = pattern.matcher(statusStringOriginal);
        if (matcher.matches()) {
            int startPositionRow = (matcher.start(groupName) - matcher.start());
            int endPositionRow = (matcher.end(groupName) - matcher.start());
            b.append(statusStringOriginal);
            b.replace(startPositionRow, endPositionRow, valueToChange);
        } else {
            logger.error("The given row and column does not match. Got: {}", statusStringOriginal);
        }
        return b.toString();
    }

}
