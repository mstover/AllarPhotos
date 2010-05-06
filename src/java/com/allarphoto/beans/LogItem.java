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

// Title: LogItem
// Author: Michael Stover
// Company: Lazer inc.
package com.lazerinc.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LogItem implements Serializable, Comparable {
	private static final long serialVersionUID = 1;

	public LogItem() {
		dateTime = new GregorianCalendar();
	}

	public void setItem(Map newItem) {
		item = newItem;
	}

	public Map<String, String> getItem() {
		return item;
	}

	public LogItem copy(LogItem newItem) {
		newItem.item.putAll(this.item);
		newItem.dateTime = (Calendar) this.dateTime.clone();
		newItem.sortBy = this.sortBy;
		return newItem;
	}

	public void setDateTime(Calendar newDateTime) {
		dateTime = newDateTime;
	}

	public void setDateTime() {
		dateTime = new GregorianCalendar();
	}

	public Calendar getDateTime() {
		return dateTime;
	}

	public void setId(int newId) {
		id = newId;
	}

	public int getId() {
		return id;
	}

	public int compareTo(Object o2) {
		if (!(o2 instanceof LogItem))
			return 1;
		LogItem L2 = (LogItem) o2;
		String val1, val2, temp;
		if (sortBy != null) {
			for (int x = 0; x < sortBy.length; x++) {
				if (sortBy[x].equals("date")) {
					if (dateTime.before(L2.getDateTime()))
						return -1;
					else if (L2.getDateTime().before(dateTime))
						return 1;
				}
				val1 = getValue(sortBy[x]);
				val2 = L2.getValue(sortBy[x]);
				if (val1 == null && val2 != null)
					return 1;
				else if (val2 == null && val1 != null)
					return -1;
				else if (val2 == null && val1 == null)
					continue;
				else if (!val1.equals(val2))
					return val1.compareTo(val2);
			}
		}
		if (dateTime.before(L2.getDateTime()))
			return -1;
		else if (L2.getDateTime().before(dateTime))
			return 1;
		Iterator it = item.keySet().iterator();
		while (it.hasNext()) {
			temp = (String) it.next();
			val1 = getValue(temp);
			val2 = L2.getValue(temp);
			if (val1 == null)
				return -1;
			else if (val2 == null)
				return 1;
			else if (!val1.equals(val2))
				return val1.compareTo(val2);
		}
		return -1;
	}

	/***************************************************************************
	 * Adds a name-value pair to the log bean.
	 * 
	 * @param n
	 *            Name of log value.
	 * @param v
	 *            Value.
	 **************************************************************************/
	public LogItem setValue(String name, String value) {
		item.put(name, value);
		return this;
	}

	/***************************************************************************
	 * Gets a value given it's name.
	 * 
	 * @param name
	 *            Name of value to retrieve.
	 * @return Value as a string.
	 **************************************************************************/
	public String getValue(String name) {
		String retVal = null;
		if (item != null) {
			if (!name.equals("date"))
				retVal = item.get(name);
			else
				retVal = dateTime.get(Calendar.MONTH) + 1 + "/"
						+ dateTime.get(Calendar.DAY_OF_MONTH) + "/"
						+ dateTime.get(Calendar.YEAR) + " "
						+ dateTime.get(Calendar.HOUR_OF_DAY) + ":"
						+ dateTime.get(Calendar.MINUTE);
		}
		return retVal;
	}

	public LogItem setSortBy(String[] newSortBy) {
		sortBy = newSortBy;
		return this;
	}

	public String[] getSortBy() {
		return sortBy;
	}

	private Calendar dateTime;

	private int id = -1;

	private java.util.Map<String, String> item = new HashMap<String, String>();

	private String[] sortBy;

}
