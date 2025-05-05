package shop.sendbox.sendbox.api;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ErrorResponseTest {

	@Test
	@DisplayName("인증되지 않은 요청에 대한 에러 응답을 생성합니다")
	void unauthorized_CreatesUnauthorizedErrorResponse() {
		// when
		ErrorResponse response = ErrorResponse.unauthorized();

		// then
		assertThat(response.statusCode()).isEqualTo(401);
		assertThat(response.message()).isEqualTo("UNAUTHORIZED");
		assertThat(response.errorCode()).isEqualTo("AUTH_001");
		assertThat(response.errorDetail()).isEqualTo("이 리소스에 접근하기 위해서는 인증이 필요합니다.");
	}

	@Test
	@DisplayName("접근 거부된 요청에 대한 에러 응답을 생성합니다")
	void accessDenied_CreatesAccessDeniedErrorResponse() {
		// when
		ErrorResponse response = ErrorResponse.accessDenied();

		// then
		assertThat(response.statusCode()).isEqualTo(403);
		assertThat(response.message()).isEqualTo("ACCESS_DENIED");
		assertThat(response.errorCode()).isEqualTo("AUTH_003");
		assertThat(response.errorDetail()).isEqualTo("이 리소스에 접근할 권한이 없습니다.");
	}

	@Test
	@DisplayName("서버 오류에 대한 에러 응답을 생성합니다")
	void serverError_CreatesServerErrorResponse() {
		// when
		ErrorResponse response = ErrorResponse.serverError();

		// then
		assertThat(response.statusCode()).isEqualTo(500);
		assertThat(response.message()).isEqualTo("SERVER_ERROR");
		assertThat(response.errorCode()).isEqualTo("SYS_001");
		assertThat(response.errorDetail()).isEqualTo("예기치 않은 오류가 발생했습니다. 나중에 다시 시도해 주세요.");
	}

	@Test
	@DisplayName("잘못된 요청에 대한 에러 응답을 생성합니다")
	void badRequest_CreatesBadRequestErrorResponse() {
		// given
		String errorMessage = "유효하지 않은 입력입니다";

		// when
		ErrorResponse response = ErrorResponse.badRequest(errorMessage);

		// then
		assertThat(response.statusCode()).isEqualTo(400);
		assertThat(response.message()).isEqualTo("BAD_REQUEST");
		assertThat(response.errorCode()).isEqualTo("SYS_002");
		assertThat(response.errorDetail()).isEqualTo(errorMessage);
	}

	@Test
	@DisplayName("사용자 정의 에러 응답을 생성합니다")
	void of_CreatesCustomErrorResponse() {
		// given
		int statusCode = 422;
		String message = "VALIDATION_ERROR";
		String errorCode = "VAL_001";
		String errorDetail = "필수 입력값이 누락되었습니다";

		// when
		ErrorResponse response = ErrorResponse.of(statusCode, message, errorCode, errorDetail);

		// then
		assertThat(response.statusCode()).isEqualTo(statusCode);
		assertThat(response.message()).isEqualTo(message);
		assertThat(response.errorCode()).isEqualTo(errorCode);
		assertThat(response.errorDetail()).isEqualTo(errorDetail);
	}
}
