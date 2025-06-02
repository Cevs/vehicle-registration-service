package com.k218b.vehicleregistration.util;

import com.sun.net.httpserver.HttpExchange;

import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Logger;

/**
 * Utility class for internationalization (i18n) message lookup and formatting.
 * <p>
 * Provides methods to resolve the client's Locale from the HTTP request
 * and to retrieve localized messages from resource bundles, formatting
 * them with provided arguments. If a localized bundle is missing for the
 * client's locale, the utility gracefully falls back to English.
 * </p>
 */
public abstract class I18nUtil {
	private static final Logger LOG = Logger.getLogger(I18nUtil.class.getName());
	private static final String BUNDLE_BASE = "i18n.messages";
	private static final Locale FALLBACK_LOCALE = Locale.ENGLISH;

	private I18nUtil() {}

	/**
	 * Determines the Locale for a given HTTP exchange by parsing the
	 * "Accept-Language" header. Falls back to {@link Locale#getDefault()}
	 * if the header is missing or cannot be parsed.
	 *
	 * @param ex the HttpExchange representing the client request
	 * @return the resolved {@link Locale}
	 */
	public static Locale resolveLocale(HttpExchange ex) {
		final String lang = ex.getRequestHeaders().getFirst("Accept-Language");
		if (lang != null && !lang.isBlank()) {
			try {
				final String tag = lang.split(",")[0].split(";")[0];
				return Locale.forLanguageTag(tag);
			} catch (Exception e) {
				LOG.warning("Failed to parse Accept-Language: %s".formatted(lang));
			}
		}
		return Locale.getDefault();
	}

	/**
	 * Retrieves a localized message for the given key and formats it
	 * using MessageFormat, based on the Locale resolved from the
	 * HTTP exchange. If the message bundle for the resolved locale
	 * is unavailable, the method falls back to English.
	 *
	 * @param ex   the HttpExchange representing the client request
	 * @param key  the message key in the resource bundle
	 * @param args optional formatting arguments for the message
	 * @return the formatted, localized message string
	 */
	public static String getMessage(HttpExchange ex, String key, Object... args) {
		final Locale locale = resolveLocale(ex);
		return getMessage(locale, key, args);
	}

	/**
	 * Retrieves a localized message for the given key and formats it
	 * using MessageFormat for a specified {@link Locale}. If the specific
	 * locale bundle is unavailable, the method falls back to English.
	 *
	 * @param locale the desired {@link Locale}
	 * @param key    the message key in the resource bundle
	 * @param args   optional formatting arguments for the message
	 * @return the formatted, localized message string
	 */
	public static String getMessage(Locale locale, String key, Object... args) {
		ResourceBundle bundle;
		try {
			bundle = ResourceBundle.getBundle(BUNDLE_BASE, locale);
		} catch (MissingResourceException e) {
			LOG.warning("Missing bundle for locale %s, falling back to English".formatted(locale));
			bundle = ResourceBundle.getBundle(BUNDLE_BASE, FALLBACK_LOCALE);
		}

		try {
			String pattern = bundle.getString(key);
			return MessageFormat.format(pattern, args);
		} catch (MissingResourceException e) {
			LOG.warning("Missing key '%s' in resource bundle for locale %s".formatted(key, locale));
			return null;
		}
	}
}
