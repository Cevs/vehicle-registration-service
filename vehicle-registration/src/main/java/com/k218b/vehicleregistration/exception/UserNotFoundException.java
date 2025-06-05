package com.k218b.vehicleregistration.exception;

public class UserNotFoundException extends RuntimeException {

	public UserNotFoundException(String message, Throwable t) {
		super(message, t);
	}

}
