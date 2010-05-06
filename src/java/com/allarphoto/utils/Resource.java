// Title: Commerce
// Version:
// Copyright: Copyright (c) 1999
// Author: Michael Stover
// Company: Lazer inc.
// Description: Your description

package com.lazerinc.utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

import com.lazerinc.cached.DatabaseObject;
import com.lazerinc.server.ResourceService;

@CoinjemaObject
public class Resource implements Serializable, Comparable<Resource>,
		DatabaseObject {
	ResourceService resService;

	private static final long serialVersionUID = 1;

	/***************************************************************************
	 * Resource Type value.
	 **************************************************************************/
	public static final int GROUP = 1;

	/***************************************************************************
	 * Resource Type value.
	 **************************************************************************/
	public static final int USER = 2;

	/***************************************************************************
	 * Resource Type value.
	 **************************************************************************/
	public static final int DATATABLE = 3;
	
	public static final int VIRT_DATATABLE = 9;

	/***************************************************************************
	 * Resource Type value.
	 **************************************************************************/
	public static final int MERCHANT = 4;

	/***************************************************************************
	 * Resource Type value.
	 **************************************************************************/
	public static final int PRICEKEY = 5;

	/***************************************************************************
	 * Resource Type value.
	 **************************************************************************/
	public static final int DATABASE = 6;

	/***************************************************************************
	 * Resource Type value.
	 **************************************************************************/
	public static final int PROTECTED_FIELD = 7;

	public static final int EXPIRED_ITEMS = 8;

	public Resource(String name, int type) {
		setName(name);
		setType(type);
	}

	public Resource(int id, String name, int type) {
		this(name, type);
		setId(id);
	}

	public Resource() {
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((name == null) ? 0 : name.hashCode());
		result = PRIME * result + type;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Resource other = (Resource) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	public int compareTo(Resource r) {
		if (r.getId() == getId())
			return 0;
		int res = getName().compareTo(r.getName());
		if (res == 0)
			res = getType() - r.getType();
		return res;
	}

	void readObject(ObjectInputStream ois) throws ClassNotFoundException,
			IOException {
		ois.defaultReadObject();
	}

	void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
	}

	public String getName() {
		return name;
	}

	public int getType() {
		return type;
	}

	public void update() {
		resService.update(this);
	}

	private String name;

	private int type;

	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(int type) {
		this.type = type;
	}

	@CoinjemaDependency(type = "resourceService")
	public void setResourceService(ResourceService rs) {
		resService = rs;
	}
}
