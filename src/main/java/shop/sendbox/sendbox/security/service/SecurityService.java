package shop.sendbox.sendbox.security.service;

import shop.sendbox.sendbox.security.dto.PasswordData;

public interface SecurityService {
	String encryptText(String plainText);

	String decryptText(String cipherText);

	PasswordData encryptPassword(String plainPassword);

	boolean verifyPassword(String plainPassword, String hashedPassword, String salt);
}
