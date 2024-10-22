package shop.sendbox.sendbox.buyer;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BuyerService {

	private final BuyerRepository buyerRepository;

	public BuyerResponse signUp(final BuyerRequest buyerRequest, final LocalDateTime createdAt) {
		Buyer buyer = Buyer.create(buyerRequest.email(), buyerRequest.password(), buyerRequest.name(),
			buyerRequest.phoneNumber(), createdAt, buyerRequest.createdBy());
		final Buyer savedBuyer = buyerRepository.save(buyer);
		return BuyerResponse.of(savedBuyer);
	}
}
