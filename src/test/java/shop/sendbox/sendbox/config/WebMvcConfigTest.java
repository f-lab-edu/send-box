package shop.sendbox.sendbox.config;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.sendbox.sendbox.security.auth.context.SecurityPrincipalHolder;
import shop.sendbox.sendbox.security.auth.filter.ExceptionTranslationFilter;
import shop.sendbox.sendbox.security.auth.filter.SessionAuthenticationFilter;
import shop.sendbox.sendbox.security.auth.interceptor.AuthorizationInterceptor;

@ExtendWith(MockitoExtension.class)
class WebMvcConfigTest {

	@Mock
	private ObjectMapper objectMapper;

	@Mock
	private SecurityPrincipalHolder securityPrincipalHolder;

	@InjectMocks
	private WebMvcConfig webMvcConfig;

	@Test
	@DisplayName("인가 인터셉터가 정상적으로 생성됩니다")
	void authorizationInterceptorCreation() {
		AuthorizationInterceptor interceptor = webMvcConfig.authorizationInterceptor();

		assertThat(interceptor).isNotNull();
	}

	@Test
	@DisplayName("인가 인터셉터는 모든 경로에 등록됩니다")
	void interceptorRegistration() {
		// Given
		InterceptorRegistry registry = mock(InterceptorRegistry.class);
		InterceptorRegistration registration = mock(InterceptorRegistration.class);
		when(registry.addInterceptor(any(AuthorizationInterceptor.class))).thenReturn(registration);

		// When
		webMvcConfig.addInterceptors(registry);

		// Then
		verify(registry).addInterceptor(any(AuthorizationInterceptor.class));
		verify(registration).addPathPatterns("/**");
	}

	@Test
	@DisplayName("예외 필터는 가장 높은 우선순위로 등록됩니다")
	void exceptionFilterRegistration() {
		FilterRegistrationBean<ExceptionTranslationFilter> registration = webMvcConfig.exceptionTranslationFilter();

		assertThat(registration).isNotNull();
		assertThat(registration.getOrder()).isEqualTo(Ordered.HIGHEST_PRECEDENCE);
		assertThat(registration.getUrlPatterns()).containsExactly("/*");
	}

	@Test
	@DisplayName("인증 필터는 예외 필터보다 나중에 등록됩니다")
	void authFilterRegistration() {
		FilterRegistrationBean<SessionAuthenticationFilter> registration = webMvcConfig.authenticationFilter();

		assertThat(registration).isNotNull();
		assertThat(registration.getOrder()).isEqualTo(Ordered.HIGHEST_PRECEDENCE + 1);
		assertThat(registration.getUrlPatterns()).containsExactly("/*");
	}

	@Test
	@DisplayName("필터 등록 순서는 충돌이 없습니다")
	void filterOrderNoConflict() {
		FilterRegistrationBean<ExceptionTranslationFilter> exceptionFilter = webMvcConfig.exceptionTranslationFilter();
		FilterRegistrationBean<SessionAuthenticationFilter> authFilter = webMvcConfig.authenticationFilter();

		assertThat(exceptionFilter.getOrder()).isNotEqualTo(authFilter.getOrder());
	}
}
