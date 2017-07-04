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
package org.testeditor.fixture.host;

import org.testeditor.fixture.host.locators.LocatorByStartStop;
import org.testeditor.fixture.host.locators.LocatorByWidth;
import org.testeditor.fixture.host.s3270.options.TerminalMode;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class LocatorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void locatorWidthTest() {
        String elementLocator = "1;2;44";
        LocatorByWidth locator = new LocatorByWidth(elementLocator, TerminalMode.MODE_24x80);
        Assert.assertEquals(locator.getStartRow(), 1);
        Assert.assertEquals(locator.getStartColumn(), 2);
        Assert.assertEquals(locator.getWidth(), 44);
    }

    @Test
    public void locatorByStartStopWithTwoArgumentsTest() {
        String elementLocator = "1;2";
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("The number of arguments is '2' but should be '4'");
        new LocatorByStartStop(elementLocator, TerminalMode.MODE_24x80);
    }

    @Test
    public void locatorByStartStopWithFourArgumentsTest() {
        String elementLocator = "6;44;0;0";
        LocatorByStartStop locator = new LocatorByStartStop(elementLocator, TerminalMode.MODE_24x80);
        Assert.assertEquals(6, locator.getStartRow());
        Assert.assertEquals(44, locator.getStartColumn());
        Assert.assertEquals(0, locator.getEndColumn());
        Assert.assertEquals(0, locator.getEndRow());
    }

}
