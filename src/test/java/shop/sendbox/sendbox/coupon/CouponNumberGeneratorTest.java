package shop.sendbox.sendbox.coupon;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import shop.sendbox.sendbox.coupon.entity.CouponType;

class CouponNumberGeneratorTest {

	@Test
	@DisplayName("신규가입 쿠폰 번호는 WELCOME이다.")
	public void generateCode() {
		// given
		CouponType couponType = CouponType.WELCOME;

		// when
		String code = couponType.generateCode();

		// then
		Assertions.assertThat(code).isEqualTo("WELCOME");
	}

}
