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
package org.testeditor.fixture.host.exceptions;

public class StatusNotFoundException extends RuntimeException {

    /**
     * Indicates that the name of the state could not be resolved.
     */
    private static final long serialVersionUID = 8794743317555774095L;

    private String state;

    public StatusNotFoundException(final String state) {
        super(state);
        this.state = state;
    }

    /**
     * Returns the name of the state that could not be resolved.
     */
    public String getState() {
        return state;
    }

}
