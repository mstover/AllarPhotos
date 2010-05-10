package com.allarphoto.category;

public class ProtectedField extends CategoryField {
	private static final long serialVersionUID = 1;

	public ProtectedField(String family, String n, int d, int s) {
		super(family, n, d, s, PROTECTED);
	}

	public ProtectedField() {
		super();
	}
}
