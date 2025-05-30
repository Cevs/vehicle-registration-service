package com.k218b.vehicleregistration.handler;

import com.k218b.vehicleregistration.exception.BadRequestException;
import com.k218b.vehicleregistration.exception.DuplicatedAccountException;
import com.k218b.vehicleregistration.factory.OpenAccountResponseFactory;
import com.k218b.vehicleregistration.model.User;
import com.k218b.vehicleregistration.request.CreateAccountRequest;
import com.k218b.vehicleregistration.response.OpenAccountResponse;
import com.k218b.vehicleregistration.service.UserService;
import com.sun.net.httpserver.HttpExchange;

public class AccountHandler extends JsonHandler<CreateAccountRequest, OpenAccountResponse> {

	private final UserService userService;

	public AccountHandler(UserService userService) {
		super(CreateAccountRequest.class);
		this.userService = userService;
	}

	@Override
	protected OpenAccountResponse handleRequest(final HttpExchange exchange, final CreateAccountRequest request) {
		if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
			throw new BadRequestException("Only POST allowed");
		}

		try {
			final User user = userService.openAccount(request.accountId());
			return OpenAccountResponseFactory.success(exchange, user);
		} catch (DuplicatedAccountException _) {
			return OpenAccountResponseFactory.duplicate(exchange, request.accountId());
		}
	}

}
