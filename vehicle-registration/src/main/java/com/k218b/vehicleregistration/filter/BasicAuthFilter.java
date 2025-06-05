package com.k218b.vehicleregistration.filter;

import com.k218b.vehicleregistration.context.SessionContext;
import com.k218b.vehicleregistration.model.User;
import com.k218b.vehicleregistration.service.UserService;
import com.k218b.vehicleregistration.util.CryptoUtil;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

/**
 * A Filter that enforces HTTP Basic authentication against our UserService.
 * <p>
 * It expects the request to contain:
 *     Authorization: Basic base64(accountId:password)
 * It retrieves the stored encrypted password and salt for that accountId,
 * it hashes the provided password and compares it to the stored hashed password it, and compares to the provided password.
 * If they match, the request is allowed to proceed; otherwise, a 401 response is sent.
 * </p>
 */
public class BasicAuthFilter extends Filter {
	private final UserService userService;

	public BasicAuthFilter(final UserService userService) {
		this.userService = userService;
	}

	@Override
	public String description() {
		return "Enforces Basic HTTP authentication against registered users";
	}

	@Override
	public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
		try {
			internalDoFilter(exchange, chain);
		} finally {
			SessionContext.clear();
		}

	}

	private void internalDoFilter(HttpExchange exchange, Chain chain) throws IOException {
		String authHeader = exchange.getRequestHeaders().getFirst("Authorization");

		// 1. Verify header presence and format
		if (!isValidAuthHeader(authHeader)) {
			sendUnauthorized(exchange);
			return;
		}

		// 2. Extract Base64 credentials
		final String base64Credentials = authHeader.substring("Basic ".length()).trim();
		final String decodedCredentials = decodeBase64(base64Credentials);
		if (decodedCredentials == null) {
			sendUnauthorized(exchange);
			return;
		}

		// 3. Split into accountId and password
		final String[] parts = splitCredentials(decodedCredentials);
		if (parts == null) {
			sendUnauthorized(exchange);
			return;
		}
		final String accountId = parts[0];
		final String presentedPassword = parts[1];


		// 4) Fetch stored hash and salt, re‐hash presented, compare
		// If valida, load a full User object
		User user = authenticateAndLoadUser(accountId, presentedPassword);
		if (user == null) {
			sendUnauthorized(exchange);
			return;
		}

		// 5. Auth succeeded; proceed
		SessionContext.setCurrentUser(user);
		chain.doFilter(exchange);
	}

	private boolean isValidAuthHeader(String authHeader) {
		return authHeader != null && authHeader.startsWith("Basic ");
	}

	private String decodeBase64(String base64Value) {
		try {
			byte[] decoded = Base64.getDecoder().decode(base64Value);
			return new String(decoded, StandardCharsets.UTF_8);
		} catch (IllegalArgumentException _) {
			return null;
		}
	}

	private String[] splitCredentials(String decoded) {
		int idx = decoded.indexOf(':');
		if (idx < 0) {
			return null;
		}
		final String accountId = decoded.substring(0, idx);
		final String password  = decoded.substring(idx + 1);
		if (accountId.isBlank() || password.isBlank()) {
			return null;
		}
		return new String[] { accountId, password };
	}

	private User authenticateAndLoadUser(String accountId, String password) {
		String storedHashedPassword;
		String storedSalt;
		try {
			final Optional<User> userOptional = userService.getUser(accountId);
			if (userOptional.isEmpty()) {
				return null;
			}
			final User user = userOptional.get();
			storedHashedPassword = user.password();
			storedSalt = user.salt();

		} catch (Exception _) {
			// Unexpected error (e.g. data access)
			return null;
		}

		if (!storedHashedPassword.equals(CryptoUtil.hashPassword(password, storedSalt))) {
			return null;
		}
		return userService.getUser(accountId).orElse(null);
	}

	private void sendUnauthorized(HttpExchange exchange) throws IOException {
		exchange.getResponseHeaders().set("WWW-Authenticate", "Basic realm=\"VehicleRegistration\"");
		exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");

		final String responseBody = "{\"error\":\"Invalid credentials\"}";
		final byte[] bytes = responseBody.getBytes(StandardCharsets.UTF_8);

		exchange.sendResponseHeaders(401, bytes.length);

		try (var os = exchange.getResponseBody()) {
			os.write(bytes);
		}
	}
}
