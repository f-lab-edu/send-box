package shop.sendbox.sendbox.security.auth.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import shop.sendbox.sendbox.security.auth.ApiEndpointEnum;
import shop.sendbox.sendbox.security.auth.Permission;
import shop.sendbox.sendbox.security.auth.UserPrincipal;
import shop.sendbox.sendbox.security.auth.context.SecurityPrincipalHolder;
import shop.sendbox.sendbox.security.auth.exception.AuthorizationException;

@RequiredArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {

	private final SecurityPrincipalHolder securityPrincipalHolder;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
		throws Exception {

		String requestUri = request.getRequestURI();
		String method = request.getMethod();

		Permission currentPermission = ApiEndpointEnum.matchEndpoint(requestUri, method);

		if (currentPermission.doesNotRequireAuth()) {
			return true;
		}

		UserPrincipal user = securityPrincipalHolder.getContext();

		if (user == null) {
			throw new AuthorizationException("로그인이 필요합니다.");
		}

		return user.hasPermission(currentPermission);
	}

}
