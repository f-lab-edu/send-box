package shop.sendbox.sendbox.security.auth.exception;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthenticationExceptionTest {

	@Test
	@DisplayName("인증 예외는 지정된 메시지와 함께 생성됩니다")
	void constructor_WithMessage_CreatesExceptionWithMessage() {
		// given
		String errorMessage = "로그인이 필요합니다.";

		// when
		AuthenticationException exception = new AuthenticationException(errorMessage);

		// then
		assertThat(exception.getMessage()).isEqualTo(errorMessage);
	}

	@Test
	@DisplayName("인증 예외는 RuntimeException을 상속받습니다")
	void authenticationException_IsInstanceOfRuntimeException() {
		// given
		AuthenticationException exception = new AuthenticationException("테스트 메시지");

		// when & then
		assertThat(exception).isInstanceOf(RuntimeException.class);
	}
}
