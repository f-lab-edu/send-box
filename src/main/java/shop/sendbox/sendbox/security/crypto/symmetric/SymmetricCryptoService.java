package shop.sendbox.sendbox.security.crypto.symmetric;

public interface SymmetricCryptoService {
	String encrypt(String input);

	String decrypt(String cipherText);
}