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
package org.testeditor.fixture.host.util;

import org.testeditor.fixture.host.locators.Locator;
import org.testeditor.fixture.host.locators.LocatorByStartStop;
import org.testeditor.fixture.host.locators.LocatorByWidth;

import java.util.List;

/**
 * This is a utility class for reading multiple lines on a Mainframe Screen. For
 * reading multiple lines, we need a start point and an end point, these start
 * and end points can received through a start row and start column and end row
 * and end column.
 *
 */
public class LineReader {

    public LineReader() {

    }

    /**
     * 
     * @param lines
     *            the rows which should be read.
     * @param locator
     *            see {@link Locator} the start and endpoint representation of
     *            String to be read.
     * @return the excerpted String from start to endpoint
     */
    public String readMultilines(List<String> lines, LocatorByStartStop locator) {
        StringBuffer sb = new StringBuffer();

        int startRow = locator.getStartRow();
        int endRow = locator.getEndRow();
        locator.checkBoundaries();
        for (int i = startRow; i <= endRow; i++) {
            String line;
            line = extracted(lines.get(i));
            if (i == startRow) {
                sb.append(line.substring(locator.getStartColumn()));
            } else if (i == endRow) {
                sb.append(line.substring(0, locator.getEndColumn() + 1));
            } else {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    /**
     * @param line
     *            the row which should be read.
     * @param locator
     *            see {@link Locator} the start and endpoint representation of
     *            String to be read.
     * @return the excerpted String from start to endpoint.
     */
    public String readSingleLine(String line, LocatorByStartStop locator) {
        int offset = 1;
        locator.checkBoundaries();
        String lineExtracted = extracted(line);
        return (lineExtracted.substring(locator.getStartColumn(), locator.getEndColumn() + offset));
    }

    /**
     * @param line
     *            the row which should be read.
     * @param locator
     *            see {@link Locator} the start and endpoint representation of
     *            String to be read.
     * @return the excerpted String from start to endpoint.
     */
    public String readSingleLineWidth(String line, LocatorByWidth locator) {
        String result = null;
        locator.checkBoundaries();
        String lineReplaced = extracted(line);
        if (locator.getWidth() <= lineReplaced.length()) {
            try {
                result = lineReplaced.substring(locator.getStartColumn(),
                        locator.getStartColumn() + locator.getWidth());
            } catch (StringIndexOutOfBoundsException e) {
                throw new RuntimeException("Your chosen argument start column '" + locator.getStartColumn()
                        + "' plus width '" + locator.getWidth() + "' is greater than the maximum column width: '"
                        + lineReplaced.length() + "'");
            }
        } else {
            throw new RuntimeException("Your chosen argument width '" + locator.getWidth()
                    + "' is greater than the actual max column width '" + lineReplaced.length() + "'");
        }

        return (result);
    }

    public static String extracted(String line) {
        String lineReplaced = line.replace("data: ", "");
        return lineReplaced;
    }

}
