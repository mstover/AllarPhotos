package com.allarphoto.client.exceptions;

/*
 * This class is being setup for use with the Intimate Apparel Web-Portal. If a user does not have permission to access
 * a sub-library, then this exception is thrown.
 */

public class InvalidPermission extends LazerwebException {
	private static final long serialVersionUID = 1;

	public InvalidPermission() {
		super(InvalidPermission.class.getName());
	}

	public InvalidPermission(String message) {
		super(message);
	}
}