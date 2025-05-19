package shop.sendbox.sendbox.seller.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import shop.sendbox.sendbox.login.LoginResponse;
import shop.sendbox.sendbox.login.LoginUser;
import shop.sendbox.sendbox.login.UserType;
import shop.sendbox.sendbox.security.crypto.dto.PasswordData;
import shop.sendbox.sendbox.security.crypto.service.SecurityService;
import shop.sendbox.sendbox.seller.entity.Seller;
import shop.sendbox.sendbox.seller.repository.SellerRepository;
import shop.sendbox.sendbox.testsupport.annotation.MockTest;

@MockTest
class SellerServiceTest {

	@Mock
	private SellerRepository sellerRepository;

	@Mock
	private SecurityService securityService;

	@InjectMocks
	private SellerService sellerService;

	@Test
	@DisplayName("판매자 등록이 성공하면 판매자 정보가 저장됩니다")
	void createSeller_WithValidData_SavesSeller() {
		// given
		SellerCreateCommand command = new SellerCreateCommand(
			"password", "판매자", "1234567890", "010-1234-5678", "seller@example.com"
		);

		String encryptedPhoneNumber = "encryptedPhoneNumber";
		String encryptedTaxEmail = "encryptedTaxEmail";
		String encryptedBusinessNumber = "encryptedBusinessNumber";
		PasswordData passwordData = new PasswordData("hashedPassword", "salt");

		when(securityService.encryptText(command.phoneNumber())).thenReturn(encryptedPhoneNumber);
		when(securityService.encryptText(command.taxEmail())).thenReturn(encryptedTaxEmail);
		when(securityService.encryptText(command.businessNumber())).thenReturn(encryptedBusinessNumber);
		when(securityService.encryptPassword(command.password())).thenReturn(passwordData);

		when(sellerRepository.existsByBusinessNumber(encryptedBusinessNumber)).thenReturn(false);
		when(sellerRepository.existsByTaxEmail(encryptedTaxEmail)).thenReturn(false);

		Seller savedSeller = Seller.create(
			passwordData.hashedPassword(),
			passwordData.salt(),
			command.name(),
			encryptedBusinessNumber,
			encryptedPhoneNumber,
			encryptedTaxEmail
		);
		// Reflection 등을 사용하여 ID를 설정해야 하지만, 테스트 목적상 mockito로 대체
		when(sellerRepository.save(any(Seller.class))).thenReturn(savedSeller);

		when(securityService.decryptText(encryptedPhoneNumber)).thenReturn(command.phoneNumber());
		when(securityService.decryptText(encryptedTaxEmail)).thenReturn(command.taxEmail());

		// when
		SellerCreateResult result = sellerService.createSeller(command);

		// then
		assertThat(result).isNotNull();
		assertThat(result.name()).isEqualTo(command.name());
		assertThat(result.phoneNumber()).isEqualTo(command.phoneNumber());
		assertThat(result.taxEmail()).isEqualTo(command.taxEmail());

		verify(sellerRepository).save(any(Seller.class));
	}

	@Test
	@DisplayName("이미 등록된 사업자번호로 판매자 등록 시 예외가 발생합니다")
	void createSeller_WithDuplicateBusinessNumber_ThrowsException() {
		// given
		SellerCreateCommand command = new SellerCreateCommand(
			"password", "판매자", "1234567890", "010-1234-5678", "seller@example.com"
		);

		String encryptedBusinessNumber = "encryptedBusinessNumber";
		when(securityService.encryptText(command.businessNumber())).thenReturn(encryptedBusinessNumber);
		when(sellerRepository.existsByBusinessNumber(encryptedBusinessNumber)).thenReturn(true);

		// when & then
		assertThatThrownBy(() -> sellerService.createSeller(command))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("이미 등록된 사업자번호입니다.");
	}

