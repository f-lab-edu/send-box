package shop.sendbox.sendbox.security.auth;

import java.util.EnumSet;
import java.util.Set;

import org.springframework.util.Assert;

import shop.sendbox.sendbox.login.UserType;

public class UserPrincipal {

	private final String id;
	private final UserType userType;
	private final Set<Permission> permissions;

	public UserPrincipal(String id, UserType userType) {
		this.id = id;
		this.userType = userType;
		this.permissions = EnumSet.noneOf(Permission.class);
		initPermissions();
	}

	private void initPermissions() {
		switch (userType) {
			case BUYER:
				permissions.add(Permission.BUYER);
				break;
			case SELLER:
				permissions.add(Permission.SELLER);
				break;
			case ADMIN:
				permissions.addAll(EnumSet.allOf(Permission.class));
				break;
		}
	}

	public boolean isAdmin() {
		return userType == UserType.ADMIN;
	}

	public boolean hasPermission(Permission currentPermission) {
		Assert.notNull(currentPermission, "currentPermission은 필수입니다.");

		if (permissions.contains(currentPermission)) {
			return true;
		}

		throw new IllegalArgumentException("권한이 없습니다.");
	}
}
