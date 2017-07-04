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
     */
    public LocatorByWidth(int startRow, int startColumn, int width, TerminalMode mode) {
        this.maxRow = mode.getMaxRow();
        this.maxColumn = mode.getMaxColumn();
        this.startColumn = startColumn;
        this.startRow = startRow;
        this.endColumn = startColumn + width;
        this.width = width;
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
     */
    public LocatorByWidth(String elementLocator, TerminalMode mode) {
        this.maxRow = mode.getMaxRow();
        this.maxColumn = mode.getMaxColumn();
        createLocatorByWidth(elementLocator);
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
     * This method creates a Locator for the {@link LocatorStrategy} WIDTH
     * 
     * @param elementLocator
     *            in the form "1;2;45"
     * @return {@link LocatorByWidth} filled with the following fields:
     *         startRow, startColumn, width (number of characters to be read).
     */
    private LocatorByWidth createLocatorByWidth(String elementLocator) {
        String[] splittedValues = elementLocator.split(";");
        checkLocatorLength(elementLocator, splittedValues);
        if (checkIfNumbers(splittedValues)) {
            this.startRow = (Integer.parseInt(splittedValues[0]));
            this.startColumn = (Integer.parseInt(splittedValues[1]));
            this.width = (Integer.parseInt(splittedValues[2]));
            this.endColumn = startColumn + width;
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
                && splittedValues[2].matches("\\d+");
    }

    private void checkLocatorLength(String elementLocator, String[] splittedValues) {
        if (splittedValues.length != 3) {
            throw new RuntimeException("The number of arguments is '" + splittedValues.length
                    + "' but should be 3 - locator: " + elementLocator + "'");
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
            throw new RuntimeException("Your chosen row width '" + startRow
                    + "' is greater than the actual maximum row width '" + (maxRow - 1) + "'");
        }
        if (endColumn >= maxColumn) {
            throw new RuntimeException("Your chosen start column plus width '" + endColumn
                    + "' is greater than the maximum column size'" + (maxColumn - 1) + "'");
        }
    }

}
