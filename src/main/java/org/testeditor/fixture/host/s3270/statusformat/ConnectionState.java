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
package org.testeditor.fixture.host.s3270.statusformat;

import org.testeditor.fixture.host.exceptions.StatusNotFoundException;

/**
 *
 * The ConnectionState is defined as follows:
 * <ul>
 * <li>C(hostname) - If connected to a host.</li>
 * <li>N - otherwise.</li>
 * </ul>
 *
 * See <a href=
 * "http://x3270.bgp.nu/x3270-script.html#Status-Format">ConnectionState<a/>
 *
 */
public enum ConnectionState {
    CONNECTED("C"), NOT_CONNECTED("N");
    private String state;

    private ConnectionState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public static ConnectionState getConnectionState(String input) {
        ConnectionState[] values = ConnectionState.values();
        for (ConnectionState connectionState : values) {
            if (connectionState.getState().equals(input)) {
                return connectionState;
            }
        }
        throw new StatusNotFoundException("FieldProtection state " + input + " is unknown!");
    }

}
