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
package org.testeditor.fixture.host.s3270.statusformat;

import org.testeditor.fixture.host.exceptions.StatusNotFoundException;

/**
 * The KeyBoardState is defined as follows:
 * <ul>
 * <li>U - if the keyboard is unlocked.</li>
 * <li>L - If the keyboard is locked waiting for a response from the host, or if
 * not connected to a host.</li>
 * <li>E - If the keyboard is locked because of an operator error (field
 * overflow, protected field, etc.).</li>
 * </ul>
 *
 * See <a href=
 * "http://x3270.bgp.nu/x3270-script.html#Status-Format">KeyboardState<a/>
 *
 */
public enum KeyboardState {
    UNLOCKED("U"), LOCKED_WAITING_FOR_RESPONSE("L"), LOCKED_OPERATOR_ERROR("E");
    private String state;

    private KeyboardState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public static KeyboardState getKeyboardState(String keyboardStateOnScreen) throws StatusNotFoundException {

        KeyboardState[] values = KeyboardState.values();
        for (KeyboardState keyboardState : values) {

            if (keyboardState.getState().equals(keyboardStateOnScreen)) {
                return keyboardState;
            }
        }
        throw new StatusNotFoundException("Keyboard state " + keyboardStateOnScreen + " is unknown!");
    }

}
