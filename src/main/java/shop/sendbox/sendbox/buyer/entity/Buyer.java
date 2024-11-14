package shop.sendbox.sendbox.buyer.entity;

import static shop.sendbox.sendbox.buyer.entity.BuyerStatus.*;
import static shop.sendbox.sendbox.buyer.entity.DeleteStatus.*;
import static shop.sendbox.sendbox.util.EncryptUtil.*;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// @Entity 어노테이션은 JPA가 관리하는 엔티티 클래스임을 나타냅니다.
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Buyer {
	// @Id 필드는 엔티티의 primary key로 매핑합니다.
	// @GeneratedValue는 자동으로 증가하는 값에 대한 옵션을 지정할 수 있습니다
	// GenerationType.IDENTITY는 데이터베이스가 자동으로 증가하는 방식인 AUTO_INCREMENT를 사용합니다.
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long buyerId;
	private String email;
	private String password;
	private String salt;
	private String name;
	private String phoneNumber;
	private Long addressId;
	private BuyerStatus buyerStatus;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String createdBy;
	private String updatedBy;
	private DeleteStatus deleteYN;

	// 클래스 위가 아닌 이유는 Buyer 엔티티의 BuyerId 필드가 생성자에 포함되지 않기 때문입니다.
	@Builder(access = AccessLevel.PRIVATE)
	private Buyer(String email, String password, String salt, String name, String phoneNumber, LocalDateTime createdAt,
		String createdBy, DeleteStatus deleteYN, BuyerStatus buyerStatus, Long addressId) {
		this.email = email;
		this.password = password;
		this.salt = salt;
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

	public static Buyer create(String email, String password, String name, String phoneNumber,
		LocalDateTime createdAt,
		String createdBy) {
		final String salt = generateSalt();
		return Buyer.builder()
			.email(email)
			.password(encrypt(password, salt))
			.salt(salt)
			.name(name)
			.phoneNumber(phoneNumber)
			.createdAt(createdAt)
			.createdBy(createdBy)
			.deleteYN(N)
			.buyerStatus(ACTIVE)
			.build();
	}

	/**
	 * 예외 처리가 아닌 boolean 반환하게 된다면
	 * 호출하는 측에서 if, else 로 한번 더 처리한뒤 실패 로직을 만들어야합니다.
	 * 예외를 던지면 if, else를 작성하지 않고 통과한 이후 로직만 작성하면 되니까 코드가 깔끔해진다고 생각합니다.
	 * 멘토님이시라면 어떻게 하실지 궁금합니다.
	 */
	public void validatePassword(String password) {
		final boolean isMatched = this.password.equals(encrypt(password, this.salt));
		if (!isMatched) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}
	}
}
