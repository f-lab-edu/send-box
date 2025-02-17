package shop.sendbox.sendbox.login;

import java.util.List;

import org.springframework.stereotype.Component;

/*
@Component 애노테이션은 해당 클래스가 컴포넌트 스캔 대상을 표시합니다.
컴포넌트 스캔 대상이 스프링 컨테이너에 빈으로 등록되면 동일한 위치와 그 이하 패키지는 모두 스프링 빈으로 등록됩니다.
 */
@Component
public class LoginHandlers {

	private final List<LoginHandler> loginHandlers;

	public LoginHandlers(List<LoginHandler> loginHandlers) {
		this.loginHandlers = List.copyOf(loginHandlers);
	}

	public LoginHandler getLoginHandler(UserType userType) {
		return loginHandlers.stream()
			.filter(loginHandler -> loginHandler.supports(userType))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("지원하지 않는 유저 타입입니다."));
	}

}
