package shop.sendbox.sendbox.buyer;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BuyerController {
	private final BuyerService buyerService;

	@PostMapping("/buyers")
	public ApiResponse<BuyerResponse> signUp(BuyerCreateRequest buyerCreateRequest) {

		final LocalDateTime now = LocalDateTime.now();
		BuyerResponse buyerResponse = buyerService.signUp(buyerCreateRequest.toServiceRequest(), now);
		return ApiResponse.ok(buyerResponse);
	}

}
