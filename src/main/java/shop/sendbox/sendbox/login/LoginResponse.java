package shop.sendbox.sendbox.login;

import shop.sendbox.sendbox.buyer.Buyer;

public record LoginResponse(
	Long id,
	String name,
	String email
) {
	public static LoginResponse of(Buyer buyer, final String email) {
		return new LoginResponse(buyer.getBuyerId(), buyer.getName(), email);
	}
}
