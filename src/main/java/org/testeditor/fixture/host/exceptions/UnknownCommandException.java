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
package org.testeditor.fixture.host.exceptions;

public class UnknownCommandException extends RuntimeException {

    /**
     * Indicates that the name of the command could not be resolved.
     */
    private static final long serialVersionUID = 8994743317555774095L;

    private String command;

    public UnknownCommandException(final String command) {
        super(command);
        this.command = command;
    }

    /**
     * Returns the ControlCommand name that could not be resolved.
     */
    public String getCommand() {
        return command;
    }

}
