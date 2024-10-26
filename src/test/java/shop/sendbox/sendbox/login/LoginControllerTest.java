package shop.sendbox.sendbox.login;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = {LoginController.class})
class LoginControllerTest {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	MockMvc mockMvc;

	@MockBean
	LoginService loginService;

	@Test
	@DisplayName("회원가입된 구매자는 올바른 정보 입력시 로그인할 수 있다.")
	void loginTest() throws Exception {
		// given
		MockHttpSession session = new MockHttpSession();
		final String mail = "test@gmail.com";
		final LoginCreateRequest createRequest = new LoginCreateRequest(mail, "password", UserType.BUYER);
		final LoginRequest loginRequest = createRequest.toServiceRequest();
		final Long userId = 1L;
		final LoginResponse response = new LoginResponse(userId, "홍길동", mail);
		Mockito.when(loginService.login(loginRequest)).thenReturn(response);

		// then
		mockMvc.perform(MockMvcRequestBuilders.post("/login")
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createRequest))
				.session(session))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.id").value(response.id()))
			.andExpect(jsonPath("$.data.name").value(response.name()))
			.andExpect(jsonPath("$.data.email").value(response.email()));

		assertThat(session.getAttribute("user")).isEqualTo(userId);
	}

	@Test
	@DisplayName("회원가입된 구매자는 올바르지 않은 정보 입력시 로그인할 수 없다.")
	void loginTestWithFail() {
		// given
	}
}
