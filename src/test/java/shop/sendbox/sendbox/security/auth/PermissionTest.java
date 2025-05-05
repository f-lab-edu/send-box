package shop.sendbox.sendbox.security.auth;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

class PermissionTest {

	static Stream<Arguments> providePermissionsAndValues() {
		return Stream.of(
			Arguments.of(Permission.PUBLIC, "public"),
			Arguments.of(Permission.BUYER, "buyer"),
			Arguments.of(Permission.SELLER, "seller"),
			Arguments.of(Permission.ADMIN, "admin")
		);
	}

	@DisplayName("권한의 값을 조회하면 설정된 값이 반환됩니다")
	@ParameterizedTest
	@MethodSource("providePermissionsAndValues")
	void getValue_ShouldReturnCorrectValue(Permission permission, String expectedValue) {
		// when
		String actualValue = permission.getValue();

		// then
		assertThat(actualValue).isEqualTo(expectedValue);
	}

	@DisplayName("PUBLIC 권한은 인증이 필요하지 않은 상태입니다")
	@Test
	void publicPermission_DoesNotRequireAuth() {
		// given
		Permission permission = Permission.PUBLIC;

		// when
		boolean result = permission.doesNotRequireAuth();

		// then
		assertThat(result).isTrue();
	}

	@DisplayName("PUBLIC이 아닌 권한은 인증이 필요한 상태입니다")
	@ParameterizedTest
	@EnumSource(value = Permission.class, names = {"BUYER", "SELLER", "ADMIN"})
	void nonPublicPermissions_RequireAuth(Permission permission) {
		// when
		boolean result = permission.doesNotRequireAuth();

		// then
		assertThat(result).isFalse();
	}

	@DisplayName("모든 권한은 올바른 문자열 값을 가지고 있습니다")
	@Test
	void allPermissions_HaveCorrectValues() {
		// given, when, then
		assertThat(Permission.PUBLIC.getValue()).isEqualTo("public");
		assertThat(Permission.BUYER.getValue()).isEqualTo("buyer");
		assertThat(Permission.SELLER.getValue()).isEqualTo("seller");
		assertThat(Permission.ADMIN.getValue()).isEqualTo("admin");
	}

	@DisplayName("권한 열거형은 모든 필요한 권한을 포함하고 있습니다")
	@Test
	void enumValues_ShouldContainAllPermissions() {
		// given, when
		Permission[] permissions = Permission.values();

		// then
		assertThat(permissions).hasSize(4)
			.contains(Permission.PUBLIC, Permission.BUYER, Permission.SELLER, Permission.ADMIN);
	}
}
