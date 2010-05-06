package com.lazerinc.beans;

import java.io.Serializable;

public class Path implements Comparable<Path>, Serializable {
	private static final long serialVersionUID = 1;

	String name;

	int id;

	public Path() {

	}

	public Path(String n) {
		name = n;
	}

	public Path(int id, String n) {
		this.id = id;
		name = n;
	}

	public String getRoot() {
		if (name.length() > 1) {
			int index = name.indexOf("/", 1);
			if (index > -1) {
				if (name.startsWith("/"))
					return name.substring(1, index);
				else
					return name.substring(0, index);
			}
			return name;
		} else
			return name;
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
		final Path other = (Path) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public int compareTo(Path o) {
		if (o == null)
			return -1;
		if (name == null && o.name == null)
			return 0;
		if (name == null)
			return 1;
		if (o.name == null)
			return -1;
		return name.compareTo(o.name);
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
		return name;
	}

}
