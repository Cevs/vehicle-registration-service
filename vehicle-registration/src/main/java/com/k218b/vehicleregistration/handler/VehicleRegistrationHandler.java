package com.k218b.vehicleregistration.handler;

import com.k218b.vehicleregistration.exception.BadRequestException;
import com.k218b.vehicleregistration.exception.DuplicatedModelException;
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
			final var vehicleRegistrationResponse =
					new VehicleRegistrationResponse(false,
													"Provided vehicle registration code: %s already exists.".formatted(request.registrationCode()));
			throw new DuplicatedModelException(vehicleRegistrationResponse);
		}

		vehicleRegistrationService.addVehicleRegistration(registrationCode, DateUtil.parseIsoDate(request.validUntil()));

		return new VehicleRegistrationResponse(true,
											   "Vehicle registration: %s already exists in the system!".formatted(request.registrationCode()));

	}

}
