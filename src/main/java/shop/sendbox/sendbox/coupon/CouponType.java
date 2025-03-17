package shop.sendbox.sendbox.coupon;

import java.util.UUID;

public enum CouponType implements CouponCodeGenerator {
	WELCOME("신규 가입 축하 쿠폰") {
		@Override
		public String generateCode() {
			return "WELCOME";
		}
	},
	BIRTHDAY("생일 축하 쿠폰") {
		@Override
		public String generateCode() {
			return "BIRTHDAY";
		}
	},
	SELLER("판매자 쿠폰") {
		@Override
		public String generateCode() {
			return UUID.randomUUID().toString().substring(0, 8);
		}
	};
	private final String description;

	CouponType(String description) {
		this.description = description;
	}
	
}
