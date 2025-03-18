package shop.sendbox.sendbox.coupon;

import java.util.HashSet;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import shop.sendbox.sendbox.coupon.entity.CouponType;

class CouponTypeTest {

	@Test
	@DisplayName("신규가입 쿠폰 번호는 WELCOME이다.")
	void generateCode() {
		// given
		CouponType couponType = CouponType.WELCOME;

		// when
		String code = couponType.generateCode();

		// then
		Assertions.assertThat(code).isEqualTo("WELCOME");
	}

	@Test
	@DisplayName("생일 쿠폰 번호는 BIRTHDAY이다.")
	void generateCode2() {
		// given
		CouponType couponType = CouponType.BIRTHDAY;

		// when
		String code = couponType.generateCode();

		// then
		Assertions.assertThat(code).isEqualTo("BIRTHDAY");
	}

	@Test
	@DisplayName("판매자 쿠폰 번호는 중복되지 않는 8자리 랜덤 문자열이다.")
	void generateCode3() {
		// given
		CouponType couponType = CouponType.SELLER;
		final int tryCount = 10000;
		final Set<String> generatedCodes = new HashSet<>();

		// when
		for (int i = 0; i < tryCount; i++) {
			generatedCodes.add(couponType.generateCode());
		}

		// then
		Assertions.assertThat(generatedCodes).hasSize(tryCount);
	}
}