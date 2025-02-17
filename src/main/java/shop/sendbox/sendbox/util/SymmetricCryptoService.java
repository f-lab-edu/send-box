package shop.sendbox.sendbox.util;

public interface SymmetricCryptoService {

	String encrypt(final String password, final String salt, String input);

	String decrypt(final String password, final String salt, String input);
}
