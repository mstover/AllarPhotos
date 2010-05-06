package com.lazerinc.client.beans;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

import org.coinjema.collections.HashTree;

/**
 * Title: Lazerweb Description: Lazerweb - Lazer Inc. version 3.0 Copyright:
 * Copyright (c) 2001 Company: Lazer Inc.
 * 
 * @author Michael Stover
 * @version 1.0
 */

public class SearchCategoryBean implements Serializable {
	private static final long serialVersionUID = 1;

	HashTree searchCategories, commonCategories;

	String rootName;

	public SearchCategoryBean() {
	}

	public void clear() {
		setSearchCategories(null);
		setCommonCategories(null);
		setRootName(null);
	}

	public void setSearchCategories(HashTree searchCategories) {
		this.searchCategories = searchCategories;
	}

	public void setCommonCategories(HashTree commonCategories) {
		this.commonCategories = commonCategories;
	}

	public HashTree getCommonCategoryTree() {
		return commonCategories;
	}

	public Collection getCommonValues(String category) {
		return getCommonCategoryTree() != null ? getCommonCategoryTree().list(
				category) : Collections.EMPTY_LIST;
	}

	public Object getCommonValue(String category) {
		return getCommonCategoryTree() != null ? getCommonCategoryTree()
				.getObject(category) : null;
	}

	public Collection getCommonCategories() {
		if (getCommonCategoryTree() != null) {
			return getCommonCategoryTree().list();
		}
		return null;
	}

	public HashTree getSearchCategories() {
		return searchCategories;
	}

	public Collection getCategories() {
		return getSearchCategories().list();
	}

	public void setRootName(String rootName) {
		this.rootName = rootName;
	}

	public boolean hasCommonCategories() {
		Collection com = getCommonCategories();
		return com != null && com.size() > 0;
	}

	public String getRootName() {
		return rootName;
	}

	public boolean hasMultipleCategories() {
		return getSearchCategories().list().size() > 1;
	}

	public int getSize() {
		return getSearchCategories().list().size();
	}

	public int getValueCount(String category) {
		return getSearchCategories().list(category).size();
	}

	public Collection getCategoryValues(String category) {
		return getSearchCategories().list(category);
	}
}