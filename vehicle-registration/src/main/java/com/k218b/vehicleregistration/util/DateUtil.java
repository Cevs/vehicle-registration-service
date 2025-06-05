package com.k218b.vehicleregistration.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utility class for parsing and formatting dates in ISO format (YYYY-MM-DD).
 * <p>
 * Provides methods to convert between {@link String} representations and {@link LocalDate}.
 * </p>
 */
public final class DateUtil {
	private static final DateTimeFormatter ISO_DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

	private DateUtil() {
		// Utility class; prevent instantiation
	}

	/**
	 * Parses a date string in the format YYYY-MM-DD to a {@link LocalDate}.
	 *
	 * @param dateString the date string in ISO format (e.g. "2025-12-31")
	 * @return the corresponding {@link LocalDate}
	 * @throws IllegalArgumentException if the input string is null, empty,
	 *         or not in the correct format
	 */
	public static LocalDate parseIsoDate(String dateString) {
		if (dateString == null || dateString.isBlank()) {
			throw new IllegalArgumentException("Date string must be non-null and non-blank");
		}
		try {
			return LocalDate.parse(dateString, ISO_DATE_FORMATTER);
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException(
					String.format("Invalid date format '%s'. Expected format: YYYY-MM-DD", dateString),
					e
			);
		}
	}

	/**
	 * Formats a {@link LocalDate} into a string using the ISO format (YYYY-MM-DD).
	 *
	 * @param date the {@link LocalDate} to format
	 * @return a string representation in ISO format (e.g. "2025-12-31")
	 * @throws IllegalArgumentException if the date is null
	 */
	public static String formatIsoDate(LocalDate date) {
		if (date == null) {
			throw new IllegalArgumentException("LocalDate must be non-null");
		}
		return date.format(ISO_DATE_FORMATTER);
	}

}
