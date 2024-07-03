package com.email.container;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestContainersConfig.class)
@SpringBootTest
class EmailTestApplicationTests {

	@Test
	void contextLoads() {
	}

}