	@Test
	@DisplayName("이미 등록된 세금계산서 이메일로 판매자 등록 시 예외가 발생합니다")
	void createSeller_WithDuplicateTaxEmail_ThrowsException() {
		// given
		SellerCreateCommand command = new SellerCreateCommand(
			"password", "판매자", "1234567890", "010-1234-5678", "seller@example.com"
		);

		String encryptedBusinessNumber = "encryptedBusinessNumber";
		String encryptedTaxEmail = "encryptedTaxEmail";

		when(securityService.encryptText(command.businessNumber())).thenReturn(encryptedBusinessNumber);
		when(sellerRepository.existsByBusinessNumber(encryptedBusinessNumber)).thenReturn(false);

		when(securityService.encryptText(command.taxEmail())).thenReturn(encryptedTaxEmail);
		when(sellerRepository.existsByTaxEmail(encryptedTaxEmail)).thenReturn(true);

		// when & then
		assertThatThrownBy(() -> sellerService.createSeller(command))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("이미 등록된 세금 계산서용 이메일입니다.");
	}

	@Test
	@DisplayName("유효한 이메일과 비밀번호로 로그인 시 로그인이 성공합니다")
	void login_WithValidCredentials_ReturnsLoginResponse() {
		// given
		LoginUser loginUser = new LoginUser("seller@example.com", "password");
		String encryptedEmail = "encryptedEmail";

		when(securityService.encryptText(loginUser.email())).thenReturn(encryptedEmail);

		Seller seller = Seller.create(
			"hashedPassword",
			"salt",
			"판매자",
			"encryptedBusinessNumber",
			"encryptedPhoneNumber",
			encryptedEmail
		);

		when(sellerRepository.findByTaxEmail(encryptedEmail)).thenReturn(Optional.of(seller));
		when(securityService.verifyPassword(loginUser.password(), seller.getPassword(), seller.getSalt())).thenReturn(
			true);

		// when
		LoginResponse response = sellerService.login(loginUser);

		// then
		assertThat(response).isNotNull();
		assertThat(response.name()).isEqualTo(seller.getName());
		assertThat(response.email()).isEqualTo(loginUser.email());
	}

	@Test
	@DisplayName("등록되지 않은 이메일로 로그인 시 예외가 발생합니다")
	void login_WithNonExistingEmail_ThrowsException() {
		// given
		LoginUser loginUser = new LoginUser("nonexisting@example.com", "password");
		String encryptedEmail = "encryptedEmail";

		when(securityService.encryptText(loginUser.email())).thenReturn(encryptedEmail);
		when(sellerRepository.findByTaxEmail(encryptedEmail)).thenReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> sellerService.login(loginUser))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("해당 이메일로 가입된 판매자가 없습니다.");
	}

	@Test
	@DisplayName("잘못된 비밀번호로 로그인 시 예외가 발생합니다")
	void login_WithInvalidPassword_ThrowsException() {
		// given
		LoginUser loginUser = new LoginUser("seller@example.com", "wrongPassword");
		String encryptedEmail = "encryptedEmail";

		when(securityService.encryptText(loginUser.email())).thenReturn(encryptedEmail);

		// Create a Seller using the provided builder pattern
		Seller seller = Seller.create(
			"hashedPassword",
			"salt",
			"판매자",
			"encryptedBusinessNumber",
			"encryptedPhoneNumber",
			encryptedEmail
		);

		when(sellerRepository.findByTaxEmail(encryptedEmail)).thenReturn(Optional.of(seller));
		when(securityService.verifyPassword(loginUser.password(), seller.getPassword(), seller.getSalt())).thenReturn(
			false);

		// when & then
		assertThatThrownBy(() -> sellerService.login(loginUser))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("비밀번호가 일치하지 않습니다.");
	}

	@Test
	@DisplayName("판매자 유형 확인 시 SELLER 유형은 지원합니다")
	void supports_WithSellerType_ReturnsTrue() {
		// when
		boolean result = sellerService.supports(UserType.SELLER);

		// then
		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("판매자 유형 확인 시 SELLER가 아닌 유형은 지원하지 않습니다")
	void supports_WithNonSellerType_ReturnsFalse() {
		// when
		boolean result = sellerService.supports(UserType.BUYER);

		// then
		assertThat(result).isFalse();
	}
}

