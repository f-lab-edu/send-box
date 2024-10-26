package shop.sendbox.sendbox.buyer;

public record BuyerResponse(Long buyerId, String email, String name, String phoneNumber, BuyerStatus buyerStatus) {

	public static BuyerResponse of(Buyer buyer) {
		return new BuyerResponse(buyer.getBuyerId(), buyer.getEmail(), buyer.getName(), buyer.getPhoneNumber(),
			buyer.getBuyerStatus());
	}
}
