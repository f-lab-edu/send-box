package shop.sendbox.sendbox.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import lombok.extern.slf4j.Slf4j;

/*
@Slf4j 애노테이션을 클래스에 붙이면 자동으로 log 객체를 생성합니다.
log 객체를 직접 생성하지 않고 log 메서드를 사용할 수 있습니다.
 */
@Slf4j
public class AesEncrypt {

	private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
	/**
	 * TODO
	 * 패스워드와 salt를 어떻게 보관할 것인지 고민입니다.
	 */
	private static final String PASSWORD = "password";
	private static final String SALT = "salt";
	private static final String DELIMITER = ":";
	private static final String PBKDF2_WITH_HMAC_SHA256 = "PBKDF2WithHmacSHA256";
	private static final Base64.Encoder ENCODER = Base64.getEncoder();
	private static final Base64.Decoder DECODER = Base64.getDecoder();

	private static SecretKey getKeyFromPassword() {
		KeySpec keySpec = new PBEKeySpec(PASSWORD.toCharArray(), SALT.getBytes(), 65536, 256);
		try {
			return new SecretKeySpec(generateSecret(keySpec), "AES");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static byte[] generateSecret(final KeySpec spec) throws InvalidKeySpecException, NoSuchAlgorithmException {
		SecretKeyFactory factory = SecretKeyFactory.getInstance(PBKDF2_WITH_HMAC_SHA256);
		return factory.generateSecret(spec).getEncoded();
	}

	private static IvParameterSpec generateIv() {
		byte[] iv = new byte[16];
		new SecureRandom().nextBytes(iv);
		return new IvParameterSpec(iv);
	}

	public static String encrypt(String input) {
		SecretKey key = getKeyFromPassword();
		final IvParameterSpec iv = generateIv();
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key, iv);
			byte[] cipherText = cipher.doFinal(input.getBytes());
			return ENCODER.encodeToString(cipherText) + DELIMITER + ENCODER.encodeToString(iv.getIV());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String decrypt(String cipherText) {
		if (cipherText == null) {
			throw new IllegalArgumentException("암호화된 문자열에 값이 없습니다.");
		}
		final String[] encryptAndIv = cipherText.split(DELIMITER);
		final SecretKey key = getKeyFromPassword();
		if (cipherText.isEmpty() || encryptAndIv.length != 2) {
			throw new IllegalArgumentException("암호화된 문자열 양식이 올바르지 않습니다.");
		}
		final int encryptIndex = 0;
		final int ivIndex = 1;
		final byte[] encryptBytes = DECODER.decode(encryptAndIv[encryptIndex]);
		final byte[] ivBytes = DECODER.decode(encryptAndIv[ivIndex]);
		IvParameterSpec iv = new IvParameterSpec(ivBytes);
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, key, iv);
			byte[] plainText = cipher.doFinal(encryptBytes);
			return new String(plainText);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
