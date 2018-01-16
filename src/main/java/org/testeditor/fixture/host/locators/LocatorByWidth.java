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
package org.testeditor.fixture.host.locators;

import java.lang.reflect.Constructor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.testeditor.fixture.host.s3270.Status;
import org.testeditor.fixture.host.screen.Offset;

/**
 * The Locator is a representation of a range on a Mainframe host screen.<br>
 * This could be represented through a declaration of a start and an end point.
 * <ol>
 * <li><b>start row ; start column ; width (number of characters)</b></li>
 * </ol>
 *
 * See {@link LocatorStrategy} for differentiation each of these reprentations
 */
public class LocatorByWidth implements Locator {

    private int startRow;
    private int startColumn;
    private int width;
    private int endColumn;
    private int maxColumn;
    private int maxRow;
    private int offsetColumn;
    private int offsetRow;
    private int startColumnWithOffset;
    private int startRowWithOffset;

    /**
     * @return the startColumnWithOffset
     */
    public int getStartColumnWithOffset() {
        return startColumnWithOffset;
    }

    /**
     * @return the startRowWithOffset
     */
    public int getStartRowWithOffset() {
        return startRowWithOffset;
    }

    /**
     * @return the endColumnWithOffset
     */
    public int getEndColumnWithOffset() {
        return endColumnWithOffset;
    }

    private int endColumnWithOffset;

    /**
     * This is a {@link Constructor} for the representation of a
     * {@link LocatorStrategy} WIDTH. That means we have a start(x,y) position
     * and a width (number of characters).
     * 
     * @param startRow
     *            the start y position representation of a screen range.
     * @param startColumn
     *            the start x position representation of a screen range.
     * @param width
     *            number of characters to be read.
     * @param status
     *            the status of the executed action
     * @param offsetRow
     *            The offset for the startpoint row in dependence on the zero
     *            origin of the host screen.
     * @param offsetColumn
     *            The offset for the startpoint column in dependence on the zero
     *            origin of the host screen.
     */
    public LocatorByWidth(int startRow, int startColumn, int width, Status status, Offset offset) {
        this.offsetRow = offset.getOffsetRow();
        this.offsetColumn = offset.getOffsetColumn();
        this.maxRow = status.getNumberRows();
        this.maxColumn = status.getNumberColumns();
        this.startColumn = startColumn;
        this.startColumnWithOffset = startColumn + offsetColumn;
        this.startRow = startRow;
        this.startRowWithOffset = startRow + offsetRow;
        this.width = width;
        this.endColumn = startColumn + width;
        this.endColumnWithOffset = startColumnWithOffset + width;
        checkBoundaries();
    }

    /**
     * This is a {@link Constructor} for the representation of a
     * {@link LocatorStrategy} WIDTH. That means we have a start(x,y) position
     * and a width (number of characters) in the form "2;5;34" where the first
     * integer represents the row, the second integer represents the column and
     * the third integer represents the width.
     * 
     * @param elementLocator
     *            A String in the form "2;5,34", where the first integer "2"
     *            represents the row, the second integer "5" represents the
     *            column and the third integer "34" represents the width..
     * @param status
     *            the status of the executed action
     * @param offsetRow
     *            The offset for the startpoint row in dependence on the zero
     *            origin of the host screen.
     * @param offsetColumn
     *            The offset for the startpoint column in dependence on the zero
     *            origin of the host screen.
     */
    public LocatorByWidth(String elementLocator, Status status, Offset offset) {
        this.maxRow = status.getNumberRows();
        this.maxColumn = status.getNumberColumns();
        this.offsetRow = offset.getOffsetRow();
        this.offsetColumn = offset.getOffsetColumn();
        initializeStartRowColumnAndWidth(elementLocator);
    }

    public int getStartColumn() {
        return startColumn;
    }

    public int getStartRow() {
        return startRow;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return the offsetColumn
     */
    public int getOffsetColumn() {
        return offsetColumn;
    }

    /**
     * @return the offsetRow
     */
    public int getOffsetRow() {
        return offsetRow;
    }

    /**
     * This method creates a Locator for the {@link LocatorStrategy} WIDTH
     * 
     * @param elementLocator
     *            in the form "1;2;45"
     * @return {@link LocatorByWidth} filled with the following fields:
     *         startRow, startColumn, width (number of characters to be read).
     */
    private void initializeStartRowColumnAndWidth(String elementLocator) {
        Pattern locatorPattern = Pattern.compile("(?<startRow>\\d+);(?<startColumn>\\d+);(?<width>\\d+)");
        Matcher matcher = locatorPattern.matcher(elementLocator);
        if (matcher.matches()) {
            this.startRow = Integer.parseInt(matcher.group("startRow"));
            this.startColumn = Integer.parseInt(matcher.group("startColumn"));
            this.startRowWithOffset = this.startRow + this.offsetRow;
            this.startColumnWithOffset = this.startColumn + this.offsetColumn;
            this.width = Integer.parseInt(matcher.group("width"));
            this.endColumn = this.startColumn + this.width;
            this.endColumnWithOffset = this.startColumnWithOffset + this.width;
            checkBoundaries();
        } else {
            throw new IllegalArgumentException(
                    "The provided locator did not match the expected pattern \"x;y;z\" where x, y and z are integer values. Got: "
                            + elementLocator);
        }
    }

    @Override
    public void checkBoundaries() {
        // because we begin to count startColumn and startRow with 0
        if (startColumn > maxColumn) {
            throw new IllegalArgumentException("Your chosen column '" + startColumn
                    + "' is greater than the maximum column '" + (maxColumn - 1) + "'");
        }
        if (startRow > maxRow) {
            throw new IllegalArgumentException("Your chosen row width '" + startRow
                    + "' is greater than the actual maximum row width '" + maxRow + "'");
        }
        if (endColumn > maxColumn) {
            throw new IllegalArgumentException("Your chosen start column plus width '" + endColumn
                    + "' is greater than the maximum column size'" + maxColumn + "'");
        }
    }

}
