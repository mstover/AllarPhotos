package com.lazerinc.ajaxclient.client.components;

import com.google.gwt.user.client.ui.Label;

public class ObjectLabel extends Label {

	Object userObject;

	public ObjectLabel() {
		// TODO Auto-generated constructor stub
	}

	public ObjectLabel(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public ObjectLabel(String text, boolean wordWrap) {
		super(text, wordWrap);
		// TODO Auto-generated constructor stub
	}

	public ObjectLabel(String text, Object userObject) {
		this(text);
		setUserObject(userObject);
	}

	public ObjectLabel(String text, Object userObject, boolean wordWrap) {
		this(text, wordWrap);
		setUserObject(userObject);
	}

	public Object getUserObject() {
		return userObject;
	}

	public void setUserObject(Object userObject) {
		this.userObject = userObject;
	}

}
