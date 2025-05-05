package shop.sendbox.sendbox.security.auth.filter;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.io.IOException;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import shop.sendbox.sendbox.login.UserType;
import shop.sendbox.sendbox.security.auth.ApiEndpointEnum;
import shop.sendbox.sendbox.security.auth.Permission;
import shop.sendbox.sendbox.security.auth.UserPrincipal;
import shop.sendbox.sendbox.security.auth.context.SecurityPrincipalHolder;
import shop.sendbox.sendbox.security.auth.exception.AuthenticationException;
import shop.sendbox.sendbox.testsupport.annotation.MockTest;

@MockTest
class SessionAuthenticationFilterTest {

	@Mock
	private SecurityPrincipalHolder securityPrincipalHolder;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private FilterChain filterChain;

	@Mock
	private HttpSession session;

	@Mock
	private UserPrincipal userPrincipal;

	private SessionAuthenticationFilter filter;

	@BeforeEach
	void setUp() {
		filter = new SessionAuthenticationFilter(securityPrincipalHolder);
	}

	@Test
	@DisplayName("인증이 필요한 API 요청 시 세션에서 유저 정보를 가져와 컨텍스트에 설정합니다")
	void doFilter_AuthenticatedEndpoint_SetsUserPrincipalAndPassesFilter() throws ServletException, IOException {
		// given - 인증이 필요한 엔드포인트로 설정 (COUPON_CREATE는 SELLER 권한 필요)
		given(request.getRequestURI()).willReturn("/coupons");
		given(request.getMethod()).willReturn("POST");
		UserPrincipal userPrincipal = new UserPrincipal("testUser", UserType.SELLER);
		given(session.getAttribute("userPrincipal")).willReturn(userPrincipal);
		given(request.getSession(false)).willReturn(session);

		// when
		filter.doFilter(request, response, filterChain);

		// then
		verify(securityPrincipalHolder).setContext(userPrincipal);
		verify(filterChain).doFilter(request, response);
		verify(securityPrincipalHolder).clear();
	}

	@Test
	@DisplayName("인증이 필요하지 않은 API 요청 시 컨텍스트 설정 없이 다음 필터로 진행합니다")
	void doFilter_NonAuthenticatedEndpoint_SkipsAuthentication() throws ServletException, IOException {
		// given - 기존에 없는 엔드포인트는 PUBLIC으로 처리됨
		given(request.getRequestURI()).willReturn("/api/products");
		given(request.getMethod()).willReturn("GET");

		// when
		filter.doFilter(request, response, filterChain);

		// then
		verify(securityPrincipalHolder, never()).setContext(any());
		verify(filterChain).doFilter(request, response);
	}

	@Test
	@DisplayName("세션이 존재하지 않을 때 인증 예외가 발생합니다")
	void doFilter_NoSession_ThrowsAuthenticationException() {
		// given
		given(request.getRequestURI()).willReturn("/coupons");
		given(request.getMethod()).willReturn("POST");
		given(request.getSession(false)).willReturn(null);

		// when & then
		assertThatThrownBy(() -> filter.doFilter(request, response, filterChain))
			.isInstanceOf(AuthenticationException.class)
			.hasMessage("로그인이 필요합니다.");

		verify(securityPrincipalHolder).clear();
	}

	@Test
	@DisplayName("세션에 유효한 유저 정보가 없을 때 인증 예외가 발생합니다")
	void doFilter_InvalidUserPrincipal_ThrowsAuthenticationException() {
		// given
		given(request.getRequestURI()).willReturn("/coupons");
		given(request.getMethod()).willReturn("POST");
		Object userPrincipal = new Object();
		given(session.getAttribute("userPrincipal")).willReturn(userPrincipal);
		given(request.getSession(false)).willReturn(session);

		// when & then
		assertThatThrownBy(() -> filter.doFilter(request, response, filterChain))
			.isInstanceOf(AuthenticationException.class)
			.hasMessage("유효한 사용자 정보가 아닙니다.");

		verify(securityPrincipalHolder).clear();
	}

