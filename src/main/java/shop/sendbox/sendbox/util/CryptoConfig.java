package shop.sendbox.sendbox.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class CryptoConfig {

	@Value("${symmetric.crypto.password}")
	private String password;

	@Value("${symmetric.crypto.salt}")
	private String salt;

}
