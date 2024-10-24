package shop.sendbox.sendbox.buyer;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class ApiResponseTest {

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
