package com.k218b.vehicleregistration.handler;

import com.k218b.vehicleregistration.exception.BadRequestException;
import com.k218b.vehicleregistration.request.StatisticsRequest;
import com.k218b.vehicleregistration.service.VehicleRegistrationService;
import com.sun.net.httpserver.HttpExchange;
import java.util.Map;

public class StatisticsHandler extends JsonHandler<StatisticsRequest, Map<String, Integer>> {

	private final VehicleRegistrationService vehicleRegistrationService;

	public StatisticsHandler(final VehicleRegistrationService vehicleRegistrationService) {
		super(StatisticsRequest.class);
		this.vehicleRegistrationService = vehicleRegistrationService;
	}

	@Override
	protected Map<String, Integer> handleRequest(final HttpExchange exchange, final StatisticsRequest request) {
		if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
			throw new BadRequestException("HTTP method: %s not supported".formatted(exchange.getRequestMethod()));
		}
		return vehicleRegistrationService.getVehicleRegistrationPerUser();
	}

}
