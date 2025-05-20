package co.kevinll.franchise_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class FranchiseServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
