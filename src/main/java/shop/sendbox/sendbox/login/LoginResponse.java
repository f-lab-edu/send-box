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

	// 더 일반적인 팩토리 메서드 추가
	public static LoginResponse from(Long id, String name, String email) {
		return new LoginResponse(id, name, email);
	}
}
