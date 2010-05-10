/***********************************************************************************************************************
 * Copyright (C) 1999 Michael Stover This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA. Michael Stover can be reached via email at mstover1@rochester.rr.com or via snail mail at 130 Corwin
 * Rd. Rochester, NY 14610 The following exception to this license exists for Lazer Incorporated: Lazer Inc is excepted
 * from all limitations and requirements stipulated in the GPL. Lazer Inc. is the only entity allowed this limitation.
 * Lazer does have the right to sell this exception, if they choose, but they cannot grant additional exceptions to any
 * other entities.
 **********************************************************************************************************************/

package com.allarphoto.utils;

import java.util.ArrayList;

/*******************************************************************************
 * Keeps an ordered list of Integers that can be quickly searched for a value.
 * It automatically sorts items as they are added. Currently, items can be
 * added, and the list can be searched to see if a value exists, but there is no
 * way to retrieve items.
 * 
 * @author Michael Stover
 * @version .9b 10/14/1998
 ******************************************************************************/
public class OrderedLongList extends OrderedList {

	/***************************************************************************
	 * @param
	 * @return
	 **************************************************************************/
	public OrderedLongList() {
		items = new ArrayList();
		pointer = -1;
	} // End Method

	/***************************************************************************
	 * Adds all the elements from an array of long.
	 * 
	 * @param list
	 *            Array of long values to be added.
	 **************************************************************************/
	public void add(long[] list) {
		int place = -1;
		while (++place < list.length)
			add(list[place], 0, items.size());
	}

	/***************************************************************************
	 * Sets the current positioning of the list pointer. Must be less than the
	 * size of the list. The list numbering starts at 0.
	 * 
	 * @param pos
	 *            position to set the pointer to.
	 **************************************************************************/
	public void setPosition(int pos) {
		pointer = pos;
	}

	/***************************************************************************
	 * Returns the total number of integers held in the list.
	 * 
	 * @return Number of integers held in the list.
	 **************************************************************************/
	public int size() {
		return items.size();
	}

	/***************************************************************************
	 * Resets list to beginning.
	 **************************************************************************/
	public void reset() {
		pointer = -1;
	}

	public long[] getArray() {
		int count = -1;
		long[] values = new long[items.size()];
		while (++count < values.length)
			values[count] = ((Long) items.get(count)).longValue();
		return values;
	}

	/***************************************************************************
	 * increments pointer to next element in list.
	 * 
	 * @return True if there is a next element, false if not.
	 **************************************************************************/
	public boolean next() {
		pointer++;
		if (pointer < items.size())
			return true;
		else
			return false;
	}

	/***************************************************************************
	 * Gets the current item in the list.
	 * 
	 * @return current integer value.
	 **************************************************************************/
	public long get() {
		if (pointer < items.size())
			return ((Long) items.get(pointer)).longValue();
		else
			return -1;
	}

	/***************************************************************************
	 * Remove an integer from the list
	 * 
	 * @param value -
	 *            integer value to be removed
	 **************************************************************************/
	public boolean remove(long value) {
		if (pointer > -1)
			pointer--;
		return remove(value, 0, items.size());
	}

	private boolean remove(long value, int start, int end) {
		boolean result = false;
		if (start != end) {
			int middle = (start + end) / 2;
			long basis = ((Long) items.get(middle)).longValue();
			if (middle == start || value == basis) {
				if (value == basis) {
					items.remove(middle);
					result = true;
				} else
					result = false;
			} else {
				if (value < basis)
					result = remove(value, start, middle);
				else
					result = remove(value, middle, end);
			}
		}
		return result;
	} // End Method

	/***************************************************************************
	 * Checks if a given integer value exists in the list
	 * 
	 * @param value
	 *            value to be checked for.
	 * @return true if value exists in list, false otherwise
	 **************************************************************************/
	public boolean checkValue(long value) {
		return checkValue(value, 0, items.size());
	} // End Method

	private boolean checkValue(long value, int start, int end) {
		boolean result = false;
		if (start != end) {
			int middle = (start + end) / 2;
			long basis = ((Long) items.get(middle)).longValue();
			if (middle == start || value == basis) {
				if (value == basis)
					result = true;
				else
					result = false;
			} else {
				if (value < basis)
					result = checkValue(value, start, middle);
				else
					result = checkValue(value, middle, end);
			}
		}
		return result;
	} // End Method

