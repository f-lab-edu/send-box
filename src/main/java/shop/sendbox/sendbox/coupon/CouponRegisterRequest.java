package shop.sendbox.sendbox.coupon;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponRegisterRequest(BigDecimal discountAmount, Long buyerId, LocalDateTime startDateTime,
									LocalDateTime endDateTime, CouponType couponType, Long sellerId,
									LocalDateTime createdAt) {
}
