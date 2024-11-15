package shop.sendbox.sendbox.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AesEncryptTest {

	@Autowired
	SymmetricCryptoService symmetricCryptoService;

	@Test
	@DisplayName("올바른 값을 입력시 암호화 복호화가 정상동작 합니다.")
	void encryptTest() {
		// given
		String input = "test";
		final String password = "AesEncrypt.PASSWORD";
		final String salt = "AesEncrypt.SALT";
		String encrypted = symmetricCryptoService.encrypt(password, salt, input);

		// when
		final String decrypted = symmetricCryptoService.decrypt(password, salt, encrypted);

		// then
		Assertions.assertThat(decrypted).isEqualTo(input);
	}

	@Test
	@DisplayName("올바른 값을 입력시 암호화 복호화가 정상동작 합니다.")
	void encryptTestWith() {
		// given
		final String input = "test";
		final String password = "AesEncrypt.PASSWORD";
		final String salt = "AesEncrypt.SALT";

		// when
		final String encrypted1 = symmetricCryptoService.encrypt(password, salt, input);
		final String encrypted2 = symmetricCryptoService.encrypt(password, salt, input);

		// then
		Assertions.assertThat(encrypted1).isEqualTo(encrypted2);
	}

	@Test
	@DisplayName("복호화시 값이 없으면 예외가 발생합니다.")
	void decryptTestWithNull() {
		// given
		String encrypted = null;
		final String password = "AesEncrypt.PASSWORD";
		final String salt = "AesEncrypt.SALT";

		// when & then
		Assertions.assertThatThrownBy(() -> symmetricCryptoService.decrypt(password, salt, encrypted))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("암호화된 문자열에 값이 없습니다.");
	}

	@Test
	@DisplayName("복호화시 암호문 양식이 올바르지 않으면 예외가 발생합니다.")
	void decryptTestWithNot() {
		// given
		String encrypted = "test";
		final String password = "AesEncrypt.PASSWORD";
		final String salt = "AesEncrypt.SALT";

		// when & then
		Assertions.assertThatThrownBy(() -> symmetricCryptoService.decrypt(password, salt, encrypted))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("암호화된 문자열이 올바르지 않습니다.");
	}
}
