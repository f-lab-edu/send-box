package shop.sendbox.sendbox.testsupport.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@TestConfiguration
public class TestNoWebMvcConfig {

	@Bean
	@Primary
	public WebMvcConfigurer webMvcConfigurerOverride() {
		return new WebMvcConfigurer() {
			@Override
			public void addInterceptors(InterceptorRegistry registry) {
				// 아무것도 등록하지 않음
			}
		};
	}
}
