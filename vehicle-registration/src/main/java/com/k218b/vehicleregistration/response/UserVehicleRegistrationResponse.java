package com.k218b.vehicleregistration.response;

public class UserVehicleRegistrationResponse extends HttpResponse{
	private final String validUntil;
	private final String message;

	public UserVehicleRegistrationResponse(String validUntil, String message, int statusCode) {
		super(statusCode);
		this.validUntil = validUntil;
		this.message = message;
	}

	public String validUntil() {
		return validUntil;
	}

	public String message() {
		return message;
	}
}
