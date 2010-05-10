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

// Title: LogModel
// Author: Michael Stover
// Company: Lazer inc.
package com.allarphoto.application;

import java.util.Calendar;
import java.util.Collection;
import java.util.Set;

import com.allarphoto.beans.LogItem;
import com.allarphoto.ecommerce.Order;

public interface LogModel {

	/***************************************************************************
	 * Adds a log item to the database. It runs the add routines in a separate
	 * thread, so the log item is not necessarily logged at the time when this
	 * method returns.
	 * 
	 * @param item
	 *            Log item to be logged.
	 **************************************************************************/
	public void addLogItem(LogItem item);

	public void addOrder(Order order);

	/***************************************************************************
	 * Queries the log database and returns an array of LogItems, using the
	 * sample item as the template of values of interest.
	 * 
	 * @param query
	 *            Set of QueryItems to be used to create the database query.
	 * @param sortBy
	 *            List of Categories to sort by.
	 * @param sampleItem
	 *            A LogItem with value names (actual values can be null) of
	 *            categories of interest.
	 * @return Array of LogItems.
	 **************************************************************************/
	public LogItem[] queryLog(Set query, String[] sortBy,
			java.util.GregorianCalendar from, java.util.GregorianCalendar to);

	public Order getOrder(String orderNo, SecurityModel security);

	public Collection<Order> getOrders(String status, Calendar date,
			SecurityModel security);

	public void setDatabase(com.allarphoto.dbtools.DBConnect database);

	public java.util.Map getValues();
}
