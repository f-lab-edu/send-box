package shop.sendbox.sendbox.login;

import shop.sendbox.sendbox.buyer.entity.Buyer;
import shop.sendbox.sendbox.seller.entity.Seller;

public record LoginResponse(
	Long id,
	String name,
	String email
) {
	public static LoginResponse of(Buyer buyer, final String email) {
		return new LoginResponse(buyer.getBuyerId(), buyer.getName(), email);
	}

	public static LoginResponse of(Seller seller, final String email) {
		return new LoginResponse(seller.getId(), seller.getName(), email);
	}

}
