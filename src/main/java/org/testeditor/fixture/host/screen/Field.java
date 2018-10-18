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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 * The character mainframe ScreenBuffer can contain codes for graphic characters
 * or field attributes. Field attributes define the start of a field and control
 * the characteristics of the field. Because each storage location in the
 * character buffer is mapped to a position on the display screen, the field
 * attribute takes up a character position on the display screen and appears as
 * a blank. The field is defined as the field attribute position plus the
 * character positions up to, but not including, the next field attribute in the
 * character buffer. ScreenBuffer result of x3270 "Readbuffer(Ascii) command in
 * the form: <br>
 * <ul>
 * <li>data: SF(c0=cd) 41 42 43 44 SF(c0=f1) 45 46 47</li>
 * <li>data: SF(c0=f0) 48 49 50 51 SF(c0=f2) 52 53 54</li>
 * <li>...</li>
 * </ul>
 * <b>Caution:</b> the result has to be delivered in formatted structure from
 * mainframe, otherwise we can not parse any field information. See also
 * {@link org.testeditor.fixture.host.HostDriverFixture#printFieldsOnScreen()}
 * 
 * @see org.testeditor.fixture.host.screen.HostTestSite#createHostScreenAsHex()
 * 
 */
public class Field {

    /**
     * Field attribute defines the following field characteristics:
     * <p>
     * Nondisplay or display/intensified display. The selected characteristics
     * apply to the entire field. Nondisplay means that any characters entered
     * from the keyboard are placed in the buffer for possible subsequent
     * transmission to the application program, but they are not displayed.
     * Intensified display means the intensified characters appear on the screen
     * brighter than the nonintensified characters. Some devices cannot
     * intensify characters on the screen and highlight characters in a
     * different manner.
     * <p>
     * The Bit representation of a nondisplay field:
     * <p>
     * <table border="1" >
     * <tr>
     * <th>Binary</td>
     * <th>Hexaddecimal</td>
     * <th>Decimal</td>
     * </tr>
     * <tr>
     * <td>00001100</td>
     * <td>c</td>
     * <td>12</td>
     * </tr>
     * </table>
     * 
     */
    public static final int DISPLAYABLE = 12;

    /**
     * Field attribute defines the following field characteristics:
     * <p>
     * Alphanumeric or numeric. Unprotected alphanumeric fields are fields into
     * which an operator enters data normally, using the shift keys
     * (uppercase/lowercase or numeric/alphabetic) as required. Fields defined
     * as numeric accept all uppercase symbols and numerics from a data entry
     * keyboard. On a typewriter keyboard, numeric has no meaning and all
     * entries are accepted.
     * <p>
     * The Bit representation of a numeric field:
     * <p>
     * <table border="1" >
     * <tr>
     * <th>Binary</td>
     * <th>Hexadecimal</td>
     * <th>Decimal</td>
     * </tr>
     * <tr>
     * <td>00010000</td>
     * <td>10</td>
     * <td>16</td>
     * </tr>
     * </table>
     * 
     */
    public static final int NUMERIC = 16;

    /**
     * Field attribute defines the following field characteristics:
     * <p>
     * Protected or unprotected. A protected field cannot be modified by the
     * operator. The operator can enter data or modify the contents of an
     * unprotected field. Unprotected fields are classified as input fields.
     * <p>
     * The Bit representation of a protected field:
     * <p>
     * <table border="1" >
     * <tr>
     * <th>Binary</td>
     * <th>Hexadecimal</td>
     * <th>Decimal</td>
     * </tr>
     * <tr>
     * <td>00100000</td>
     * <td>20</td>
     * <td>32</td>
     * </tr>
     * </table>
     */
    public static final int PROTECTED = 32;

    /**
     * Number of field attributes
     */
    private int width;

    /**
     * Number of field character attributes
     */
    private int characterWidth;

    /**
     * String Array of field attributes in the form : "(c0=c1), 33, 34, 35"
     */
    private String[] splittedFieldAttributes;

    /**
     * String of the field control characters in the form "c0=c1,42=f5" .
     */
    private String controlCharacters;

    /**
     * String of the field control character value in the form "c1"
     */
    private String controlCharacter;

    /**
     * ArrayList of the field hexadecimal character values in the form "33",
     * "34", "35"
     */
    private ArrayList<String> charactersAsHex;

    /**
     * Parsed field hexadecimal character values as human readable characters in
     * the form "z/OS"
     */
    private String stringRepresentation;

    /**
     * Number of the actual row where the {@link Field} is defined.
     */
    private int rowCount;

    /**
     * Number of the actual {@link Field} in the actual row where the
     * {@link Field} is defined.
     */
    private int fieldCountPerlIne;

    /**
     * Complete row, not parsed e.g. "SF(c0=c1) 31 32 33" "SF(c0=c1) 34 35 36"
     */
    private ArrayList<String> row;

    /**
     * Startpoint of the {@link Field}
     */
    private int startpoint;

    /**
     * Endpoint of the {@link Field}
     */
    private int endpoint;

    /**
     * Startpoint of the field hexadecimal character values
     */
    private int characterStartpoint;

    /**
     * See {@link Field#NUMERIC}
     */
    private boolean numeric;

    /**
     * See {@link Field#DISPLAYABLE}
     */
    private boolean hidden;

    /**
     * See {@link Field#PROTECTED}
     */
    private boolean protectedField;;

    /**
     * @return Control Character
     */
    public String getControlCharacter() {
        return this.controlCharacter;
    }

    /**
     * Represents a String Array with the field attributes in the form "c0=c1",
     * "33", "34", "35".
     * 
     * @param splittedFieldAttributes
     */
    public void setSplittedFieldAttributes(String[] splittedFieldAttributes) {
        this.splittedFieldAttributes = splittedFieldAttributes;
    }

    /**
     * A ControlCharacter is a buffer location which occupies one Byte on a 3270
     * screen. It defines the start of the field and controls the
     * characteristics of a field see IBM Documentation at
     * 3270_Data_Stream_Programmers_Reference @ http://publibz.boulder.ibm.com.
     * 
     * @param splittedFieldAttributes
     *            This is the {@link Field} representation and contains the
     *            control characters called field attribute characters e.g.
     *            SF(c0=c1) and the character attributes as hexadecimal values
     *            like "33 34 35".
     */
    public void setControlCharacters(String[] splittedFieldAttributes) {
        for (String attribute : splittedFieldAttributes) {
            if (attribute.startsWith("(")) {
                this.controlCharacters = attribute;
                break;
            }
        }
    }

    /**
     * Amount of field attributes, the so called control characters and "normal"
     * displayed characters on a 3270 terminal screen. E.g. a Field defined as
     * "SF(c0=c1) 33 34 35 36" has one control character attribute "SF(c0=c1")
     * and four character attributes as hexadecimal values "33 34 35 36" that
     * makes in sum five attributes. So the width of this field would be 5.
     * 
     * @param width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Amount of character attributes, the so called "normal" displayed
     * characters on a 3270 terminal screen. E.g. a Field defined as "SF(c0=c1)
     * 33 34 35 36" has one control character attribute "SF(c0=c1") and four
     * character attributes as hexadecimal values "33 34 35 36" but only the
     * amount of character attributes will be counted here. So the width of this
     * field would be 4.
     * 
     * @param characterWidth
     */
    public void setCharacterWidth(int characterWidth) {
        this.characterWidth = characterWidth;
    }

    /**
     * Field character attributes are the "normal" displayed characters on the
     * display of a 3270 terminal screen. See IBM Documentation at
     * 3270_Data_Stream_Programmers_Reference @ http://publibz.boulder.ibm.com.
     * 
     * @param splittedFieldAttributes
     *            This is the {@link Field} representation and contains the
     *            control characters called field attribute characters e.g.
     *            SF(c0=c1) and the character attributes as hexadecimal values
     *            like "33 34 35".
     */
    public void setCharacters(String[] splittedFieldAttributes) {
        ArrayList<String> charactersAsHex = new ArrayList<String>();

        for (String attribute : splittedFieldAttributes) {
            if (!attribute.startsWith("(")) {
                charactersAsHex.add(attribute);
            }
        }
        this.charactersAsHex = charactersAsHex;
    }

    public ArrayList<String> getCharactersAsHex() {
        return this.charactersAsHex;
    }

    /**
     * The {@link ArrayList} of Hex values {"41", "42", "43", "44", "45", "46"}
     * parsed into a String representation --> "ABCDEF"
     * 
     * @param charactersAsHex
     *            List of characters in Hex format {"41", "42", "43", "44",
     *            "45", "46"}
     */
    public void setStringRepresentation(ArrayList<String> charactersAsHex) {
        StringBuffer buffer = new StringBuffer();
        for (String string : charactersAsHex) {
            buffer.append(string);
        }
        char[] characters = buffer.toString().toCharArray();
        byte[] bytes;
        try {
            bytes = Hex.decodeHex(characters);
            this.stringRepresentation = new String(bytes, "UTF-8");
        } catch (DecoderException | UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public void setColumnNumber(int rowCount) {
        this.rowCount = rowCount;

    }

    public void setFieldCountPerLine(int fieldCount) {
        this.fieldCountPerlIne = fieldCount;

    }

    /**
     * Sets {@link Field} attributes like startpoint, width, endpoint,
     * characterWidth, characterStartpoint, row
     * 
     * @param row
     *            ArrayList of all Fields in one row in the form "(c0=cd) 30 31
     *            32 33" , "(c0=c1) 34 35 36 37"
     * @param fieldCount
     *            The number of the actual {@link Field} to set the field
     *            attributes in actual row.
     */
    public void setFieldAttributes(ArrayList<String> row, int fieldCount) {
        ArrayList<Integer> allLengthValuesOfRow = new ArrayList<Integer>();
        ArrayList<String> allFieldValuesInRow = new ArrayList<String>();

        int begin = fieldCount - 1;
        int startpoint = 0;
        int lengthToAdd = 0;
        int fieldWidth = 0;

        for (String field : row) {
            String[] splittedValues = field.split(" ");
            fieldWidth = splittedValues.length;
            allFieldValuesInRow.add(field);
            allLengthValuesOfRow.add(new Integer(fieldWidth));
        }

        for (int i = 0; i < begin; i++) {
            lengthToAdd = lengthToAdd + allLengthValuesOfRow.get(i);
        }
        startpoint = lengthToAdd;
        this.startpoint = startpoint;
        this.width = allLengthValuesOfRow.get(begin);
        this.endpoint = startpoint + this.width - 1;
        this.characterWidth = this.width - 1;
        this.characterStartpoint = this.startpoint + 1;
        this.row = row;
    }

    public int getFieldCountPerline() {
        return this.fieldCountPerlIne;
    }

    public int getRowCount() {
        return this.rowCount;
    }

    public int getCharacterWidth() {
        return this.characterWidth;
    }

    public int getCharacterStartpoint() {
        return this.characterStartpoint;
    }

    public int getEndpoint() {
        return this.endpoint;
    }

    public boolean IsNumeric() {
        return this.numeric;
    }

    public boolean isProtected() {
        return this.protectedField;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public String getStringReperentation() {
        return this.stringRepresentation;
    }

    public void setControlCharacter(String line, int fieldNr) {
        this.controlCharacter = createControlCharacter(line, fieldNr);
    }

    /**
     * More information can be found under:
     * 
     * <pre>
     * File : x3270\include\3270ds.h
     * Sources : <a href=
    * "https://sourceforge.net/p/x3270/code/ci/master/tree/">https://sourceforge.net/p/x3270/code/ci/master/tree/</a>
     * 
     * Here are the bit definitions from the source  :
     * 
     * #define FA_PRINTABLE     0xc0    these make the character "printable" 
     * #define FA_PROTECT       0x20    unprotected (0) / protected (1) 
     * #define FA_NUMERIC       0x10    alphanumeric (0) /numeric (1)
     * #define FA_INTENSITY     0x0c    display/selector pen detectable:
     * #define FA_INT_NORM_NSEL 0x00    00 normal, non-detect 
     * #define FA_INT_NORM_SEL  0x04    01 normal, detectable 
     * #define FA_INT_HIGH_SEL  0x08    10 intensified, detectable 
     * #define FA_INT_ZERO_NSEL 0x0c    11 nondisplay, non-detect 
     * #define FA_RESERVED      0x02    must be 0 
     * #define FA_MODIFY        0x01    modified (1) 
     * 
     * This translates as follows:
     *      +--------------------+----------+---------+----------------------+---------+--------+
     * Bits |    7         6     |    5     |    4    |     3          2     |    1    |   0    |
     *      +--------------------+----------+---------+----------------------+---------+--------+
     *      |       Ignore       | Protect  | Numeric |     Display Mode     | Ignore  |Modified|
     *      +--------------------+----------+---------+----------------------+---------+--------+
     * </pre>
     * 
     * @param controlCharacter
     *            The so called field attribute defines the start of a field and
     *            the characteristics of the field.
     *            <p>
     *            Field attribute defines the following field characteristics:
     *            <ul>
     *            <li>Protected or unprotected. A protected field cannot be
     *            modified by the operator. The operator can enter data or
     *            modify the contents of an unprotected field. Unprotected
     *            fields are classified as input fields.
     *            <p>
     *            The Bit representation of a protected field:
     *            <table border="1" >
     *            <tr>
     *            <th>Binary</td>
     *            <th>Hexadecimal</td>
     *            <th>Decimal</td>
     *            </tr>
     *            <tr>
     *            <td>00100000</td>
     *            <td>20</td>
     *            <td>32</td>
     *            </tr>
     *            </table>
     *            </li>
     *            <li>Alphanumeric or numeric. Unprotected alphanumeric fields
     *            are fields into which an operator enters data normally, using
     *            the shift keys (uppercase/lowercase or numeric/alphabetic) as
     *            required. Fields defined as numeric accept all uppercase
     *            symbols and numerics from a data entry keyboard. On a
     *            typewriter keyboard, numeric has no meaning and all entries
     *            are accepted.
     *            <p>
     *            The Bit representation of a numeric field:
     *            <table border="1" >
     *            <tr>
     *            <th>Binary</td>
     *            <th>Hexadecimal</td>
     *            <th>Decimal</td>
     *            </tr>
     *            <tr>
     *            <td>00010000</td>
     *            <td>10</td>
     *            <td>16</td>
     *            </tr>
     *            </table>
     *            </li>
     *            <li>Nondisplay or display/intensified display. The selected
     *            characteristics apply to the entire field. Nondisplay means
     *            that any characters entered from the keyboard are placed in
     *            the buffer for possible subsequent transmission to the
     *            application program, but they are not displayed. Intensified
     *            display means the intensified characters appear on the screen
     *            brighter than the nonintensified characters. Some devices
     *            cannot intensify characters on the screen and highlight
     *            characters in a different manner.
     *            <p>
     *            The Bit representation of a nondisplay field:
     *            <table border="1" >
     *            <tr>
     *            <th>Binary</td>
     *            <th>Hexaddecimal</td>
     *            <th>Decimal</td>
     *            </tr>
     *            <tr>
     *            <td>00001100</td>
     *            <td>c</td>
     *            <td>12</td>
     *            </tr>
     *            </table>
     *            </li>
     *            </ul>
     * 
     * @param hexValueForAttribute
     *            the hexadecimal representation of a {@link Field} attribute
     *            valie
     * @return true if the hex value for the attribute correlates with the
     *         control character, false otherwise. If a control character
     *         correlates bitwise.
     * 
     */
    public boolean isAttribute(String controlCharacter, int hexValueForAttribute) {
        boolean isAttribute = false;
        int intValue = Long.decode("0x" + controlCharacter).intValue();
        int attributeInt = intValue & hexValueForAttribute;
        if (attributeInt == hexValueForAttribute) {
            isAttribute = true;
        }
        return isAttribute;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public void setNumeric(boolean numeric) {
        this.numeric = numeric;
    }

    public void setProtected(boolean protectedField) {
        this.protectedField = protectedField;
    }

    /**
     * Extracts the {@link Field} Control Characters within a row.
     * 
     * @param line
     *            The complete raw row String.
     * @param fieldNr
     *            The number of the specified Field on one row.
     * @return the extracted Control Character in the form "cd".
     */
    private String createControlCharacter(String line, int fieldNr) {
        String result = null;
        String controlCharacter = null;
        Matcher m = Pattern.compile("\\((.*?)\\)").matcher(line);
        int counter = 1;
        while (m.find()) {
            String group = m.group(1);
            if (counter++ == fieldNr) {
                Matcher m2 = Pattern.compile("c0=.{2}").matcher(group);
                while (m2.find()) {
                    controlCharacter = m2.group(0);

                    String[] splittedHexValues = controlCharacter.split("=");
                    result = splittedHexValues[1];
                }
            }
        }
        return result;
    }

}
