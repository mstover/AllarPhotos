package com.lazerinc.servlet.taglib;

public class ForValueTemplateObject extends ValueTemplateObject {
	private static final long serialVersionUID = 1;

	boolean display = false;

	public void setDisplay(boolean d) {
		display = d;
	}

	public void setDisplay(String d) {
		display = Boolean.parseBoolean(d);
	}

	/***************************************************************************
	 * !ToDoo (Method description)
	 * 
	 * @param value
	 *            !ToDo (Parameter description)
	 * @return !ToDo (Return description)
	 **************************************************************************/
	protected Object getContextValue() {
		Object value = validateValue(getCurrentValue());
		if (display && value instanceof Integer) {
			value = new Integer(((Integer) value).intValue() + 1);
		}
		return value;
	}
}