	@Test
	@DisplayName("필터 처리 중 예외가 발생해도 컨텍스트는 정상적으로 초기화됩니다")
	void doFilter_ExceptionInChain_ClearsContextAnyway() throws ServletException, IOException {
		// given
		given(request.getRequestURI()).willReturn("/coupons");
		given(request.getMethod()).willReturn("POST");
		Object userPrincipal = new UserPrincipal("testUser", UserType.SELLER);
		given(session.getAttribute("userPrincipal")).willReturn(userPrincipal);
		given(request.getSession(false)).willReturn(session);
		RuntimeException expectedException = new RuntimeException("필터 체인 처리 중 오류 발생");
		doThrow(expectedException).when(filterChain).doFilter(request, response);

		// when & then
		assertThatThrownBy(() -> filter.doFilter(request, response, filterChain))
			.isInstanceOf(RuntimeException.class);

		verify(securityPrincipalHolder).clear();
	}

	@DisplayName("권한에 따라 인증 필요 여부가 올바르게 결정됩니다")
	@ParameterizedTest
	@MethodSource("providePermissionsAndAuthRequirements")
	void permission_DoesNotRequireAuth_ReturnsCorrectValue(Permission permission, boolean expectedAuthRequired) {
		// when & then
		assertThat(permission.doesNotRequireAuth()).isEqualTo(expectedAuthRequired);
	}

	static Stream<Arguments> providePermissionsAndAuthRequirements() {
		return Stream.of(
			Arguments.of(Permission.PUBLIC, true),
			Arguments.of(Permission.BUYER, false),
			Arguments.of(Permission.SELLER, false),
			Arguments.of(Permission.ADMIN, false)
		);
	}

	@DisplayName("Permission 열거형은 지정된 값을 올바르게 반환합니다")
	@ParameterizedTest
	@MethodSource("providePermissionsAndValues")
	void permission_GetValue_ReturnsCorrectValue(Permission permission, String expectedValue) {
		// when & then
		assertThat(permission.getValue()).isEqualTo(expectedValue);
	}

	static Stream<Arguments> providePermissionsAndValues() {
		return Stream.of(
			Arguments.of(Permission.PUBLIC, "public"),
			Arguments.of(Permission.BUYER, "buyer"),
			Arguments.of(Permission.SELLER, "seller"),
			Arguments.of(Permission.ADMIN, "admin")
		);
	}

	@Test
	@DisplayName("ApiEndpointEnum은 경로와 메서드에 따라 올바른 권한을 매칭합니다")
	void apiEndpointEnum_MatchesCorrectPermission() {
		// given & when & then
		assertThat(ApiEndpointEnum.matchEndpoint("/coupons", "POST")).isEqualTo(Permission.SELLER);

		// 매칭되는 엔드포인트가 없으면 PUBLIC 반환
		assertThat(ApiEndpointEnum.matchEndpoint("/unknown", "GET")).isEqualTo(Permission.PUBLIC);
	}

	@Test
	@DisplayName("ApiEndpointEnum은 경로 매칭 시 쿼리 파라미터를 제거합니다")
	void apiEndpointEnum_RemovesQueryString() {
		// given
		ApiEndpointEnum endpoint = ApiEndpointEnum.COUPON_CREATE;

		// when & then
		assertThat(endpoint.matches("/coupons?name=test", "POST")).isTrue();
		assertThat(endpoint.matches("/coupons", "POST")).isTrue();
	}

	@Test
	@DisplayName("ApiEndpointEnum은 HTTP 메서드가 일치하지 않으면 매칭되지 않습니다")
	void apiEndpointEnum_DoesNotMatchForDifferentMethod() {
		// given
		ApiEndpointEnum endpoint = ApiEndpointEnum.COUPON_CREATE;

		// when & then
		assertThat(endpoint.matches("/coupons", "GET")).isFalse();
	}
}
