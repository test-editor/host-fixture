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
package org.testeditor.fixture.host.s3270.actions;

import org.testeditor.fixture.host.exceptions.UnknownCommandException;

/**
 * Following special commands can be used in tests :
 * <ul>
 * <li>{@link #BACKTAB} - move cursor to previous input field</li>
 * <li>{@link #CLEAR} - clear screen</li>
 * <li>{@link #ENTER} - send an command</li>
 * <li>{@link #ERASE_EOF} - erase to end of current field</li>
 * <li>{@link #PF3} - get back to to previous screen</li>
 * <li>{@link #RESET} - reset locked keyboard</li>
 * <li>{@link #TAB} - move cursor to next input field</li>
 * </ul>
 * <p>
 * These commands are a subset of the complete acion list.
 * <p>
 * See <a href= "http://x3270.bgp.nu/s3270-man.html#Actions" target=
 * "_top">http://x3270.bgp.nu/s3270-man.html#Actions</a> for a complete list of
 * actions.
 *
 */
public enum ControlCommand {

    BACKTAB("BackTab"), CLEAR("Clear"), ENTER("Enter"), ERASE_EOF("EraseEOF"), ERASE_INPUT("EraseInput"), RESET(
            "Reset"), TAB("Tab");
    private String command;

    private ControlCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public static ControlCommand getControlCommand(String input) {
        ControlCommand[] values = ControlCommand.values();
        for (ControlCommand controlCommand : values) {
            if (controlCommand.getCommand().equals(input)) {
                return controlCommand;
            }
        }
        throw new UnknownCommandException("ControlCommand " + input + " is unknown!");
    }

}
