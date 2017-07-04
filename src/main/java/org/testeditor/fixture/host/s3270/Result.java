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
package org.testeditor.fixture.host.s3270;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     */
    public Result(final List<String> dataLines, final String statusString, String resultOfCommand) {
        this.dataLines = dataLines;
        this.statusString = statusString;
        this.resultOfCommand = resultOfCommand;
        this.status = new Status(statusString);
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
        logger.debug(getStatus().toString());
    }

    public String getResultOfCommand() {
        return resultOfCommand;
    }

}
