package shop.sendbox.sendbox.buyer;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BuyerTest {

	@Test
	@DisplayName("구매자가 회원가입을 하면 활성화 상태이다.")
	void create() {
		// given
		String email = "test@gmail.com";
		String password = "password";
		String name = "홍길동";
		String phoneNumber = "01012345678";
		String createdBy = "admin";
		LocalDateTime createdAt = LocalDateTime.of(2024, 10, 22, 11, 28);

		// when
		Buyer buyer = Buyer.create(email, password, name, phoneNumber, createdAt, createdBy);

		// then
		Assertions.assertThat(buyer.getBuyerStatus()).isEqualTo("ACTIVE");
	}

	@Test
	@DisplayName("구매자가 회원가입시 생성일을 등록하며 수정일은 생성일과 동일하다.")
	void createCreatedAtAndUpdatedAt() {
		// given
		String email = "test@gmail.com";
		String password = "password";
		String name = "홍길동";
		String phoneNumber = "01012345678";
		String createdBy = "admin";
		LocalDateTime createdAt = LocalDateTime.of(2024, 10, 22, 11, 28);

		// when
		Buyer buyer = Buyer.create(email, password, name, phoneNumber, createdAt, createdBy);

		// then
		Assertions.assertThat(buyer.getCreatedAt()).isEqualTo(createdAt);
		Assertions.assertThat(buyer.getUpdatedAt()).isEqualTo(createdAt);
	}
}
