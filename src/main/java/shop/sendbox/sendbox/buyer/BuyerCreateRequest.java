package shop.sendbox.sendbox.buyer;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BuyerCreateRequest {

	private String email;
	private String password;
	private String name;
	private String phoneNumber;
	private Long addressId;
	private String createdBy;

	public BuyerCreateRequest(String email, String password, String name, String phoneNumber, Long addressId,
		String createdBy) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.addressId = addressId;
		this.createdBy = createdBy;
	}

	public BuyerRequest toServiceRequest() {
		return new BuyerRequest(email, password, name, phoneNumber, addressId, createdBy);
	}
}
