package shop.sendbox.sendbox.seller.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.sendbox.sendbox.api.ApiControllerAdvice;
import shop.sendbox.sendbox.seller.service.SellerCreateCommand;
import shop.sendbox.sendbox.seller.service.SellerCreateResult;
import shop.sendbox.sendbox.seller.service.SellerService;

@WebMvcTest(SellerController.class)
@Import(ApiControllerAdvice.class)
@DisplayName("판매자 컨트롤러 테스트")
class SellerControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private SellerService sellerService;

	@Nested
	@DisplayName("판매자 생성 API (/sellers) 테스트")
	class CreateSellerTest {

		@Test
		@DisplayName("정상적인 요청 시 판매자 정보가 성공적으로 생성됩니다.")
		void createSellerSuccess() throws Exception {
			// given
			SellerCreateRequest request = new SellerCreateRequest("password123", "테스트상점", "123-45-67890",
				"010-1234-5678", "tax@example.com");
			SellerCreateResult serviceResponse = new SellerCreateResult(1L, "테스트상점", "010-1234-5678",
				"tax@example.com");
			given(sellerService.createSeller(any(SellerCreateCommand.class))).willReturn(serviceResponse);

			// when
			ResultActions actions = mockMvc.perform(post("/sellers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)));

			// then
			actions.andExpect(status().isCreated())
				.andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value())) // ApiResponse 구조 검증
				.andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
				.andExpect(jsonPath("$.message").value(HttpStatus.OK.name())) // ApiResponse.ok()는 메시지를 status 이름으로 설정
				.andExpect(jsonPath("$.data.id").value(serviceResponse.id()))
				.andExpect(jsonPath("$.data.name").value(serviceResponse.name()))
				.andExpect(jsonPath("$.data.phoneNumber").value(serviceResponse.phoneNumber()))
				.andExpect(jsonPath("$.data.taxEmail").value(serviceResponse.taxEmail()));

			then(sellerService).should(times(1)).createSeller(any(SellerCreateCommand.class));
		}

		@Test
		@DisplayName("서비스 계층에서 예외 발생 시 400 상태 코드와 에러 메시지를 반환합니다.")
		void createSellerFailWhenServiceThrowsException() throws Exception {
			// given
			SellerCreateRequest request = new SellerCreateRequest("password123", "중복상점", "111-22-33333",
				"010-1111-2222", "duplicate@example.com");
			String errorMessage = "이미 등록된 사업자번호입니다.";
			given(sellerService.createSeller(any(SellerCreateCommand.class)))
				.willThrow(new IllegalArgumentException(errorMessage));

			// when
			ResultActions actions = mockMvc.perform(post("/sellers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)));

			// then
			actions.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value())) // ApiResponse 구조 검증
				.andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
				.andExpect(jsonPath("$.message").value(errorMessage)) // 예외 메시지 검증
				.andExpect(jsonPath("$.data").isEmpty()); // 데이터는 null 또는 비어있음

			then(sellerService).should(times(1)).createSeller(any(SellerCreateCommand.class));
		}
	}
}
