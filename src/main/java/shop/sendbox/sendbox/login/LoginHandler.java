package shop.sendbox.sendbox.login;

public interface LoginHandler {

	boolean supports(UserType userType);

	LoginResponse login(LoginUser user);
}
