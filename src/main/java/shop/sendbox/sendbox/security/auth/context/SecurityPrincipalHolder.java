package shop.sendbox.sendbox.security.auth.context;

import org.springframework.stereotype.Component;

import shop.sendbox.sendbox.security.auth.UserPrincipal;

@Component
public class SecurityPrincipalHolder {

	private final ThreadLocal<UserPrincipal> userContext = new ThreadLocal<>();

	public void setContext(UserPrincipal user) {
		if (user == null) {
			throw new IllegalArgumentException("UserPrincipal은 필수입니다.");
		}
		userContext.set(user);
	}

	public UserPrincipal getContext() {
		UserPrincipal user = userContext.get();
		if (user == null) {
			throw new IllegalStateException("UserPrincipal이 설정되지 않았습니다.");
		}
		return user;
	}

	public void clear() {
		userContext.remove();
	}
}
