package shop.sendbox.sendbox.config;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

class AuditorBaseAwareImplTest {

	private AuditorBaseAwareImpl auditorBaseAware;

	@BeforeEach
	void setUp() {
		auditorBaseAware = new AuditorBaseAwareImpl();
	}

	@AfterEach
	void tearDown() {
		RequestContextHolder.resetRequestAttributes();
	}

	@Test
	@DisplayName("HTTP 요청 헤더에 사용자 ID가 있으면 해당 ID를 반환한다")
	void getCurrentAuditor_WithUserId_ReturnsUserId() {
		// given
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("X-User-Id", "123");
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		// when
		Optional<Long> result = auditorBaseAware.getCurrentAuditor();

		// then
		assertThat(result).isPresent();
		assertThat(result.get()).isEqualTo(123L);
	}

	private static Stream<Arguments> requestHeaderFails() {
		return Stream.of(
			Arguments.of("사용자 ID 헤더 없음", null, true),
			Arguments.of("RequestAttributes가 null", null, false),
			Arguments.of("사용자 ID 헤더가 빈 문자열", "", true),
			Arguments.of("사용자 ID 헤더가 숫자가 아님", "not number", true)
		);
	}

	@MethodSource("requestHeaderFails")
	@ParameterizedTest(name = "{0}")
	@DisplayName("잘못된 요청 정보는 빈 Optional 반환")
	void shouldReturnEmptyOptionalForInvalidCases(String caseName,
		Object headerValue,
		boolean setRequestAttributes) {
		// given
		if (setRequestAttributes) {
			MockHttpServletRequest request = new MockHttpServletRequest();
			if (headerValue != null) {
				request.addHeader("X-User-Id", headerValue);
			}
			RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		} else {
			RequestContextHolder.resetRequestAttributes();
		}

		// when
		Optional<Long> result = auditorBaseAware.getCurrentAuditor();

		// then
		assertThat(result).isEmpty();
	}

}