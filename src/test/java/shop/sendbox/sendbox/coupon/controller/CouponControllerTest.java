package shop.sendbox.sendbox.coupon.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.sendbox.sendbox.coupon.entity.CouponType;
import shop.sendbox.sendbox.coupon.service.CouponService;

@WebMvcTest(controllers = {CouponController.class})
class CouponControllerTest {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	MockMvc mockMvc;

	@MockBean
	CouponService couponService;

	@Test
	@DisplayName("쿠폰을 등록할때 판매자 정보가 없는 경우 오류가 발생한다.")
	void couponRegistWithoutSellerId() throws Exception {
		// given
		LocalDateTime startDateTime = LocalDateTime.of(2025, 3, 14, 1, 1);
		LocalDateTime endDateTime = LocalDateTime.of(2025, 5, 14, 1, 1);
		LocalDateTime createAt = LocalDateTime.of(2025, 3, 13, 1, 1);
		CouponCreateRequest couponCreateRequest = new CouponCreateRequest(BigDecimal.valueOf(5000), 1L, startDateTime,
			endDateTime, CouponType.WELCOME, 1L, createAt);
		when(couponService.registerCoupon(any())).thenThrow(new IllegalArgumentException("존재하지 않는 사용자입니다."));

		// when // then
		mockMvc.perform(post("/coupons").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(couponCreateRequest)))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.statusCode").value(400))
			.andExpect(jsonPath("$.status").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("존재하지 않는 사용자입니다."));
	}

	@Test
	@DisplayName("쿠폰을 등록할때 판매자 정보")
	void couponRegist() throws Exception {
		// given
		LocalDateTime startDateTime = LocalDateTime.of(2025, 3, 14, 1, 1);
		LocalDateTime endDateTime = LocalDateTime.of(2025, 5, 14, 1, 1);
		LocalDateTime createAt = LocalDateTime.of(2025, 3, 13, 1, 1);
		CouponCreateRequest couponCreateRequest = new CouponCreateRequest(BigDecimal.valueOf(5000), 1L, startDateTime,
			endDateTime, CouponType.WELCOME, 1L, createAt);

		// when // then
		mockMvc.perform(post("/coupons").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(couponCreateRequest)))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value(200))
			.andExpect(jsonPath("$.status").value("OK"))
			.andExpect(jsonPath("$.message").value("OK"));
	}

}
