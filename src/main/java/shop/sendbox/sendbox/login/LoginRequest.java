package shop.sendbox.sendbox.login;

public record LoginRequest(String email, String password, UserType userType) {
}
