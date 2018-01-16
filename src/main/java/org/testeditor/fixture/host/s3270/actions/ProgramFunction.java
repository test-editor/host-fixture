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
package org.testeditor.fixture.host.s3270.actions;

import org.testeditor.fixture.host.exceptions.UnknownCommandException;

public enum ProgramFunction {

    PF_1("PF(1)"), PF_2("PF(2)"), PF_3("PF(3)"), PF_4("PF(4)"), PF_5("PF(5)"), PF_6("PF(6)"), PF_7("PF(7)"), PF_8(
            "PF(8)"), PF_9("PF(9)"), PF_10("PF(10)"), PF_11("PF(11)"), PF_12("PF(12)"), PF_13("PF(13)"), PF_14(
                    "PF(14)"), PF_15("PF(15)"), PF_16("PF(16)"), PF_17("PF(17)"), PF_18(
                            "PF(18)"), PF_19("PF(19)"), PF_20(
                                    "PF(20)"), PF_21("PF(21)"), PF_22("PF(22)"), PF_23("PF(23)"), PF_24("PF(24)");

    private String command;

    private ProgramFunction(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public static ProgramFunction getControlCommand(String input) {
        ProgramFunction[] values = ProgramFunction.values();
        for (ProgramFunction functionCommand : values) {
            if (functionCommand.getCommand().equals(input)) {
                return functionCommand;
            }
        }
        throw new UnknownCommandException("ProgramFunction " + input + " is unknown!");
    }

}
