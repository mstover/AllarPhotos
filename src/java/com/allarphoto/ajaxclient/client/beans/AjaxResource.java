package com.lazerinc.ajaxclient.client.beans;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AjaxResource implements IsSerializable, Comparable {
	public static final int DATATABLE = 3;

	public static final int GROUP = 1;

	public static final int MERCHANT = 4;

	public static final int PROTECTED_FIELD = 7;

	public static final int EXPIRED_ITEMS = 8;

	public static final int DATABASE = 6;

	String name;

	int type;

	public AjaxResource() {
	}

	public AjaxResource(String n, int t) {
		name = n;
		type = t;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int compareTo(Object o) {
		AjaxResource other = (AjaxResource) o;
		return name.compareTo(other.getName());
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((name == null) ? 0 : name.hashCode());
		result = PRIME * result + type;
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		final AjaxResource other = (AjaxResource) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

}
