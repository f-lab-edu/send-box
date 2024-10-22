package shop.sendbox.sendbox.buyer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = {
	BuyerController.class,
})
class BuyerControllerTest {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	MockMvc mockMvc;

	@MockBean
	BuyerService buyerService;

	@Test
	@DisplayName("구매자는 정보를 입력하여 가입할 수 있다.")
	void signUp() throws Exception {
		// given
		String email = "test@gmail.com";
		String password = "password";
		String name = "홍길동";
		String phoneNumber = "01012345678";
		String createdBy = "admin";

		// when
		BuyerCreateRequest buyerCreateRequest =
			new BuyerCreateRequest(email, password, name, phoneNumber, null, createdBy);

		// when // then
		mockMvc.perform(post("/buyers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(buyerCreateRequest))
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value(200))
			.andExpect(jsonPath("$.status").value("OK"))
			.andExpect(jsonPath("$.message").value("OK"));
	}

}
