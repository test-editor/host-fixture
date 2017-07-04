package org.testeditor.fixture.host.net;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import org.testeditor.fixture.host.s3270.Result;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ConnectionTest {

    @Test
    public void testConnectionWithData() throws IOException {
        // given
        Connection con = new Connection(mock(Process.class), "test", mock(BufferedReader.class),
                mock(PrintWriter.class));
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
        Result actualResult = spy.doCommand("test");

        // then
        assertEquals("test1", actualResult.getDataLines().get(0));
        assertEquals("U F U C(abcdefg.google.mainframe.de) I 2 24 80 3 9 0x0 -", actualResult.getStatusString());
        assertEquals("ok", actualResult.getResultOfCommand());
    }

    @Test
    public void testConnectionWithoutData() throws IOException {
        // given
        Connection con = new Connection(mock(Process.class), "test", mock(BufferedReader.class),
                mock(PrintWriter.class));
        Connection spy = spy(con);

        // You have to use doReturn() for stubbing
        List<String> datalines = new ArrayList<String>();
        datalines.add("U F U C(abcdefg.google.mainframe.de) I 2 24 80 3 9 0x0 -");
        datalines.add("ok");

        doReturn(datalines).when(spy).readOutput();

        // when
        Result actualResult = spy.doCommand("test");

        // then
        assertEquals(0, actualResult.getDataLines().size());
        assertEquals("U F U C(abcdefg.google.mainframe.de) I 2 24 80 3 9 0x0 -", actualResult.getStatusString());
        assertEquals("ok", actualResult.getResultOfCommand());
    }

}
