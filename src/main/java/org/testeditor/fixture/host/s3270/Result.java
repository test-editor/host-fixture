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
package org.testeditor.fixture.host.s3270;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testeditor.fixture.host.screen.Offset;

/**
 * Represents the result of an s3270 / ws3270 command.
 */
public class Result {

    private static final Logger logger = LoggerFactory.getLogger(Result.class);

    private final List<String> dataLines;
    /**
     * The statusString in the format:
     * <p>
     * "U F U C(abcdefg.google.mainframe.de) I 2 24 80 3 9 0x0 -"
     */
    private final String statusString;
    /**
     * The {@link Status} created from a String in the format:
     */
    private Status status;

    /**
     * The resultOfCommand can only be "ok" or "error"
     */
    private String resultOfCommand;
    private int offsetRow;
    private int offsetColumn;

    /**
     * 
     * @param dataLines
     *            The result of data which is responded by the s3270 driver.
     * @param statusString
     *            The statusString in the format:<br>
     *            "U F U C(abcdefg.google.mainframe.de) I 2 24 80 3 9 0x0 -"
     * @param resultOfCommand
     *            String "ok" for command was executed succesfully, "error"
     *            otherwise.
     * @param offsetRow
     *            The offsetRow for the screen origin. Default for the x3270
     *            driver is that the row and column are zero-origin (0;0), that
     *            means the begin point is at row 0 and column 0. The offsetRow
     *            will rearrange the startpoint around the value for the row .
     * @param offsetColumn
     *            The offsetColumn for the screen origin. Default for the x3270
     *            driver is that the row and column are zero-origin (0;0), that
     *            means the begin point is at row 0 and column 0. The
     *            offsetColumn will rearrange the startpoint around the value
     *            for the column.
     */
    public Result(final List<String> dataLines, final String statusString, String resultOfCommand, Offset offset) {
        this.dataLines = dataLines;
        this.statusString = statusString;
        this.resultOfCommand = resultOfCommand;
        this.offsetRow = offset.getOffsetRow();
        this.offsetColumn = offset.getOffsetColumn();
        Status statusOriginal = new Status(statusString, offset);
        int currentRowOriginal = statusOriginal.getCurrentCursorRow();
        int currentColumnOriginal = statusOriginal.getCurrentCursorColumn();
        statusOriginal.setCurrentCursorColumn(currentColumnOriginal + (-offsetColumn));
        statusOriginal.setCurrentCursorRow(currentRowOriginal + (-offsetRow));
        this.status = statusOriginal;
    }

    public List<String> getDataLines() {
        return this.dataLines;
    }

    public String getStatusString() {
        return this.statusString;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void logStatus() {
        logger.trace(getStatus().toString());
    }

    public String getResultOfCommand() {
        return resultOfCommand;
    }

}
