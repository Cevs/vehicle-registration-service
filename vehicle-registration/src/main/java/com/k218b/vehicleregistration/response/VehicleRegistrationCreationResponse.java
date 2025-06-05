package com.k218b.vehicleregistration.response;

public class VehicleRegistrationCreationResponse extends HttpResponse {
	private final boolean success;
	private final String description;

	public VehicleRegistrationCreationResponse(boolean success, String description, int statusCode) {
		super(statusCode);
		this.success = success;
		this.description = description;
	}

	public boolean success() {
		return success;
	}

	public String description() {
		return description;
	}
}
