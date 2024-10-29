package shop.sendbox.sendbox.util;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;
import shop.sendbox.sendbox.buyer.SymmetricCryptoService;

/*
@Slf4j 애노테이션을 클래스에 붙이면 자동으로 log 객체를 생성합니다.
log 객체를 직접 생성하지 않고 log 메서드를 사용할 수 있습니다.
 */
@Slf4j
@Component
public class AesEncrypt implements SymmetricCryptoService {

	private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
	/**
	 * TODO
	 * 패스워드와 salt를 어떻게 보관할 것인지 고민입니다.
	 */
	private static final String PASSWORD = "password";
	private static final String SALT = "salt";
	private static final String PBKDF2_WITH_HMAC_SHA256 = "PBKDF2WithHmacSHA256";
	private static final Base64.Encoder ENCODER = Base64.getEncoder();
	private static final Base64.Decoder DECODER = Base64.getDecoder();
	private static final byte[] IV = generateIv();

	private SecretKey getKeyFromPassword() {
		KeySpec keySpec = new PBEKeySpec(PASSWORD.toCharArray(), SALT.getBytes(), 65536, 256);
		try {
			return new SecretKeySpec(generateSecret(keySpec), "AES");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private byte[] generateSecret(final KeySpec spec) throws InvalidKeySpecException, NoSuchAlgorithmException {
		SecretKeyFactory factory = SecretKeyFactory.getInstance(PBKDF2_WITH_HMAC_SHA256);
		return factory.generateSecret(spec).getEncoded();
	}

	@Override
	public String encrypt(String input) {
		SecretKey key = getKeyFromPassword();
		IvParameterSpec ivSpec = new IvParameterSpec(IV);

		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec); // 고정 IV로 초기화

			byte[] cipherText = cipher.doFinal(input.getBytes());

			byte[] combined = new byte[IV.length + cipherText.length];
			System.arraycopy(IV, 0, combined, 0, IV.length);
			System.arraycopy(cipherText, 0, combined, IV.length, cipherText.length);

			return ENCODER.encodeToString(combined);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String decrypt(String cipherText) {
		Assert.hasText(cipherText, "암호화된 문자열에 값이 없습니다.");

		SecretKey key = getKeyFromPassword();
		byte[] combined = DECODER.decode(cipherText);

		// IV와 암호화된 텍스트 분리
		byte[] encryptedBytes = new byte[combined.length - IV.length];
		System.arraycopy(combined, 0, IV, 0, IV.length);
		System.arraycopy(combined, IV.length, encryptedBytes, 0, encryptedBytes.length);

		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IV));

			byte[] plainText = cipher.doFinal(encryptedBytes);
			return new String(plainText);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static byte[] generateIv() {
		return new byte[16];
	}
}
