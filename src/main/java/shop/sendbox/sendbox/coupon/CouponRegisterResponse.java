package shop.sendbox.sendbox.coupon;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponRegisterResponse(BigDecimal discountAmount, LocalDateTime startDateTime, LocalDateTime endDateTime,
									 Long sellerId, String code, LocalDateTime createdAt, String createdBy) {
	public static CouponRegisterResponse from(Coupon coupon) {
		return new CouponRegisterResponse(coupon.getDiscountAmount(), coupon.getStartDateTime(),
			coupon.getEndDateTime(), coupon.getSellerId(), coupon.getCode(), coupon.getCreatedAt(),
			coupon.getCreatedBy());
	}
}
