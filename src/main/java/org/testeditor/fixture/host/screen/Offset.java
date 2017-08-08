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
package org.testeditor.fixture.host.screen;

public class Offset {

    private int offsetRow;
    private int offsetColumn;

    public Offset(int offsetRow, int offsetColumn) {
        this.setOffsetRow(offsetRow);
        this.setOffsetColumn(offsetColumn);
    }

    /**
     * @return the offsetRow
     */
    public int getOffsetRow() {
        return offsetRow;
    }

    /**
     * @param offsetRow
     *            the offsetRow to set
     */
    public void setOffsetRow(int offsetRow) {
        this.offsetRow = offsetRow;
    }

    /**
     * @return the offsetColumn
     */
    public int getOffsetColumn() {
        return offsetColumn;
    }

    /**
     * @param offsetColumn
     *            the offsetColumn to set
     */
    public void setOffsetColumn(int offsetColumn) {
        this.offsetColumn = offsetColumn;
    }

}
