package shop.sendbox.sendbox.buyer;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import shop.sendbox.sendbox.buyer.entity.Buyer;
import shop.sendbox.sendbox.buyer.entity.BuyerStatus;
import shop.sendbox.sendbox.buyer.repository.BuyerRepository;
import shop.sendbox.sendbox.buyer.service.BuyerRequest;
import shop.sendbox.sendbox.buyer.service.BuyerResponse;
import shop.sendbox.sendbox.buyer.service.BuyerService;
import shop.sendbox.sendbox.config.TestAuditorConfig;
import shop.sendbox.sendbox.login.LoginUser;
import shop.sendbox.sendbox.login.UserType;
import shop.sendbox.sendbox.security.crypto.symmetric.SymmetricCryptoService;

@ActiveProfiles("test")
@SpringBootTest
@Import(TestAuditorConfig.class)
class BuyerServiceTest {

	@Autowired
	BuyerService buyerService;

	@Autowired
	SymmetricCryptoService symmetricCryptoService;

	@Autowired
	BuyerRepository buyerRepository;

	@BeforeEach
	void setUp() {
		// 테스트용 요청 생성 및 헤더 설정
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("X-User-Id", "1");
		ServletRequestAttributes attributes = new ServletRequestAttributes(request);
		RequestContextHolder.setRequestAttributes(attributes);
		buyerRepository.deleteAllInBatch();
	}

	@AfterEach
	void tearDown() {
		// 테스트 후 RequestContextHolder 초기화
		RequestContextHolder.resetRequestAttributes();
	}

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
		BuyerRequest buyerRequest = new BuyerRequest(email, password, name, phoneNumber, createdBy);

		// when
		BuyerResponse buyerResponse = buyerService.signUp(buyerRequest, createdAt);

		// then
		Assertions.assertThat(buyerResponse.email()).isEqualTo(email);
		Assertions.assertThat(buyerResponse.name()).isEqualTo(name);
		Assertions.assertThat(buyerResponse.phoneNumber()).isEqualTo(phoneNumber);
		Assertions.assertThat(buyerResponse.buyerStatus()).isEqualTo(BuyerStatus.ACTIVE);
	}

	@Test
	@DisplayName("구매자는 아이디와 비밀번호가 일치하는지 확인할 수 있다.")
	void matchPassword() {
		// given
		String email = "test@gmail.com";
		String password = "password";
		buyerService.signUp(new BuyerRequest(email, password, "홍길동", "01012345678", "admin"), LocalDateTime.now());

		// when & then
		Assertions.assertThatCode(() -> buyerService.login(new LoginUser(email, password)))
			.doesNotThrowAnyException();
	}

	@Test
	@DisplayName("구매자는 아이디와 비밀번호가 일치하지 않는 경우 예외를 발생시킨다.")
	void matchPasswordWithFail() {
		// given
		final String email = "test@gmail.com";
		final String password = "password";
		final String failPassword = "failPassword";
		final LocalDateTime now = LocalDateTime.of(2024, 10, 22, 11, 28);
		final BuyerResponse buyerResponse = buyerService.signUp(
			new BuyerRequest(email, password, "홍길동", "01012345678", "admin"), now);

		System.out.println("buyerResponse = " + buyerResponse);

		// when & then
		Assertions.assertThatThrownBy(() -> buyerService.login(new LoginUser(email, failPassword)))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("비밀번호가 일치하지 않습니다.");
	}

	@Test
	@DisplayName("구매자 서비스는 구매자 타입을 지원한다.")
	void supports() {
		// given
		UserType userType = UserType.BUYER;

		// when
		boolean result = buyerService.supports(userType);

		// then
		Assertions.assertThat(result).isTrue();
	}

	@Test
	@DisplayName("구매자 서비스는 구매자 타입이 아닌 경우 지원하지 않는다.")
	void supportsWithFail() {
		// given
		UserType userType = UserType.SELLER;

		// when
		boolean result = buyerService.supports(userType);

		// then
		Assertions.assertThat(result).isFalse();
	}

	private Buyer createBuyer(String email, String password) {
		String name = "홍길동";
		String phoneNumber = "01012345678";
		String createdBy = "admin";
		final String salt = "salt";
		LocalDateTime createdAt = LocalDateTime.of(2024, 10, 22, 11, 28);
		return Buyer.create(email, password, salt, name, phoneNumber);
	}
}
