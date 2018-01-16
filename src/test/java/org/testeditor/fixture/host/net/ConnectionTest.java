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
package org.testeditor.fixture.host.net;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.testeditor.fixture.host.s3270.Result;
import org.testeditor.fixture.host.s3270.actions.Command;
import org.testeditor.fixture.host.screen.Offset;

public class ConnectionTest {

    @Test
    public void testConnectionWithData() throws IOException {
        // given
        Offset offset = new Offset(-1, -1);

        Connection con = new Connection(mock(Process.class), "test", mock(BufferedReader.class),
                mock(PrintWriter.class), offset);
        Connection spy = spy(con);

        // You have to use doReturn() for stubbing
        List<String> datalines = new ArrayList<String>();
        datalines.add("test1");
        datalines.add("test2");
        datalines.add("test3");
        datalines.add("test4");
        datalines.add("test5");
        datalines.add("test6");
        datalines.add("U F U C(abcdefg.google.mainframe.de) I 2 24 80 3 9 0x0 -");
        datalines.add("ok");

        doReturn(datalines).when(spy).readOutput();

        // when
        Command command = new Command("Test", "jsut for test");
        Result actualResult = spy.doCommand("test", command);

        // then
        assertEquals("test1", actualResult.getDataLines().get(0));
        assertEquals("U F U C(abcdefg.google.mainframe.de) I 2 24 80 3 9 0x0 -", actualResult.getStatusString());
        assertEquals("ok", actualResult.getResultOfCommand());
    }

    @Test
    public void testConnectionWithoutData() throws IOException {
        // given
        Offset offset = new Offset(-1, -1);
        Connection con = new Connection(mock(Process.class), "test", mock(BufferedReader.class),
                mock(PrintWriter.class), offset);
        Connection spy = spy(con);

        // You have to use doReturn() for stubbing
        List<String> datalines = new ArrayList<String>();
        datalines.add("U F U C(abcdefg.google.mainframe.de) I 2 24 80 3 9 0x0 -");
        datalines.add("ok");

        doReturn(datalines).when(spy).readOutput();

        // when
        Command command = new Command("Test", "jsut for test");
        Result actualResult = spy.doCommand("test", command);

        // then
        assertEquals(0, actualResult.getDataLines().size());
        assertEquals("U F U C(abcdefg.google.mainframe.de) I 2 24 80 3 9 0x0 -", actualResult.getStatusString());
        assertEquals("ok", actualResult.getResultOfCommand());
    }

}
