package com.allarphoto.beans;

import java.io.Serializable;

import com.allarphoto.cached.DatabaseObject;

public class Industry implements Comparable<Industry>, Serializable,
		DatabaseObject {
	private static final long serialVersionUID = 1;

	String name;

	int id;

	public Industry() {
		super();
	}

	public Industry(String n) {
		name = n;
	}

	public int compareTo(Industry o) {
		int ret = getName().compareTo(o.getName());
		if (ret == 0)
			ret = id - o.id;
		return ret;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((name == null) ? 0 : name.hashCode());
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
		final Industry other = (Industry) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return getName();
	}

}
