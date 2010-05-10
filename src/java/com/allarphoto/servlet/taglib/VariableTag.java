package com.allarphoto.servlet.taglib;

import javax.servlet.jsp.tagext.TagSupport;

/**
 * Title: Lazerweb Description: Lazerweb - Lazer Inc. version 3.0 Copyright:
 * Copyright (c) 2001 Company: Lazer Inc.
 * 
 * @author Michael Stover
 * @version 1.0
 */

public class VariableTag extends TagSupport {
	private static final long serialVersionUID = 1;

	private String name;

	private Object object;

	public int doStartTag() {
		pageContext.setAttribute(name, object);
		return SKIP_BODY;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setObject(Object o) {
		object = o;
	}

	public void setObject(String s) {
		object = s;
	}
}