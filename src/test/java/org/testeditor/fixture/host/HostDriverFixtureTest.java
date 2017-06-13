package org.testeditor.fixture.host;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testeditor.fixture.host.s3270.options.CharacterSet.CHAR_GERMAN_EURO;
import static org.testeditor.fixture.host.s3270.options.TerminalMode.MODE_24x80;
import static org.testeditor.fixture.host.s3270.options.TerminalType.TYPE_3279;

import org.testeditor.fixture.host.net.Connection;

import org.junit.Assert;
import org.junit.Test;

public class HostDriverFixtureTest {

    String S3270_PATH = "S3270_PATH";
    String hostUrl = "HOST_URL";
    int hostPort = 1234;
    HostDriverFixture hdf;

    @Test
    public void connectionSuccessfulTest() {

        // given
        Connection con = mock(Connection.class);
        HostDriverFixture hdf = new HostDriverFixture(con);
        when(con.isConnected()).thenReturn(true);

        // when
        boolean connected = hdf.connect(S3270_PATH, hostUrl, hostPort);

        // then
        verify(con).connect(S3270_PATH, hostUrl, hostPort, TYPE_3279, MODE_24x80, CHAR_GERMAN_EURO);
        Assert.assertTrue(connected);
    }

    @Test
    public void connectionUnsuccessfulTest() {

        // given
        Connection con = mock(Connection.class);
        HostDriverFixture hdf = new HostDriverFixture(con);
        when(con.isConnected()).thenReturn(false);

        // when
        boolean connected = hdf.connect(S3270_PATH, hostUrl, hostPort);

        // then
        verify(con).connect(S3270_PATH, hostUrl, hostPort, TYPE_3279, MODE_24x80, CHAR_GERMAN_EURO);
        Assert.assertFalse(connected);
    }

    @Test
    public void diconnectionSuccessfulTest() {

        // given
        Connection con = mock(Connection.class);
        HostDriverFixture hdf = new HostDriverFixture(con);
        when(con.disconnect()).thenReturn(true);

        // when
        boolean disconnected = hdf.disconnect();

        // then
        Assert.assertTrue(disconnected);
    }

    @Test(expected = RuntimeException.class)
    public void diconnectionUnsuccessfulTest() {

        // given
        Connection con = new Connection();
        HostDriverFixture hdf = new HostDriverFixture(con);

        // when
        boolean disconnected = hdf.disconnect();

        // then
        Assert.assertTrue(disconnected);
    }
}
