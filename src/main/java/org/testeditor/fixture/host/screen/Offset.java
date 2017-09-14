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
        this.offsetRow = offsetRow;
        this.offsetColumn = offsetColumn;
    }

    public int getOffsetRow() {
        return offsetRow;
    }

    public int getOffsetColumn() {
        return offsetColumn;
    }

}