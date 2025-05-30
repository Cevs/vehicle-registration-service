package com.k218b.vehicleregistration.handler;

import com.k218b.vehicleregistration.exception.BadRequestException;
import com.k218b.vehicleregistration.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class JsonHandler<T, R> implements HttpHandler {

	private static final Logger LOG = Logger.getLogger(JsonHandler.class.getName());
	private final Class<T> requestType;

	protected JsonHandler(Class<T> requestType) {
		this.requestType = requestType;
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		// 1) Deserialize request
		try (InputStream in = exchange.getRequestBody()) {
			final T requestObject = JsonUtil.fromJson(in, requestType);

			// 2) Delegate to subclass
			final R responseObject = handleRequest(exchange, requestObject);

			// 3) Serialize response
			byte[] respBytes = JsonUtil.toJson(responseObject)
									   .getBytes(StandardCharsets.UTF_8);
			exchange.getResponseHeaders()
					.add("Content-Type", "application/json; charset=UTF-8");
			exchange.sendResponseHeaders(200, respBytes.length);
			try (OutputStream out = exchange.getResponseBody()) {
				out.write(respBytes);
			}
		} catch (BadRequestException e) {
			LOG.log(Level.WARNING, "Unsupported request received: %s ".formatted(e.getMessage()), e);
			sendError(exchange, 400, e.getMessage());
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Issue occurred while handling request: %s ".formatted(e.getMessage()), e);
			sendError(exchange, 500, "Internal error: " + e.getMessage());

		}
	}

	private void sendError(HttpExchange ex, int code, String msg) throws IOException {
		final byte[] bytes = ("{\"error\":\"" + msg + "\"}")
				.getBytes(StandardCharsets.UTF_8);
		ex.getResponseHeaders().add("Content-Type", "application/json");
		ex.sendResponseHeaders(code, bytes.length);
		try (OutputStream out = ex.getResponseBody()) {
			out.write(bytes);
		}
	}

	/** Subclasses implement this. */
	protected abstract R handleRequest(HttpExchange exchange, T request);
}
