/*******************************************************************************
 * Copyright (C) 1999 Michael Stover This program is free software; you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version. This program is distributed
 * in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details. You should have received
 * a copy of the GNU General Public License along with this program; if not,
 * write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA. Michael Stover can be reached via email at
 * mstover1@rochester.rr.com or via snail mail at 130 Corwin Rd. Rochester, NY
 * 14610 The following exception to this license exists for Lazer Incorporated:
 * Lazer Inc is excepted from all limitations and requirements stipulated in the
 * GPL. Lazer Inc. is the only entity allowed this limitation. Lazer does have
 * the right to sell this exception, if they choose, but they cannot grant
 * additional exceptions to any other entities.
 ******************************************************************************/

// Title: WebBean
// Author: Michael Stover
// Company: Lazer inc.
package com.allarphoto.utils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import strategiclibrary.service.webaction.HandlerData;
import strategiclibrary.util.Converter;

public class WebBean {

	/***************************************************************************
	 * Takes as input a bean and a ServletHttpRequest object. It automatically
	 * introspects the bean and sets the properties that match with the request
	 * object's input parameters.
	 * 
	 * @param bean
	 *            Bean to introspect.
	 * @param request
	 *            ServletRequest object.
	 **************************************************************************/
	public static <T> T setValues(T bean, HttpServletRequest request) {
		if (bean == null || request == null)
			throw new RuntimeException("BadBean");
		Map<Class, Map<String, PropertyMethods>> propertyNames = new HashMap<Class, Map<String, PropertyMethods>>();
		propertyNames.put(bean.getClass(), introspectProperties(bean));
		String className = bean.getClass().getSimpleName() + ".";
		Enumeration names = request.getParameterNames();
		while (names.hasMoreElements()) {
			try {
				String param = (String) names.nextElement();
				String paramTemp = null;
				if (param.startsWith(className))
					paramTemp = param.substring(className.length());
				else
					paramTemp = param.toLowerCase();
				paramTemp = paramTemp.toLowerCase();
				String[] parts = paramTemp.split("\\.");
				int i = 0;
				Object obj = bean;
				if (propertyNames.get(bean.getClass()).containsKey(parts[i])) {
					while (i + 1 < parts.length) {
						obj = createOrGet(parts[i], obj, propertyNames);
						i++;
					}
					Object[] args = { request.getParameter(param) };
					if (args.length > 1)
						throw new RuntimeException("BadBean");
					setValueInBean(obj, args, parts[i], propertyNames);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return bean;
	}

	private static void convertArgument(Method method, Object[] args) {
		Class type = method.getParameterTypes()[0];
		args[0] = Converter.convert(args[0], type);
	}

	/***************************************************************************
	 * Takes as input a bean and a HandlerData object. It automatically
	 * introspects the bean and sets the properties that match with the request
	 * object's input parameters.
	 * 
	 * @param bean
	 *            Bean to introspect.
	 * @param request
	 *            ServletRequest object.
	 **************************************************************************/
	public static <T> T setValues(T bean, HandlerData request) {
		if (bean == null || request == null)
			throw new RuntimeException("BadBean");
		Map<Class, Map<String, PropertyMethods>> propertyNames = new HashMap<Class, Map<String, PropertyMethods>>();
		propertyNames.put(bean.getClass(), introspectProperties(bean));
		String className = bean.getClass().getSimpleName() + ".";
		for (String param : request.getParamNames()) {
			try {
				String paramTemp = null;
				if (param.startsWith(className))
					paramTemp = param.substring(className.length());
				else
					paramTemp = param.toLowerCase();
				paramTemp = paramTemp.toLowerCase();
				String[] parts = paramTemp.split("\\.");
				int i = 0;
				Object obj = bean;
				if (propertyNames.get(bean.getClass()).containsKey(parts[i])) {
					while (i + 1 < parts.length) {
						obj = createOrGet(parts[i], obj, propertyNames);
						i++;
					}
					Object[] args = { request.getParameter(param) };
					if (args.length > 1)
						throw new RuntimeException("BadBean");
					setValueInBean(obj, args, parts[i], propertyNames);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return bean;
	}

	static private void setValueInBean(Object bean, Object[] args, String prop,
			Map<Class, Map<String, PropertyMethods>> propertyNames)
			throws Exception {
		if (!propertyNames.containsKey(bean.getClass())) {
			propertyNames.put(bean.getClass(), introspectProperties(bean));
		}
		convertArgument(propertyNames.get(bean.getClass()).get(prop).setter,
				args);
		try {
			propertyNames.get(bean.getClass()).get(prop).setter.invoke(bean,
					args);
		} catch (RuntimeException e) {
			throw new RuntimeException("Failed to set property named " + prop
					+ " on bean " + bean, e);
		}
	}

	static private Object createOrGet(String prop, Object bean,
			Map<Class, Map<String, PropertyMethods>> propertyNames)
			throws Exception {
		if (!propertyNames.containsKey(bean.getClass())) {
			propertyNames.put(bean.getClass(), introspectProperties(bean));
		}
		Method getter = propertyNames.get(bean.getClass()).get(prop).getter;
		Object subObj = getter.invoke(bean);
		if (subObj == null) {
			try {
				subObj = getter.getReturnType().newInstance();
				propertyNames.get(bean.getClass()).get(prop).setter.invoke(
						bean, subObj);
			} catch (Exception e) {
				subObj = null;
			}
		}
		return subObj;
	}

	private static Map<String, PropertyMethods> introspectProperties(Object bean) {
		Map<String, PropertyMethods> desc = new HashMap<String, PropertyMethods>();
		PropertyDescriptor[] props = null;
		try {
			props = Introspector.getBeanInfo(bean.getClass())
					.getPropertyDescriptors();
		} catch (IntrospectionException e) {
			throw new RuntimeException(e);
		}
		Method meth = null;
		for (int x = 0; x < props.length; x++) {
			PropertyMethods meths = new PropertyMethods(null, null);
			if ((meth = props[x].getWriteMethod()) != null)
				meths.setter = meth;
			if ((meth = props[x].getReadMethod()) != null)
				meths.getter = meth;
			if (meths.getter != null || meths.setter != null)
				desc.put(props[x].getName().toLowerCase(), meths);
		}
		return desc;
	}

	static class PropertyMethods {
		Method getter;

		Method setter;

		public PropertyMethods(Method getter, Method setter) {
			this.getter = getter;
			this.setter = setter;
		}
	}
}
