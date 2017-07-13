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
public class LocatorByStart implements Locator {

    private int startRow;
    private int startColumn;
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
     */
    public LocatorByStart(int startRow, int startColumn, TerminalMode mode) {
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.maxRow = mode.getMaxRow();
        this.maxColumn = mode.getMaxColumn();
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
     */
    public LocatorByStart(String elementLocator, TerminalMode mode) {
        this.maxRow = mode.getMaxRow();
        this.maxColumn = mode.getMaxColumn();
        createLocatorByStart(elementLocator);
    }

    public int getStartColumn() {
        return startColumn;
    }

    public int getStartRow() {
        return startRow;
    }

    /**
     * This method creates a Locator for the {@link LocatorStrategy} START
     * 
     * @param elementLocator
     *            in the form "1;2"
     * @return {@link LocatorByStart} filled with the following fields:
     *         startRow, startColumn.
     */
    private LocatorByStart createLocatorByStart(String elementLocator) {
        checkBoundaries();
        String[] splittedValues = elementLocator.split(";");
        if (splittedValues.length != 2) {
            throw new RuntimeException("The number of arguments is '" + splittedValues.length
                    + "' but should be '2' - locator: '" + elementLocator + "'");
        }
        if (splittedValues[0].matches("\\d+") && splittedValues[1].matches("\\d+")) {
            this.startRow = (Integer.parseInt(splittedValues[0]));
            this.startColumn = (Integer.parseInt(splittedValues[1]));
        } else {
            throw new RuntimeException("One of your locator arguments is not an integer value: startRow'"
                    + splittedValues[0] + "' startColumn'" + splittedValues[1]);
        }
        return this;
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
    }

}
