package shop.sendbox.sendbox.coupon.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import shop.sendbox.sendbox.buyer.entity.Buyer;
import shop.sendbox.sendbox.buyer.repository.BuyerRepository;
import shop.sendbox.sendbox.coupon.entity.Coupon;
import shop.sendbox.sendbox.coupon.entity.CouponType;
import shop.sendbox.sendbox.coupon.repository.CouponRepository;

@Service
public class CouponService {

	private final CouponRepository couponRepository;
	private final BuyerRepository buyerRepository;

	public CouponService(CouponRepository couponRepository, BuyerRepository buyerRepository) {
		this.couponRepository = couponRepository;
		this.buyerRepository = buyerRepository;
	}

	// 쿠폰을 등록한다.
	public CouponRegisterResponse registerCoupon(CouponRegisterRequest couponRegisterRequest) {
		Coupon coupon = createCouponAt(couponRegisterRequest);

		Coupon savedCoupon = couponRepository.save(coupon);

		return CouponRegisterResponse.from(savedCoupon);
	}

	private Coupon createCouponAt(CouponRegisterRequest couponRegisterRequest) {
		BigDecimal discountAmount = couponRegisterRequest.discountAmount();
		CouponType couponType = couponRegisterRequest.couponType();
		Long sellerId = couponRegisterRequest.sellerId();
		LocalDateTime startDateTime = couponRegisterRequest.startDateTime();
		LocalDateTime endDateTime = couponRegisterRequest.endDateTime();
		LocalDateTime createAt = couponRegisterRequest.createdAt();
		String code = couponType.generateCode();

		Buyer buyer = buyerRepository.findById(sellerId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

		return Coupon.create(code, discountAmount, startDateTime, endDateTime, createAt, buyer.getName(), sellerId);
	}
}
