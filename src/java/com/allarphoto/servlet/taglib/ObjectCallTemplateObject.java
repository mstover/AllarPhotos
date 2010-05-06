package com.lazerinc.servlet.taglib;

import java.lang.reflect.Method;

import com.lazerinc.utils.Functions;

/*******************************************************************************
 * Title: Description: Copyright: Copyright (c) 2000 Company:
 * 
 * @author
 * @created $Date: 2007/07/17 15:26:21 $
 * @version 1.0
 ******************************************************************************/

public class ObjectCallTemplateObject extends TemplateObject {
	private static final long serialVersionUID = 1;

	/***************************************************************************
	 * !ToDo (Field description)
	 **************************************************************************/
	protected Object[] args = new Object[0];

	/***************************************************************************
	 * !ToDo (Field description)
	 **************************************************************************/
	protected String methodName;

	/***************************************************************************
	 * !ToDo (Field description)
	 **************************************************************************/
	protected Object object;

	protected Class[] classes = new Class[0];

	int index = -1;

	/***************************************************************************
	 * !ToDo (Method description)
	 * 
	 * @param on
	 *            !ToDo (Parameter description)
	 **************************************************************************/
	public void setObject(String on) {
		if (on == null) {
			object = null;
			return;
		}
		object = getContextValue(on);
		if (object == null) {
			object = pageContext.findAttribute(on);
			if (object == null)
				try {
					object = Class.forName(on);
				} catch (ClassNotFoundException e) {
					object = on;
				}
		}
	}

	public void setObject(Object o) {
		if (o instanceof String) {
			setObject((String) o);
		} else {
			object = o;
		}
	}

	/***************************************************************************
	 * !ToDo (Method description)
	 * 
	 * @param mn
	 *            !ToDo (Parameter description)
	 **************************************************************************/
	public void setMethod(String mn) {
		methodName = mn;
	}

	/***************************************************************************
	 * !ToDo (Method description)
	 * 
	 * @param a
	 *            !ToDo (Parameter description)
	 **************************************************************************/
	public void setArguments(String a) {
		String[] tempArgs = Functions.split(a, ",");
		args = new Object[tempArgs.length];
		for (int i = 0; i < tempArgs.length; i++) {
			args[i] = tempArgs[i];
		}
		classes = new Class[args.length];
		for (int i = 0; i < classes.length; i++) {
			Object val = getContextValue(tempArgs[i]);
			if (val != null) {
				args[i] = val;
			}
			classes[i] = args[i].getClass();
		}
	}

	/***************************************************************************
	 * !ToDo (Method description)
	 * 
	 * @return !ToDo (Return description)
	 **************************************************************************/
	public int doAfterBody() {
		try {
			Method method = getMethod(object, methodName, classes);
			bodyContent.getEnclosingWriter().write(
					encodeString(method.invoke(object, args).toString()));
		} catch (Exception e) {
			writeNullText();
		}
		return SKIP_BODY;
	}

	/***************************************************************************
	 * !ToDoo (Method description)
	 * 
	 * @return !ToDo (Return description)
	 **************************************************************************/
	protected Method getMethod(Object object, String methodName, Class[] classes)
			throws NoSuchMethodException {
		Method method;
		try {
			if (object instanceof Class)
				method = ((Class) object).getMethod(methodName, classes);
			else
				method = object.getClass().getMethod(methodName, classes);
		} catch (NoSuchMethodException e) {
			Method[] methods;
			if (object instanceof Class)
				methods = ((Class) object).getMethods();
			else
				methods = object.getClass().getMethods();
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].getName().equals(methodName)) {
					Class[] paramTypes = methods[i].getParameterTypes();
					boolean rightMethod = (paramTypes.length == classes.length);
					for (int j = 0; j < paramTypes.length && j < classes.length; j++) {
						if (!paramTypes[j].isAssignableFrom(classes[j])) {
							rightMethod = false;
							break;
						}
					}
					if (rightMethod) {
						return methods[i];
					}
				}
			}
			throw new NoSuchMethodException();
		}
		return method;
	}
}
