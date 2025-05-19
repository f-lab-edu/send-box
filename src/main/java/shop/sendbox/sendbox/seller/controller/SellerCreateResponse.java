package shop.sendbox.sendbox.seller.controller;

import shop.sendbox.sendbox.seller.service.SellerCreateResult;

public record SellerCreateResponse(
	Long id, String name, String phoneNumber, String taxEmail
) {
	public static SellerCreateResponse from(SellerCreateResult result) {
		return new SellerCreateResponse(
			result.id(), result.name(), result.phoneNumber(), result.taxEmail()
		);
	}
}
