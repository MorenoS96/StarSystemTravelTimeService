package com.moreno.starsystemtraveltimeservice;

import org.junit.jupiter.api.Test;
import org.neo4j.cypherdsl.core.renderer.Configuration;
import org.neo4j.cypherdsl.core.renderer.Dialect;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

@SpringBootTest

class StarSystemTravelTimeServiceApplicationTests {

	@Test
	void contextLoads() {
	}
	@Bean
	Configuration cypherDslConfiguration() {
		return Configuration.newConfig().withDialect(Dialect.NEO4J_5).build();
	}

}
