package com.k218b.vehicleregistration.response;

public class OpenAccountResponse extends HttpResponse{
	private final boolean success;
	private final String message;
	private final String password;

	private OpenAccountResponse(boolean success, String message, String password, int statusCode) {
		super(statusCode);
		this.success = success;
		this.message = message;
		this.password = password;
	}

	public boolean success() {
		return success;
	}

	public String message() {
		return message;
	}
	public String password() {
		return password;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private boolean success;
		private String message;
		private String password;
		private int statusCode;

		private Builder() { }

		public Builder success(boolean success) {
			this.success = success;
			return this;
		}

		public Builder message(String message) {
			this.message = message;
			return this;
		}

		public Builder password(String password) {
			this.password = password;
			return this;
		}

		public Builder statusCode(int statusCode) {
			this.statusCode = statusCode;
			return this;
		}

		public OpenAccountResponse build() {
			return new OpenAccountResponse(success, message, password, statusCode);
		}
	}
}
