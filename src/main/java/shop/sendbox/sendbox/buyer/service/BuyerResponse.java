package shop.sendbox.sendbox.buyer.service;

import lombok.AccessLevel;
import lombok.Builder;
import shop.sendbox.sendbox.buyer.entity.Buyer;
import shop.sendbox.sendbox.buyer.entity.BuyerStatus;

// 빌더는 빌더 객체로 사용할 수 있도록 해주는 애노테이션입니다.
@Builder(access = AccessLevel.PRIVATE)
public record BuyerResponse(Long buyerId, String email, String name, String phoneNumber, BuyerStatus buyerStatus) {

	public static BuyerResponse of(Buyer buyer) {
		return BuyerResponse.builder()
			.buyerId(buyer.getBuyerId())
			.email(buyer.getEmail())
			.name(buyer.getName())
			.phoneNumber(buyer.getPhoneNumber())
			.buyerStatus(buyer.getBuyerStatus())
			.build();
	}
}
