package shop.sendbox.sendbox.login;

public record LoginCreateRequest(String email, String password, UserType userType) {
	public LoginRequest toServiceRequest() {
		return new LoginRequest(email, password, userType);
	}
}
