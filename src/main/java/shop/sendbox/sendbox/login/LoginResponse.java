package shop.sendbox.sendbox.login;

import shop.sendbox.sendbox.buyer.entity.Buyer;

public record LoginResponse(
	Long id,
	String name,
	String email
) {
	public static LoginResponse of(Buyer buyer) {
		return new LoginResponse(buyer.getBuyerId(), buyer.getName(), buyer.getEmail());
	}
}
