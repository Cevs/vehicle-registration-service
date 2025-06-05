package com.k218b.vehicleregistration.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.UUID;

/**
 * Utility for hashing passwords using PBKDF2WithHmacSHA256,
 * and for generating cryptographic salts and random passwords.
 * <p>
 * The salt is generated as a 16-byte random value and Base64-encoded
 * for storage. Password hashing uses 65536 iterations and a 128-bit key length.
 * </p>
 */
public class CryptoUtil {
	private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
	private static final int ITERATIONS = 65536;
	private static final int KEY_LENGTH = 128; // bits
	private static final int SALT_LENGTH = 16; // bytes
	private static final int PASSWORD_LENGTH = 8;
	private static final SecureRandom RANDOM = new SecureRandom();

	private CryptoUtil() {
		// Utility class; prevent instantiation
	}

	/**
	 * Generates a new random salt of length 16 bytes, Base64-encoded.
	 *
	 * @return the Base64-encoded salt
	 */
	public static String generateSalt() {
		byte[] salt = new byte[SALT_LENGTH];
		RANDOM.nextBytes(salt);
		return Base64.getEncoder().encodeToString(salt);
	}

	/**
	 * Hashes the given password with the provided Base64-encoded salt using PBKDF2.
	 *
	 * @param password  the raw password to hash
	 * @param saltBase64 the Base64-encoded salt
	 * @return the Base64-encoded hashed password
	 * @throws IllegalStateException if hashing fails or salt is invalid
	 */
	public static String hashPassword(String password, String saltBase64) {
		try {
			byte[] salt = Base64.getDecoder().decode(saltBase64);
			if (salt.length != SALT_LENGTH) {
				throw new IllegalArgumentException("Salt must be " + SALT_LENGTH + " bytes");
			}
			PBEKeySpec spec = new PBEKeySpec(
					password.toCharArray(),
					salt,
					ITERATIONS,
					KEY_LENGTH
			);
			SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
			byte[] hash = skf.generateSecret(spec).getEncoded();
			return Base64.getEncoder().encodeToString(hash);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new IllegalStateException("Error hashing password", e);
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException("Invalid salt provided", e);
		}
	}

	/**
	 * Generates a secure random password of length 8 based on a UUID.
	 * <p>
	 * Takes a random UUID, removes hyphens, and uses the first 8 characters.
	 * The resulting password contains hexadecimal digits (0-9, a-f).
	 * </p>
	 *
	 * @return an 8-character random password derived from a UUID
	 */
	public static String generateRandomPassword() {
		String uuid = UUID.randomUUID().toString().replace("-", "");
		return uuid.substring(0, PASSWORD_LENGTH);
	}
}
