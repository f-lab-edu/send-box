package shop.sendbox.sendbox.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@ConfigurationProperties(prefix = "security")
@Component
public class SecurityConfig {
	// 대칭키 암호화 설정
	private final Symmetric symmetric = new Symmetric();

	// 해싱 설정
	private final Hashing hashing = new Hashing();

	@Getter
	@Setter
	public static class Symmetric {
		private String password;
		private String salt;
		private String algorithm = "AES/CBC/PKCS5Padding";
	}

	@Getter
	@Setter
	public static class Hashing {
		private String algorithm = "SHA-256";
		private int saltLength = 16;
	}
}