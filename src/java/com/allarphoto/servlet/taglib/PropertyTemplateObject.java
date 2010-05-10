package com.allarphoto.servlet.taglib;

import java.lang.reflect.Method;

public class PropertyTemplateObject extends TemplateObject {
	private static final long serialVersionUID = 1;

	String property;

	/***************************************************************************
	 * !ToDo (Method description)
	 * 
	 * @return !ToDo (Return description)
	 **************************************************************************/
	public int doAfterBody() {
		Object value = getCurrentValue();
		try {
			Method method = value.getClass().getMethod(property, new Class[0]);
			bodyContent.getEnclosingWriter()
					.write(
							encodeString(method.invoke(value, new Object[0])
									.toString()));
		} catch (Exception e) {
			writeNullText();
		}
		return SKIP_BODY;
	}

	/***************************************************************************
	 * !ToDo (Method description)
	 * 
	 * @param p
	 *            !ToDo (Parameter description)
	 **************************************************************************/
	public void setProperty(String p) {
		if (p.length() > 1)
			property = p.substring(0, 1).toUpperCase() + p.substring(1);

		else
			property = p.toUpperCase();
		property = "get" + property;

	}
}
