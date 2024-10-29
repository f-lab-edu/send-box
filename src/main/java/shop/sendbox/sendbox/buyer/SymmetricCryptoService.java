package shop.sendbox.sendbox.buyer;

public interface SymmetricCryptoService {

	String encrypt(String planText);

	String decrypt(String planText);
}
