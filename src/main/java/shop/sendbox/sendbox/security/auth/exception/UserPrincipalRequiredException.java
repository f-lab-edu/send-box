package shop.sendbox.sendbox.security.auth.exception;

public class UserPrincipalRequiredException extends IllegalArgumentException {
	public UserPrincipalRequiredException(String message) {
		super(message);
	}
}
