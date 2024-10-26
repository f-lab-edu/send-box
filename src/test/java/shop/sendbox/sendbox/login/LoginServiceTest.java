package shop.sendbox.sendbox.login;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import shop.sendbox.sendbox.buyer.Buyer;
import shop.sendbox.sendbox.buyer.BuyerRepository;
import shop.sendbox.sendbox.buyer.BuyerRequest;
import shop.sendbox.sendbox.buyer.BuyerService;

@ActiveProfiles("test")
@SpringBootTest
class LoginServiceTest {

	@Autowired
	LoginService loginService;

	@Autowired
	BuyerRepository buyerRepository;

	@Autowired
	BuyerService buyerService;

	@BeforeEach
	void setUp() {
		buyerRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName("회원 가입된 구매자는 올바른 정보로 로그인할 수 있다.")
	void loginBuyer() {
		// given
		String email = "test@gmail.com";
		String password = "password";
		BuyerRequest buyerRequest = new BuyerRequest(email, password, "홍길동", "01012345678", null, "admin");
		buyerService.signUp(buyerRequest, LocalDateTime.of(2024, 10, 22, 11, 28));

		UserType userType = UserType.BUYER;
		LoginRequest loginRequest = new LoginRequest(email, password, userType);

		// when
		LoginResponse loginResponse = loginService.login(loginRequest);

		// then
		assertThat(loginResponse.email()).isEqualTo(email);
		assertThat(loginResponse.name()).isEqualTo("홍길동");
	}

	@Test
	@DisplayName("회원 정보가 없는 구매자는 로그인을 할 수 없다.")
	void loginBuyerWithFail() {
		// given
		String email = "test@gmail.com";
		String password = "password";

		UserType userType = UserType.BUYER;
		LoginRequest loginRequest = new LoginRequest(email, password, userType);

		// when & then
		Assertions.assertThatThrownBy(() -> loginService.login(loginRequest))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("해당 이메일로 가입된 회원이 없습니다.");
	}

	private Buyer createBuyer(String email, String password) {
		String name = "홍길동";
		String phoneNumber = "01012345678";
		String createdBy = "admin";
		LocalDateTime createdAt = LocalDateTime.of(2024, 10, 22, 11, 28);
		return Buyer.create(email, password, name, phoneNumber, createdAt, createdBy);
	}
}
