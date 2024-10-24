package shop.sendbox.sendbox.buyer;

public record BuyerRequest(
	String email,
	String password,
	String name,
	String phoneNumber,
	Long addressId,
	String createdBy
) {
}
