package shop.sendbox.sendbox.api;

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
import shop.sendbox.sendbox.testsupport.config.TestHolderConfig;

@WebMvcTest(controllers = {
	LoginController.class
})
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
		mockMvc.perform(
				MockMvcRequestBuilders.post("/login")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(createRequest)))
			.andDo(print())
			.andExpect(MockMvcResultMatchers.status().isBadRequest())
			.andExpect(MockMvcResultMatchers.jsonPath("$.message").isString());
	}

}
