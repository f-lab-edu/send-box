package shop.sendbox.sendbox.coupon;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CouponTest {

	@Test
	@DisplayName("쿠폰은 생성시 삭제되지 않은 상태로 생성된다.")
	void couponIsNotDelete() {
		// given
		// when
		Coupon coupon = Coupon.create("new", BigDecimal.valueOf(5000), LocalDateTime.now(), LocalDateTime.now(),
			LocalDateTime.now(), "system", 1L);
		// then
		Assertions.assertThat(coupon.isDeleted()).isFalse();
	}

	@Test
	@DisplayName("쿠폰 이용기간 시작일이 생성일보다 이전이면 예외가 발생한다.")
	void couponIsCreated() {
		// given
		LocalDateTime startDate = LocalDateTime.of(2024, 12, 31, 23, 59, 59);
		LocalDateTime endDate = LocalDateTime.of(2025, 1, 2, 0, 0);
		LocalDateTime createAt = LocalDateTime.of(2025, 1, 1, 0, 0);
		BigDecimal discountAmount = BigDecimal.valueOf(5000);

		// when & then
		Assertions.assertThatThrownBy(
				() -> Coupon.create("new", discountAmount, startDate, endDate, createAt, "system", 1L))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("쿠폰 이용기간 시작일이 종료일보다 크면 예외가 발생한다.")
	void couponCreateThrowEx() {
		// given
		LocalDateTime startDate = LocalDateTime.of(2024, 12, 31, 23, 59, 59);
		LocalDateTime endDate = LocalDateTime.of(2024, 12, 31, 23, 59, 58);
		LocalDateTime createAt = LocalDateTime.of(2024, 12, 31, 23, 59, 57);
		BigDecimal discountAmount = BigDecimal.valueOf(5000);

		// when & then
		Assertions.assertThatThrownBy(
				() -> Coupon.create("new", discountAmount, startDate, endDate, createAt, "system", 1L))
			.isInstanceOf(IllegalArgumentException.class);
	}
}

