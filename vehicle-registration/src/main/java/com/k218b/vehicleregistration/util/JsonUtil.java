package com.k218b.vehicleregistration.util;

import com.k218b.vehicleregistration.exception.JsonSerializationException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Utility class for JSON parsing and serialization of POJO records.
 * <p>
 * Provides methods to deserialize a flat JSON object into a record
 * instance and to serialize a record or simple POJO back to a JSON string.
 * Supported component types are String, Number, Boolean, and java.time.LocalDate.
 * </p>
 *
 * <p>This class is stateless and should not be instantiated.</p>
 */
public class JsonUtil {

	private JsonUtil() {}


	/**
	 * Deserialize a JSON object from the given input stream into an instance of the specified record class.
	 * <p>
	 * The JSON must represent a flat object with keys matching the record component names.
	 * Supported component types are String, Number, Boolean, and LocalDate.
	 * </p>
	 *
	 * @param in    the InputStream from which to read the JSON payload
	 * @param clazz the target record class to instantiate
	 * @param <T>   the type of the record
	 * @return an instance of type T populated with values from the JSON
	 * @throws IOException if the JSON is malformed or if instantiation fails
	 */
	public static <T> T fromJson(InputStream in, Class<T> clazz) throws IOException {
		// 1) read whole body
		final String json = new String(in.readAllBytes(), StandardCharsets.UTF_8).trim();
		if (!json.startsWith("{") || !json.endsWith("}")) {
			throw new IOException("Invalid JSON object");
		}
		// 2) strip { } and split on commas (no nested objects supported)
		final String body = json.substring(1, json.length() - 1).trim();
		final Map<String, String> keyValuePair = new LinkedHashMap<>();
		if (!body.isEmpty()) {
			String[] parts = body.split("\\s*,\\s*");
			for (String part : parts) {
				String[] pair = part.split("\\s*:\\s*", 2);
				String key = unquote(pair[0]);
				String val = pair[1].trim();
				keyValuePair.put(key, val);
			}
		}

		try {
			final RecordComponent[] comps = clazz.getRecordComponents();
			final Class<?>[] types = new Class<?>[comps.length];
			final Object[] args = new Object[comps.length];
			for (int i = 0; i < comps.length; i++) {
				final RecordComponent rc = comps[i];
				types[i] = rc.getType();
				final String raw = keyValuePair.get(rc.getName());
				if (raw == null) {
					args[i] = null;
				} else {
					args[i] = convert(raw, types[i]);
				}
			}
			final Constructor<T> constructor = clazz.getDeclaredConstructor(types);
			return constructor.newInstance(args);

		} catch (Exception e) {
			throw new IOException("Failed to deserialize JSON to %s".formatted(clazz.getSimpleName()), e);
		}
	}

	/**
	 * Serialize a record or simple POJO (with getters matching record component names)
	 * to a flat JSON object string.
	 * <p>
	 * Only non-null component values are included. Supported types are String,
	 * Number, Boolean, and LocalDate. Other component types will cause
	 * JsonSerializationException.
	 * </p>
	 *
	 * @param obj the record or POJO instance to serialize
	 * @return a JSON string representing the object
	 * @throws JsonSerializationException if reflection fails or an unsupported type is encountered
	 */
	public static String toJson(Object obj) {
		if (obj instanceof Map map) {
			return toJson(map);
		}

		final StringBuilder sb = new StringBuilder();
		sb.append("{");
		final Class<?> clazz = obj.getClass();

		final RecordComponent[] comps = clazz.getRecordComponents();
		boolean first = true;

		for (RecordComponent rc : comps) {
			try {
				final Object val = clazz.getMethod(rc.getAccessor().getName()).invoke(obj);
				if (val != null) {
					if (!first) {
						sb.append(",");
					}
					sb.append("\"")
					  .append(rc.getName())
					  .append("\":")
					  .append(quote(val));
					first = false;
				}
			} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
				throw new JsonSerializationException(clazz, rc.getName(), e);
			}
		}

		sb.append("}");
		return sb.toString();
	}

	/**
	 * Serialize a Map&lt;String, ?&gt; to a flat JSON object. Each key is used as a JSON property name
	 * and quoted. Values are obtained via {@code toString()}, wrapped in quotes if they are String.
	 * <p>
	 * Example: If the map contains {"a@x":10, "b@y":"hello"}, returns:
	 * {"a@x":10,"b@y":"hello"}
	 * </p>
	 *
	 * @param map the map to serialize (non-null)
	 * @return a JSON string representing the map as an object
	 */
	public static String toJson(Map<String, ?> map) {
		final StringBuilder sb = new StringBuilder();
		sb.append("{");
		boolean first = true;
		for (Map.Entry<String, ?> entry : map.entrySet()) {
			if (!first) sb.append(",");
			first = false;
			String key = entry.getKey();
			Object value = entry.getValue();
			// Quote key
			String keyEscaped = key.replace("\\", "\\\\").replace("\"", "\\\"");
			sb.append("\"").append(keyEscaped).append("\":");
			// Quote value if String, else use toString()
			if (value instanceof String string) {
				String v = string.replace("\\", "\\\\").replace("\"", "\\\"");
				sb.append("\"").append(v).append("\"");
			} else if (value == null) {
				sb.append("null");
			} else {
				sb.append(value.toString());
			}
		}
		sb.append("}");
		return sb.toString();
	}

	// —— helpers —— //
	private static String unquote(String s) {
		s = s.trim();
		if (s.startsWith("\"") && s.endsWith("\"")) {
			return s.substring(1, s.length() - 1)
					.replace("\\\"", "\"")
					.replace("\\\\", "\\");
		}
		return s;
	}

	private static String quote(Object val) {
		switch (val) {
			case null -> {
				return "null";
			}
			case String string -> {
				String s = string.replace("\\", "\\\\")
								 .replace("\"", "\\\"");
				return "\"" + s + "\"";
			}
			case LocalDate localDate -> {
				return "\"" + localDate + "\"";
			}
			default -> {
				return val.toString();
			}
		}
	}

	private static Object convert(String raw, Class<?> target) {
		// strip quotes for strings/dates
		if (target == String.class) {
			return unquote(raw);
		}
		if (target == LocalDate.class) {
			return LocalDate.parse(unquote(raw));
		}
		if (target == Integer.class || target == int.class) {
			return Integer.valueOf(raw);
		}
		if (target == Long.class || target == long.class) {
			return Long.valueOf(raw);
		}
		if (target == Boolean.class || target == boolean.class) {
			return Boolean.valueOf(raw);
		}
		if (target == Double.class || target == double.class) {
			return Double.valueOf(raw);
		}

		throw new IllegalArgumentException("Unsupported type: %s".formatted(target));
	}
}
