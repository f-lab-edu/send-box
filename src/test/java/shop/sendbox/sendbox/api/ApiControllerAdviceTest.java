package shop.sendbox.sendbox.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.sendbox.sendbox.login.LoginController;
import shop.sendbox.sendbox.login.LoginCreateRequest;
import shop.sendbox.sendbox.login.LoginService;
import shop.sendbox.sendbox.login.UserType;
import shop.sendbox.sendbox.security.auth.UserPrincipal;
import shop.sendbox.sendbox.security.auth.context.SecurityPrincipalHolder;
import shop.sendbox.sendbox.security.auth.exception.AuthenticationException;
import shop.sendbox.sendbox.security.auth.exception.AuthorizationException;
import shop.sendbox.sendbox.security.auth.exception.UserPrincipalNotSetException;
import shop.sendbox.sendbox.security.auth.exception.UserPrincipalRequiredException;
import shop.sendbox.sendbox.testsupport.config.TestHolderConfig;

@WebMvcTest(controllers = {LoginController.class})
@Import(TestHolderConfig.class)
class ApiControllerAdviceTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	LoginService loginService;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	@DisplayName("클라이언트가 잘못된 입력을 할 경우 예외는 400으로 변환됩니다.")
	void errorWith400() throws Exception {
		// given
		final LoginCreateRequest createRequest = new LoginCreateRequest("test@example.com", "fail_password",
			UserType.BUYER);
		Mockito.when(loginService.login(any())).thenThrow(new IllegalArgumentException("비밀번호가 일치하지 않습니다."));

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createRequest)))
			.andDo(print())
			.andExpect(MockMvcResultMatchers.status().isBadRequest())
			.andExpect(MockMvcResultMatchers.jsonPath("$.message").isString());
	}

	@Test
	@DisplayName("입력값 검증 실패 시 400 상태 코드와 필드 에러 정보를 반환합니다.")
	void returnsFieldErrorsForValidationException() throws Exception {
		// given
		final LoginCreateRequest invalidRequest = new LoginCreateRequest("", "", null);

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(invalidRequest)))
			.andDo(print())
			.andExpect(MockMvcResultMatchers.status().isBadRequest())
			.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("입력값 검증에 실패했습니다"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data").isMap());
	}

	@Test
	@DisplayName("인증 예외가 발생하면 401 상태 코드를 반환합니다.")
	void returnsUnauthorizedForAuthenticationException() throws Exception {
		// given
		final LoginCreateRequest request = new LoginCreateRequest("test@example.com", "password", UserType.BUYER);
		Mockito.when(loginService.login(any())).thenThrow(new AuthenticationException());

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andDo(print())
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(401));
	}

	@Test
	@DisplayName("권한 예외가 발생하면 403 상태 코드를 반환합니다.")
	void returnsAccessDeniedForAuthorizationException() throws Exception {
		// given
		final LoginCreateRequest request = new LoginCreateRequest("test@example.com", "password", UserType.BUYER);
		Mockito.when(loginService.login(any())).thenThrow(new AuthorizationException());

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andDo(print())
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(403));
	}

	@Test
	@DisplayName("UserPrincipal이 설정되지 않은 예외가 발생하면 적절한 에러 응답을 반환합니다.")
	void returnsMissingPrincipalForUserPrincipalNotSetException() throws Exception {
		// given
		final LoginCreateRequest request = new LoginCreateRequest("test@example.com", "password", UserType.BUYER);
		Mockito.when(loginService.login(any())).thenThrow(new UserPrincipalNotSetException("사용자 정보가 없습니다."));

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andDo(print())
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(401));
	}

	@Test
	@DisplayName("UserPrincipal이 필요한데 없을 경우 인증 실패 응답을 반환합니다.")
	void returnsUnauthorizedForUserPrincipalRequiredException() throws Exception {
		// given
		final LoginCreateRequest request = new LoginCreateRequest("test@example.com", "password", UserType.BUYER);
		Mockito.when(loginService.login(any())).thenThrow(new UserPrincipalRequiredException("사용자 인증이 필요합니다."));

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andDo(print())
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(401));
	}

	@Test
	@DisplayName("UserPrincipal이 null일 경우 UserPrincipalRequiredException이 발생합니다.")
	void throwsUserPrincipalRequiredExceptionWhenUserIsNull() {
		// given
		SecurityPrincipalHolder holder = new SecurityPrincipalHolder();

		// when & then
		assertThrows(UserPrincipalRequiredException.class, () -> holder.setContext(null));
	}

	@Test
	@DisplayName("UserPrincipal이 설정되지 않은 상태에서 getContext 호출 시 UserPrincipalNotSetException이 발생합니다.")
	void throwsUserPrincipalNotSetExceptionWhenContextIsNotSet() {
		// given
		SecurityPrincipalHolder holder = new SecurityPrincipalHolder();

		// when & then
		assertThrows(UserPrincipalNotSetException.class, holder::getContext);
	}

	@Test
	@DisplayName("UserPrincipal을 설정하고 정상적으로 가져올 수 있습니다.")
	void successfullySetsAndGetsUserPrincipal() {
		// given
		SecurityPrincipalHolder holder = new SecurityPrincipalHolder();
		UserPrincipal userPrincipal = new UserPrincipal("testUser", UserType.BUYER);

		// when
		holder.setContext(userPrincipal);
		UserPrincipal result = holder.getContext();

		// then
		assertEquals(userPrincipal, result);
	}

	@Test
	@DisplayName("clear 호출 후 UserPrincipal이 제거됩니다.")
	void clearsUserPrincipalSuccessfully() {
		// given
		SecurityPrincipalHolder holder = new SecurityPrincipalHolder();
		UserPrincipal userPrincipal = new UserPrincipal("testUser", UserType.BUYER);
		holder.setContext(userPrincipal);

		// when
		holder.clear();

		// then
		assertThrows(UserPrincipalNotSetException.class, holder::getContext);
	}
}
