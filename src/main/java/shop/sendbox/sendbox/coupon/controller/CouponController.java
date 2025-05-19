package shop.sendbox.sendbox.coupon.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.sendbox.sendbox.api.ApiResponse;
import shop.sendbox.sendbox.coupon.service.CouponRegisterResponse;
import shop.sendbox.sendbox.coupon.service.CouponService;

/*
RestController 애노테이션은 웹 컴포넌트 빈으로 등록됩니다.
Rest 키워드를 통해 응답을 view로 변환하지 않고, HTTP 응답으로 반환합니다.
 */
@RestController
@RequiredArgsConstructor
public class CouponController {

	private final CouponService couponService;

	@PostMapping("/coupons")
	public ApiResponse<CouponRegisterResponse> createCoupon(@RequestBody CouponCreateRequest couponCreateRequest) {
		CouponRegisterResponse couponRegisterResponse = couponService.registerCoupon(
			couponCreateRequest.toServiceRequest());
		return ApiResponse.ok(couponRegisterResponse);
	}
}
