package com.lazerinc.beans;

import java.io.Serializable;

import com.lazerinc.cached.DatabaseObject;

public class State implements Serializable, Comparable<State>, DatabaseObject {

	private static final long serialVersionUID = 1;

	int id;

	String name;

	String code;

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

	public State() {
		super();
	}

	public State(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String abbr) {
		this.code = abbr;
	}

	public String toString() {
		return getName();
	}

	public int compareTo(State o) {
		int ret = getName().compareTo(o.getName());
		if (ret == 0)
			ret = getCode().compareTo(o.getCode());
		if (ret == 0)
			ret = id - o.id;
		return ret;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((name == null) ? 0 : name.hashCode());
		result = PRIME * result + ((code == null) ? 0 : code.hashCode());
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
		final State other = (State) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

}
