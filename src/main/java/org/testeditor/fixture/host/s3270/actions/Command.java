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

public class Command {

    private String action;
    private String actionForLog;
    private Object[] attributes;

    public Command(String action, String actionForLog) {
        this.action = action;
        this.actionForLog = actionForLog;
    }

    public Command(String action, String actionForLog, Object... attributes) {
        this.action = action;
        this.actionForLog = actionForLog;
        this.attributes = attributes;
    }

    /**
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /**
     * @return the actionForLog
     */
    public String getActionForLog() {
        return actionForLog;
    }

    /**
     * @return the attributes
     */
    public Object[] getAttributes() {
        return attributes;
    }

}
