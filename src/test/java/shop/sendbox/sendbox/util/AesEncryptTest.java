package shop.sendbox.sendbox.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import shop.sendbox.sendbox.buyer.SymmetricCryptoService;

@SpringBootTest
class AesEncryptTest {

	@Autowired
	SymmetricCryptoService symmetricCryptoService;

	@Test
	@DisplayName("올바른 값을 입력시 암호화 복호화가 정상동작 합니다.")
	void encryptTest() {
		// given
		final AesEncrypt aesEncrypt = new AesEncrypt();
		final String input = "test";
		final String encrypted = aesEncrypt.encrypt(input);

		// when
		final String decrypted = aesEncrypt.decrypt(encrypted);

		// then
		Assertions.assertThat(decrypted).isEqualTo(input);
	}

	@Test
	@DisplayName("올바른 값을 입력시 암호화 복호화가 정상동작 합니다.")
	void encryptTestWith() {
		// given
		final String input = "test";

		// when
		final String encrypted1 = symmetricCryptoService.encrypt(input);
		final String encrypted2 = symmetricCryptoService.encrypt(input);

		// then
		Assertions.assertThat(encrypted1).isEqualTo(encrypted2);
	}

	@Test
	@DisplayName("복호화시 값이 없으면 예외가 발생합니다.")
	void decryptTestWithNull() {
		// given
		final AesEncrypt aesEncrypt = new AesEncrypt();
		String encrypted = null;

		// when & then
		Assertions.assertThatThrownBy(() -> aesEncrypt.decrypt(encrypted))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("암호화된 문자열에 값이 없습니다.");
	}

}
