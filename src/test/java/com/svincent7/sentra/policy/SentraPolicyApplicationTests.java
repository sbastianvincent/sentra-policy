package com.svincent7.sentra.policy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SentraPolicyApplicationTests {

	@Autowired
	private SentraPolicyApplication application;

	@Test
	public void contextLoads() {
		// Ensure that the application context loads successfully
		Assertions.assertNotNull(application);
	}

	@Test
	public void testApplicationStart() {
		application.main(new String[] {});
	}

}
