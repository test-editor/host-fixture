package org.testeditor.fixture.host.screen;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

public class FieldTest {

    @Test
    public void firstFieldTest() {
        // given
        TerminalScreen screen = new TerminalScreen();
        ArrayList<Field> allFieldsOnScreen = screen.createFields(HostTestSite.createHostScreenAsHex());

        // when
        Field field_1 = allFieldsOnScreen.get(14);

        // then
        ArrayList<String> charactersAsHex = field_1.getCharactersAsHex();
        Assert.assertEquals("56", charactersAsHex.get(0));
        Assert.assertEquals(53, field_1.getCharacterStartpoint());
        Assert.assertEquals(24, field_1.getCharacterWidth());
        Assert.assertEquals("c0", field_1.getControlCharacter());
        Assert.assertEquals(76, field_1.getEndpoint());
        Assert.assertEquals(2, field_1.getFieldCountPerline());
        Assert.assertEquals(3, field_1.getRowCount());
        Assert.assertEquals("VTAM Terminal = SC0TCP80", field_1.getStringReperentation());
        Assert.assertEquals(false, field_1.isHidden());
        Assert.assertEquals(false, field_1.IsNumeric());
        Assert.assertEquals(false, field_1.isProtected());
    }

}
