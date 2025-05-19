package shop.sendbox.sendbox.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.sendbox.sendbox.security.auth.context.SecurityPrincipalHolder;
import shop.sendbox.sendbox.security.auth.filter.ExceptionTranslationFilter;
import shop.sendbox.sendbox.security.auth.filter.SessionAuthenticationFilter;
import shop.sendbox.sendbox.security.auth.interceptor.AuthorizationInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	private final ObjectMapper objectMapper;
	private final SecurityPrincipalHolder securityPrincipalHolder;

	public WebMvcConfig(ObjectMapper objectMapper, SecurityPrincipalHolder securityPrincipalHolder) {
		this.objectMapper = objectMapper;
		this.securityPrincipalHolder = securityPrincipalHolder;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authorizationInterceptor())
			.addPathPatterns("/**");
	}

	@Bean
	public AuthorizationInterceptor authorizationInterceptor() {
		return new AuthorizationInterceptor(securityPrincipalHolder);
	}

	@Bean
	public FilterRegistrationBean<ExceptionTranslationFilter> exceptionTranslationFilter() {
		FilterRegistrationBean<ExceptionTranslationFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new ExceptionTranslationFilter(objectMapper));
		registrationBean.addUrlPatterns("/*");
		registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE); // 가장 먼저 실행
		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean<SessionAuthenticationFilter> authenticationFilter() {
		FilterRegistrationBean<SessionAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new SessionAuthenticationFilter(securityPrincipalHolder));
		registrationBean.addUrlPatterns("/*");
		registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1); // 예외 필터 다음으로 실행
		return registrationBean;
	}
}
