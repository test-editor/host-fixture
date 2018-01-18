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
package org.testeditor.fixture.host.s3270.options;

/**
 * The model of 3270 display to be emulated. The model name is in two parts,
 * either of which may be omitted:
 *
 * The first part is the base model, which is either 3278 or 3279. 3278
 * specifies a monochrome (green on black) 3270 display; 3279 specifies a color
 * 3270 display.
 *
 * See <a href="http://x3270.bgp.nu/ws3270-man.html#Options">under Options
 * -model</a>
 *
 */
public enum TerminalType {
    TYPE_3278("3278"), TYPE_3279("3279");
    private String type;

    private TerminalType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
