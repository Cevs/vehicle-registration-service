package com.k218b.vehicleregistration.exception;

public class DuplicatedModelException extends RuntimeException {

	private final Object object;

	public DuplicatedModelException(Object object) {
		this.object = object;
	}

	public Object getObject() {
		return object;
	}

}
