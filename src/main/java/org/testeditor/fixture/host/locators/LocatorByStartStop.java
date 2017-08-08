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
package org.testeditor.fixture.host.locators;

import java.lang.reflect.Constructor;
import org.testeditor.fixture.host.s3270.Status;

/**
 * The LocatorByStartStop is a representation of a range on a Mainframe host
 * screen.<br>
 * This could be represented through a declaration of a start and an end points.
 * <ol>
 * <li><b>start row ; start column ; end row ; end column</b></li>
 * </ol>
 *
 * See {@link LocatorStrategy} for differentiation each of these reprentations
 */
public class LocatorByStartStop implements Locator {

    private int startRow;
    private int startColumn;
    private int endRow;
    private int endColumn;
    private int maxRow;
    private int maxColumn;
    private int offsetColumn;
    private int offsetRow;
    private int startRowWithOffset;
    private int startColumnWithOffset;
    private int endColumnWithOffset;
    private int endRowWithOffset;

    /**
     * This is a {@link Constructor} for the representation of a
     * {@link LocatorStrategy} START_STOP. That means we have a start(x,y) and
     * an end(x,y) position.
     * 
     * @param startRow
     *            the start y position representation of a screen range
     * @param startColumn
     *            the start x position representation of a screen range
     * @param endRow
     *            the end y position representation of a screen range
     * @param endColumn
     *            the end x position representation of a screen range
     * @param status
     *            the status of the executed action
     * @param offsetRow
     *            The offset for the startpoint row in dependence on the zero
     *            origin of the host screen.
     * @param offsetColumn
     *            The offset for the startpoint column in dependence on the zero
     *            origin of the host screen.
     */
    public LocatorByStartStop(int startRow, int startColumn, int endRow, int endColumn, Status status, int offsetRow,
            int offsetColumn) {
        this.startRow = startRow;
        this.startRowWithOffset = startRow + offsetRow;
        this.startColumn = startColumn;
        this.startColumnWithOffset = startColumn + offsetColumn;
        this.endRow = endRow;
        this.endRowWithOffset = endRow + offsetRow;
        this.endColumn = endColumn;
        this.endColumnWithOffset = endColumn + offsetColumn;
        this.maxRow = status.getNumberRows();
        this.maxColumn = status.getNumberColumns();
        this.offsetRow = offsetRow;
        this.offsetColumn = offsetColumn;
        checkBoundaries();
    }

    /**
     * This is a {@link Constructor} for the representation of a
     * {@link LocatorStrategy} START_STOP. That means we have a start(x,y) and
     * an end(x,y) position. The Elementlocator has to be in the form <br>
     * <b> (int startRow; int startColumn; int endRow; int endColumn).</b>
     * <p>
     * <code>
     * Usage:<br>
     * elementLocator = "1;2;4;15"<br> 
     * means startRow = 1, startColumn = 2, endRow = 4, endColumn = 15.
     * </code>
     * 
     * @param elementLocator
     *            The start and end position representation of a screen range of
     *            a mainframe host screen.
     * @param status
     *            the status of the executed action
     * @param offsetRow
     *            The offset for the startpoint row in dependence on the zero
     *            origin of the host screen.
     * @param offsetColumn
     *            The offset for the startpoint column in dependence on the zero
     *            origin of the host screen.
     */
    public LocatorByStartStop(String elementLocator, Status status, int offsetRow, int offsetColumn) {
        this.maxRow = status.getNumberRows();
        this.maxColumn = status.getNumberColumns();
        this.offsetRow = offsetRow;
        this.offsetColumn = offsetColumn;
        createLocatorByStartStop(elementLocator);
    }

    public int getStartColumn() {
        return this.startColumn;
    }

    public int getStartRow() {
        return this.startRow;
    }

    public int getStartRowWithOffset() {
        return this.startRowWithOffset;
    }

    public int getStartColumnWithOffset() {
        return this.startColumnWithOffset;
    }

    /**
     * @return the endRow
     */
    public int getEndRow() {
        return this.endRow;
    }

    /**
     * @return the endColumn
     */
    public int getEndColumn() {
        return this.endColumn;
    }

    /**
     * @return the endColumnWithOffset
     */
    public int getEndColumnWithOffset() {
        return endColumnWithOffset;
    }

    /**
     * @return the endRowWithOffset
     */
    public int getEndRowWithOffset() {
        return endRowWithOffset;
    }

    /**
     * This method creates a Locator for the {@link LocatorStrategy} START_STOP
     * 
     * @param elementLocator
     *            in the form "1;2;4;5"
     * @return {@link LocatorByStartStop} filled with the following fields:
     *         startRow, startColumn, endRow, endColumn.
     */
    private LocatorByStartStop createLocatorByStartStop(String elementLocator) {
        String[] splittedValues = elementLocator.split(";");
        checkLocatorLength(elementLocator, splittedValues);
        if (checkIfNumbers(splittedValues)) {
            this.startRow = Integer.parseInt(splittedValues[0]);
            this.startRowWithOffset = (Integer.parseInt(splittedValues[0]) + this.offsetRow);
            this.startColumn = Integer.parseInt(splittedValues[1]);
            this.startColumnWithOffset = (Integer.parseInt(splittedValues[1]) + this.offsetColumn);
            this.endRow = Integer.parseInt(splittedValues[2]);
            this.endRowWithOffset = (Integer.parseInt(splittedValues[2]) + this.offsetRow);
            this.endColumn = Integer.parseInt(splittedValues[3]);
            this.endColumnWithOffset = (Integer.parseInt(splittedValues[3]) + this.offsetColumn);
            checkBoundaries();
        } else {
            throw new RuntimeException("One of your locator arguments is not an integer value: startRow'"
                    + splittedValues[0] + "' startColumn'" + splittedValues[1] + "' endRow'" + splittedValues[2]
                    + "' endColumn'" + splittedValues[3] + "'");
        }
        return this;
    }

    private boolean checkIfNumbers(String[] splittedValues) {
        return splittedValues[0].matches("\\d+") && splittedValues[1].matches("\\d+")
                && splittedValues[2].matches("\\d+") && splittedValues[3].matches("\\d+");
    }

    private void checkLocatorLength(String elementLocator, String[] splittedValues) {
        if (splittedValues.length != 4) {
            throw new RuntimeException("The number of arguments is '" + splittedValues.length
                    + "' but should be '4' - locator: '" + elementLocator + "'");
        }
    }

    @Override
    public void checkBoundaries() {
        if (startColumn > maxColumn) {
            throw new RuntimeException(
                    "Your chosen column '" + startColumn + "' is greater than the maximum column '" + maxColumn + "'");
        }
        if (startRow > maxRow) {
            throw new RuntimeException(
                    "Your chosen row '" + startRow + "' is greater than the maximum row '" + maxRow + "'");
        }
        if (endColumn > maxColumn) {
            throw new RuntimeException(
                    "Your chosen column '" + endColumn + "' is greater than the maximum column '" + maxColumn + "'");
        }
        if (endRow > maxRow) {
            throw new RuntimeException(
                    "Your chosen row '" + endRow + "' is greater than the maximum row '" + maxRow + "'");
        }
    }

}
