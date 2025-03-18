package shop.sendbox.sendbox.coupon.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import shop.sendbox.sendbox.coupon.entity.CouponType;
import shop.sendbox.sendbox.coupon.service.CouponRegisterRequest;

public record CouponCreateRequest(BigDecimal discountAmount, Long buyerId, LocalDateTime startDateTime,
								  LocalDateTime endDateTime, CouponType couponType, Long sellerId,
								  LocalDateTime createdAt) {
	public CouponRegisterRequest toServiceRequest() {
		return new CouponRegisterRequest(discountAmount, buyerId, startDateTime, endDateTime, couponType, sellerId,
			createdAt);
	}
}
