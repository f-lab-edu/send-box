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

import lombok.extern.slf4j.Slf4j;

/*
애노테이션 프로세서는 해당 애노테이션을 확인하면 컴파일 시점에 Logger 클래스를 생성합니다.
따라서 별도의 Logger 객체를 생성할 필요 없이 log 객체를 사용할 수 있습니다.
 */
@Slf4j
@Component
public class AesEncrypt implements SymmetricCryptoService {

	private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
	private static final String DELIMITER = ":";
	private static final String PBKDF2_WITH_HMAC_SHA256 = "PBKDF2WithHmacSHA256";
	private static final Base64.Encoder ENCODER = Base64.getEncoder();
	private static final Base64.Decoder DECODER = Base64.getDecoder();
	public static final int IV_LENGTH = 16;

	private static SecretKey getKeyFromPassword(final String password, final String salt) {
		KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
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
		byte[] iv = new byte[IV_LENGTH];
		return new IvParameterSpec(iv);
	}

	@Override
	public String encrypt(final String password, final String salt, final String input) {
		SecretKey key = getKeyFromPassword(password, salt);
		final IvParameterSpec iv = generateIv();
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key, iv); // 고정 IV로 초기화

			byte[] cipherText = cipher.doFinal(input.getBytes());

			final String ivEncoded = ENCODER.encodeToString(iv.getIV());
			final String cipherTextEncoded = ENCODER.encodeToString(cipherText);
			return cipherTextEncoded + DELIMITER + ivEncoded;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String decrypt(final String password, final String salt, final String cipherText) {
		if (cipherText == null) {
			throw new IllegalArgumentException("암호화된 문자열에 값이 없습니다.");
		}
		String[] encryptAndIv = cipherText.split(DELIMITER);
		if (encryptAndIv.length != 2) {
			throw new IllegalArgumentException("암호화된 문자열이 올바르지 않습니다.");
		}
		final SecretKey key = getKeyFromPassword(password, salt);
		byte[] ivBytes = DECODER.decode(encryptAndIv[1]);
		byte[] cipherBytes = DECODER.decode(encryptAndIv[0]);
		final IvParameterSpec iv = new IvParameterSpec(ivBytes);

		try {
			final Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, key, iv);

			byte[] plainText = cipher.doFinal(cipherBytes);
			return new String(plainText);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
