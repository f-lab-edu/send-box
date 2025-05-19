package shop.sendbox.sendbox.seller.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import shop.sendbox.sendbox.buyer.entity.YnCode;
import shop.sendbox.sendbox.security.service.SecurityService;
import shop.sendbox.sendbox.seller.entity.Seller;
import shop.sendbox.sendbox.seller.entity.SellerStatus;
import shop.sendbox.sendbox.seller.repository.SellerRepository;

@SpringBootTest
@Transactional
class SellerServiceTest {

	@Autowired
	private SellerRepository sellerRepository;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private SellerService sellerService;

	@Test
	@DisplayName("판매자 회원가입: 정상적인 정보 입력 시 성공적으로 가입 처리됩니다.")
	void createSellerSuccess() {
		// given
		SellerCreateCommand request = new SellerCreateCommand("홍길동상회", "password123",
			"123-45-67890", "010-1234-5678", "tax@example.com");

		// when
		SellerCreateResult response = sellerService.createSeller(request);

		// then
		assertThat(response.id()).isNotNull();
		assertThat(response.name()).isEqualTo(request.name());
		assertThat(response.phoneNumber()).isEqualTo(request.phoneNumber());
		assertThat(response.taxEmail()).isEqualTo(request.taxEmail());

		Seller savedSeller = sellerRepository.findById(response.id()).orElseThrow();
		assertThat(savedSeller.getName()).isEqualTo(request.name());
		assertThat(securityService.decryptText(savedSeller.getBusinessNumber())).isEqualTo(request.businessNumber());
		assertThat(securityService.decryptText(savedSeller.getPhoneNumber())).isEqualTo(request.phoneNumber());
		assertThat(securityService.decryptText(savedSeller.getTaxEmail())).isEqualTo(request.taxEmail());
		assertThat(savedSeller.getSellerStatus()).isEqualTo(SellerStatus.ACTIVE);
		assertThat(savedSeller.getDeleteYn()).isEqualTo(YnCode.N);
		assertThat(securityService.verifyPassword(request.password(), savedSeller.getPassword(),
			savedSeller.getSalt())).isTrue();
	}

	@Test
	@DisplayName("판매자 회원가입 실패: 이미 등록된 사업자 번호로 가입 시도 시 예외가 발생합니다.")
	void createSellerFailDueToDuplicateBusinessNumber() {
		// given
		SellerCreateCommand initialRequest = new SellerCreateCommand("기존상회", "password123",
				"987-65-43210", "010-9999-8888", "initial@example.com");
		sellerService.createSeller(initialRequest);

		SellerCreateCommand duplicateRequest = new SellerCreateCommand("새로운상회", "newpass",
			"987-65-43210", "010-1111-2222", "new@example.com");

		// when & then
		assertThatThrownBy(() -> sellerService.createSeller(duplicateRequest)).isInstanceOf(
			IllegalArgumentException.class).hasMessage("이미 등록된 사업자번호입니다.");
	}
}

