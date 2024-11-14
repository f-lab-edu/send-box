package shop.sendbox.sendbox.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public class HashingEncrypt {
	static final int SALT_LENGTH = 16;

	public static String encrypt(String input, String salt) {
		final MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		digest.update(salt.getBytes());
		digest.update(input.getBytes());
		return Arrays.toString(digest.digest());
	}

	public static String generateSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[SALT_LENGTH];
		random.nextBytes(salt);
		return Base64.getEncoder().encodeToString(salt);
	}
}
