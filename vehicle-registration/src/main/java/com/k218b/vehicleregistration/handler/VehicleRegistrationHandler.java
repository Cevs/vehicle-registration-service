package com.k218b.vehicleregistration.handler;

import com.k218b.vehicleregistration.exception.BadRequestException;
import com.k218b.vehicleregistration.model.VehicleRegistration;
import com.k218b.vehicleregistration.request.VehicleRegistrationRequest;
import com.k218b.vehicleregistration.response.VehicleRegistrationResponse;
import com.k218b.vehicleregistration.service.VehicleRegistrationService;
import com.k218b.vehicleregistration.util.DateUtil;
import com.sun.net.httpserver.HttpExchange;
import java.util.Optional;

public class VehicleRegistrationHandler extends JsonHandler<VehicleRegistrationRequest, VehicleRegistrationResponse> {

	private final VehicleRegistrationService vehicleRegistrationService;

	public VehicleRegistrationHandler(VehicleRegistrationService vehicleRegistrationService) {
		super(VehicleRegistrationRequest.class);
		this.vehicleRegistrationService = vehicleRegistrationService;
	}

	@Override
	protected VehicleRegistrationResponse handleRequest(final HttpExchange exchange, final VehicleRegistrationRequest request) {
		if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
			throw new BadRequestException("HTTP method: %s not supported".formatted(exchange.getRequestMethod()));
		}

		final String registrationCode = request.registrationCode();
		final Optional<VehicleRegistration> vehicleRegistrationOptional = vehicleRegistrationService.getVehicleRegistration(registrationCode);

		if (vehicleRegistrationOptional.isPresent()) {
			return new VehicleRegistrationResponse(false,
												   "Vehicle registration: %s is already added.".formatted(request.registrationCode()));
		}

		final boolean registrationSuccess =
				vehicleRegistrationService.addVehicleRegistration(registrationCode,
																  DateUtil.parseIsoDate(request.validUntil()));

		if (registrationSuccess) {
			return new VehicleRegistrationResponse(true,
												   "Vehicle registration: %s already exists in the system!".formatted(request.registrationCode()));
		}
		return new VehicleRegistrationResponse(false,
											   "Vehicle registration: %s is not added.".formatted(request.registrationCode()));
	}

}
