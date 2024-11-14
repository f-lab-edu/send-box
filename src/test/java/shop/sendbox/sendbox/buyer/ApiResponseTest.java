package shop.sendbox.sendbox.buyer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import shop.sendbox.sendbox.api.ApiResponse;

class ApiResponseTest {
	/**
	 * @Test 애노테이션 설명
	 * 해당 애노테이션인 달린 메소드는 테스트 메소드를 나타냅니다.
	 * 메타 애노테이션으로 사용할 수있으며 커스텀 애노테이션도 작성가능합니다.
	 * Junit 프레임워크가 동작하여 해당 어노테이션을 찾아 실행합니다.
	 */
	@Test
	@DisplayName("ApiResponse는 응답코드,메세지,데이터를 입력받아 생성한다.")
	void create() {
		// given
		HttpStatus statusCode = HttpStatus.OK;
		String message = "success";
		String data = "data";
		// when
		ApiResponse<String> apiResponse = ApiResponse.of(statusCode, message, data);

		// then
		// Assertions.assertThat(apiResponse.getStatusCode()).isEqualTo(statusCode.value());
		// Assertions.assertThat(apiResponse.getStatus()).isEqualTo(statusCode);
		// Assertions.assertThat(apiResponse.getMessage()).isEqualTo(message);
		// Assertions.assertThat(apiResponse.getData()).isEqualTo(data);
	}

}
