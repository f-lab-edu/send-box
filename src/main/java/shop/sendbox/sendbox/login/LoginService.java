package shop.sendbox.sendbox.login;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

/*
@Service 애노테이션은 해당 클래스가 서비스 로직을 처리하는 주석을 나타내며,
스프링 컨테이너는 해당 클래스를 스프링 빈으로 등록합니다.

@RequiredArgsConstructor 애노테이션은 필수로 들어가야하는 파라미터만 있는 생성자를 생성해줍니다.
 */
@Service
@RequiredArgsConstructor
public class LoginService {

	private final LoginHandlers loginHandlerList;

	/*
	JPA는 기본적으로 데이터를 조회하면 스냅샷을 만들고, Dirty Checking을 통해 변경된 데이터를 감지합니다.
	@Transactional(readOnly = true)는 해당 메서드에서 데이터의 변경이 없음을 명시하는 것으로,
	스냅샷을 만들지 않아 성능을 최적화할 수 있습니다.
	 */
	@Transactional(readOnly = true)
	public LoginResponse login(LoginRequest loginRequest) {
		final LoginHandler loginHandler = this.getLoginHandler(loginRequest.userType());
		return loginHandler.login(new LoginUser(loginRequest.email(), loginRequest.password()));
	}

	private LoginHandler getLoginHandler(UserType userType) {
		return loginHandlerList.getLoginHandler(userType);
	}

}
