package shop.sendbox.sendbox.config;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.core.Ordered;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import jakarta.servlet.Filter;
import shop.sendbox.sendbox.security.auth.filter.ExceptionTranslationFilter;
import shop.sendbox.sendbox.security.auth.filter.SessionAuthenticationFilter;
import shop.sendbox.sendbox.security.auth.interceptor.AuthorizationInterceptor;

@SpringBootTest
@ContextConfiguration
class WebMvcConfigIntegrationTest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Test
	@DisplayName("필터가 올바른 순서로 등록됩니다")
	void filterRegistrationOrder() {
		// Given
		Map<String, FilterRegistrationBean> filterBeans = webApplicationContext.getBeansOfType(
			FilterRegistrationBean.class);

		// When
		FilterRegistrationBean<?> exceptionFilter = findFilterByType(filterBeans, ExceptionTranslationFilter.class);
		FilterRegistrationBean<?> authFilter = findFilterByType(filterBeans, SessionAuthenticationFilter.class);

		// Then
		assertThat(exceptionFilter).isNotNull();
		assertThat(authFilter).isNotNull();

		assertThat(exceptionFilter.getOrder()).isEqualTo(Ordered.HIGHEST_PRECEDENCE);
		assertThat(authFilter.getOrder()).isEqualTo(Ordered.HIGHEST_PRECEDENCE + 1);

		assertThat(exceptionFilter.getUrlPatterns()).containsExactly("/*");
		assertThat(authFilter.getUrlPatterns()).containsExactly("/*");
	}

	@Test
	@DisplayName("필터 order 번호에 중복이 없습니다")
	void filterOrderUniqueness() {
		// Given
		Map<String, FilterRegistrationBean> filterBeans = webApplicationContext.getBeansOfType(
			FilterRegistrationBean.class);

		// When
		Map<Integer, List<FilterRegistrationBean>> orderGroups = filterBeans.values().stream()
			.collect(Collectors.groupingBy(bean -> bean.getOrder()));

		// Then
		orderGroups.forEach((order, filters) -> {
			assertThat(filters).as("Order " + order + "에 대해 하나의 필터만 있어야 합니다").hasSize(1);
		});
	}

	@Test
	@DisplayName("인터셉터 간에 order 번호 중복이 없습니다")
	void interceptorOrderUniqueness() throws Exception {
		// given
		RequestMappingHandlerMapping mapping = webApplicationContext.getBean(RequestMappingHandlerMapping.class);
		MockHttpServletRequest request = new MockHttpServletRequest("POST", "/buyers");

		// when
		HandlerExecutionChain handlerChain = mapping.getHandler(request);
		Assertions.assertNotNull(handlerChain);
		List<HandlerInterceptor> interceptors = Arrays.asList(Objects.requireNonNull(handlerChain.getInterceptors()));

		// then
		List<? extends Class<?>> actualInterceptorClasses = interceptors.stream()
			.map(Object::getClass)
			.toList();

		List<Class<? extends Object>> expectedOrder = List.of(
			AuthorizationInterceptor.class
		);

		for (int i = 0; i < expectedOrder.size(); i++) {
			Class<? extends Object> expectedClass = expectedOrder.get(i);
			assertThat(actualInterceptorClasses)
				.as("인덱스 " + i + "에 " + expectedClass.getSimpleName() + " 클래스가 있어야 합니다")
				.element(i)
				.isEqualTo(expectedClass);
		}
	}

	private FilterRegistrationBean<?> findFilterByType(Map<String, FilterRegistrationBean> filterBeans,
		Class<? extends Filter> filterType) {
		return filterBeans.values().stream()
			.filter(bean -> filterType.isInstance(bean.getFilter()))
			.findFirst()
			.orElse(null);
	}
}
