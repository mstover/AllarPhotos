package com.allarphoto.ecommerce;

import org.coinjema.context.CoinjemaDynamic;

public class ExpiredImage extends CommerceProduct {
	private static final long serialVersionUID = 1;

	public ExpiredImage() {
		setId(-1);
	}

	@Override
	@CoinjemaDynamic(alias = "expiredImage.filename")
	public String getPrimary() {
		return null;
	}

	@CoinjemaDynamic(alias = "expiredImage.directory")
	private String getThumbnail() {
		return null;
	}

	@Override
	public Object getValue(String valueName) {
		if (valueName.equals("Thumbnail Directory"))
			return getThumbnail() + "/" + getPrimary();
		return super.getValue(valueName);
	}

}
