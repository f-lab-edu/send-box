package shop.sendbox.sendbox.coupon;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import shop.sendbox.sendbox.buyer.entity.Buyer;
import shop.sendbox.sendbox.buyer.repository.BuyerRepository;

@SpringBootTest
class CouponServiceTest {

	@Autowired
	private CouponService couponService;

	@Autowired
	private BuyerRepository buyerRepository;

	@Test
	@DisplayName("쿠폰을 등록할때 판매자 정보가 없는 경우 오류가 발생한다.")
	void registerCoupon() {
		// given
		LocalDateTime startDateTime = LocalDateTime.of(2025, 1, 1, 1, 1);
		LocalDateTime endDateTime = LocalDateTime.of(2025, 12, 31, 23, 59);
		LocalDateTime createAt = LocalDateTime.of(2019, 12, 31, 23, 59);
		CouponRegisterRequest couponRegisterRequest = new CouponRegisterRequest(BigDecimal.valueOf(5000), 1L,
			startDateTime, endDateTime, CouponType.WELCOME, 1L, createAt);

		// when
		Assertions.assertThatThrownBy(() -> couponService.registerCoupon(couponRegisterRequest))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("쿠폰을 등록할때 판매자 정보가 있는 경우 정상 등록이 된다.")
	void registerCoupon2() {
		// given
		LocalDateTime createAt = LocalDateTime.of(2025, 1, 1, 1, 1, 1);
		LocalDateTime startDateTime = LocalDateTime.of(2025, 1, 2, 1, 1);
		LocalDateTime endDateTime = LocalDateTime.of(2025, 12, 31, 23, 59, 59);
		CouponRegisterRequest couponRegisterRequest = new CouponRegisterRequest(BigDecimal.valueOf(5000), 1L,
			startDateTime, endDateTime, CouponType.WELCOME, 1L, createAt);
		buyerRepository.save(
			Buyer.create("test@gmail.com", "password", "salt", "test", "010-1234-5678", createAt, "system"));

		// when
		CouponRegisterResponse couponRegisterResponse = couponService.registerCoupon(couponRegisterRequest);

		// then
		Assertions.assertThat(couponRegisterResponse.discountAmount()).isEqualTo(BigDecimal.valueOf(5000));
		Assertions.assertThat(couponRegisterResponse.startDateTime()).isEqualTo(startDateTime);
		Assertions.assertThat(couponRegisterResponse.endDateTime()).isEqualTo(endDateTime);
		Assertions.assertThat(couponRegisterResponse.sellerId()).isEqualTo(1L);
		Assertions.assertThat(couponRegisterResponse.code()).isNotBlank();
		Assertions.assertThat(couponRegisterResponse.createdAt()).isNotNull();
	}

}