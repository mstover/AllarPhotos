package com.lazerinc.servlet.taglib;

import java.lang.reflect.Method;

public class MethodTemplateObject extends ObjectCallTemplateObject {
	private static final long serialVersionUID = 1;

	public int doAfterBody() {
		Object value = getCurrentValue();
		try {
			Method method = getMethod(value, methodName, classes);
			bodyContent.getEnclosingWriter().write(
					encodeString(method.invoke(value, args).toString()));
		} catch (Exception e) {
			writeNullText();
		}
		return SKIP_BODY;
	}
}