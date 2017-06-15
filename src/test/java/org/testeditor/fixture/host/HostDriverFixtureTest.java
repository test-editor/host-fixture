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

    private static final String S3270_PATH = "S3270_PATH";
    private static final String HOST_URL = "HOST_URL";
    private static final int HOST_PORT = 1234;
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
