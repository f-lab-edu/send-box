package shop.sendbox.sendbox.security.crypto.hashing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import shop.sendbox.sendbox.security.crypto.config.SecurityConfig;

@Service
@RequiredArgsConstructor
public class Sha256HashingService implements HashingService {

	private final SecurityConfig securityConfig;

	@Override
	public String hash(String input, String salt) {
		try {
			MessageDigest digest = MessageDigest.getInstance(securityConfig.getHashing().getAlgorithm());
			digest.update(salt.getBytes());
			digest.update(input.getBytes());
			byte[] hashBytes = digest.digest();
			return Base64.getEncoder().encodeToString(hashBytes);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("해싱 알고리즘을 찾을 수 없습니다", e);
		}
	}

	@Override
	public String generateSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[securityConfig.getHashing().getSaltLength()];
		random.nextBytes(salt);
		return Base64.getEncoder().encodeToString(salt);
	}

	@Override
	public boolean verify(String input, String hashedInput, String salt) {
		String newHashedInput = hash(input, salt);
		return newHashedInput.equals(hashedInput);
	}
}
