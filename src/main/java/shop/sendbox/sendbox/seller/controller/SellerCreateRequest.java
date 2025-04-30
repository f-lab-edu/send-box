package shop.sendbox.sendbox.seller.controller;

import shop.sendbox.sendbox.seller.service.SellerCreateCommand;

public record SellerCreateRequest(String password, String name, String businessNumber, String phoneNumber,
	String taxEmail) {
	public SellerCreateCommand toServiceCommand() {
		return new SellerCreateCommand(password, name, businessNumber, phoneNumber, taxEmail);
	}
}
