package shop.sendbox.sendbox.coupon;

import org.springframework.stereotype.Component;

@Component
public class CouponCodeGenerator {

	public String generateCode(CouponType couponType) {
		if (couponType == CouponType.WELCOME) {
			return "WELCOME";
		}
		return "1234";
	}
}
