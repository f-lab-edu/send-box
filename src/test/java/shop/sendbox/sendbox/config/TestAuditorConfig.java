package shop.sendbox.sendbox.config;

import java.util.Optional;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.AuditorAware;

@TestConfiguration
public class TestAuditorConfig {
	@Bean
	@Primary
	@Profile("test")
	public AuditorAware<Long> testAuditorProvider() {
		return new TestAuditorAwareImpl();
	}

	public static class TestAuditorAwareImpl implements AuditorAware<Long> {
		private Long currentUserId = 1L;

		public void setUserId(Long userId) {
			this.currentUserId = userId;
		}

		@Override
		public Optional<Long> getCurrentAuditor() {
			return Optional.of(currentUserId);
		}
	}
}
