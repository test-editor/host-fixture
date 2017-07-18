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
 * The Locator is a representation of a range on a Mainframe host screen.<br>
 * This could be represented through a declaration of a start and an end point.
 * 
 * See {@link LocatorStrategy} for differentiation each of these reprentations
 */
public interface Locator {

    public void checkBoundaries();

}
