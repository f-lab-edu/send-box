package shop.sendbox.sendbox.buyer;

import static shop.sendbox.sendbox.util.HashingEncrypt.*;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.sendbox.sendbox.login.LoginHandler;
import shop.sendbox.sendbox.login.LoginResponse;
import shop.sendbox.sendbox.login.LoginUser;
import shop.sendbox.sendbox.login.UserType;
import shop.sendbox.sendbox.util.HashingEncrypt;

// 서비스 클래스임을 명시하고, 컴포넌트 스캔에 포함되기 위한 @Component를 가진 메타 애노테이션을 추가했습니다.
// RequiredArgsConstructor 애노테이션을 사용하여 final 필드를 생성자로 주입받도록 했습니다.
@Slf4j
@Service
@RequiredArgsConstructor
public class BuyerService implements LoginHandler {

	private final BuyerRepository buyerRepository;
	private final SymmetricCryptoService symmetricCryptoService;

	/*
	@Transactional 애노테이션은 메서드가 실행될 때 트랜잭션을 시작합니다.
	메서드가 정상 종료되면 트랜잭션을 커밋합니다.
	만약 예외가 발생하면 롤백합니다.
	예외는 런타임 예외를 지정하거나 rollbackFor 속성을 사용하여 롤백할 예외를 지정할 수 있습니다.
	 */
	@Transactional
	public BuyerResponse signUp(final BuyerRequest buyerRequest, final LocalDateTime createdAt) {
		final String encryptEmail = symmetricCryptoService.encrypt(buyerRequest.email());
		final String salt = generateSalt();
		final String encryptPassword = encryptPassword(buyerRequest.password(), salt);
		log.info("encryptEmail: {}", encryptEmail);
		final Buyer buyer = Buyer.create(encryptEmail, encryptPassword, salt, buyerRequest.name(),
			buyerRequest.phoneNumber(), createdAt, buyerRequest.createdBy());
		final Buyer savedBuyer = buyerRepository.save(buyer);
		return BuyerResponse.of(savedBuyer, buyerRequest.email(), buyerRequest.phoneNumber());
	}

	/*
	@Override 애노테이션은 없어도 동적 바인딩으로 자식의 메서드를 실행할 수 있습니다.
	애노테이션을 추가할 경우 컴파일러가 해당 메서드가 오버라이드되었는지 확인해줍니다.
	오타나 잘못된 메서드명을 사용했을 때 컴파일러가 알려주기 때문에 오류를 줄일 수 있습니다.
	 */
	@Override
	public boolean supports(final UserType userType) {
		return userType == UserType.BUYER;
	}

	@Override
	@Transactional(readOnly = true)
	public LoginResponse login(final LoginUser user) {
		final String encryptedEmail = symmetricCryptoService.encrypt(user.email());
		final Buyer findBuyer = buyerRepository.findByEmail(encryptedEmail)
			.orElseThrow(() -> new IllegalArgumentException("해당 이메일로 가입된 회원이 없습니다."));
		final String encryptPassword = encryptPassword(user.password(), findBuyer.getSalt());
		validatePassword(findBuyer, encryptPassword);
		return LoginResponse.of(findBuyer, user.email());
	}

	private void validatePassword(final Buyer findBuyer, final String encryptPassword) {
		if (!findBuyer.isPasswordEquals(encryptPassword)) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}
	}

	private String encryptPassword(final String password, final String salt) {
		return HashingEncrypt.encrypt(password, salt);
	}
}
