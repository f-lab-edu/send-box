package shop.sendbox.sendbox.buyer;

import static shop.sendbox.sendbox.buyer.entity.BuyerStatus.*;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import shop.sendbox.sendbox.buyer.entity.Buyer;

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
		Assertions.assertThat(buyer.getBuyerStatus()).isEqualTo(ACTIVE);
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

	@Test
	@DisplayName("구매자는 비밀번호 확인을 통해 비밀번호를 일치하면 오류가 발생하지 않는다.")
	void matchPassword() {
		// given
		String email = "test@gmail.com";
		String password = "password";
		String name = "홍길동";
		String phoneNumber = "01012345678";
		String createdBy = "admin";
		LocalDateTime createdAt = LocalDateTime.of(2024, 10, 22, 11, 28);
		Buyer buyer = Buyer.create(email, password, name, phoneNumber, createdAt, createdBy);

		// when & then
		Assertions.assertThatCode(() -> buyer.validatePassword(password)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("구매자는 비밀번호 확인을 통해 비밀번호를 일치하지 않으면 오류가 발생한다")
	void matchPasswordWithFail() {
		// given
		String email = "test@gmail.com";
		String password = "password";
		String name = "홍길동";
		String phoneNumber = "01012345678";
		String createdBy = "admin";
		LocalDateTime createdAt = LocalDateTime.of(2024, 10, 22, 11, 28);
		Buyer buyer = Buyer.create(email, password, name, phoneNumber, createdAt, createdBy);
		String failPassword = "luma";

		// when & then
		Assertions.assertThatThrownBy(() -> buyer.validatePassword(failPassword))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("비밀번호가 일치하지 않습니다.");

	}
}
