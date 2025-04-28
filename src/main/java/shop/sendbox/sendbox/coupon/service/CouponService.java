package shop.sendbox.sendbox.coupon.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.sendbox.sendbox.buyer.entity.Buyer;
import shop.sendbox.sendbox.buyer.repository.BuyerRepository;
import shop.sendbox.sendbox.coupon.entity.Coupon;
import shop.sendbox.sendbox.coupon.entity.CouponType;
import shop.sendbox.sendbox.coupon.repository.CouponRepository;

@Service
@RequiredArgsConstructor
public class CouponService {

	private final CouponRepository couponRepository;
	private final BuyerRepository buyerRepository;

	// 쿠폰을 등록한다.
	@Transactional
	public CouponRegisterResponse registerCoupon(CouponRegisterRequest couponRegisterRequest) {
		Coupon coupon = createCouponFromRequest(couponRegisterRequest);

		Coupon savedCoupon = couponRepository.save(coupon);

		return CouponRegisterResponse.from(savedCoupon);
	}

	/*
	추상화 레벨을 맞추기 위해서 별도 메서드를 분리했습니다.
	추상화 레벨을 맞춘다는 의미는
	1. 쿠폰을 요청에서 생성한다.
	2. 쿠폰을 저장한다.
	3. 쿠폰을 응답으로 변환한다.
	추상적인 메서드로 가독성이 좋도록 하고 싶었습니다.
	 */
	private Coupon createCouponFromRequest(CouponRegisterRequest couponRegisterRequest) {
		BigDecimal discountAmount = couponRegisterRequest.discountAmount();
		CouponType couponType = couponRegisterRequest.couponType();
		Long sellerId = couponRegisterRequest.sellerId();
		LocalDateTime startDateTime = couponRegisterRequest.startDateTime();
		LocalDateTime endDateTime = couponRegisterRequest.endDateTime();
		LocalDateTime createdAt = couponRegisterRequest.createdAt();
		String code = couponType.generateCode();

		Buyer buyer = buyerRepository.findById(sellerId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

		return Coupon.create(code, discountAmount, startDateTime, endDateTime, createdAt, buyer.getName(), sellerId);
	}
}
