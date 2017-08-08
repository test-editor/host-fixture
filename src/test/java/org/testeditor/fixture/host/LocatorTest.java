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

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.testeditor.fixture.host.locators.LocatorByStartStop;
import org.testeditor.fixture.host.locators.LocatorByWidth;
import org.testeditor.fixture.host.s3270.Status;
import org.testeditor.fixture.host.screen.Offset;

public class LocatorTest {
    private static Status status = new Status("U F U C(google.de) I 2 24 80 2 11 0x0 -", new Offset(-1, -1));
    private int offsetRow = -1;
    private int offsetColumn = -1;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void locatorWidthTest() {
        String elementLocator = "1;2;44";
        LocatorByWidth locator = new LocatorByWidth(elementLocator, status, offsetRow, offsetColumn);
        Assert.assertEquals(1, locator.getStartRow());
        Assert.assertEquals(2, locator.getStartColumn());
        Assert.assertEquals(0, locator.getStartRowWithOffset());
        Assert.assertEquals(1, locator.getStartColumnWithOffset());
        Assert.assertEquals(44, locator.getWidth());
    }

    @Test
    public void locatorByStartStopWithTwoArgumentsTest() {
        String elementLocator = "1;2";
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("The number of arguments is '2' but should be '4'");
        new LocatorByStartStop(elementLocator, status, offsetRow, offsetColumn);
    }

    @Test
    public void locatorByStartStopWithFourArgumentsTest() {
        String elementLocator = "7;45;5;6";
        LocatorByStartStop locator = new LocatorByStartStop(elementLocator, status, offsetRow, offsetColumn);
        Assert.assertEquals(7, locator.getStartRow());
        Assert.assertEquals(45, locator.getStartColumn());
        Assert.assertEquals(6, locator.getStartRowWithOffset());
        Assert.assertEquals(44, locator.getStartColumnWithOffset());
        Assert.assertEquals(5, locator.getEndRow());
        Assert.assertEquals(6, locator.getEndColumn());
        Assert.assertEquals(4, locator.getEndRowWithOffset());
        Assert.assertEquals(5, locator.getEndColumnWithOffset());

    }

}
