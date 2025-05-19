package shop.sendbox.sendbox.login;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LoginCreateRequestTest {

	@Test
	@DisplayName("LoginCreateRequest는 생성자: 이메일 주소, 비밀번호, 사용자 타입을 입력받는다.")
	void create() {
		// given
		String email = "test@gmail.com";
		String password = "password";
		UserType userType = UserType.BUYER;

		// when
		LoginCreateRequest loginCreateRequest = new LoginCreateRequest(email, password, userType);

		// then
		assertThat(loginCreateRequest.email()).isEqualTo(email);
		assertThat(loginCreateRequest.password()).isEqualTo(password);
		assertThat(loginCreateRequest.userType()).isEqualTo(userType);
	}

	@Test
	@DisplayName("LoginCreateRequest를 LoginRequest로 변환한다.")
	void toService() {
		// given
		String email = "test@gmail.com";
		String password = "password";
		UserType userType = UserType.BUYER;
		LoginCreateRequest loginCreateRequest = new LoginCreateRequest(email, password, userType);

		// when
		LoginRequest loginRequest = loginCreateRequest.toServiceRequest();

		// then
		assertThat(loginRequest.email()).isEqualTo(email);
		assertThat(loginRequest.password()).isEqualTo(password);
		assertThat(loginRequest.userType()).isEqualTo(userType);
	}

}
