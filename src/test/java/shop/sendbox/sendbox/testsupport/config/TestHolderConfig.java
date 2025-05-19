package shop.sendbox.sendbox.testsupport.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import shop.sendbox.sendbox.security.auth.context.SecurityPrincipalHolder;

@TestConfiguration
public class TestHolderConfig {

	@Bean
	public SecurityPrincipalHolder testSecurityPrincipalHolder() {
		return new SecurityPrincipalHolder();
	}
}
