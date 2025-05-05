package shop.sendbox.sendbox.security.auth;

import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;

public enum ApiEndpointEnum {
	COUPON_CREATE("/coupons", HttpMethod.POST, Permission.SELLER);

	private final String path;
	private final HttpMethod method;
	private final Permission requiredPermission;
	private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

	ApiEndpointEnum(String path, HttpMethod method, Permission requiredPermission) {
		Assert.hasText(path, "Path는 필수 값입니다.");
		Assert.notNull(method, "HttpMethod는 필수 값입니다.");
		Assert.notNull(requiredPermission, "Permission은 필수 값입니다.");
		this.path = path;
		this.method = method;
		this.requiredPermission = requiredPermission;
	}

	public boolean matches(String requestUri, String requestMethod) {
		String pathWithoutQuery = removeUriQueryString(requestUri);
		return isSameMethod(requestMethod) && PATH_MATCHER.match(path, pathWithoutQuery);
	}

	private String removeUriQueryString(String requestUri) {
		return requestUri.contains("?") ? requestUri.substring(0, requestUri.indexOf("?")) : requestUri;
	}

	private boolean isSameMethod(String requestMethod) {
		return method.name().equalsIgnoreCase(requestMethod);
	}

	public static Permission matchEndpoint(String requestUri, String requestMethod) {
		for (ApiEndpointEnum endpoint : values()) {
			if (endpoint.matches(requestUri, requestMethod)) {
				return endpoint.requiredPermission;
			}
		}
		return Permission.PUBLIC;
	}

}
