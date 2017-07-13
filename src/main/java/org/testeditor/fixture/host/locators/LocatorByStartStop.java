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

import org.testeditor.fixture.host.s3270.options.TerminalMode;

import java.lang.reflect.Constructor;

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
     */
    public LocatorByStartStop(int startRow, int startColumn, int endRow, int endColumn, TerminalMode mode) {
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.endRow = endRow;
        this.endColumn = endColumn;
        this.maxRow = mode.getMaxRow();
        this.maxColumn = mode.getMaxColumn();
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
     */
    public LocatorByStartStop(String elementLocator, TerminalMode mode) {
        this.maxRow = mode.getMaxRow();
        this.maxColumn = mode.getMaxColumn();
        createLocatorByStartStop(elementLocator);
    }

    public int getStartColumn() {
        return startColumn;
    }

    public int getStartRow() {
        return startRow;
    }

    /**
     * @return the endRow
     */
    public int getEndRow() {
        return endRow;
    }

    /**
     * @return the endColumn
     */
    public int getEndColumn() {
        return endColumn;
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
            this.startRow = (Integer.parseInt(splittedValues[0]));
            this.startColumn = (Integer.parseInt(splittedValues[1]));
            this.endRow = (Integer.parseInt(splittedValues[2]));
            this.endColumn = (Integer.parseInt(splittedValues[3]));
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
        // because we begin to count startColumn and startRow with 0
        if (startColumn >= maxColumn) {
            throw new RuntimeException("Your chosen column '" + startColumn + "' is greater than the maximum column '"
                    + (maxColumn - 1) + "'");
        }
        if (startRow >= maxRow) {
            throw new RuntimeException(
                    "Your chosen row '" + startRow + "' is greater than the maximum row '" + (maxRow - 1) + "'");
        }
        if (endColumn >= maxColumn) {
            throw new RuntimeException("Your chosen column '" + endColumn + "' is greater than the maximum column '"
                    + (maxColumn - 1) + "'");
        }
        if (endRow >= maxRow) {
            throw new RuntimeException(
                    "Your chosen row '" + endRow + "' is greater than the maximum row '" + (maxRow - 1) + "'");
        }
    }

}
