package com.allarphoto.category;

public class TagField extends CategoryField {

	public TagField() {
		super();
	}

	public TagField(String family, String n, int d, int s) {
		super(family, n, d, s, TAG);
	}

	public TagField(String family, String n, int d) {
		super(family, n, d, TAG);
	}

}
