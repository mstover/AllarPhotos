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

// Title: DBKey
// Author: Michael Stover
// Company: Lazer Inc.
package com.allarphoto.dbtools;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class DBKey implements Serializable {
	private static final long serialVersionUID = 1;

	public DBKey() {
	}

	void readObject(ObjectInputStream ois) throws ClassNotFoundException,
			IOException {
		ois.defaultReadObject();
	}

	void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
	}

	public void setUrl(String newUrl) {
		url = newUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUsername(String newUsername) {
		username = newUsername;
	}

	public String getUsername() {
		return username;
	}

	public void setPassword(String newPassword) {
		password = newPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setDriver(String newDriver) {
		driver = newDriver;
	}

	public String getDriver() {
		return driver;
	}

	public void setMaxUsage(int newMaxUsage) {
		maxUsage = newMaxUsage;
	}

	public int getMaxUsage() {
		return maxUsage;
	}

	public void setMaxConnections(int newMaxConnections) {
		maxConnections = newMaxConnections;
	}

	public int getMaxConnections() {
		return maxConnections;
	}

	/***************************************************************************
	 * Determines if the two DBKey objects have the same property values.
	 * 
	 * @param key
	 *            DBKey to compare with this one.
	 * @return bool True if equal, false otherwise.
	 **************************************************************************/
	public boolean equals(Object key) {
		if (key instanceof DBKey)
			return url.equals(((DBKey) key).getUrl());
		else
			return false;
	}

	public int hashCode() {
		return url.hashCode() * 11;
	}

	private String url;

	private String username;

	private String password;

	private String driver;

	private int maxUsage;

	private int maxConnections;
}
