package shop.sendbox.sendbox.security.auth;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ApiEndpointEnumTest {

	@Test
	@DisplayName("경로와 메서드가 정확히 일치하면 true를 반환합니다")
	void matches_whenExactMatch_returnsTrue() {
		// given
		String requestUri = "/coupons";
		String requestMethod = "POST";

		// when
		boolean result = ApiEndpointEnum.COUPON_CREATE.matches(requestUri, requestMethod);

		// then
		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("쿼리 파라미터가 있어도 경로가 일치하면 true를 반환합니다")
	void matches_withQueryParams_returnsTrue() {
		// given
		String requestUri = "/coupons?name=discount&amount=1000";
		String requestMethod = "POST";

		// when
		boolean result = ApiEndpointEnum.COUPON_CREATE.matches(requestUri, requestMethod);

		// then
		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("메서드가 대소문자 구분 없이 일치하면 true를 반환합니다")
	void matches_withCaseInsensitiveMethod_returnsTrue() {
		// given
		String requestUri = "/coupons";
		String requestMethod = "post";

		// when
		boolean result = ApiEndpointEnum.COUPON_CREATE.matches(requestUri, requestMethod);

		// then
		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("경로는 같지만 메서드가 다르면 false를 반환합니다")
	void matches_withDifferentMethod_returnsFalse() {
		// given
		String requestUri = "/coupons";
		String requestMethod = "GET";

		// when
		boolean result = ApiEndpointEnum.COUPON_CREATE.matches(requestUri, requestMethod);

		// then
		assertThat(result).isFalse();
	}

	@Test
	@DisplayName("메서드는 같지만 경로가 다르면 false를 반환합니다")
	void matches_withDifferentPath_returnsFalse() {
		// given
		String requestUri = "/orders";
		String requestMethod = "POST";

		// when
		boolean result = ApiEndpointEnum.COUPON_CREATE.matches(requestUri, requestMethod);

		// then
		assertThat(result).isFalse();
	}

	@Test
	@DisplayName("일치하는 엔드포인트가 있으면 해당 권한을 반환합니다")
	void matchEndpoint_withMatchingEndpoint_returnsCorrectPermission() {
		// given
		String requestUri = "/coupons";
		String requestMethod = "POST";

		// when
		Permission result = ApiEndpointEnum.matchEndpoint(requestUri, requestMethod);

		// then
		assertThat(result).isEqualTo(Permission.SELLER);
	}

	@Test
	@DisplayName("일치하는 엔드포인트가 없으면 PUBLIC 권한을 반환합니다")
	void matchEndpoint_withNoMatchingEndpoint_returnsPublicPermission() {
		// given
		String requestUri = "/non-existing-path";
		String requestMethod = "GET";

		// when
		Permission result = ApiEndpointEnum.matchEndpoint(requestUri, requestMethod);

		// then
		assertThat(result).isEqualTo(Permission.PUBLIC);
	}
}
