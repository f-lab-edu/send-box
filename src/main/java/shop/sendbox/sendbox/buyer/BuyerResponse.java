package shop.sendbox.sendbox.buyer;

import lombok.Builder;

@Builder
public record BuyerResponse(Long buyerId, String email, String name, String phoneNumber, BuyerStatus buyerStatus) {

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
