package shop.sendbox.sendbox.coupon.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import shop.sendbox.sendbox.coupon.entity.CouponType;

public record CouponRegisterRequest(BigDecimal discountAmount, Long buyerId, LocalDateTime startDateTime,
									LocalDateTime endDateTime, CouponType couponType, Long sellerId,
									LocalDateTime createdAt) {
}
