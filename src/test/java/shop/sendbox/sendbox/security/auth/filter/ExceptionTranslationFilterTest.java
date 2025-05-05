package shop.sendbox.sendbox.security.auth.filter;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import shop.sendbox.sendbox.api.ErrorResponse;
import shop.sendbox.sendbox.security.auth.exception.AuthenticationException;
import shop.sendbox.sendbox.security.auth.exception.AuthorizationException;
import shop.sendbox.sendbox.testsupport.annotation.MockTest;

@MockTest
class ExceptionTranslationFilterTest {

	private ExceptionTranslationFilter filter;
	private ObjectMapper objectMapper;

	@Mock
	private HttpServletResponse response;

	@Mock
	private ServletRequest request;

	@Mock
	private FilterChain filterChain;

	private StringWriter stringWriter;
	private PrintWriter writer;

	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
		filter = new ExceptionTranslationFilter(objectMapper);

		stringWriter = new StringWriter();
		writer = new PrintWriter(stringWriter);
	}

	@Test
	@DisplayName("예외가 발생하지 않으면 필터 체인이 정상적으로 실행됩니다")
	void normalCase() throws IOException, ServletException {
		// given
		doNothing().when(filterChain).doFilter(request, response);

		// when
		filter.doFilter(request, response, filterChain);

		// then
		verify(filterChain, times(1)).doFilter(request, response);
		verify(response, never()).setStatus(anyInt());
		verify(response, never()).setContentType(anyString());
	}

	@Test
	@DisplayName("인증 예외가 발생하면 401 응답을 반환합니다")
	void authenticationExceptionCase() throws IOException, ServletException {
		// given
		doThrow(new AuthenticationException("인증 실패"))
			.when(filterChain)
			.doFilter(request, response);
		when(response.isCommitted()).thenReturn(false);
		when(response.getWriter()).thenReturn(writer);

		// when
		filter.doFilter(request, response, filterChain);
		writer.flush();

		// then
		verify(response).setStatus(401);
		verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
		verify(response).setCharacterEncoding("UTF-8");

		String actualResponse = stringWriter.toString();
		ErrorResponse errorResponse = objectMapper.readValue(actualResponse, ErrorResponse.class);

		assertThat(errorResponse.statusCode()).isEqualTo(401);
		assertThat(errorResponse.message()).isEqualTo("UNAUTHORIZED");
		assertThat(errorResponse.errorCode()).isEqualTo("AUTH_001");
		assertThat(errorResponse.errorDetail()).isEqualTo("이 리소스에 접근하기 위해서는 인증이 필요합니다.");
	}

	@Test
	@DisplayName("인가 예외가 발생하면 403 응답을 반환합니다")
	void authorizationExceptionCase() throws IOException, ServletException {
		// given
		doThrow(new AuthorizationException("권한 없음"))
			.when(filterChain)
			.doFilter(request, response);
		when(response.isCommitted()).thenReturn(false);
		when(response.getWriter()).thenReturn(writer);

		// when
		filter.doFilter(request, response, filterChain);
		writer.flush();

		// then
		verify(response).setStatus(403);
		verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
		verify(response).setCharacterEncoding("UTF-8");

		String actualResponse = stringWriter.toString();
		ErrorResponse errorResponse = objectMapper.readValue(actualResponse, ErrorResponse.class);

		assertThat(errorResponse.statusCode()).isEqualTo(403);
		assertThat(errorResponse.message()).isEqualTo("ACCESS_DENIED");
		assertThat(errorResponse.errorCode()).isEqualTo("AUTH_003");
		assertThat(errorResponse.errorDetail()).isEqualTo("이 리소스에 접근할 권한이 없습니다.");
	}

	@Test
	@DisplayName("예기치 않은 예외가 발생하면 500 응답을 반환합니다")
	void unexpectedExceptionCase() throws IOException, ServletException {
		// given
		doThrow(new RuntimeException("예기치 않은 오류"))
			.when(filterChain)
			.doFilter(request, response);
		when(response.isCommitted()).thenReturn(false);
		when(response.getWriter()).thenReturn(writer);

		// when
		filter.doFilter(request, response, filterChain);
		writer.flush();

		// then
		verify(response).setStatus(500);
		verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
		verify(response).setCharacterEncoding("UTF-8");

		String actualResponse = stringWriter.toString();
		ErrorResponse errorResponse = objectMapper.readValue(actualResponse, ErrorResponse.class);

		assertThat(errorResponse.statusCode()).isEqualTo(500);
		assertThat(errorResponse.message()).isEqualTo("SERVER_ERROR");
		assertThat(errorResponse.errorCode()).isEqualTo("SYS_001");
		assertThat(errorResponse.errorDetail()).isEqualTo("예기치 않은 오류가 발생했습니다. 나중에 다시 시도해 주세요.");
	}

	@Test
	@DisplayName("응답이 이미 커밋된 경우 에러 응답을 추가하지 않습니다")
	void responseAlreadyCommittedCase() throws IOException, ServletException {
		// given
		doThrow(new AuthenticationException("인증 실패"))
			.when(filterChain)
			.doFilter(request, response);
		when(response.isCommitted()).thenReturn(true);

		// when
		filter.doFilter(request, response, filterChain);

		// then
		verify(response, never()).setStatus(anyInt());
		verify(response, never()).setContentType(anyString());
		verify(response, never()).setCharacterEncoding(anyString());
		verify(response, never()).getWriter();
	}
}
