package shop.sendbox.sendbox.security.crypto.service;

import shop.sendbox.sendbox.security.crypto.dto.PasswordData;

public interface SecurityService {
	String encryptText(String plainText);

	String decryptText(String cipherText);

	PasswordData encryptPassword(String plainPassword);

	boolean verifyPassword(String plainPassword, String hashedPassword, String salt);
}
