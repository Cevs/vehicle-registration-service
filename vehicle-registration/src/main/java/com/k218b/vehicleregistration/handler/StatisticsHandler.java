package com.k218b.vehicleregistration.handler;

import com.k218b.vehicleregistration.service.VehicleRegistrationService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import static com.k218b.vehicleregistration.handler.JsonHandler.CONTENT_TYPE_HEADER_NAME;
import static com.k218b.vehicleregistration.handler.JsonHandler.CONTENT_TYPE_HEADER_VALUE;

public class StatisticsHandler implements HttpHandler {

	private final VehicleRegistrationService vehicleRegistrationService;

	public StatisticsHandler(final VehicleRegistrationService vehicleRegistrationService) {
		this.vehicleRegistrationService = vehicleRegistrationService;
	}

	@Override
	public void handle(final HttpExchange exchange) throws IOException {
		final Map<String, Integer> registrationsPerUser = vehicleRegistrationService.getVehicleRegistrationPerUser();
		final byte[] respBytes = registrationsPerUser.toString().getBytes(StandardCharsets.UTF_8);
		exchange.getResponseHeaders()
				.add(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE_HEADER_VALUE + "; charset=UTF-8");
		exchange.sendResponseHeaders(200, respBytes.length);
		try (OutputStream out = exchange.getResponseBody()) {
			out.write(respBytes);
		}
	}

}
