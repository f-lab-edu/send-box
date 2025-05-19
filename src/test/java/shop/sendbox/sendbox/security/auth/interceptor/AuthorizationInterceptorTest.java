package shop.sendbox.sendbox.security.auth.interceptor;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import shop.sendbox.sendbox.security.auth.ApiEndpointEnum;
import shop.sendbox.sendbox.security.auth.Permission;
import shop.sendbox.sendbox.security.auth.UserPrincipal;
import shop.sendbox.sendbox.security.auth.context.SecurityPrincipalHolder;
import shop.sendbox.sendbox.security.auth.exception.AuthorizationException;
import shop.sendbox.sendbox.testsupport.annotation.MockTest;

@MockTest
class AuthorizationInterceptorTest {

	@Mock
	private SecurityPrincipalHolder securityPrincipalHolder;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private Object handler;

	@Mock
	private UserPrincipal userPrincipal;

	@Mock
	private Permission permission;

	@InjectMocks
	private AuthorizationInterceptor interceptor;

	@BeforeEach
	void setUp() {
		when(request.getRequestURI()).thenReturn("/api/v1/test");
		when(request.getMethod()).thenReturn("GET");
	}

	@Test
	@DisplayName("인증이 필요 없는 엔드포인트는 항상 접근이 허용됩니다")
	void permitAllEndpointTest() throws Exception {
		// given
		try (MockedStatic<ApiEndpointEnum> apiEndpointEnumMockedStatic = mockStatic(ApiEndpointEnum.class)) {
			apiEndpointEnumMockedStatic.when(() -> ApiEndpointEnum.matchEndpoint(anyString(), anyString()))
				.thenReturn(permission);
			when(permission.doesNotRequireAuth()).thenReturn(true);

			// when
			boolean result = interceptor.preHandle(request, response, handler);

			// then
			assertThat(result).isTrue();
			verify(securityPrincipalHolder, never()).getContext();
			verify(userPrincipal, never()).hasPermission(any());
		}
	}

	@Test
	@DisplayName("사용자가 필요한 권한을 가지고 있으면 접근이 허용됩니다")
	void userHasPermissionTest() throws Exception {
		// given
		try (MockedStatic<ApiEndpointEnum> apiEndpointEnumMockedStatic = mockStatic(ApiEndpointEnum.class)) {
			apiEndpointEnumMockedStatic.when(() -> ApiEndpointEnum.matchEndpoint(anyString(), anyString()))
				.thenReturn(permission);
			when(permission.doesNotRequireAuth()).thenReturn(false);
			when(securityPrincipalHolder.getContext()).thenReturn(userPrincipal);
			when(userPrincipal.hasPermission(permission)).thenReturn(true);

			// when
			boolean result = interceptor.preHandle(request, response, handler);

			// then
			assertThat(result).isTrue();
			verify(securityPrincipalHolder).getContext();
			verify(userPrincipal).hasPermission(permission);
		}
	}

	@Test
	@DisplayName("사용자가 필요한 권한을 가지고 있지 않으면 접근이 거부됩니다")
	void userDoesNotHavePermissionTest() throws Exception {
		// given
		try (MockedStatic<ApiEndpointEnum> apiEndpointEnumMockedStatic = mockStatic(ApiEndpointEnum.class)) {
			apiEndpointEnumMockedStatic.when(() -> ApiEndpointEnum.matchEndpoint(anyString(), anyString()))
				.thenReturn(permission);
			when(permission.doesNotRequireAuth()).thenReturn(false);
			when(securityPrincipalHolder.getContext()).thenReturn(userPrincipal);
			when(userPrincipal.hasPermission(permission)).thenReturn(false);

			// when
			boolean result = interceptor.preHandle(request, response, handler);

			// then
			assertThat(result).isFalse();
			verify(securityPrincipalHolder).getContext();
			verify(userPrincipal).hasPermission(permission);
		}
	}

	@Test
	@DisplayName("SecurityPrincipalHolder에 값이 없는 경우 요청이 거부됩니다.")
	void nullUserPrincipalTest() throws Exception {
		// given
		try (MockedStatic<ApiEndpointEnum> apiEndpointEnumMockedStatic = mockStatic(ApiEndpointEnum.class)) {
			apiEndpointEnumMockedStatic.when(() -> ApiEndpointEnum.matchEndpoint(anyString(), anyString()))
				.thenReturn(permission);
			when(permission.doesNotRequireAuth()).thenReturn(false);
			when(securityPrincipalHolder.getContext()).thenReturn(null);

			// when
			assertThatThrownBy(() -> interceptor.preHandle(request, response, handler))
				.isInstanceOf(AuthorizationException.class)
				.hasMessage("로그인이 필요합니다.");
		}
	}
}
