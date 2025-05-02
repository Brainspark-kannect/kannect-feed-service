package com.kannect.feed.utils;

import java.util.Base64;
import java.util.Optional;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DecryptionUtil {

	@Value("${security.login.decryption.key:#{null}}")
	private Optional<String> loginCredentialsDecryptionKey;

	@Value("${security.login.decryption.algorithm:#{null}}")
	private Optional<String> loginCredentialsDecryptionAlgorithm;

	private static final Logger LOGGER = LoggerFactory.getLogger(DecryptionUtil.class);

	public String decrypt(String encryptedText) {
		try { // Retrieve key and algorithm from configuration
			String key = getLoginCredentialsDecryptionKey();
			String algorithm = getLoginCredentialsDecryptionAlgorithm();

			if (key == null || algorithm == null) {
				LOGGER.error("Login credential decryption key or algorithm is missing.");
				return new String();
			}

			// Perform decryption using retrieved values
			byte[] keyBytes = key.getBytes();
			SecretKeySpec secretKey = new SecretKeySpec(keyBytes, algorithm);

			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);

			byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
			byte[] decryptedBytes = cipher.doFinal(decodedBytes);

			return new String(decryptedBytes);
		} catch (Exception e) {
			LOGGER.error("Decryption failed for input: {}", encryptedText, e);
			return new String();
		}
	}

	private String getLoginCredentialsDecryptionKey() {
		if (loginCredentialsDecryptionKey.isEmpty()) {
			LOGGER.info("Login Credential's Decryption Key is empty: {}", loginCredentialsDecryptionKey.get());
			return null;
		}
		return loginCredentialsDecryptionKey.get();
	}

	private String getLoginCredentialsDecryptionAlgorithm() {
		if (loginCredentialsDecryptionAlgorithm.isEmpty()) {
			LOGGER.info("Login Credential's Decryption Algorithm is empty: {}",
					loginCredentialsDecryptionAlgorithm.get());
			return null;
		}
		return loginCredentialsDecryptionAlgorithm.get();
	}
}