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
 * The EmulatorMode is defined as follows:
 * <ul>
 * <li>I - if connected in 3270 mode.</li>
 * <li>L - if connected in NVT line mode.</li>
 * <li>C - if connected in NVT character mode.</li>
 * <li>P - if connected in unnegotiated mode (no BIND active from the
 * host).</li>
 * <li>N - if not connected.</li>
 * </ul>
 *
 * See <a href=
 * "http://x3270.bgp.nu/x3270-script.html#Status-Format">EmulatorMode<a/>
 *
 */
public enum EmulatorMode {
    M3270_MODE("I"), NVT_LINE_MODE("L"), NVT_CHAR_MODE("C"), UNNEGOTIATED_MODE("P"), NOT_CONNECTED("N");
    private String mode;

    private EmulatorMode(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

    public static EmulatorMode getEmulatorMode(String input) {
        EmulatorMode[] values = EmulatorMode.values();
        for (EmulatorMode emulatorMode : values) {
            if (emulatorMode.getMode().equals(input)) {
                return emulatorMode;
            }
        }
        throw new StatusNotFoundException("Emulator mode " + input + " is unknown!");
    }

}
