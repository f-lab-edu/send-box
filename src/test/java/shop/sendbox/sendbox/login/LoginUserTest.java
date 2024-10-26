package shop.sendbox.sendbox.login;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LoginUserTest {

	@Test
	@DisplayName("LoginUser는 생성자: 이메일 주소, 비밀번호를 입력받는다.")
	void create() {
		// given
		String email = "test@gmail.com";
		String password = "password";

		// when
		LoginUser loginUser = new LoginUser(email, password);

		// then
		assertThat(loginUser.email()).isEqualTo(email);
		assertThat(loginUser.password()).isEqualTo(password);
	}
}
