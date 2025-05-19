package shop.sendbox.sendbox.seller.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import shop.sendbox.sendbox.seller.entity.Seller;

public interface SellerRepository extends JpaRepository<Seller, Long> {
	boolean existsByBusinessNumber(String businessNumber);

	Optional<Seller> findByTaxEmail(String taxEmail);

	boolean existsByTaxEmail(String taxEmail);
}
