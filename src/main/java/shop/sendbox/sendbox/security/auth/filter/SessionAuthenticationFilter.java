package shop.sendbox.sendbox.security.auth.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import shop.sendbox.sendbox.security.auth.ApiEndpointEnum;
import shop.sendbox.sendbox.security.auth.Permission;
import shop.sendbox.sendbox.security.auth.UserPrincipal;
import shop.sendbox.sendbox.security.auth.context.SecurityPrincipalHolder;
import shop.sendbox.sendbox.security.auth.exception.AuthenticationException;

@RequiredArgsConstructor
public class SessionAuthenticationFilter implements Filter {

	private final SecurityPrincipalHolder securityPrincipalHolder;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest)request;
		String requestUri = httpRequest.getRequestURI();
		String method = httpRequest.getMethod();

		try {
			Permission endpoint = ApiEndpointEnum.matchEndpoint(requestUri, method);

			if (endpoint.doesNotRequireAuth()) {
				chain.doFilter(request, response);
				return;
			}

			UserPrincipal extractedUserPrincipal = extractUserPrincipal(httpRequest);

			securityPrincipalHolder.setContext(extractedUserPrincipal);

			chain.doFilter(request, response);
		} finally {
			securityPrincipalHolder.clear();
		}
	}

	private UserPrincipal extractUserPrincipal(HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		if (session == null) {
			throw new AuthenticationException("로그인이 필요합니다.");
		}

		Object attr = session.getAttribute("userPrincipal");
		if (!(attr instanceof UserPrincipal)) {
			throw new AuthenticationException("유효한 사용자 정보가 아닙니다.");
		}
		return (UserPrincipal)attr;
	}

}
