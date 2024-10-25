package shop.sendbox.sendbox.buyer;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

// 서비스 클래스임을 명시하고, 컴포넌트 스캔에 포함되기 위한 @Component를 가진 메타 애노테이션을 추가했습니다.
// RequiredArgsConstructor 애노테이션을 사용하여 final 필드를 생성자로 주입받도록 했습니다.
@Service
@RequiredArgsConstructor
public class BuyerService {

	private final BuyerRepository buyerRepository;

	@Transactional
	public BuyerResponse signUp(final BuyerRequest buyerRequest, final LocalDateTime createdAt) {
		Buyer buyer = Buyer.create(buyerRequest.email(), buyerRequest.password(), buyerRequest.name(),
			buyerRequest.phoneNumber(), createdAt, buyerRequest.createdBy());
		final Buyer savedBuyer = buyerRepository.save(buyer);
		return BuyerResponse.of(savedBuyer);
	}
}
