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
 * The LocatorByStartStop is a representation of a range on a mainframe host
 * screen.<br>
 * This could be represented through a declaration of a start and an end point.
 * <ol>
 * <li><b>start row ; start column ; end row ; end column</b></li>
 * </ol>
 *
 * See {@link LocatorStrategy} for differentiation each of these reprentations
 */
public class LocatorByStart implements Locator {

    private int startRow;
    private int startRowWithOffset;
    private int startColumn;
    private int startColumnWithOffset;
    private int maxRow;
    private int maxColumn;
    private int offsetColumn;
    private int offsetRow;

    /**
     * This is a {@link Constructor} for the representation of a
     * {@link LocatorStrategy} START_STOP. That means we have a start(x,y) and
     * an end(x,y) position.
     * 
     * @param startRow
     *            the start y position representation of a screen range
     * @param startColumn
     *            the start x position representation of a screen range
     * @param status
     *            the status of the executed action
     * @param offsetRow
     *            The offset for the startpoint row in dependence on the zero
     *            origin of the host screen.
     * @param offsetColumn
     *            The offset for the startpoint column in dependence on the zero
     *            origin of the host screen.
     */
    public LocatorByStart(int startRow, int startColumn, Status status, Offset offset) {
        this.offsetRow = offset.getOffsetRow();
        this.offsetColumn = offset.getOffsetColumn();
        this.startRow = startRow;
        this.startRowWithOffset = startRow + offsetRow;
        this.startColumn = startColumn;
        this.startColumnWithOffset = startColumn + offsetColumn;
        this.maxRow = status.getCurrentCursorRow();
        this.maxColumn = status.getNumberColumns();
        checkBoundaries();
    }

    /**
     * This is a {@link Constructor} for the representation of a
     * {@link LocatorStrategy} START. That means we have a start(x ; y). The
     * Elementlocator has to be in the form <br>
     * <b> ( int startRow; int startColumn ).</b>
     * <p>
     * <b> Usage:</b><br>
     * <code>
     * elementLocator = "1;2"<br> 
     * means startRow = 1, startColumn = 2.
     * </code>
     * 
     * @param elementLocator
     *            The start position representation of a mainframe host screen.
     * @param status
     *            the status of the executed action
     * @param offsetRow
     *            The offset for the startpoint row in dependence on the zero
     *            origin of the host screen.
     * @param offsetColumn
     *            The offset for the startpoint column in dependence on the zero
     *            origin of the host screen.
     */
    public LocatorByStart(String elementLocator, Status status, Offset offset) {
        this.maxRow = status.getNumberRows();
        this.maxColumn = status.getNumberColumns();
        this.offsetRow = offset.getOffsetRow();
        this.offsetColumn = offset.getOffsetColumn();
        initializeStartRowAndColumn(elementLocator);
    }

    /**
     * @return the startRowWithOffset
     */
    public int getStartRowWithOffset() {
        return startRowWithOffset;
    }

    /**
     * @return the startColumnWithOffset
     */
    public int getStartColumnWithOffset() {
        return startColumnWithOffset;
    }

    /**
     * This method creates a Locator for the {@link LocatorStrategy} START
     * 
     * @param elementLocator
     *            in the form "1;2"
     * @return {@link LocatorByStart} filled with the following fields:
     *         startRow, startColumn.
     */
    private void initializeStartRowAndColumn(String elementLocator) {
        Pattern locatorPattern = Pattern.compile("(?<startRow>\\d+);(?<startColumn>\\d+)");
        Matcher matcher = locatorPattern.matcher(elementLocator);
        if (matcher.matches()) {
            this.startRow = Integer.parseInt(matcher.group("startRow"));
            this.startColumn = Integer.parseInt(matcher.group("startColumn"));
            this.startRowWithOffset = this.startRow + this.offsetRow;
            this.startColumnWithOffset = this.startColumn + this.offsetColumn;
            checkBoundaries();
        } else {
            throw new IllegalArgumentException(
                    "The provided locator did not match the expected pattern \"x;y\" where x and y are both integer values. Got: " + elementLocator);
        }
    }

    @Override
    public void checkBoundaries() {
        // because we begin to count startColumn and startRow with 0
        if (startColumn > maxColumn) {
            String columnExceptionMessage = "Your chosen column '" + startColumn + "' is greater than the maximum column '" + (maxColumn - 1) + "'";
            throw new IllegalArgumentException(columnExceptionMessage);
        }
        if (startRow > maxRow) {
            String rowExceptionMessage = "Your chosen row '" + startRow + "' is greater than the maximum row '" + maxRow + "'";
            throw new IllegalArgumentException(rowExceptionMessage);
        }
    }

}
