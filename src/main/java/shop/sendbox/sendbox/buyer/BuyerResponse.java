package shop.sendbox.sendbox.buyer;

public record BuyerResponse(Long buyerId, String email, String name, String phoneNumber, BuyerStatus buyerStatus) {

	public static BuyerResponse of(Buyer buyer, final String email, final String phoneNumber) {
		return new BuyerResponse(buyer.getBuyerId(), email, buyer.getName(), phoneNumber, buyer.getBuyerStatus());
	}
}
