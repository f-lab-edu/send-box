package shop.sendbox.sendbox.buyer.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.sendbox.sendbox.buyer.entity.Buyer;
import shop.sendbox.sendbox.buyer.repository.BuyerRepository;
import shop.sendbox.sendbox.login.LoginHandler;
import shop.sendbox.sendbox.login.LoginResponse;
import shop.sendbox.sendbox.login.LoginUser;
import shop.sendbox.sendbox.login.UserType;
import shop.sendbox.sendbox.security.crypto.dto.PasswordData;
import shop.sendbox.sendbox.security.crypto.service.SecurityService;

@Service
@RequiredArgsConstructor
public class BuyerService implements LoginHandler {

	private final BuyerRepository buyerRepository;
	private final SecurityService securityService;

	@Transactional
	public BuyerResponse signUp(final BuyerRequest buyerRequest, final LocalDateTime createdAt) {
		// 이메일, 전화번호 암호화
		String encryptedEmail = securityService.encryptText(buyerRequest.email());
		String encryptedPhoneNumber = securityService.encryptText(buyerRequest.phoneNumber());

		// 비밀번호 암호화
		PasswordData passwordData = securityService.encryptPassword(buyerRequest.password());

		// 회원 생성 및 저장
		Buyer buyer = Buyer.create(
			encryptedEmail,
			passwordData.hashedPassword(),
			passwordData.salt(),
			buyerRequest.name(),
			encryptedPhoneNumber
		);

		Buyer savedBuyer = buyerRepository.save(buyer);

		// 암호화된 데이터 복호화
		String decryptedEmail = securityService.decryptText(savedBuyer.getEmail());
		String decryptedPhoneNumber = securityService.decryptText(savedBuyer.getPhoneNumber());

		return BuyerResponse.of(savedBuyer, decryptedEmail, decryptedPhoneNumber);
	}

	@Override
	@Transactional(readOnly = true)
	public LoginResponse login(final LoginUser user) {
		// 이메일 암호화하여 회원 조회
		String encryptedEmail = securityService.encryptText(user.email());

		Buyer foundBuyer = buyerRepository.findByEmail(encryptedEmail)
			.orElseThrow(() -> new IllegalArgumentException("해당 이메일로 가입된 회원이 없습니다."));

		// 비밀번호 검증
		boolean isPasswordValid = securityService.verifyPassword(
			user.password(),
			foundBuyer.getPassword(),
			foundBuyer.getSalt()
		);

		if (!isPasswordValid) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}

		return LoginResponse.of(foundBuyer, user.email());
	}

	@Override
	public boolean supports(UserType userType) {
		return UserType.BUYER == userType;
	}
}
