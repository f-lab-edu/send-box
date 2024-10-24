package shop.sendbox.sendbox.util;

import java.security.SecureRandom;
import java.util.Base64;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

public class EncryptUtil {
	static final int saltLength = 16;

	public static String encrypt(String input, String salt) {
		final HashCode hashCode = Hashing.sha256().hashString(input + salt, Charsets.UTF_8);
		return hashCode.toString();
	}

	public static String generateSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[saltLength];
		random.nextBytes(salt);
		return Base64.getEncoder().encodeToString(salt);
	}
}
