package org.testeditor.fixture.host;

import org.junit.Test;

public class HostDriverFixtureTest {
	
	
	@Test
	public void test(){
		HostDriverFixture hdf = new HostDriverFixture();
		hdf.connect();
	}

}
