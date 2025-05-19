package shop.sendbox.sendbox.coupon;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import shop.sendbox.sendbox.coupon.entity.Coupon;

class CouponTest {

	@Test
	@DisplayName("쿠폰은 생성시 할인금액을 가진다.")
	void couponHasDiscountAmount() {
		// given
		BigDecimal discountAmount = BigDecimal.valueOf(5000);
		LocalDateTime startDateTime = LocalDateTime.of(2025, 4, 1, 0, 1);
		LocalDateTime endDateTime = LocalDateTime.of(2025, 12, 31, 23, 59);
		LocalDateTime createdAt = LocalDateTime.of(2025, 4, 1, 0, 0);

		// when
		Coupon coupon = Coupon.create("new", discountAmount, startDateTime, endDateTime, createdAt, "system", 1L);

		// then
		Assertions.assertThat(coupon.getDiscountAmount()).isEqualTo(discountAmount);
	}

	@Test
	@DisplayName("쿠폰은 생성시 삭제되지 않은 상태로 생성된다.")
	void couponIsNotDelete() {
		// given
		LocalDateTime startDateTime = LocalDateTime.of(2025, 4, 1, 0, 1);
		LocalDateTime endDateTime = LocalDateTime.of(2025, 12, 31, 23, 59);
		LocalDateTime createdAt = LocalDateTime.of(2025, 4, 1, 0, 0);

		// when
		Coupon coupon = Coupon.create("new", BigDecimal.valueOf(5000), startDateTime, endDateTime,
			createdAt, "system", 1L);

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
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("이용기간 시작일은 생성일보다 커야 합니다.");
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
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("이용기간 종료일은 시작일보다 커야 합니다.");
	}
}

