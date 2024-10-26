package shop.sendbox.sendbox.login;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import shop.sendbox.sendbox.buyer.ApiResponse;

@RestController
@RequiredArgsConstructor
public class LoginController {

	private final LoginService loginService;

	@PostMapping("/login")
	public ApiResponse<LoginResponse> login(@RequestBody final LoginCreateRequest loginDto,
		final HttpServletRequest request) {
		final LoginResponse loginResponse = loginService.login(loginDto.toServiceRequest());
		final HttpSession session = request.getSession();
		session.setAttribute("user", loginResponse.id());
		return ApiResponse.ok(loginResponse);
	}
}
