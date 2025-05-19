package shop.sendbox.sendbox.security.auth.exception;

public class AuthenticationException extends RuntimeException {

	public AuthenticationException(String message) {
		super(message);
	}

	public AuthenticationException() {
		super();
	}
}
