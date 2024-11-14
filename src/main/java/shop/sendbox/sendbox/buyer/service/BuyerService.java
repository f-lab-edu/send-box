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

// 서비스 클래스임을 명시하고, 컴포넌트 스캔에 포함되기 위한 @Component를 가진 메타 애노테이션을 추가했습니다.
// RequiredArgsConstructor 애노테이션을 사용하여 final 필드를 생성자로 주입받도록 했습니다.
@Service
@RequiredArgsConstructor
public class BuyerService implements LoginHandler {

	private final BuyerRepository buyerRepository;

	/**
	 * Transactional을 명시적으로 추가하여 해당 메서드는 트랜잭션 범위에서 실행됨을 명시했습니다.
	 */
	@Transactional
	public BuyerResponse signUp(final BuyerRequest buyerRequest, final LocalDateTime createdAt) {
		final Buyer buyer = Buyer.create(buyerRequest.email(), buyerRequest.password(), buyerRequest.name(),
			buyerRequest.phoneNumber(), createdAt, buyerRequest.createdBy());
		final Buyer savedBuyer = buyerRepository.save(buyer);
		return BuyerResponse.of(savedBuyer);
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
	public LoginResponse login(final LoginUser user) {
		final Buyer findBuyer = buyerRepository.findByEmail(user.email())
			.orElseThrow(() -> new IllegalArgumentException("해당 이메일로 가입된 회원이 없습니다."));
		findBuyer.validatePassword(user.password());
		return LoginResponse.of(findBuyer);
	}
}
