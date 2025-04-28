package shop.sendbox.sendbox.coupon.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.sendbox.sendbox.buyer.entity.YnCode;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {
	@Id
	@GeneratedValue
	private Long id;
	private String code;
	private BigDecimal discountAmount;
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String createdBy;
	private String updatedBy;
	private Long sellerId;
	private YnCode deleteYN;

	@Builder(access = AccessLevel.PRIVATE)
	private Coupon(final String code, final BigDecimal discountAmount, final LocalDateTime startDateTime,
		final LocalDateTime endDateTime, final LocalDateTime createdAt, final LocalDateTime updatedAt,
		final String createdBy,
		final String updatedBy, final Long sellerId, final YnCode deleteYN) {
		this.code = code;
		this.discountAmount = discountAmount;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
		this.sellerId = sellerId;
		this.deleteYN = deleteYN;
	}

	public static Coupon create(final String code, final BigDecimal discountAmount, final LocalDateTime startDateTime,
		final LocalDateTime endDateTime, final LocalDateTime createdAt, final String createdBy, final Long sellerId) {
		if (startDateTime.isBefore(createdAt)) {
			throw new IllegalArgumentException("이용기간 시작일은 생성일보다 커야 합니다.");
		}

		if (endDateTime.isBefore(startDateTime)) {
			throw new IllegalArgumentException("이용기간 종료일은 시작일보다 커야 합니다.");
		}

		return Coupon.builder()
			.code(code)
			.discountAmount(discountAmount)
			.startDateTime(startDateTime)
			.endDateTime(endDateTime)
			.createdAt(createdAt)
			.updatedAt(createdAt)
			.createdBy(createdBy)
			.updatedBy(createdBy)
			.sellerId(sellerId)
			.deleteYN(YnCode.N)
			.build();
	}

	public boolean isDeleted() {
		return deleteYN.isDeleted();
	}

}
