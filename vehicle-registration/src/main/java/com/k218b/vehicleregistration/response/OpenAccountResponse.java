package com.k218b.vehicleregistration.response;

public record OpenAccountResponse(
		boolean success,
		String message,
		String password
) {
	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private boolean success;
		private String message;
		private String password;

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

		public OpenAccountResponse build() {
			return new OpenAccountResponse(success, message, password);
		}
	}
}
