package shop.sendbox.sendbox.coupon;

public enum CouponType {
	WELCOME("신규 가입 축하 쿠폰"),
	BIRTHDAY("생일 축하 쿠폰"),
	EVENT("이벤트 쿠폰");
	private final String description;

	CouponType(String description) {
		this.description = description;
	}
}
