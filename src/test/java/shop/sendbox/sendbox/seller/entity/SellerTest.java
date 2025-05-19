package shop.sendbox.sendbox.seller.entity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import shop.sendbox.sendbox.buyer.entity.YnCode;

class SellerTest {

	@Test
	@DisplayName("판매자 생성: 입력된 정보가 올바르게 저장되는지 확인")
	void createSellerSuccess() {
		// given
		String password = "password123";
		String salt = "randomSaltValue"; // salt 추가
		String name = "홍길동상회";
		String businessNumber = "123-45-67890";
		String phoneNumber = "010-1234-5678";
		String taxEmail = "tax@example.com";

		// when
		Seller seller = Seller.create(password, salt, name, businessNumber, phoneNumber, taxEmail); // salt 인자 추가

		// then
		assertThat(seller.getPassword()).isEqualTo(password);
		assertThat(seller.getSalt()).isEqualTo(salt);
		assertThat(seller.getName()).isEqualTo(name);
		assertThat(seller.getBusinessNumber()).isEqualTo(businessNumber);
		assertThat(seller.getPhoneNumber()).isEqualTo(phoneNumber);
		assertThat(seller.getTaxEmail()).isEqualTo(taxEmail);
	}

	@Test
	@DisplayName("판매자 생성시 기본 상태가 'ACTIVE'로 설정되는지 확인")
	void createSellerSetsDefaultStatus() {
		// given
		String password = "password123";
		String salt = "randomSaltValue";
		String name = "판매자";
		String businessNumber = "123-45-67890";
		String phoneNumber = "010-1234-5678";
		String taxEmail = "tax@example.com";

		// when
		Seller seller = Seller.create(password, salt, name, businessNumber, phoneNumber, taxEmail);

		// then
		assertThat(seller.getSellerStatus()).isEqualTo(SellerStatus.ACTIVE);
	}

	@Test
	@DisplayName("판매자 생성: 기본 삭제 여부가 'N'으로 설정되는지 확인")
	void createSellerSetsDefaultDeleteYn() {
		// given
		String password = "password123";
		String salt = "randomSaltValue";
		String name = "판매자";
		String businessNumber = "123-45-67890";
		String phoneNumber = "010-1234-5678";
		String taxEmail = "tax@example.com";

		// when
		Seller seller = Seller.create(password, salt, name, businessNumber, phoneNumber, taxEmail);

		// then
		assertThat(seller.getDeleteYn()).isEqualTo(YnCode.N);
	}

	@Test
	@DisplayName("비밀번호 확인: 입력된 비밀번호가 저장된 비밀번호와 일치하는 경우 true 반환")
	void isPasswordEqualsCorrectPasswordReturnsTrue() {
		// given
		String correctPassword = "password123";
		String salt = "randomSaltValue";
		Seller seller = Seller.create(correctPassword, salt, "판매자", "123-45-67890", "010-1234-5678", "tax@example.com");

		// when
		boolean result = seller.isPasswordEquals(correctPassword);

		// then
		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("비밀번호 확인: 입력된 비밀번호가 저장된 비밀번호와 일치하지 않는 경우 false 반환")
	void isPasswordEqualsIncorrectPasswordReturnsFalse() {
		// given
		String correctPassword = "password123";
		String incorrectPassword = "wrongPassword";
		String salt = "randomSaltValue";
		Seller seller = Seller.create(correctPassword, salt, "판매자", "123-45-67890", "010-1234-5678", "tax@example.com");

		// when
		boolean result = seller.isPasswordEquals(incorrectPassword);

		// then
		assertThat(result).isFalse();
	}
}
