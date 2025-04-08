package shop.sendbox.sendbox.security.crypto.symmetric;

import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import shop.sendbox.sendbox.security.config.SecurityConfig;

@Service
@RequiredArgsConstructor
public class AesEncryptService implements SymmetricCryptoService {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String DELIMITER = ":";
    private static final String SECRET_KEY_FACTORY = "PBKDF2WithHmacSHA256";
    private static final int IV_LENGTH = 16;
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 256;

    private final SecurityConfig securityConfig;

    @Override
    public String encrypt(String input) {
        try {
            SecretKey key = getSecretKey();
            IvParameterSpec iv = generateIv();

            Cipher cipher = Cipher.getInstance(securityConfig.getSymmetric().getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);

            byte[] cipherText = cipher.doFinal(input.getBytes());
            String cipherTextBase64 = Base64.getEncoder().encodeToString(cipherText);
            String ivBase64 = Base64.getEncoder().encodeToString(iv.getIV());

            return cipherTextBase64 + DELIMITER + ivBase64;
        } catch (Exception e) {
            throw new RuntimeException("암호화 과정에서 오류가 발생했습니다", e);
        }
    }

    @Override
    public String decrypt(String cipherText) {
        if (cipherText == null) {
            throw new IllegalArgumentException("암호화된 문자열이 null입니다");
        }

        String[] parts = cipherText.split(DELIMITER);
        if (parts.length != 2) {
            throw new IllegalArgumentException("잘못된 형식의 암호화 문자열입니다");
        }

        try {
            SecretKey key = getSecretKey();

            byte[] cipherBytes = Base64.getDecoder().decode(parts[0]);
            byte[] ivBytes = Base64.getDecoder().decode(parts[1]);
            IvParameterSpec iv = new IvParameterSpec(ivBytes);

            Cipher cipher = Cipher.getInstance(securityConfig.getSymmetric().getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, key, iv);

            byte[] plainBytes = cipher.doFinal(cipherBytes);
            return new String(plainBytes);
        } catch (Exception e) {
            throw new RuntimeException("복호화 과정에서 오류가 발생했습니다", e);
        }
    }

    private SecretKey getSecretKey() throws Exception {
        KeySpec spec = new PBEKeySpec(
            securityConfig.getSymmetric().getPassword().toCharArray(),
            securityConfig.getSymmetric().getSalt().getBytes(),
            ITERATION_COUNT,
            KEY_LENGTH
        );

        SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_FACTORY);
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(keyBytes, "AES");
    }

    private IvParameterSpec generateIv() {
        byte[] iv = new byte[IV_LENGTH];
        return new IvParameterSpec(iv);
    }
}