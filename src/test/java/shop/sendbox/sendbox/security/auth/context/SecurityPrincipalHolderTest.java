package shop.sendbox.sendbox.security.auth.context;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import shop.sendbox.sendbox.login.UserType;
import shop.sendbox.sendbox.security.auth.UserPrincipal;
import shop.sendbox.sendbox.testsupport.annotation.MockTest;

@MockTest
class SecurityPrincipalHolderTest {

	@InjectMocks
	private SecurityPrincipalHolder securityPrincipalHolder;

	@AfterEach
	void tearDown() {
		securityPrincipalHolder.clear();
	}

	@Test
	@DisplayName("컨텍스트에 사용자 정보가 정상적으로 설정됩니다")
	void setContextStoresPrincipalCorrectly() {
		// given
		UserPrincipal userPrincipal = createUserPrincipal();

		// when
		securityPrincipalHolder.setContext(userPrincipal);

		// then
		UserPrincipal result = securityPrincipalHolder.getContext();
		assertThat(result).isEqualTo(userPrincipal);
	}

	@Test
	@DisplayName("컨텍스트에 null 값을 설정하면 예외가 발생합니다")
	void setContextWithNullThrowsException() {
		// given
		UserPrincipal nullPrincipal = null;

		// when & then
		assertThatThrownBy(() -> securityPrincipalHolder.setContext(nullPrincipal)).isInstanceOf(
			IllegalArgumentException.class).hasMessage("UserPrincipal은 필수입니다.");
	}

	@Test
	@DisplayName("설정되지 않은 컨텍스트를 조회하면 예외가 발생합니다")
	void getContextFromUnsetContextThrowsException() {
		// given
		// 컨텍스트가 설정되지 않은 상태

		// when & then
		assertThatThrownBy(() -> securityPrincipalHolder.getContext()).isInstanceOf(IllegalStateException.class)
			.hasMessage("UserPrincipal이 설정되지 않았습니다.");
	}

	@Test
	@DisplayName("컨텍스트를 초기화하고 사용하면 예외가 발생합니다.")
	void clearRemovesContext() {
		// given
		UserPrincipal userPrincipal = createUserPrincipal();
		securityPrincipalHolder.setContext(userPrincipal);

		// when
		securityPrincipalHolder.clear();

		// then
		assertThatThrownBy(() -> securityPrincipalHolder.getContext()).isInstanceOf(IllegalStateException.class)
			.hasMessage("UserPrincipal이 설정되지 않았습니다.");
	}

	@Test
	@DisplayName("컨텍스트에 여러 번 설정하면 마지막 사용자 정보로 덮어쓰기됩니다")
	void setContextOverwritesPreviousValue() {
		// given
		UserPrincipal firstPrincipal = createUserPrincipal();
		UserPrincipal secondPrincipal = createUserPrincipal("2");

		// when
		securityPrincipalHolder.setContext(firstPrincipal);
		securityPrincipalHolder.setContext(secondPrincipal);

		// then
		UserPrincipal result = securityPrincipalHolder.getContext();
		assertThat(result).isEqualTo(secondPrincipal);
		assertThat(result).isNotEqualTo(firstPrincipal);
	}

	@Test
	@DisplayName("초기화 후 재설정 시 컨텍스트가 정상적으로 작동합니다")
	void setContextAfterClearWorks() {
		// given
		UserPrincipal firstPrincipal = createUserPrincipal();
		securityPrincipalHolder.setContext(firstPrincipal);
		securityPrincipalHolder.clear();

		UserPrincipal secondPrincipal = createUserPrincipal("2");

		// when
		securityPrincipalHolder.setContext(secondPrincipal);

		// then
		assertThat(securityPrincipalHolder.getContext()).isEqualTo(secondPrincipal);
	}

	private UserPrincipal createUserPrincipal() {
		return createUserPrincipal("1");
	}

	private UserPrincipal createUserPrincipal(String id) {
		return new UserPrincipal(id, UserType.BUYER);
	}
}
