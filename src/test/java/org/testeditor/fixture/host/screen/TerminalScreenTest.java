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
package org.testeditor.fixture.host.screen;

import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;

public class TerminalScreenTest {

    @Test
    public void testFieldsOnScreen() {
        // given
        TerminalScreen screen = new TerminalScreen();
        String header = "|Nr |row|field|width|colStart|colEnd|numeric|protected|hidden|value";
        String protectedField = "|1  |0  |1    |4    |1       |4     |       |true     |false | 'z/OS'";
        String hidden = "|4  |0  |4    |31   |23      |53    |false  |false    |true  | '";
        String numeric = "|3  |0  |3    |5    |17      |21    |true   |false    |false | ' 0609'";
        String normal = "|8  |1  |2    |24   |53      |76    |false  |false    |false | 'VTAM Terminal = SC0TCP80'";

        // when
        String printAllFieldAsString = screen.printAllFieldAsString(HostTestSite.createHostScreenAsHex());
        System.err.println(printAllFieldAsString);
        // then
        Assert.assertTrue(printAllFieldAsString.contains(protectedField));
        Assert.assertTrue(printAllFieldAsString.contains(header));
        Assert.assertTrue(printAllFieldAsString.contains(hidden));
        Assert.assertTrue(printAllFieldAsString.contains(numeric));
        Assert.assertTrue(printAllFieldAsString.contains(normal));
    }

    @Test
    public void testFieldAtFirstEntry() {
        // given
        TerminalScreen screen = new TerminalScreen();
        ArrayList<Field> allFields = screen.createFields(HostTestSite.createHostScreenAsHex());

        // when
        Field field = screen.getField(0, 1, allFields);

        // then
        Assert.assertEquals(0, field.getRowCount());
        Assert.assertEquals(1, field.getCharacterStartpoint());
        Assert.assertEquals(4, field.getCharacterWidth());
        Assert.assertEquals(4, field.getEndpoint());
        Assert.assertEquals(1, field.getFieldCountPerline());
        Assert.assertEquals("7a", field.getCharactersAsHex().get(0));
        Assert.assertEquals("4f", field.getCharactersAsHex().get(2));
        Assert.assertEquals("53", field.getCharactersAsHex().get(3));
        Assert.assertEquals("e9", field.getControlCharacter());
        Assert.assertEquals("z/OS", field.getStringReperentation());
    }

    @Test
    public void testFieldRowAndFieldNumber() {
        // given
        TerminalScreen screen = new TerminalScreen();
        ArrayList<Field> allFields = screen.createFields(HostTestSite.createHostScreenAsHex());

        // when
        Field field = screen.getField(16, 2, allFields);

        // then
        Assert.assertEquals(16, field.getRowCount());
        Assert.assertEquals(21, field.getCharacterStartpoint());
        Assert.assertEquals(33, field.getCharacterWidth());
        Assert.assertEquals(53, field.getEndpoint());
        Assert.assertEquals(2, field.getFieldCountPerline());
        Assert.assertEquals("53", field.getCharactersAsHex().get(0));
        Assert.assertEquals("6d", field.getCharactersAsHex().get(5));
        Assert.assertEquals("2a", field.getCharactersAsHex().get(32));
        Assert.assertEquals("c0", field.getControlCharacter());
        Assert.assertEquals("System Customization - ADCD.Z18.*", field.getStringReperentation());
    }

}
