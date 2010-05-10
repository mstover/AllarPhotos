package com.allarphoto.client.exceptions;

public class FatalException extends LazerwebException {
	private static final long serialVersionUID = 1;

	public FatalException() {
		super(FatalException.class.getName());
	}

	public FatalException(String message) {
		super(message);
	}
}