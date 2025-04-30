package shop.sendbox.sendbox.seller.service;

import shop.sendbox.sendbox.seller.entity.Seller;

public record SellerCreateResult(
	Long id, String name, String phoneNumber, String taxEmail
) {
	public static SellerCreateResult of(Seller seller, String phoneNumber, String taxEmail) {
		return new SellerCreateResult(seller.getId(),	seller.getName(), phoneNumber, taxEmail);
	}
}
