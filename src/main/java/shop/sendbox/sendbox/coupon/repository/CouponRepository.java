package shop.sendbox.sendbox.coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import shop.sendbox.sendbox.coupon.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
