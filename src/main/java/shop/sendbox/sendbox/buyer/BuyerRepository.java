package shop.sendbox.sendbox.buyer;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BuyerRepository extends JpaRepository<Buyer, Long> {
	Optional<Buyer> findByEmail(String email);
}
