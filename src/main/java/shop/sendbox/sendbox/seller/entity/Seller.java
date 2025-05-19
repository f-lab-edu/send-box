package shop.sendbox.sendbox.seller.entity;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.sendbox.sendbox.base.BasePersonEntity;
import shop.sendbox.sendbox.buyer.entity.YnCode;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seller extends BasePersonEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String password;
	private String salt; // 추가된 salt 필드
	private String name;
	private String businessNumber;
	private String phoneNumber;
	private String taxEmail;
	@Enumerated(EnumType.STRING)
	private SellerStatus sellerStatus;
	@Enumerated(EnumType.STRING)
	private YnCode deleteYn = YnCode.N;

	@Builder(access = AccessLevel.PRIVATE)
	public Seller(String password, String salt, String name, String businessNumber, String phoneNumber, String taxEmail,
		SellerStatus sellerStatus, YnCode deleteYn) {
		this.password = password;
		this.salt = salt;
		this.name = name;
		this.businessNumber = businessNumber;
		this.phoneNumber = phoneNumber;
		this.taxEmail = taxEmail;
		this.sellerStatus = sellerStatus;
		this.deleteYn = deleteYn;
	}

	public static Seller create(String password, String salt, String name, String businessNumber, String phoneNumber,
		String taxEmail) {
		return Seller.builder()
			.password(password)
			.salt(salt)
			.name(name)
			.businessNumber(businessNumber)
			.phoneNumber(phoneNumber)
			.taxEmail(taxEmail)
			.sellerStatus(SellerStatus.ACTIVE)
			.deleteYn(YnCode.N)
			.build();
	}

	public boolean isPasswordEquals(final String password) {
		return Objects.equals(this.password, password);
	}
}
