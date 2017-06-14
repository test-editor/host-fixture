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
    private final String statusString;
    private Status status;

    public Result(final List<String> dataLines, final String statusString) {
        this.dataLines = dataLines;
        this.statusString = statusString;
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

    public void createStatus() {
        setStatus(new Status(getStatusString()));
        logger.debug(getStatus().toString());
    }

}
