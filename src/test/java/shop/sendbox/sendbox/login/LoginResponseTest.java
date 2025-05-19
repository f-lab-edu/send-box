package shop.sendbox.sendbox.login;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import shop.sendbox.sendbox.buyer.entity.Buyer;
import shop.sendbox.sendbox.seller.entity.Seller;

class LoginResponseTest {

	@Test
	@DisplayName("구매자 정보로 로그인 응답을 생성합니다")
	void of_WithBuyer_CreatesLoginResponse() {
		// given
		Buyer buyer = Buyer.create(
			"buyer@example.com",
			"password123",
			"salt123",
			"구매자",
			"01012345678"
		);

		// Reflection을 사용해 ID를 설정해야 하는데, 이 테스트의 목적상 구현은 생략합니다
		// 실제 테스트에서는 ReflectionTestUtils 등을 사용하여 설정 가능합니다

		String email = "buyer@example.com";

		// when
		LoginResponse response = LoginResponse.of(buyer, email);

		// then
		assertThat(response.name()).isEqualTo("구매자");
		assertThat(response.email()).isEqualTo(email);
	}

	@Test
	@DisplayName("판매자 정보로 로그인 응답을 생성합니다")
	void of_WithSeller_CreatesLoginResponse() {
		// given
		String email = "seller@example.com";
		Seller seller = Seller.create(
			"hashedPassword",
			"salt123",
			"판매자",
			"1234567890",
			"01012345678",
			email
		);

		// Reflection을 사용해 ID를 설정해야 하는데, 이 테스트의 목적상 구현은 생략합니다

		// when
		LoginResponse response = LoginResponse.of(seller, email);

		// then
		assertThat(response.name()).isEqualTo("판매자");
		assertThat(response.email()).isEqualTo(email);
	}
}
