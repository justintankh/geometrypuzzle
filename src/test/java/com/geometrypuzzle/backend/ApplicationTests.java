package com.geometrypuzzle.backend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {

	@Test()
	@EnabledOnOs({OS.MAC, OS.WINDOWS})
		/* To mock disabled on github action
	* - For future  of spring profiles */
	void contextLoads() {
	}

}
