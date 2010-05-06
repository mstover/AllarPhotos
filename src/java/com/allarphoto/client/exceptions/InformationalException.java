package com.lazerinc.client.exceptions;

public class InformationalException extends LazerwebException {
	private static final long serialVersionUID = 1;

	public InformationalException() {
		super(InformationalException.class.getName());
	}

	public InformationalException(String message) {
		super(message);
	}
}