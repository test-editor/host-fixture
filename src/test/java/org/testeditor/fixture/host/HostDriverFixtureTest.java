package org.testeditor.fixture.host;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class HostDriverFixtureTest {

    @Test
    public void connectionTest() {
        String S2370_PATH = "S3270_PATH";
        String hostUrl = "HOST_URL";
        int hostPort = 1234;

        HostDriverFixture hdf = mock(HostDriverFixture.class);
        when(hdf.connect(S2370_PATH, hostUrl, hostPort)).thenReturn(true);
        assertEquals(hdf.connect(S2370_PATH, hostUrl, hostPort), true);
        hdf.disconnect();

    }

}
