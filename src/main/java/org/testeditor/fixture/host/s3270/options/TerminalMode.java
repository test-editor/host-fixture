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
package org.testeditor.fixture.host.s3270.options;

import org.testeditor.fixture.host.exceptions.StatusNotFoundException;

/**
 * The model number, which specifies the number of rows and columns.
 * <ul>
 * <li>Model 2 specifies a mainframe Terminal Emulator window with 24 rows and
 * 80 columns.</li>
 * <li>Model 3 specifies a mainframe Terminal Emulator window with 32 rows and
 * 80 columns.</li>
 * <li>Model 4 specifies a mainframe Terminal Emulator window with 43 rows and
 * 80 columns.</li>
 * <li>Model 5 specifies a mainframe Terminal Emulator window with 27 rows and
 * 132 columns.</li>
 * </ul>
 * <p>
 * Model 4 is the default when nothing will be specified under the application
 * S3270.
 *
 */
public enum TerminalMode {
    MODE_24x80(2), MODE_32x80(3), MODE_43x80(4), MODE_27x132(5);
    private int mode;
    private int maxRow;
    private int maxColumn;

    private TerminalMode(int mode) {
        this.mode = mode;
        setMaxRow();
        setMaxColumn();
    }

    private void setMaxColumn() {
        String string = super.toString();
        String replacedString = string.replace("MODE_", "");
        String[] splittedStrings = replacedString.split("x");
        this.maxColumn = Integer.parseInt(splittedStrings[1]);

    }

    private void setMaxRow() {
        String string = super.toString();
        String replacedString = string.replace("MODE_", "");
        String[] splittedStrings = replacedString.split("x");
        this.maxRow = Integer.parseInt(splittedStrings[0]);
    }

    public int getMode() {
        return mode;
    }

    public static TerminalMode getTerminalMode(int input) {
        for (TerminalMode terminalMode : TerminalMode.values()) {
            if (terminalMode.getMode() == input) {
                return terminalMode;
            }
        }
        throw new StatusNotFoundException("Terminal mode " + input + " is unknown!");
    }

    public int getMaxRow() {
        return this.maxRow;
    }

    public int getMaxColumn() {
        return this.maxColumn;
    }

}
