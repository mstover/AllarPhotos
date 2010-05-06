/***********************************************************************************************************************
 * Copyright 1999 (c) Lazer Incorporated All code contained in this file is the property of Lazer Incorporated. All
 * rights reserved.
 **********************************************************************************************************************/

package com.lazerinc.application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaContext;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaDynamic;
import org.coinjema.context.CoinjemaObject;
import org.coinjema.util.Functor;

import com.lazerinc.dbtools.DBConnect;
import com.lazerinc.ecommerce.DatabaseUtilities;

@CoinjemaObject(type = "lazerweb")
public class Controller {
	private transient DatabaseUtilities dbUtil;

	private transient DBConnect database;

	private transient LogModel logModel;

	Logger log;

	public Controller() {
	}

	public Controller(CoinjemaContext ctx) {
	}

	@CoinjemaDynamic(alias = "lazerweb.config")
	private Properties getConfig() {
		return null;
	}

	public Logger getLog() {
		return log;
	}

	@CoinjemaDependency(alias = "log4j")
	public void setLog(Logger log) {
		this.log = log;
	}

	void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
	}

	void readObject(ObjectInputStream ois) throws ClassNotFoundException,
			IOException {
		ois.defaultReadObject();
	}

	/***************************************************************************
	 * Gets a value from the application config file.
	 * 
	 * @param key
	 *            Name of value to get from config file.
	 * @return Config file value.
	 **************************************************************************/
	public String getConfigValue(String key) {
		if (log.isDebugEnabled())
			log.debug("Current context is "
					+ new Functor(this, "getCoinjemaContext").invoke());
		return getConfig().getProperty(key);
	}

	/***************************************************************************
	 * Gets a list of all config properties held by this controller.
	 * 
	 * @return Array of property names.
	 **************************************************************************/
	public String[] getConfigProperties() {
		String[] propNames = (String[]) getProperties().keySet().toArray(
				new String[0]);
		return propNames;
	}

	public Properties getProperties() {
		if (log.isDebugEnabled())
			log.debug("properties = " + getConfig());
		return getConfig();
	}

	public void setProperty(String key, String prop) {
		getConfig().put(key, prop);
	}

}
