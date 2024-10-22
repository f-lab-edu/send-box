package shop.sendbox.sendbox.buyer;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BuyerResponse {

	private final Long buyerId;
	private final String email;
	private final String name;
	private final String phoneNumber;
	private final String buyerStatus;

	@Builder
	private BuyerResponse(Long buyerId, String email, String name, String phoneNumber, String buyerStatus) {
		this.buyerId = buyerId;
		this.email = email;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.buyerStatus = buyerStatus;
	}

	public static BuyerResponse of(Buyer buyer) {
		return BuyerResponse.builder()
			.buyerId(buyer.getBuyerId())
			.email(buyer.getEmail())
			.name(buyer.getName())
			.phoneNumber(buyer.getPhoneNumber())
			.buyerStatus(buyer.getBuyerStatus())
			.build();
	}
}
