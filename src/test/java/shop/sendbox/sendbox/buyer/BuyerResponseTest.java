package shop.sendbox.sendbox.buyer;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import shop.sendbox.sendbox.buyer.entity.Buyer;
import shop.sendbox.sendbox.buyer.entity.BuyerStatus;
import shop.sendbox.sendbox.buyer.service.BuyerResponse;

class BuyerResponseTest {

	@Test
	@DisplayName("BuyerResponse 생성자 테스트")
	void createBuyerResponse() {
		// given
		final Long buyerId = 1L;
		final String email = "test@gmail.com";
		final String name = "홍길동";
		final String phoneNumber = "01012345678";
		final BuyerStatus buyerStatus = BuyerStatus.ACTIVE;

		// when
		BuyerResponse buyerResponse = new BuyerResponse(buyerId, email, name, phoneNumber, buyerStatus);

		// then
		assertThat(buyerResponse.buyerId()).isEqualTo(buyerId);
		assertThat(buyerResponse.email()).isEqualTo(email);
		assertThat(buyerResponse.name()).isEqualTo(name);
		assertThat(buyerResponse.phoneNumber()).isEqualTo(phoneNumber);
		assertThat(buyerResponse.buyerStatus()).isEqualTo(buyerStatus);
	}

	@Test
	@DisplayName("BuyerResponse of 메서드 테스트")
	void of() {
		// given
		String email = "test@gmail.com";
		String password = "password";
		String salt = "salt";
		String name = "홍길동";
		String phoneNumber = "01012345678";
		LocalDateTime createdAt = LocalDateTime.of(2024, 10, 22, 11, 28);
		String createdBy = "admin";
		final Buyer buyer = Buyer.create(email, password, salt, name, phoneNumber, createdAt, createdBy);

		// when
		BuyerResponse buyerResponse = BuyerResponse.of(buyer, email, phoneNumber);

		// then
		assertThat(buyerResponse.email()).isEqualTo(email);
		assertThat(buyerResponse.name()).isEqualTo(name);
		assertThat(buyerResponse.phoneNumber()).isEqualTo(phoneNumber);
		assertThat(buyerResponse.buyerStatus()).isEqualTo(buyer.getBuyerStatus());
	}
}
