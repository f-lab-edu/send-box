package shop.sendbox.sendbox.login;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;

/*
MVC 테스트와 관련된 컴포넌트만 등록합니다.
@Controller,@ControllerAdvice,@JsonComponent,@Converter,@Filter,@WebMvcConfigure 빈이 포함되며
@Component,@Service,@Repository 빈은 포함되지 않습니다.
컨트롤러 계층을 슬라이스 테스트 하고 싶을 때 사용합니다.
 */
@WebMvcTest(controllers = {LoginController.class})
class LoginControllerTest {

	@Autowired
	ObjectMapper objectMapper;
	/*
	@Autowired의 역할은 스프링 컨테이너에 등록된 빈을 주입하는 것입니다.
	MockMvc는 @WebMvcTest을 사용하는 경우 제공되는 빈입니다.
	 */
	@Autowired
	MockMvc mockMvc;
	/*
	@WebMvcTest에 필요한 의존성 빈을 Mock으로 대체합니다.
	Mock이란 실제 객체와 동일한 구조를 가지지만 실제 로직이나 기능을 수행하지 않는 객체를 말합니다.
	컨트롤러를 테스트 할때 필요한 결과를 반환하도록 설정할 수 있습니다.
	 */
	@MockBean
	LoginService loginService;

	private MockHttpSession session;
	private final String validEmail = "test@gmail.com";
	private final String validPassword = "password";
	private final String validName = "홍길동";
	private final Long validUserId = 1L;

	@BeforeEach
	void setUp() {
		session = new MockHttpSession();
	}

	@Nested
	@DisplayName("로그인 성공 테스트")
	class LoginSuccess {
		@Test
		@DisplayName("회원가입된 구매자는 올바른 정보 입력시 로그인할 수 있습니다")
		void loginTest() throws Exception {
			// given
			final LoginCreateRequest createRequest = new LoginCreateRequest(validEmail, validPassword, UserType.BUYER);
			final LoginRequest loginRequest = createRequest.toServiceRequest();
			final LoginResponse response = new LoginResponse(validUserId, validName, validEmail);
			when(loginService.login(loginRequest)).thenReturn(response);

			// when & then
			mockMvc.perform(MockMvcRequestBuilders.post("/login")
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(createRequest))
					.session(session))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.id").value(response.id()))
				.andExpect(jsonPath("$.data.name").value(response.name()))
				.andExpect(jsonPath("$.data.email").value(response.email()));

			assertThat(session.getAttribute("user")).isEqualTo(validUserId);
		}

		@Test
		@DisplayName("회원가입된 판매자는 올바른 정보 입력시 로그인할 수 있습니다")
		void loginSellerTest() throws Exception {
			// given
			final LoginCreateRequest createRequest = new LoginCreateRequest(validEmail, validPassword, UserType.SELLER);
			final LoginRequest loginRequest = createRequest.toServiceRequest();
			final LoginResponse response = new LoginResponse(validUserId, validName, validEmail);
			when(loginService.login(loginRequest)).thenReturn(response);

			// when & then
			mockMvc.perform(MockMvcRequestBuilders.post("/login")
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(createRequest))
					.session(session))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.id").value(response.id()))
				.andExpect(jsonPath("$.data.name").value(response.name()))
				.andExpect(jsonPath("$.data.email").value(response.email()));

			assertThat(session.getAttribute("user")).isEqualTo(validUserId);
		}
	}

	@Nested
	@DisplayName("로그인 실패 테스트")
	class LoginFail {
		@Test
		@DisplayName("회원가입된 구매자는 올바르지 않은 정보 입력시 로그인할 수 없습니다")
		void loginTestWithFail() throws Exception {
			// given
			final LoginCreateRequest createRequest = new LoginCreateRequest(validEmail, "wrongPassword", UserType.BUYER);
			final LoginRequest loginRequest = createRequest.toServiceRequest();
			when(loginService.login(loginRequest)).thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."));

			// when & then
			mockMvc.perform(MockMvcRequestBuilders.post("/login")
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(createRequest))
					.session(session))
				.andDo(print())
				.andExpect(status().isUnauthorized());

			assertThat(session.getAttribute("user")).isNull();
		}

		@Test
		@DisplayName("존재하지 않는 이메일로 로그인 시도시 로그인할 수 없습니다")
		void loginWithNonExistentEmail() throws Exception {
			// given
			final LoginCreateRequest createRequest = new LoginCreateRequest("nonexistent@gmail.com", validPassword, UserType.BUYER);
			final LoginRequest loginRequest = createRequest.toServiceRequest();
			when(loginService.login(loginRequest)).thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

			// when & then
			mockMvc.perform(MockMvcRequestBuilders.post("/login")
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(createRequest))
					.session(session))
				.andDo(print())
				.andExpect(status().isNotFound());

			assertThat(session.getAttribute("user")).isNull();
		}
	}

	@Nested
	@DisplayName("로그인 엣지 케이스 테스트")
	class LoginEdgeCases {
		@Test
		@DisplayName("이메일이 빈 값일 경우 로그인할 수 없습니다")
		void loginWithEmptyEmail() throws Exception {
			// given
			final LoginCreateRequest createRequest = new LoginCreateRequest("", validPassword, UserType.BUYER);

			// when & then
			mockMvc.perform(MockMvcRequestBuilders.post("/login")
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(createRequest))
					.session(session))
				.andDo(print())
				.andExpect(status().isBadRequest());

			verify(loginService, never()).login(any());
		}

		@Test
		@DisplayName("비밀번호가 빈 값일 경우 로그인할 수 없습니다")
		void loginWithEmptyPassword() throws Exception {
			// given
			final LoginCreateRequest createRequest = new LoginCreateRequest(validEmail, "", UserType.BUYER);

			// when & then
			mockMvc.perform(MockMvcRequestBuilders.post("/login")
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(createRequest))
					.session(session))
				.andDo(print())
				.andExpect(status().isBadRequest());

			verify(loginService, never()).login(any());
		}

		@Test
		@DisplayName("잘못된 형식의 이메일로 로그인할 수 없습니다")
		void loginWithInvalidEmailFormat() throws Exception {
			// given
			final LoginCreateRequest createRequest = new LoginCreateRequest("invalid-email", validPassword, UserType.BUYER);

			// when & then
			mockMvc.perform(MockMvcRequestBuilders.post("/login")
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(createRequest))
					.session(session))
				.andDo(print())
				.andExpect(status().isBadRequest());

			verify(loginService, never()).login(any());
		}

		@Test
		@DisplayName("사용자 유형이 null일 경우 로그인할 수 없습니다")
		void loginWithNullUserType() throws Exception {
			// given
			final LoginCreateRequest createRequest = new LoginCreateRequest(validEmail, validPassword, null);

			// when & then
			mockMvc.perform(MockMvcRequestBuilders.post("/login")
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(createRequest))
					.session(session))
				.andDo(print())
				.andExpect(status().isBadRequest());

			verify(loginService, never()).login(any());
		}
	}
}
