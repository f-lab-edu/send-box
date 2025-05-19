package shop.sendbox.sendbox.security.auth;

public enum Permission {
	PUBLIC("public"),
	BUYER("buyer"),
	SELLER("seller"),
	ADMIN("admin");

	private final String value;

	Permission(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public boolean doesNotRequireAuth() {
		return this == PUBLIC;
	}
}
