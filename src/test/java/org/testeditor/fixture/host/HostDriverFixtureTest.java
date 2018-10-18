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


import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.testeditor.fixture.host.net.Connection;
import org.testeditor.fixture.host.s3270.Status;
import org.testeditor.fixture.host.screen.Offset;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ org.testeditor.fixture.host.HostDriverFixture.class, org.testeditor.fixture.host.net.Connection.class })
@PowerMockIgnore("javax.management.*")
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
    public void connectionSuccessfulTest() {

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
    public void connectionUnsuccessfulTest() {

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
    public void diconnectionSuccessfulTest() {

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
    public void diconnectionUnsuccessfulTest() {

        // given
        when(con.disconnect()).thenCallRealMethod();

        // when
        boolean disconnected = fixture.disconnect();

        // then
        Assert.assertTrue(disconnected);
    }

}
