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
import java.util.Formatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A TermminalScreen represents the ScreenBuffer of a mainframe 3270 screen. The
 * result is a dump of the contents of the screen buffer, one line at a time.
 * Positions inside data fields are generally output as 2-digit hexadecimal
 * codes in the current display character set, displayed in the form:
 * <ul>
 * <li>data: SF(c0=cd) 41 42 43 44 SF(c0=f1) 45 46 47</li>
 * <li>data: SF(c0=f0) 48 49 50 51 SF(c0=f2) 52 53 54</li>
 * <li>...</li>
 * </ul>
 * 
 * Where start-of-field characters (each of which takes up a display position)
 * are output as <STRONG>SF(aa=nn[,...])</STRONG>, where aa is a field attribute
 * type and nn is its value. For more information see <a href=
 * "http://x3270.bgp.nu/x3270-script.html#Script-Specific%20Actions">Script-Specific
 * Actions</a> at section "ReadBuffer(Ascii)".
 * 
 */
public class TerminalScreen {

    private static final Logger logger = LoggerFactory.getLogger(TerminalScreen.class);

    /**
     * 
     * Input is the ScreenBuffer result of x3270 "Readbuffer(Ascii) command in
     * the form: <br>
     * <ul>
     * <li>data: SF(c0=cd) 41 42 43 44 SF(c0=f1) 45 46 47</li>
     * <li>data: SF(c0=f0) 48 49 50 51 SF(c0=f2) 52 53 54</li>
     * <li>...</li>
     * </ul>
     * <b>Caution:</b> the result has to be delivered in formatted structure
     * from mainframe, otherwise we can not parse any field information. See
     * also
     * {@link org.testeditor.fixture.host.HostDriverFixture#printFieldsOnScreen()}
     * 
     * @see org.testeditor.fixture.host.screen.HostTestSite#createHostScreenAsHex()
     * @return All found {@link Field} on screen as {@link ArrayList}.
     */
    public ArrayList<Field> createFields(List<String> screenAsHex) {
        ArrayList<Field> fieldsPerline = new ArrayList<Field>();
        /*
         * At this point you can define to start the row at 0 for the open
         * source x3270 application or for IBM clients starting at position 1.
         * To start at row position 0 "++rowCounter" has to be chosen. To start
         * at position 1 "rowCounter++" has to be chosen.
         */
        int rowCounter = 0;
        for (String rowAsHex : screenAsHex) {
            fieldsPerline.addAll(createFieldsPerLine(rowAsHex, rowCounter++));
        }
        return fieldsPerline;
    }

    /**
     * The result of x3270 "Readbuffer(Ascii) command will be parsed and printed
     * in the form:
     * 
     * <pre>
     * |----------------------------------------------------------------------------------------------------------------------------------------------|
     * |Nr |row|field|width|colStart|colEnd|numeric|protected|hidden|value                                                                           
     * |----------------------------------------------------------------------------------------------------------------------------------------------|
     * |1  |0  |1    |4    |1       |4     |false  |true     |false | 'z/OS'                                                                         
     * |2  |0  |2    |10   |6       |15    |false  |false    |false | ' Z18 Level'                                                                   
     * |3  |0  |3    |5    |17      |21    |true   |false    |false | ' 0609'                                                                        
     * |4  |0  |4    |31   |23      |53    |false  |false    |true  | '                               '                                              
     * |5  |0  |5    |24   |55      |78    |false  |false    |false | 'IP Address = 78.51.59.98'                                                     
     * |6  |0  |6    |6    |80      |85    |false  |false    |false | '
     * ...
     * </pre>
     * 
     * @param screenAsHex
     *            Input is the ScreenBuffer result of x3270 "Readbuffer(Ascii)
     *            command in the form: <br>
     *            <ul>
     *            <li>data: SF(c0=cd) 41 42 43 44 SF(c0=f1) 45 46 47</li>
     *            <li>data: SF(c0=f0) 48 49 50 51 SF(c0=f2) 52 53 54</li>
     *            <li>...</li>
     *            </ul>
     *            <b>Caution:</b> the result has to be delivered in formatted
     *            structure from mainframe, otherwise we can not parse any field
     *            information. See also
     *            {@link org.testeditor.fixture.host.HostDriverFixture#printFieldsOnScreen()}
     * 
     * @see org.testeditor.fixture.host.screen.HostTestSite#createHostScreenAsHex()
     * @return All found {@link Field} reprentations on screen as String.
     */
    public String printAllFieldAsString(List<String> screenAsHex) {
        logger.debug("*** Output of defined fields and their locations on the mainframe screen. ***");
        String delimiter = "\n|----------------------------------------------------------------------------------------------------------------------------------------------|\n";
        String delimiterEnd = "|----------------------------------------------------------------------------------------------------------------------------------------------|\n";
        // @formatter:off
        //                                                    column-      nume-  pro-
        //                        Nr.    row  field  width  start  end     ric   tected hidden  value
        // @formatter:on
        String headerFormat = "|%1$-3s|%2$-3s|%3$-5s|%4$-5s|%5$-8s|%6$-6s|%7$-7s|%8$-9s|%9$-6s|%10$-80s";
        String outputFormat = "|%1$-3d|%2$-3d|%3$-5d|%4$-5d|%5$-8d|%6$-6s|%7$-7s|%8$-9s|%9$-6s|%10$-80s";
        StringBuffer screenBuffer = new StringBuffer();
        int fieldCounter = 0;

        Formatter header = new Formatter();
        Formatter haederFormat = header.format(headerFormat, "Nr", "row", "field", "width", "colStart", "colEnd",
                "numeric", "protected", "hidden", "value");
        screenBuffer.append(delimiter);
        screenBuffer.append(haederFormat);
        screenBuffer.append(delimiter);
        header.close();

        ArrayList<Field> allFieldsOnScreen = createFields(screenAsHex);
        for (Field field : allFieldsOnScreen) {
            String numeric = "";
            // only Input Fields can be set as numeric fields.
            if (!field.isProtected()) {
                numeric = String.valueOf(field.IsNumeric());
            }
            fieldCounter++;
            Formatter formatter = new Formatter();
            formatter.format(outputFormat, fieldCounter, field.getRowCount(), field.getFieldCountPerline(),
                    field.getCharacterWidth(), field.getCharacterStartpoint(), field.getEndpoint(), numeric,
                    field.isProtected(), field.isHidden(), " '" + field.getStringReperentation() + "'");
            screenBuffer.append(formatter + "\n");
            formatter.close();
        }
        String result = screenBuffer.append(delimiterEnd).toString();
        return result;

    }

