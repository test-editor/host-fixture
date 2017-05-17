package org.testeditor.fixture.host;

import org.junit.Test;

public class HostDriverFixtureTest {

  @Test
  public void connectionTest() {
    String S2370_PATH = System.getenv("S3270_PATH");
    String hostUrl = System.getenv("HOST_URL");
    int hostPort = Integer.parseInt(System.getenv("HOST_PORT"));

    HostDriverFixture hdf = new HostDriverFixture();
    hdf.connect(S2370_PATH, hostUrl, hostPort);
    hdf.disconnect();

  }

}
