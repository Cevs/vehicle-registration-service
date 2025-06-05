package com.k218b.vehicleregistration.config;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads application properties from classpath.
 * <p>Use {@link #get(String)} to retrieve property values.</p>
 */
public final class AppConfig {
	private static final Properties PROPS = new Properties();

	private AppConfig() {}

	static {
		try (InputStream in = AppConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
			if (in == null) {
				throw new IllegalStateException("application.properties not found on classpath");
			}
			PROPS.load(in);
		} catch (IOException e) {
			throw new ExceptionInInitializerError("Failed to load application.properties: %s".formatted(e.getMessage()));
		}
	}

	/**
	 * Get the integer property value for the given key.
	 * @param key the property name
	 * @return the integer value
	 * @throws NumberFormatException if the property value is not a valid integer
	 */
	public static int getInt(String key) {
		String value = get(key);
		if (value == null) {
			throw new NumberFormatException("Property '%s' not found".formatted(key));
		}
		return Integer.parseInt(value);
	}

	/**
	 * Get the property value for the given key.
	 * @param key the property name
	 * @return the property value (or null if not defined)
	 */
	public static String get(String key) {
		return PROPS.getProperty(key);
	}

	/**
	 * Get the property value for the given key, or return default if missing.
	 * @param key the property name
	 * @param defaultValue fallback value
	 * @return the property value or defaultValue
	 */
	public static String getOrDefault(String key, String defaultValue) {
		return PROPS.getProperty(key, defaultValue);
	}
}