	/***************************************************************************
	 * Adds all the elements from one OrderedIntList into another
	 * 
	 * @param list
	 *            OrderedIntList to be added.
	 **************************************************************************/
	public void add(OrderedLongList list) {
		int place = 0;
		list.reset();
		while (list.next()) {
			if (place == -1)
				place = 0;
			place = add(list.get(), place, items.size());
		}
	}

	public String toString() {
		long[] list = getArray();
		int count = -1;
		StringBuffer res = new StringBuffer("");
		while (++count < list.length) {
			res.append("Item " + count + " = " + list[count] + "\r\n");
		}
		return res.toString();
	}

	/***************************************************************************
	 * Removes all the elements from one OrderedIntList in another
	 * 
	 * @param list
	 *            OrderedIntList elements to be removed.
	 **************************************************************************/
	public void remove(OrderedLongList list) {
		list.reset();
		while (list.next()) {
			remove(list.get());
		}
	}

	/***************************************************************************
	 * Takes the AND logical value of this OrderedLongList with the given
	 * OrderedLongList.
	 * 
	 * @param list
	 *            OrderedIntList to be ANDed with this OrderedIntList.
	 **************************************************************************/
	public OrderedLongList and(OrderedLongList list) {
		// String
		// i=PerformanceLog.start("OrderedLongList(and(OrderedLongList))");
		reset();
		OrderedLongList temp = new OrderedLongList();
		while (next()) {
			if (list.checkValue(get()))
				temp.add(get());
		}
		// PerformanceLog.end("OrderedLongList(and(OrderedLongList))",i);
		return temp;
	}

	/***************************************************************************
	 * Takes the AND logical value of this OrderedLongList with the given
	 * OrderedLongList.
	 * 
	 * @param list
	 *            OrderedIntList to be ANDed with this OrderedIntList.
	 **************************************************************************/
	public OrderedLongList andNot(OrderedLongList list) {
		reset();
		OrderedLongList temp = new OrderedLongList();
		while (next()) {
			if (!list.checkValue(get()))
				temp.add(get());
		}
		return temp;
	}

	/***************************************************************************
	 * Takes the AND logical value of this OrderedLongList with the given Array
	 * of long values.
	 * 
	 * @param list
	 *            Array of long values to be ANDed with this OrderedIntList.
	 **************************************************************************/
	public OrderedLongList andNot(long[] list) {
		int count = -1;
		while (++count < list.length) {
			if (checkValue(list[count]))
				remove(list[count]);
		}
		return this;
	}

	/***************************************************************************
	 * Takes the AND logical value of this OrderedLongList with the given Array
	 * of long values.
	 * 
	 * @param list
	 *            Array of long values to be ANDed with this OrderedIntList.
	 **************************************************************************/
	public OrderedLongList and(long[] list) {
		// String i=PerformanceLog.start("OrderedLongList(and(long[]))");
		int count = -1;
		OrderedLongList temp = new OrderedLongList();
		while (++count < list.length) {
			if (checkValue(list[count]))
				temp.add(list[count]);
		}
		// PerformanceLog.end("OrderedLongList(and(long[]))",i);
		return temp;
	}

	/***************************************************************************
	 * Adds an integer to the list, putting it in order.
	 * 
	 * @param value
	 *            integer to be added to list
	 * @return false if value added, true if value already in list.
	 **************************************************************************/
	public boolean add(long value) {
		boolean retVal;
		if (add(value, 0, items.size()) > -1)
			retVal = false;
		else
			retVal = true;
		return retVal;
	} // End Method

	private int add(long value, int start, int end) {
		// String i=PerformanceLog.start("OrderedLongList(add)");
		int result = -1;
		if (start == end) {
			result = start;
			items.add(new Long(value));
		} else {
			int middle = (start + end) / 2;
			long basis = ((Long) items.get(middle)).longValue();
			if (middle == start || value == basis) {
				if (value < basis) {
					result = middle;
					items.add(middle, new Long(value));
				} else if (value > basis) {
					if ((middle + 1) < items.size())
						items.add(middle + 1, new Long(value));
					else
						items.add(new Long(value));
					result = middle;
				} else
					result = -1;
			} else {
				if (value < basis)
					result = add(value, start, middle);
				else
					result = add(value, middle, end);
			}
		}
		// PerformanceLog.end("OrderedLongList(add)",i);
		return result;
	} // End Method

} // End Class
