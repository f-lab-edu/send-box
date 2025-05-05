package shop.sendbox.sendbox.security.crypto.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import shop.sendbox.sendbox.security.crypto.dto.PasswordData;
import shop.sendbox.sendbox.security.crypto.hashing.HashingService;
import shop.sendbox.sendbox.security.crypto.symmetric.SymmetricCryptoService;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

	private final SymmetricCryptoService symmetricCryptoService;
	private final HashingService hashingService;

	@Override
	public String encryptText(String plainText) {
		return symmetricCryptoService.encrypt(plainText);
	}

	@Override
	public String decryptText(String cipherText) {
		return symmetricCryptoService.decrypt(cipherText);
	}

	@Override
	public PasswordData encryptPassword(String plainPassword) {
		String salt = hashingService.generateSalt();
		String hashedPassword = hashingService.hash(plainPassword, salt);
		return new PasswordData(hashedPassword, salt);
	}

	@Override
	public boolean verifyPassword(String plainPassword, String hashedPassword, String salt) {
		return hashingService.verify(plainPassword, hashedPassword, salt);
	}
}
