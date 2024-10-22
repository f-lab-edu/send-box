package shop.sendbox.sendbox.buyer;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Buyer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long buyerId;
	private String email;
	private String password;
	private String name;
	private String phoneNumber;
	private Long addressId;
	private String buyerStatus;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String createdBy;
	private String updatedBy;
	private String deleteYN;

	@Builder
	private Buyer(String email, String password, String name, String phoneNumber, LocalDateTime createdAt,
		String createdBy, String deleteYN, String buyerStatus, Long addressId) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.addressId = addressId;
		this.buyerStatus = buyerStatus;
		this.createdAt = createdAt;
		this.updatedAt = createdAt;
		this.createdBy = createdBy;
		this.updatedBy = createdBy;
		this.deleteYN = deleteYN;
	}

	public static Buyer create(String email, String password, String name, String phoneNumber, LocalDateTime createdAt,
		String createdBy) {
		return Buyer.builder()
			.email(email)
			.password(password)
			.name(name)
			.phoneNumber(phoneNumber)
			.createdAt(createdAt)
			.createdBy(createdBy)
			.deleteYN("N")
			.buyerStatus("ACTIVE")
			.build();
	}

}
