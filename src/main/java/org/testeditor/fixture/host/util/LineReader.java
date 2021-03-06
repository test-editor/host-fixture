/*******************************************************************************
 * Copyright (c) 2012 - 2018 Signal Iduna Corporation and others.
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

import java.util.List;

import org.testeditor.fixture.core.FixtureException;
import org.testeditor.fixture.host.locators.Locator;
import org.testeditor.fixture.host.locators.LocatorByStartStop;
import org.testeditor.fixture.host.locators.LocatorByWidth;

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
     * @throws FixtureException 
     */
    public String readMultilines(List<String> lines, LocatorByStartStop locator) {
        StringBuffer sb = new StringBuffer();

        int startRow = locator.getStartRowWithOffset();
        int endRow = locator.getEndRowWithOffset();
        locator.checkBoundaries();
        for (int i = startRow; i <= endRow; i++) {
            String line;
            line = extracted(lines.get(i));
            if (i == startRow) {
                sb.append(line.substring(locator.getStartColumnWithOffset()));
            } else if (i == endRow) {
                sb.append(line.substring(0, locator.getEndColumnWithOffset() + 1));
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
     * @throws FixtureException 
     */
    public String readSingleLine(String line, LocatorByStartStop locator) {
        // because String.subString begins at 0
        int offsetForSubstring = 1;
        int startColumn = locator.getStartColumnWithOffset();
        int endColumn = locator.getEndColumnWithOffset();
        locator.checkBoundaries();
        String lineExtracted = extracted(line);
        return (lineExtracted.substring(startColumn, endColumn + offsetForSubstring));
    }

    /**
     * @param line
     *            The row which should be read, this value includes offset.
     * @param locator
     *            See {@link Locator} the start and endpoint representation of
     *            String to be read.
     * @param offsetRow
     *            The offset for the startpoint row in dependence on the zero
     *            origin of the host screen.
     * @param offsetColumn
     *            The offset for the startpoint column in dependence on the zero
     *            origin of the host screen.
     * 
     * @return the excerpted String from start to endpoint.
     */
    public String readSingleLineWidth(String line, LocatorByWidth locator) {
        String result = null;
        int startColumn = locator.getStartColumnWithOffset();
        locator.checkBoundaries();
        String lineReplaced = extracted(line);
        if (locator.getWidth() <= lineReplaced.length()) {
            try {
                result = lineReplaced.substring(startColumn, startColumn + locator.getWidth());
            } catch (StringIndexOutOfBoundsException e) {
                throw new IllegalArgumentException(
                        "Your chosen argument start column '" + startColumn + "' plus width '" + locator.getWidth()
                                + "' is greater than the maximum column width: '" + lineReplaced.length() + "'");
            }
        } else {
            throw new IllegalArgumentException("Your chosen argument width '" + locator.getWidth()
                    + "' is greater than the actual max column width '" + lineReplaced.length() + "'");
        }

        return (result);
    }

    public static String extracted(String line) {
        String lineReplaced = line.replace("data: ", "");
        return lineReplaced;
    }

}
