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
package org.testeditor.fixture.host;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.testeditor.fixture.core.FixtureException;
import org.testeditor.fixture.host.net.Connection;
import org.testeditor.fixture.host.s3270.Status;
import org.testeditor.fixture.host.screen.Offset;

public class HostDriverFixtureTest {

    private static final String S3270_PATH = "S3270_PATH";
    private static final String HOST_URL = "HOST_URL";
    private static final int HOST_PORT = 1234;
    HostDriverFixture fixture;
    Connection con;
    Offset offset = mock(Offset.class);
    private int offsetRow = 1;
    private int offsetColumn = 1;

    @Before
    public void init() {
        // fixture = mock(HostDriverFixture.class);
        con = mock(Connection.class);
        fixture = new HostDriverFixture(con);

    }

    @Test
    public void connectionSuccessfulTest() throws FixtureException {

        // given
        when(con.connect(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(con);
        when(con.isConnected()).thenReturn(true);

        // when
        String defaultStatusString = "U U U C(abcdefg.hi.google-mainframe.com) I 2 24 80 6 44 0x0 -";
        Offset offset = new Offset(1, 1);
        Status status = new Status(defaultStatusString, offset);
        when(con.getStatus()).thenReturn(status);
        boolean connected = fixture.connect(S3270_PATH, HOST_URL, HOST_PORT, offsetRow, offsetColumn);

        // then
        Assert.assertTrue(connected);
    }

    @Test
    public void connectionUnsuccessfulTest() throws FixtureException {

        // given
        when(con.connect(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(con);
        when(con.isConnected()).thenReturn(false);

        // when
        boolean connected = fixture.connect(S3270_PATH, HOST_URL, HOST_PORT, offsetRow, offsetColumn);

        // then
        Assert.assertFalse(connected);
    }

    @Test
    public void disconnectionSuccessfulTest() throws FixtureException {

        // given
        when(con.connect(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(con);

        when(con.disconnect()).thenReturn(true);

        // when
        boolean disconnected = fixture.disconnect();

        // then
        Assert.assertTrue(disconnected);
    }

    @Test(expected = RuntimeException.class)
    public void disconnectionUnsuccessfulTest() throws FixtureException {

        // given
        when(con.disconnect()).thenCallRealMethod();

        // when
        boolean disconnected = fixture.disconnect();

        // then
        Assert.assertTrue(disconnected);
    }

}
