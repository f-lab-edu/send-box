package shop.sendbox.sendbox.buyer;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import shop.sendbox.sendbox.buyer.entity.BuyerStatus;
import shop.sendbox.sendbox.buyer.service.BuyerRequest;
import shop.sendbox.sendbox.buyer.service.BuyerResponse;
import shop.sendbox.sendbox.buyer.service.BuyerService;

@ActiveProfiles("test")
@SpringBootTest
class BuyerServiceTest {

	@Autowired
	BuyerService buyerService;

	@Test
	@DisplayName("구매자는 정보를 입력하여 가입할 수 있다.")
	void signUp() {
		// given
		final LocalDateTime createdAt = LocalDateTime.of(2024, 10, 22, 11, 28);
		final String email = "test@gmail.com";
		final String password = "password";
		final String name = "홍길동";
		final String phoneNumber = "01012345678";
		final String createdBy = "admin";
		BuyerRequest buyerRequest = new BuyerRequest(email, password, name, phoneNumber, null, createdBy);

		// when
		BuyerResponse buyerResponse = buyerService.signUp(buyerRequest, createdAt);

		// then
		Assertions.assertThat(buyerResponse.email()).isEqualTo(email);
		Assertions.assertThat(buyerResponse.name()).isEqualTo(name);
		Assertions.assertThat(buyerResponse.phoneNumber()).isEqualTo(phoneNumber);
		Assertions.assertThat(buyerResponse.buyerStatus()).isEqualTo(BuyerStatus.ACTIVE);
	}
}
