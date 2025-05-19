package shop.sendbox.sendbox.login;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
