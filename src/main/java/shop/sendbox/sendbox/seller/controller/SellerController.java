package shop.sendbox.sendbox.seller.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.sendbox.sendbox.api.ApiResponse;
import shop.sendbox.sendbox.seller.service.SellerCreateResult;
import shop.sendbox.sendbox.seller.service.SellerService;

@RestController
@RequiredArgsConstructor
public class SellerController {

	private final SellerService sellerService;

	@PostMapping("/sellers")
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResponse<SellerCreateResponse> createSeller(@RequestBody SellerCreateRequest request) {
		SellerCreateResult result = sellerService.createSeller(request.toServiceCommand());
		return ApiResponse.ok(SellerCreateResponse.from(result));
	}
}
