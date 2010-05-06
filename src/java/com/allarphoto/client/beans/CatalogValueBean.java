package com.lazerinc.client.beans;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class CatalogValueBean implements Serializable {
	private static final long serialVersionUID = 1;

	Collection<String> categories = new TreeSet<String>();

	Map<String, Collection<String>> valuemap = new HashMap<String, Collection<String>>();

	public void clear() {
		categories.clear();
		valuemap.clear();
	}

	public void setCategories(Collection categories) {
		categories.clear();
		categories.addAll(categories);
	}

	public Collection<String> getCategories() {
		return categories;
	}

	public Map<String, Collection<String>> getValuemap() {
		return valuemap;
	}

	public void setCategoryValues(String category, Collection<String> values) {
		valuemap.put(category, new TreeSet<String>(values));
	}

	public Collection<String> getCategoryValues(String category) {
		return valuemap.get(category);
	}

	public boolean isPopulated() {
		return getCategories() != null && getCategories().size() > 0
				&& valuemap.size() > 0;
	}

}