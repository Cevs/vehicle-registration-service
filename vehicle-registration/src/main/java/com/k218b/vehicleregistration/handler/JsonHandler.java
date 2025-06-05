package com.k218b.vehicleregistration.handler;

import com.k218b.vehicleregistration.exception.BadRequestException;
import com.k218b.vehicleregistration.exception.DuplicatedModelException;
import com.k218b.vehicleregistration.response.HttpResponse;
import com.k218b.vehicleregistration.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class JsonHandler<T> implements HttpHandler {

	private static final Logger LOG = Logger.getLogger(JsonHandler.class.getName());
	public static final String CONTENT_TYPE_HEADER_NAME = "Content-Type";
	public static final String CONTENT_TYPE_HEADER_VALUE = "application/json";
	private final Class<T> requestType;

	protected JsonHandler(Class<T> requestType) {
		this.requestType = requestType;
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		try {
			// 1) Deserialize request, unless requestType == Void.class
			T requestObject = null;
			if (!Void.class.equals(requestType)) {
				try (InputStream in = exchange.getRequestBody()) {
					requestObject = JsonUtil.fromJson(in, requestType);
				}
			}

			// 2) Delegate to subclass
			final HttpResponse responseObject = handleRequest(exchange, requestObject);

			// 3) Serialize response
			final byte[] respBytes = JsonUtil.toJson(responseObject)
											 .getBytes(StandardCharsets.UTF_8);
			exchange.getResponseHeaders()
					.add(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE_HEADER_VALUE + "; charset=UTF-8");
			exchange.sendResponseHeaders(responseObject.getHttpResponse(), respBytes.length);

			try (OutputStream out = exchange.getResponseBody()) {
				out.write(respBytes);
			}

		} catch (BadRequestException e) {
			LOG.log(Level.WARNING, "Unsupported request received: {0}", e.getMessage());
			sendError(exchange, 400, e.getMessage());

		} catch (DuplicatedModelException e) {
			sendError(exchange, 409, e.getObject());

		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Issue occurred while handling request: {0}", e.getMessage());
			sendError(exchange, 500, "Internal error: %s".formatted(e.getMessage()));
		}
	}

	private void sendError(HttpExchange ex, int code, String msg) throws IOException {
		final byte[] bytes = ("{\"error\":\"" + msg + "\"}")
				.getBytes(StandardCharsets.UTF_8);
		ex.getResponseHeaders().add(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE_HEADER_VALUE);
		ex.sendResponseHeaders(code, bytes.length);
		try (OutputStream out = ex.getResponseBody()) {
			out.write(bytes);
		}
	}

	private void sendError(HttpExchange ex, int code, Object object) throws IOException {
		byte[] respBytes = JsonUtil.toJson(object).getBytes(StandardCharsets.UTF_8);
		ex.getResponseHeaders().add(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE_HEADER_VALUE);
		ex.sendResponseHeaders(code, respBytes.length);
		try (OutputStream out = ex.getResponseBody()) {
			out.write(respBytes);
		}
	}

	/** Subclasses implement this. */
	protected abstract HttpResponse handleRequest(HttpExchange exchange, T request);
}
