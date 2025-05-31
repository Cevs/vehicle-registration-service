package com.k218b.vehicleregistration.util;

import java.util.UUID;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

/**
 * Utility for symmetric encryption and decryption of text using AES/GCM.
 * <p>
 * Uses the provided salt (Base64-encoded 16-byte key) as the AES key.
 * Each encryption generates a random 12-byte IV. Ciphertext is Base64-encoded
 * in the form <code>IV||ciphertext</code>. Decryption reverses this process.
 * </p>
 * <p>
 * AES/GCM provides authenticated encryption ensuring both confidentiality
 * and integrity of the data.</p>
 */
public class CryptoUtil {

	private static final String TRANSFORMATION = "AES/GCM/NoPadding";
	private static final String ALGORITHM = "AES";
	private static final int IV_LENGTH = 12; 				// bytes
	private static final int TAG_LENGTH_BITS = 128;			// bits
	private static final int SALT_LENGTH = 16;				// bytes
	private static final int PASSWORD_LENGTH = 8;
	private static final SecureRandom RANDOM = new SecureRandom();

	private CryptoUtil() {}

	/**
	 * Encrypts the given plaintext using AES/GCM with a random IV.
	 *
	 * @param plaintext  the text to encrypt
	 * @param salt the Base64-encoded 16-byte AES key
	 * @return Base64-encoded string containing IV + ciphertext
	 * @throws IllegalStateException if encryption fails
	 */
	public static String encrypt(String plaintext, String salt) {
		try {
			final byte[] keyBytes = Base64.getDecoder()
										  .decode(salt);
			final SecretKeySpec keySpec = new SecretKeySpec(keyBytes, ALGORITHM);

			final byte[] iv = new byte[IV_LENGTH];
			RANDOM.nextBytes(iv);
			final GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH_BITS, iv);

			final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec);
			final byte[] encrypted = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

			final byte[] combined = new byte[iv.length + encrypted.length];
			System.arraycopy(iv, 0, combined, 0, iv.length);
			System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

			return Base64.getEncoder().encodeToString(combined);
		} catch (Exception e) {
			throw new IllegalStateException("Error encrypting data", e);
		}
	}

	/**
	 * Decrypts the given Base64-encoded IV+ciphertext using AES/GCM.
	 *
	 * @param encryptedPassword the Base64-encoded IV + ciphertext
	 * @param salt     the Base64-encoded 16-byte AES key
	 * @return the decrypted plaintext
	 * @throws IllegalStateException if decryption fails
	 */
	public static String decrypt(String encryptedPassword, String salt) {
		try {
			final byte[] combined = Base64.getDecoder()
										  .decode(encryptedPassword);
			final byte[] iv = Arrays.copyOfRange(combined, 0, IV_LENGTH);
			final byte[] ciphertext = Arrays.copyOfRange(combined, IV_LENGTH, combined.length);

			final byte[] keyBytes = Base64.getDecoder()
										  .decode(salt);
			final SecretKeySpec keySpec = new SecretKeySpec(keyBytes, ALGORITHM);
			final GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH_BITS, iv);

			final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec);
			final byte[] decrypted = cipher.doFinal(ciphertext);

			return new String(decrypted, StandardCharsets.UTF_8);
		} catch (Exception e) {
			throw new IllegalStateException("Error decrypting data", e);
		}
	}

	/**
	 * Generates a new random salt of length 16 bytes, Base64-encoded.
	 *
	 * @return the Base64-encoded salt
	 */
	public static String generateSalt() {
		final byte[] salt = new byte[SALT_LENGTH];
		RANDOM.nextBytes(salt);
		return Base64.getEncoder().encodeToString(salt);
	}

	/**
	 * Generates a secure random password of length 8 based on a UUID.
	 * <p>
	 * Takes a random UUID, removes hyphens, and uses the first 8 characters.
	 * The resulting password contains hexadecimal digits (0-9, a-f).
	 * </p>
	 *
	 * @return an 8-character password derived from a UUID
	 */
	public static String generateRandomPassword() {
		String uuid = UUID.randomUUID().toString().replace("-", "");
		return uuid.substring(0, PASSWORD_LENGTH);
	}

}
