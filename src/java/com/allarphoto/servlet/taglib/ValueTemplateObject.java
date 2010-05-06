package com.lazerinc.servlet.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;

public class ValueTemplateObject extends TemplateObject {
	private static final long serialVersionUID = 1;

	/***************************************************************************
	 * !ToDo (Constructor description)
	 **************************************************************************/
	public ValueTemplateObject() {
	}

	/***************************************************************************
	 * !ToDo (Method description)
	 * 
	 * @return !ToDo (Return description)
	 **************************************************************************/
	public int doEndTag() throws JspException {
		Object value = validateValue(getCurrentValue());
		try {
			bodyContent.getEnclosingWriter().write(
					encodeString(value.toString()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return super.doEndTag();
	}
}
