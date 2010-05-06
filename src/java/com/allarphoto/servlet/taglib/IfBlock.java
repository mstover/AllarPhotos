package com.lazerinc.servlet.taglib;

import java.io.IOException;
import java.lang.reflect.Method;

import com.lazerinc.utils.Functions;

/**
 * Title: Description: Copyright: Copyright (c) 2000 Company:
 * 
 * @author
 * @version 1.0
 */

public class IfBlock extends ObjectCallTemplateObject {
	private static final long serialVersionUID = 1;

	protected boolean cond = true;

	protected boolean thisObject = false;

	protected Object equals = null;

	public IfBlock() {
	}

	public void setObject(String on) {
		thisObject = true;
		if (on == null) {
			cond = false;
			return;
		}
		super.setObject(on);
	}

	public void setEquals(String e) {
		if (e == null) {
			return;
		}
		equals = validateValue(getContextValue(e), e);
	}

	public void setEquals(Object e) {
		if (e instanceof String) {
			setEquals((String) e);
			return;
		}
		equals = e;

	}

	public void setObject(Object o) {
		thisObject = true;
		super.setObject(o);
		if (object == null) {
			cond = false;
		}

	}

	protected boolean isThisObject() {
		return thisObject;
	}

	public boolean isConditionTrue() {
		return cond;
	}

	public void setBoolean(boolean b) {
		Functions.javaLog("setBoolean() : boolean = " + b);
		thisObject = true;
		cond = b;
	}

	public void setBoolean(String b) {
		thisObject = true;
		cond = Boolean.parseBoolean(b);
	}

	public int doStartTag() {
		if (methodName == null) {
			return EVAL_BODY_TAG;
		}
		try {
			Method method = getMethod(object, methodName, classes);
			Object result = method.invoke(object, args);
			if (equals != null) {
				cond = equals.equals(result);
			} else if (result.toString().equals("true"))
				cond = true;
			else
				cond = false;
		} catch (Exception e) {
			cond = false;
		}
		return EVAL_BODY_TAG;
	}

	public int doAfterBody() {
		try {
			bodyContent.writeOut(bodyContent.getEnclosingWriter());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}
}