    /**
     * Creates {@link Field}s of a complete row.
     * 
     * @param rowAsHex
     *            Input of raw line in hexadecimal format:"data: SF(c0=c1) 33 34
     *            35 SF(c0=c1) 3d 36 37 38"
     *            <p>
     *            Input is the result of x3270 "Readbuffer(Ascii) command. <br>
     *            <b>Caution:</b> the result has to be delivered in formatted
     *            structure from mainframe, otherwise we can not parse any field
     *            information.
     * 
     * @param rowCount
     *            The number of row
     * @see org.testeditor.fixture.host.screen.HostTestSite#createHostScreenAsHex()
     * @return all {@link Field}s in one row.
     */
    private ArrayList<Field> createFieldsPerLine(String rowAsHex, int rowCount) {
        ArrayList<Field> fieldsPerline = new ArrayList<Field>();
        // separate Fields "(c0=c1) 33 34 35" as String
        int fieldCount = 0;
        ArrayList<String> row = separateFields(rowAsHex);
        for (String rawField : row) {
            fieldsPerline.add(parseField(row, rawField, rowCount, ++fieldCount, rowAsHex));
        }
        rowCount = 0;
        return fieldsPerline;
    }

    /**
     * Parses the String reprentation of a Field in the format: "SF(c0=c1) 33 34
     * 35 SF(c0=c1) 3d 36 37 38"
     * 
     * @param row
     *            The complete List of all rawField String representations in
     *            one row.
     * @param rawField
     *            String reprentation of a {@link Field} in the format: (c0=c1)
     *            33 34 35
     * @param rowCount
     *            The row number on whole screen.
     * @param fieldCount
     *            The number of a field in one line.
     * @param completeRowAsHex
     *            Raw String of the whole line in the format: "data: SF(c0=e9)
     *            7a 2f 4f 53 SF(c0=c0) 20 5a 31 38 20 4c 65 76 65 6c SF(c0=d1)
     *            20 30 36 30 39"
     * @return {@link Field}
     */
    private Field parseField(ArrayList<String> row, String rawField, int rowCount, int fieldCount,
            String completeRowAsHex) {
        // like separated "c0=c1", "33", "34", "35"
        String[] splittedFieldAttributes = rawField.split(" ");
        Field field = new Field();
        field.setFieldAttributes(row, fieldCount);
        field.setColumnNumber(rowCount);
        field.setFieldCountPerLine(fieldCount);
        field.setSplittedFieldAttributes(splittedFieldAttributes);
        field.setControlCharacters(splittedFieldAttributes);
        field.setCharacters(splittedFieldAttributes);
        field.setStringRepresentation(field.getCharactersAsHex());
        field.setControlCharacter(completeRowAsHex, fieldCount);
        field.setHidden(field.isAttribute(field.getControlCharacter(), Field.DISPLAYABLE));
        field.setNumeric(field.isAttribute(field.getControlCharacter(), Field.NUMERIC));
        field.setProtected(field.isAttribute(field.getControlCharacter(), Field.PROTECTED));
        return field;
    }

    /**
     * 
     * @param rowAsHex
     *            Raw String of the whole line in the format: "data: SF(c0=e9)
     *            7a 2f 4f 53 SF(c0=c0) 20 5a 31 38 20 4c 65 76 65 6c SF(c0=d1)
     *            20 30 36 30 39"
     * @return The complete List of all rawField String representations in one
     *         row. String reprentation of a {@link Field} in the format:
     *         "(c0=c1) 33 34 35", "(c0=c2) 36 37 38", ...
     */
    private ArrayList<String> separateFields(String rowAsHex) {
        ArrayList<String> fieldValues = new ArrayList<String>();
        String dataReplaced = rowAsHex.replaceFirst("^data: ", "");
        String[] splittedFields = dataReplaced.split("SF");
        for (String fieldString : splittedFields) {
            if (!fieldString.isEmpty()) {
                fieldValues.add(fieldString.trim());
            }
        }
        return fieldValues;
    }

    /**
     * 
     * @param row
     *            This is the number of the row starting at 0.
     * @param fieldNumber
     *            This is the number of the field in one row, starting at 1.
     * @param allFields
     *            List of all available {@link Field} s
     * @return The specified {@link Field} with the above chosen arguments.
     */
    public Field getField(int row, int fieldNumber, ArrayList<Field> allFields) {
        Field foundField = null;
        for (Field field : allFields) {
            if (field.getRowCount() == row && field.getFieldCountPerline() == fieldNumber) {
                foundField = field;
            }
        }
        if (foundField == null) {
            throw new RuntimeException(
                    "No field with the specified row:" + row + " and field entry:" + fieldNumber + " found.");
        }
        return foundField;
    }

}
