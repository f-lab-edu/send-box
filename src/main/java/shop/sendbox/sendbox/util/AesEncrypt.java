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

@Slf4j
public class AesEncrypt {

	private static final String algorithm = "AES/CBC/PKCS5Padding";
	/**
	 * TODO
	 * 패스워드와 salt를 어떻게 보관할 것인지 고민입니다.
	 */
	private static final String password = "password";
	private static final String salt = "salt";
	private static final String delimiter = ":";
	private static final String pbkdf2WithHmacSHA256 = "PBKDF2WithHmacSHA256";
	private static final Base64.Encoder encoder = Base64.getEncoder();
	private static final Base64.Decoder decoder = Base64.getDecoder();

	private static SecretKey getKeyFromPassword() {
		KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
		try {
			return new SecretKeySpec(generateSecret(keySpec), "AES");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static byte[] generateSecret(final KeySpec spec) throws InvalidKeySpecException, NoSuchAlgorithmException {
		SecretKeyFactory factory = SecretKeyFactory.getInstance(pbkdf2WithHmacSHA256);
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
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.ENCRYPT_MODE, key, iv);
			byte[] cipherText = cipher.doFinal(input.getBytes());
			return encoder.encodeToString(cipherText) + delimiter + encoder.encodeToString(iv.getIV());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String decrypt(String cipherText) {
		if (cipherText == null) {
			throw new IllegalArgumentException("암호화된 문자열에 값이 없습니다.");
		}
		final String[] encryptAndIv = cipherText.split(delimiter);
		final SecretKey key = getKeyFromPassword();
		if (cipherText.isEmpty() || encryptAndIv.length != 2) {
			throw new IllegalArgumentException("암호화된 문자열 양식이 올바르지 않습니다.");
		}
		final int encryptIndex = 0;
		final int ivIndex = 1;
		final byte[] encryptBytes = decoder.decode(encryptAndIv[encryptIndex]);
		final byte[] ivBytes = decoder.decode(encryptAndIv[ivIndex]);
		IvParameterSpec iv = new IvParameterSpec(ivBytes);
		try {
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.DECRYPT_MODE, key, iv);
			byte[] plainText = cipher.doFinal(encryptBytes);
			return new String(plainText);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
