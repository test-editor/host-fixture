package org.testeditor.fixture.host;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testeditor.fixture.host.s3270.options.CharacterSet.CHAR_GERMAN_EURO;
import static org.testeditor.fixture.host.s3270.options.TerminalMode.MODE_24x80;
import static org.testeditor.fixture.host.s3270.options.TerminalType.TYPE_3279;

import org.testeditor.fixture.host.net.Connection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HostDriverFixtureTest {

    String S3270_PATH = "S3270_PATH";
    String HOST_URL = "HOST_URL";
    int HOST_PORT = 1234;
    HostDriverFixture fixture;
    Connection con;

    @Before
    public void init() {
        con = mock(Connection.class);
        fixture = new HostDriverFixture(con);
    }

    @Test
    public void connectionSuccessfulTest() {

        // given
        when(con.isConnected()).thenReturn(true);

        // when
        boolean connected = fixture.connect(S3270_PATH, HOST_URL, HOST_PORT);

        // then
        verify(con).connect(S3270_PATH, HOST_URL, HOST_PORT, TYPE_3279, MODE_24x80, CHAR_GERMAN_EURO);
        Assert.assertTrue(connected);
    }

    @Test
    public void connectionUnsuccessfulTest() {

        // given
        when(con.isConnected()).thenReturn(false);

        // when
        boolean connected = fixture.connect(S3270_PATH, HOST_URL, HOST_PORT);

        // then
        verify(con).connect(S3270_PATH, HOST_URL, HOST_PORT, TYPE_3279, MODE_24x80, CHAR_GERMAN_EURO);
        Assert.assertFalse(connected);
    }

    @Test
    public void diconnectionSuccessfulTest() {

        // given
        when(con.disconnect()).thenReturn(true);

        // when
        boolean disconnected = fixture.disconnect();

        // then
        Assert.assertTrue(disconnected);
    }

    @Test(expected = RuntimeException.class)
    public void diconnectionUnsuccessfulTest() {

        // given
        Connection connection = new Connection();
        HostDriverFixture hostDriverFixture = new HostDriverFixture(connection);

        // when
        boolean disconnected = hostDriverFixture.disconnect();

        // then
        Assert.assertTrue(disconnected);
    }
}
