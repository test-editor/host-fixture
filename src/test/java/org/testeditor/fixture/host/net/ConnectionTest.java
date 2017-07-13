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
    public void testLinkedListSpyCorrect() throws IOException {
        //
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

        Result actualResult = spy.doCommand("test");
        assertEquals("test1", actualResult.getDataLines().get(0));
        assertEquals("ok", actualResult.getStatusString());
    }

}
