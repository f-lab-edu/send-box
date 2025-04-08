package shop.sendbox.sendbox.security.crypto.hashing;

public interface HashingService {
	String hash(String input, String salt);

	String generateSalt();

	boolean verify(String input, String hashedInput, String salt);
}