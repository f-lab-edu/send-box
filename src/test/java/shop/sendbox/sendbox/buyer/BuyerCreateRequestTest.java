package shop.sendbox.sendbox.buyer;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BuyerCreateRequestTest {

	@Test
	@DisplayName("BuyerCreateRequest를 BuyerRequest로 변환한다.")
	void toService() {
		// given
		String email = "test@gmail.com";
		String password = "password";
		String name = "홍길동";
		String phoneNumber = "01012345678";
		String createdBy = "admin";

		// when
		BuyerCreateRequest buyerCreateRequest =
			new BuyerCreateRequest(email, password, name, phoneNumber, createdBy);

		// then
		BuyerRequest buyerRequest = buyerCreateRequest.toServiceRequest();
		assertThat(buyerRequest.email()).isEqualTo(email);
		assertThat(buyerRequest.password()).isEqualTo(password);
		assertThat(buyerRequest.name()).isEqualTo(name);
		assertThat(buyerRequest.phoneNumber()).isEqualTo(phoneNumber);
		assertThat(buyerRequest.createdBy()).isEqualTo(createdBy);
	}

	@Test
	@DisplayName("BuyerCreateRequest는 생성자: 이메일 주소, 비밀번호, 이름, 전화번호, 주소 ID, 생성자를 입력받는다.")
	void create() {
		// given
		String email = "test@gmail.com";
		String password = "password";
		String name = "홍길동";
		String phoneNumber = "01012345678";
		String createdBy = "admin";

		// when
		BuyerCreateRequest buyerCreateRequest =
			new BuyerCreateRequest(email, password, name, phoneNumber, createdBy);

		// then
		assertThat(buyerCreateRequest.email()).isEqualTo(email);
		assertThat(buyerCreateRequest.password()).isEqualTo(password);
		assertThat(buyerCreateRequest.name()).isEqualTo(name);
		assertThat(buyerCreateRequest.phoneNumber()).isEqualTo(phoneNumber);
		assertThat(buyerCreateRequest.createdBy()).isEqualTo(createdBy);
	}
}
