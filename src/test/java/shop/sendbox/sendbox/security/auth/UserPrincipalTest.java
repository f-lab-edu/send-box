package shop.sendbox.sendbox.security.auth;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import shop.sendbox.sendbox.login.UserType;

class UserPrincipalTest {

	@DisplayName("사용자 타입이 ADMIN인 경우 isAdmin은 true를 반환합니다")
	@Test
	void isAdmin_WhenUserTypeIsAdmin_ReturnsTrue() {
		// given
		UserPrincipal adminUser = new UserPrincipal("admin123", UserType.ADMIN);

		// when
		boolean result = adminUser.isAdmin();

		// then
		assertThat(result).isTrue();
	}

	@DisplayName("사용자 타입이 ADMIN이 아닌 경우 isAdmin은 false를 반환합니다")
	@ParameterizedTest
	@EnumSource(value = UserType.class, names = {"BUYER", "SELLER"})
	void isAdmin_WhenUserTypeIsNotAdmin_ReturnsFalse(UserType userType) {
		// given
		UserPrincipal nonAdminUser = new UserPrincipal("user123", userType);

		// when
		boolean result = nonAdminUser.isAdmin();

		// then
		assertThat(result).isFalse();
	}

	@DisplayName("BUYER 사용자는 BUYER 권한을 가지고 있습니다")
	@Test
	void hasPermission_WhenBuyerChecksForBuyerPermission_ReturnsTrue() {
		// given
		UserPrincipal buyerUser = new UserPrincipal("buyer123", UserType.BUYER);

		// when, then
		assertThat(buyerUser.hasPermission(Permission.BUYER)).isTrue();
	}

	@DisplayName("SELLER 사용자는 SELLER 권한을 가지고 있습니다")
	@Test
	void hasPermission_WhenSellerChecksForSellerPermission_ReturnsTrue() {
		// given
		UserPrincipal sellerUser = new UserPrincipal("seller123", UserType.SELLER);

		// when, then
		assertThat(sellerUser.hasPermission(Permission.SELLER)).isTrue();
	}

	@DisplayName("ADMIN 사용자는 모든 권한을 가지고 있습니다")
	@ParameterizedTest
	@EnumSource(value = Permission.class)
	void hasPermission_WhenAdminChecksForAnyPermission_ReturnsTrue(Permission permission) {
		// given
		UserPrincipal adminUser = new UserPrincipal("admin123", UserType.ADMIN);

		// when, then
		assertThat(adminUser.hasPermission(permission)).isTrue();
	}

	@DisplayName("BUYER 사용자는 BUYER 외 다른 권한을 확인하면 예외가 발생합니다")
	@ParameterizedTest
	@EnumSource(value = Permission.class, names = {"SELLER", "ADMIN", "PUBLIC"})
	void hasPermission_WhenBuyerChecksForNonBuyerPermission_ThrowsException(Permission permission) {
		// given
		UserPrincipal buyerUser = new UserPrincipal("buyer123", UserType.BUYER);

		// when, then
		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> buyerUser.hasPermission(permission))
			.withMessage("권한이 없습니다.");
	}

	@DisplayName("SELLER 사용자는 SELLER 외 다른 권한을 확인하면 예외가 발생합니다")
	@ParameterizedTest
	@EnumSource(value = Permission.class, names = {"BUYER", "ADMIN", "PUBLIC"})
	void hasPermission_WhenSellerChecksForNonSellerPermission_ThrowsException(Permission permission) {
		// given
		UserPrincipal sellerUser = new UserPrincipal("seller123", UserType.SELLER);

		// when, then
		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> sellerUser.hasPermission(permission))
			.withMessage("권한이 없습니다.");
	}

	@DisplayName("권한 확인시 null을 전달하면 예외가 발생합니다")
	@Test
	void hasPermission_WithNullPermission_ThrowsException() {
		// given
		UserPrincipal user = new UserPrincipal("user123", UserType.BUYER);

		// when, then
		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> user.hasPermission(null))
			.withMessage("currentPermission은 필수입니다.");
	}

	@DisplayName("사용자 생성시 ID와 유형이 정상적으로 설정됩니다")
	@Test
	void constructor_SetsIdAndUserTypeCorrectly() {
		// given
		String id = "test123";
		UserType userType = UserType.BUYER;

		// when
		UserPrincipal user = new UserPrincipal(id, userType);

		// then
		assertThat(user.isAdmin()).isFalse();
		assertThat(user.hasPermission(Permission.BUYER)).isTrue();
	}
}
