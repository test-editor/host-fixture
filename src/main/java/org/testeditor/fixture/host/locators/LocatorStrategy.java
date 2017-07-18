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
package org.testeditor.fixture.host.locators;

/**
 * The LocatorStrategy represents the possibilities how to define a Locator and
 * therefore how to react when using one of this possibilities.
 * <p>
 * <b>Usage: </b>
 * <ul>
 * <li><b>START :</b> when only <b>start(x,y)</b> positions are available</li>
 * <li><b>START_STOP :</b> when <b>start(x,y)</b>, <b>end(x,y)</b> positions are
 * available</li>
 * <li><b>WIDTH :</b> when <b>start(x,y)</b> position and <b>width</b> (a
 * quantity of characters) are available</li>
 * </ul>
 * 
 */
public enum LocatorStrategy {
    START, START_STOP, WIDTH
}
