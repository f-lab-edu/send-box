package shop.sendbox.sendbox.buyer.controller;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.sendbox.sendbox.api.ApiResponse;
import shop.sendbox.sendbox.buyer.service.BuyerResponse;
import shop.sendbox.sendbox.buyer.service.BuyerService;

/*
빈 등록과 해당 클래스가 컨트롤러 역할,그리고 Json 응답을 변환해주는 @RestController 애노테이션을 추가했습니다.
RequiredArgsConstructor는 생성자에 필요한 필수 파라미터가 포함된 생성자를 만들어줍니다.
 */
@RestController
@RequiredArgsConstructor
public class BuyerController {
	private final BuyerService buyerService;
	// POST /buyers 요청을 처리하도록 핸들러를 등록하는 애노테이션을 추가했습니다.
	@PostMapping("/buyers")
	public ApiResponse<BuyerResponse> signUp(@RequestBody BuyerCreateRequest buyerCreateRequest) {
		final LocalDateTime now = LocalDateTime.now();
		return ApiResponse.ok(buyerService.signUp(buyerCreateRequest.toServiceRequest(), now));
	}

}
