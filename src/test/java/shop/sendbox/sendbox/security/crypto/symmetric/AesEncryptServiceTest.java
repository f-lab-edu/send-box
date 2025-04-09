package shop.sendbox.sendbox.security.crypto.symmetric;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import shop.sendbox.sendbox.security.config.SecurityConfig;

@SpringBootTest
@ActiveProfiles("test")
class AesEncryptServiceTest {

	@Autowired
	private AesEncryptService aesEncryptService;

	@Autowired
	private SecurityConfig securityConfig;

	@Test
	@DisplayName("문자열 암호화 및 복호화가 정상적으로 동작한다")
	void encryptAndDecrypt_Success() {
		// given
		String plainText = "안녕하세요, 테스트 문자열입니다.";

		// when
		String encrypted = aesEncryptService.encrypt(plainText);
		String decrypted = aesEncryptService.decrypt(encrypted);

		// then
		assertThat(encrypted).isNotEqualTo(plainText);
		assertThat(encrypted).contains(":");
		assertThat(decrypted).isEqualTo(plainText);
	}

	@Test
	@DisplayName("빈 문자열도 암호화/복호화가 정상적으로 동작한다")
	void encryptAndDecrypt_EmptyString_Success() {
		// given
		String plainText = "";

		// when
		String encrypted = aesEncryptService.encrypt(plainText);
		String decrypted = aesEncryptService.decrypt(encrypted);

		// then
		assertThat(decrypted).isEmpty();
	}

	@Test
	@DisplayName("같은 문자열을 두 번 암호화하면 같은 결과가 나온다 (IV가 동일하기에)")
	void encrypt_SameStringTwice_DifferentResults() {
		// given
		String plainText = "동일한 문자열";

		// when
		String encrypted1 = aesEncryptService.encrypt(plainText);
		String encrypted2 = aesEncryptService.encrypt(plainText);

		// then
		assertThat(encrypted1).isEqualTo(encrypted2);
		assertThat(aesEncryptService.decrypt(encrypted1)).isEqualTo(plainText);
		assertThat(aesEncryptService.decrypt(encrypted2)).isEqualTo(plainText);
	}

	@Test
	@DisplayName("설정된 알고리즘이 올바르게 적용되는지 확인한다")
	void checkAlgorithm() {
		// then
		assertThat(securityConfig.getSymmetric().getAlgorithm()).isEqualTo("AES/CBC/PKCS5Padding");
	}

	@Test
	@DisplayName("null을 복호화하면 예외가 발생한다")
	void decrypt_Null_ThrowsException() {
		// when & then
		assertThatThrownBy(() -> aesEncryptService.decrypt(null))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("암호화된 문자열이 null입니다");
	}

	@Test
	@DisplayName("잘못된 형식의 암호화된 문자열을 복호화하면 예외가 발생한다")
	void decrypt_InvalidFormat_ThrowsException() {
		// when & then
		assertThatThrownBy(() -> aesEncryptService.decrypt("잘못된형식"))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("잘못된 형식의 암호화 문자열입니다");
	}

	@Test
	@DisplayName("특수 문자와 유니코드를 포함한 문자열도 정상적으로 암호화/복호화된다")
	void encryptAndDecrypt_SpecialCharacters_Success() {
		// given
		String plainText = "특수문자!@#$%^&*()_+{}|:<>?한글과English混合";

		// when
		String encrypted = aesEncryptService.encrypt(plainText);
		String decrypted = aesEncryptService.decrypt(encrypted);

		// then
		assertThat(decrypted).isEqualTo(plainText);
	}

	@Test
	@DisplayName("긴 문자열도 정상적으로 암호화/복호화된다")
	void encryptAndDecrypt_LongString_Success() {
		// given
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 1000; i++) {
			sb.append("테스트문자열");
		}
		String plainText = sb.toString();

		// when
		String encrypted = aesEncryptService.encrypt(plainText);
		String decrypted = aesEncryptService.decrypt(encrypted);

		// then
		assertThat(decrypted).isEqualTo(plainText);
	}
}
