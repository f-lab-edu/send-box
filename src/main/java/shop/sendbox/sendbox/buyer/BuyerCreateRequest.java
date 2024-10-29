package shop.sendbox.sendbox.buyer;

public record BuyerCreateRequest(
	String email, String password, String name, String phoneNumber, String createdBy) {
	public BuyerRequest toServiceRequest() {
		return new BuyerRequest(email, password, name, phoneNumber, createdBy);
	}
}
