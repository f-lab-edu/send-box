package shop.sendbox.sendbox.security.auth.exception;

public class UserPrincipalNotSetException extends IllegalStateException {
	public UserPrincipalNotSetException(String message) {
		super(message);
	}
}
