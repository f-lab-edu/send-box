package shop.sendbox.sendbox.seller.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.sendbox.sendbox.login.LoginHandler;
import shop.sendbox.sendbox.login.LoginResponse;
import shop.sendbox.sendbox.login.LoginUser;
import shop.sendbox.sendbox.login.UserType;
import shop.sendbox.sendbox.security.crypto.dto.PasswordData;
import shop.sendbox.sendbox.security.crypto.service.SecurityService;
import shop.sendbox.sendbox.seller.entity.Seller;
import shop.sendbox.sendbox.seller.repository.SellerRepository;

@Service
@RequiredArgsConstructor
public class SellerService implements LoginHandler {

	private final SellerRepository sellerRepository;
	private final SecurityService securityService;

	@Transactional
	public SellerCreateResult createSeller(SellerCreateCommand command) {
		validateBusinessNumberNotDuplicate(command);
		validateTaxEmailNotDuplicate(command);

		String encryptedPhoneNumber = securityService.encryptText(command.phoneNumber());
		String encryptedTaxEmail = securityService.encryptText(command.taxEmail());
		String encryptedBusinessNumber = securityService.encryptText(command.businessNumber());

		PasswordData passwordData = securityService.encryptPassword(command.password());

		Seller seller = Seller.create(passwordData.hashedPassword(), passwordData.salt(), command.name(),
			encryptedBusinessNumber, encryptedPhoneNumber, encryptedTaxEmail);

		Seller savedSeller = sellerRepository.save(seller);

		String decryptedPhoneNumber = securityService.decryptText(savedSeller.getPhoneNumber());
		String decryptedTaxEmail = securityService.decryptText(savedSeller.getTaxEmail());

		return SellerCreateResult.of(
			savedSeller, decryptedPhoneNumber, decryptedTaxEmail
		);
	}

	private void validateBusinessNumberNotDuplicate(SellerCreateCommand command) {
		String encryptedBusinessNumber = securityService.encryptText(command.businessNumber());
		if (sellerRepository.existsByBusinessNumber(encryptedBusinessNumber)) {
			throw new IllegalArgumentException("이미 등록된 사업자번호입니다.");
		}
	}

	private void validateTaxEmailNotDuplicate(SellerCreateCommand command) {
		String encryptedTaxEmail = securityService.encryptText(command.taxEmail());
		if (sellerRepository.existsByTaxEmail(encryptedTaxEmail)) {
			throw new IllegalArgumentException("이미 등록된 세금 계산서용 이메일입니다.");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public LoginResponse login(final LoginUser user) {
		// 이메일 암호화하여 회원 조회
		String encryptedEmail = securityService.encryptText(user.email());

		Seller foundSeller = sellerRepository.findByTaxEmail(encryptedEmail)
			.orElseThrow(() -> new IllegalArgumentException("해당 이메일로 가입된 판매자가 없습니다."));

		// 비밀번호 검증
		boolean isPasswordValid = securityService.verifyPassword(
			user.password(),
			foundSeller.getPassword(),
			foundSeller.getSalt()
		);

		if (!isPasswordValid) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}

		return LoginResponse.of(foundSeller, user.email());
	}

	@Override
	public boolean supports(UserType userType) {
		return UserType.SELLER == userType;
	}
}
