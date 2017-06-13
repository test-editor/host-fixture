package org.testeditor.fixture.host.integrationtest;

import org.testeditor.fixture.host.HostDriverFixture;

import org.junit.Assert;
import org.junit.Ignore;

public class HostDriverIntegrationTest {

    @Ignore("manual execution only in special environments")
    /**
     * This integrationtest is only for a special environment and not for
     * executing in a gradle test environment. You can uncomment the
     * annotation @Test to test this special environment. Please do not delete
     * this file.
     */
    public void connectionTest() {
        String S2370_PATH = System.getenv("S3270_PATH");
        String hostUrl = System.getenv("HOST_URL");
        int hostPort = Integer.parseInt(System.getenv("HOST_PORT"));

        HostDriverFixture hdf = new HostDriverFixture();
        Assert.assertTrue(hdf.connect(S2370_PATH, hostUrl, hostPort));
        Assert.assertTrue(hdf.disconnect());

    }

}
