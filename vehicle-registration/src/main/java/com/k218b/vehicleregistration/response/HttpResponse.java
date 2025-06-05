package com.k218b.vehicleregistration.response;

public abstract class HttpResponse {
	final int responseCode;

	protected HttpResponse(final int responseCode) {
		this.responseCode = responseCode;
	}

	public int getHttpResponse() {
		return responseCode;
	}

}
