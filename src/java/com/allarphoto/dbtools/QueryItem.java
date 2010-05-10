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

// Title: QueryItem
// Author: Michael Stover
// Company: Lazer Inc.
package com.allarphoto.dbtools;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class QueryItem implements Serializable {
	private static final long serialVersionUID = 1;

	public QueryItem() {
	}

	void readObject(ObjectInputStream ois) throws ClassNotFoundException,
			IOException {
		ois.defaultReadObject();
	}

	void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
	}

	public void setValues(Set newValues) {
		values = newValues;
	}

	public void setValues(String[] newValues) {
		for (int x = 0; x < newValues.length; x++)
			values.add(newValues[x]);
	}

	public Set getValues() {
		return values;
	}

	public String getValue() {
		Iterator valIt = values.iterator();
		if (valIt.hasNext()) {
			return (String) valIt.next();
		} else
			return null;
	}

	public void addValue(String val) {
		values.add(val);
	}

	public void setCategory(String newCategory) {
		category = newCategory;
	}

	public String getCategory() {
		return category;
	}

	public void setCompareType(int newCompareType) {
		compareType = newCompareType;
	}

	public int getCompareType() {
		return compareType;
	}

	public void setSearchType(int newSearchType) {
		searchType = newSearchType;
	}

	public int getSearchType() {
		return searchType;
	}

	public void setAnd(boolean newAnd) {
		and = newAnd;
	}

	public boolean isAnd() {
		return and;
	}

	public String toString() {
		String nl = System.getProperty("line.separator");
		StringBuffer ret = new StringBuffer("");
		ret.append("QueryItem {");
		ret.append("category = " + category + "; ");
		ret.append("values = ");
		Iterator it = values.iterator();
		while (it.hasNext())
			ret.append(it.next() + ", ");
		ret.append("; ");
		ret.append("compareType = " + compareType + "; ");
		ret.append("searchType = " + searchType + "; ");
		ret.append(and + "}");
		return ret.toString();
	}

	public boolean equals(Object c) {
		boolean retVal = true;
		if (c instanceof QueryItem) {
			QueryItem s = (QueryItem) c;
			if (!category.equals(s.getCategory()))
				retVal = false;
			else if (compareType != s.getCompareType())
				retVal = false;
			else if (searchType != s.getSearchType())
				retVal = false;
			else if (and != s.isAnd())
				retVal = false;
			else {
				if (!(values.containsAll(s.getValues()))
						|| !(s.getValues().containsAll(values)))
					retVal = false;
			}
		} else
			retVal = false;
		return retVal;
	}

	public int hashCode() {
		int ret = 0;
		ret += category.hashCode();
		ret += compareType;
		ret += searchType;
		if (and)
			ret += 1;
		ret += values.hashCode();
		return ret;
	}

	public void setExternalLogic(int newExternalLogic) {
		externalLogic = newExternalLogic;
	}

	public int getExternalLogic() {
		return externalLogic;
	}

	private Set values = new HashSet();

	private String category;

	private int compareType = DBConnect.EQ;

	private int searchType = DBConnect.IS;

	private boolean and = false;

	private int externalLogic = QueryItem.AND;

	public static final int AND = 0;

	public static final int OR = 1;

	public static final int ANDNOT = 2;

}
