package shop.sendbox.sendbox.seller.service;

public record SellerCreateCommand(String password, String name, String businessNumber, String phoneNumber,
	String taxEmail) {
}
