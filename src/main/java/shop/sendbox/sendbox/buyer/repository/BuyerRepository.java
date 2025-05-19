package shop.sendbox.sendbox.buyer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import shop.sendbox.sendbox.buyer.entity.Buyer;

public interface BuyerRepository extends JpaRepository<Buyer, Long> {
	Optional<Buyer> findByEmail(String email);
}
