package com.k218b.vehicleregistration.handler;

import com.k218b.vehicleregistration.exception.BadRequestException;
import com.k218b.vehicleregistration.model.VehicleRegistration;
import com.k218b.vehicleregistration.response.UserVehicleRegistrationResponse;
import com.k218b.vehicleregistration.service.VehicleRegistrationService;
import com.k218b.vehicleregistration.util.DateUtil;
import com.k218b.vehicleregistration.util.I18nUtil;
import com.sun.net.httpserver.HttpExchange;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

public class UserHandler extends JsonHandler<Void>{

	private static final String HEADER_REG_CODE = "Registration-Code";
	private final VehicleRegistrationService vehicleRegistrationService;

	public UserHandler(final VehicleRegistrationService vehicleRegistrationService) {
		super(Void.class);
		this.vehicleRegistrationService = vehicleRegistrationService;
	}

	@Override
	protected UserVehicleRegistrationResponse handleRequest(final HttpExchange exchange, final Void request) {
		if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
			throw new BadRequestException("HTTP method: %s not supported".formatted(exchange.getRequestMethod()));
		}

		String codeFromHeader = exchange.getRequestHeaders().getFirst(HEADER_REG_CODE);
		if (codeFromHeader == null || codeFromHeader.isBlank()) {
			throw new BadRequestException("Missing required header: %s".formatted(HEADER_REG_CODE));
		}
		codeFromHeader = codeFromHeader.trim();

		final Optional<VehicleRegistration> vehicleRegistrationOptional = vehicleRegistrationService.getVehicleRegistration(codeFromHeader);
		if (vehicleRegistrationOptional.isEmpty()) {
			return new UserVehicleRegistrationResponse(null,
													   I18nUtil.getMessage(Locale.getDefault(), "registration.not.found", codeFromHeader),
													   404);
		}

		final VehicleRegistration vehicleRegistration = vehicleRegistrationOptional.get();
		final LocalDate validUntilDate = vehicleRegistration.validUntil();
		final LocalDate now = LocalDate.now();
		final String messageKey = validUntilDate.isBefore(now) ? "registration.expired" : "registration.valid";
		final String message = I18nUtil.getMessage(Locale.getDefault(), messageKey);

		return new UserVehicleRegistrationResponse(DateUtil.formatIsoDate(validUntilDate), message, 200);

	}

}
