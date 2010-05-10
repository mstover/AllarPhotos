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

// Title: DatabaseLogger
// Author: Michael Stover
// Company: Lazer inc.
package com.allarphoto.utils;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.coinjema.context.CoinjemaContext;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

import strategiclibrary.service.sql.DefaultMappingService;
import strategiclibrary.service.sql.ObjectMappingService;
import strategiclibrary.util.Converter;

import com.allarphoto.application.LogModel;
import com.allarphoto.application.SecurityModel;
import com.allarphoto.beans.Address;
import com.allarphoto.beans.DownloadItem;
import com.allarphoto.beans.LogItem;
import com.allarphoto.beans.OrderItem;
import com.allarphoto.beans.UploadItem;
import com.allarphoto.client.exceptions.LazerwebException;
import com.allarphoto.dbtools.DBConnect;
import com.allarphoto.dbtools.QueryItem;
import com.allarphoto.ecommerce.DatabaseUtilities;
import com.allarphoto.ecommerce.Order;
import com.allarphoto.ecommerce.ProductFamily;
import com.allarphoto.server.ProductService;
import com.allarphoto.server.UserService;

@CoinjemaObject
public class DatabaseLogger implements LogModel {

	LinkedList toBeLogged = new LinkedList();

	ObjectMappingService objectMapping = new DefaultMappingService(
			new CoinjemaContext("logging"));

	DatabaseUtilities dbUtil;

	ProductService productService;

	org.apache.log4j.Logger log;

	UserService ugd;

	protected UserService getUgd() {
		return ugd;
	}

	@CoinjemaDependency(alias = "log4j")
	public void setLogger(org.apache.log4j.Logger l) {
		this.log = l;
	}

	@CoinjemaDependency(type = "userService", method = "userService")
	public void setUserService(UserService ugd) {
		this.ugd = ugd;
	}

	private org.apache.log4j.Logger getLogger() {
		return log;
	}

	Logger logger = new Logger();

	boolean stop = false;

	public DatabaseLogger() {
	}

	public void stopLogging() {
		stop = true;
	}

	public void startLogging() {
		stop = false;
	}

	private void queue(LogItem item) {
		synchronized (logger) {
			toBeLogged.addLast(item);
			logger.notify();
		}
	}

	private void queue(Order order) {
		synchronized (logger) {
			toBeLogged.addLast(order);
			logger.notify();
		}
	}

	private Object getQueued() {
		Object i;
		synchronized (logger) {
			try {
				i = toBeLogged.removeFirst();
			} catch (NoSuchElementException e) {
				i = null;
			}
		}
		return i;
	}

	public void addLogItem(LogItem item) {
		queue(item);
	}

	public void addOrder(Order order) {
		logger.logIt(order);
	}

	/**
	 * Returns a list of categories currently in the logging database
	 * 
	 * @return An array of categories as String.
	 */
	public String[] getCategories() {
		String[] tables = { categoryTable };
		String[] cols = { valueColumn };
		Data data = database.select(tables, cols, null);
		return data.getColumn(cols[0]);
	}

	/**
	 * Returns a Map object holding all the categories mapped to a list of
	 * values in a Set
	 * 
	 * @return Map object pairing category names to Sets of values
	 */
	public Map getValues() {
		Map map = new HashMap();
		Set valueSet;
		String name, value;
		String[] tables = { categoryTable, logTable, valueTable };
		String[] cols = { categoryTable + "." + valueColumn,
				valueTable + "." + valueColumn };
		String where = database.where(valueTable + "." + valueIDColumn,
				logTable + "." + valueIDColumn, DBConnect.EQ, DBConnect.COLUMN)
				+ " AND "
				+ database.where(categoryTable + "." + categoryIDColumn,
						logTable + "." + categoryIDColumn, DBConnect.EQ,
						DBConnect.COLUMN);
		Data data = database.select(tables, cols, where);
		data.reset();
		while (data.next()) {
			name = (String) data.getColumnValue(cols[0]);
			value = (String) data.getColumnValue(cols[1]);
			valueSet = (Set) map.get(name);
			if (valueSet == null) {
				valueSet = new HashSet();
				valueSet.add(value);
				map.put(name, valueSet);
			} else
				valueSet.add(value);
		}
		return map;
	}

	public LogItem[] queryLog(Set query, String[] sortBy,
			GregorianCalendar from, GregorianCalendar to) {
		Iterator it = query.iterator();
		OrderedIntList temp, list = new OrderedIntList();
		QueryItem q;
		int id;
		String category, value;
		StringBuffer valueTemp;
		Set values;
		boolean and, eachFirst;
		int logic;
		list.add(getDateRange(from, to));
		while (it.hasNext()) {
			q = (QueryItem) it.next();
			temp = getThisQuery(q);
			logic = q.getExternalLogic();
			if (logic == QueryItem.OR)
				list.add(temp);
			else if (logic == QueryItem.AND)
				list = list.and(temp);
			else
				list = list.andNot(temp);
		}
		SortedSet items = new TreeSet();
		list.reset();
		for (int count = 0; list.next(); count++) {
			LogItem tempItem = getLogItem(sortBy, list.get(), new LogItem());
			if (tempItem != null)
				items.add(tempItem);
		}
		return (LogItem[]) items.toArray(new LogItem[0]);
	}

	public Order getOrder(String orderNo, SecurityModel security) {
		Map values = new HashMap();
		values.put("category", "orderNo");
		values.put("value", orderNo);
		values.put("date", null);
		Collection<Order> orders = Order.loadOrders(objectMapping.getObjects(
				"getOrderItems.sql", values));
		if (orders != null && orders.size() == 1)
		{
			Order o = orders.iterator().next();
			o.setSecurity(security);
			return o;
		}
		throw new RuntimeException(LazerwebException.INVALID_ORDER);

	}

	public Collection<Order> getOrders(String status, Calendar date,
			SecurityModel security) {
		Map values = new HashMap();
		values.put("category", "status");
		values.put("value", status);
		values.put("date", date);
		Collection<Order> orders =  Order.loadOrders(objectMapping.getObjects("getOrderItems.sql",
				values));
		for(Order o : orders) o.setSecurity(security);
		return orders;
	}

	public Collection<Order> getOrders(ProductFamily family, String status,
			Calendar date, SecurityModel security) {
		Map values = new HashMap();
		values.put("category", "status");
		values.put("value", status);
		values.put("date", date);
		values.put("family", family);
		Collection<Order> orders = Order.loadOrders(objectMapping.getObjects("getOrderItems.sql",
				values));
		for(Order o : orders) o.setSecurity(security);
		return orders;
	}

	public Collection<Order> getUserOrders(String username, Calendar date,
			SecurityModel security) {
		Map values = new HashMap();
		values.put("category", "user");
		values.put("value", username);
		values.put("date", date);
		Collection<Order> orders = Order.loadOrders(objectMapping.getObjects("getOrderItems.sql",
				values));
		for(Order o : orders) o.setSecurity(security);
		return orders;
	}

	public Collection<DownloadItem> getDownloadRequests(ProductFamily family,
			Calendar sinceDate) {
		Map values = new HashMap();
		values.put("category", "family");
		values.put("value", family.getDescriptiveName());
		values.put("date", sinceDate);
		return (Collection<DownloadItem>) objectMapping.getObjects(
				"getDownloadItems.sql", values);
	}

	public Collection<DownloadItem> getDownloadRequests(ProductFamily family,
			Calendar sinceDate, Calendar toDate) {
		Map values = new HashMap();
		values.put("category", "family");
		values.put("value", family.getDescriptiveName());
		values.put("fromDate", sinceDate);
		values.put("toDate", toDate);
		return (Collection<DownloadItem>) objectMapping.getObjects(
				"getDownloadItems.sql", values);
	}

	public Collection<UploadItem> getUploadRequests(ProductFamily family,
			Calendar sinceDate) {
		Map values = new HashMap();
		values.put("category", "family");
		values.put("value", family.getTableName());
		values.put("date", sinceDate);
		return (Collection<UploadItem>) objectMapping.getObjects(
				"getUploadItems.sql", values);
	}

	public Collection<UploadItem> getUploadRequests(ProductFamily family,
			Calendar sinceDate, Calendar toDate) {
		Map values = new HashMap();
		values.put("category", "family");
		values.put("value", family.getTableName());
		values.put("fromDate", sinceDate);
		values.put("toDate", toDate);
		return (Collection<UploadItem>) objectMapping.getObjects(
				"getUploadItems.sql", values);
	}

	public Collection<LogItem> getLogItems(String actionType, String category,
			String value, Calendar sinceDate, Calendar toDate) {
		Map values = new HashMap();
		values.put("category", category);
		values.put("value", value);
		values.put("action", actionType);
		values.put("fromDate", sinceDate);
		values.put("toDate", toDate);
		return (Collection<LogItem>) objectMapping.getObjects(
				"getEventItems.sql", values);
	}

	public Order getOrder(int orderId, SecurityModel security) {
		Order order = loadOrder(orderId);
		order = loadOrderItems(order, security);
		return order;
	}

	private Order loadOrder(int id) {
		LogItem item = null;
		Order order = null;
		int count = 10;
		do {
			item = getLogItem(new String[] { "ALL" }, id, new LogItem());
			try {
				order = (Order) Class.forName(item.getValue("Order.class"))
						.newInstance();
			} catch (Exception e) {
				try {
					database.updateTable(logTable);
					Thread.sleep(50);
				} catch (InterruptedException e1) {
					Thread.currentThread().interrupt();
				}
			}
		} while (order == null && (count--) > 0);
		if (order == null)
			throw new RuntimeException("Failed to load order for orderId=" + id);
		try {
			order.setDateTime(item.getDateTime());
			order.setId(id);
			order.setMerchant(dbUtil.getMerchant(item.getValue("merchant")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(
					"Failed to load order for orderId=" + id, e);
		}
		try {
			item.getItem().remove("merchant");
			order.setOrderNo(item.getValue("orderNo"));
			item.getItem().remove("orderNo");
			order.setStatus(Order.Status.getStatus(item.getValue("status")));
			item.getItem().remove("status");
			order.setUser(getUgd().getUser(item.getValue("user")));
			if (item.getValue("shipping_address") != null) {
				Map values = new HashMap();
				values.put("id", item.getValue("shipping_address"));
				item.getItem().remove("shipping_address");
				Collection<Address> shippingAddress = objectMapping.getObjects(
						"getAddress.sql", values);
				if (shippingAddress == null || shippingAddress.size() == 0) {
					order.setShippingAddress(order.getUser()
							.getShippingAddress());
				} else {
					order.setShippingAddress(shippingAddress.iterator().next());
				}
			} else
				order.setShippingAddress(order.getUser().getShippingAddress());
		} catch (Exception e) {
			throw new RuntimeException(
					"Failed to load order for orderId=" + id, e);
		}
		try {
			item.getItem().remove("user");
			order.getInfoMap().putAll(item.getItem());
			return order;
		} catch (Exception e) {
			throw new RuntimeException(
					"Failed to load order for orderId=" + id, e);
		}
	}

	private int findOrderId(String orderNo) {
		String[] tables = { itemTable, logTable, valueTable };
		String[] cols = { itemTable + "." + itemIDColumn };
		String where = database.where(itemTable + "." + itemIDColumn, logTable
				+ "." + itemIDColumn, DBConnect.EQ, DBConnect.COLUMN)
				+ " AND "
				+ database.where(logTable + "." + valueIDColumn, valueTable
						+ "." + valueIDColumn, DBConnect.EQ, DBConnect.COLUMN)
				+ " AND "
				+ database.where(valueTable + "." + valueColumn, orderNo,
						DBConnect.EQ, DBConnect.IS)
				+ "and exists (select log.item_id from log "
				+ "inner join log_names on log.category_id=log_names.category_id "
				+ "where log_names.val='Order.class' "
				+ "and log.item_id=log_items.item_id)";
		Data data = database.select(tables, cols, where);
		data.reset();
		if (data.next()) {
			return ((Number) data.getColumnValue(0)).intValue();
		}
		return -1;
	}

	private Collection<Number> findOrderIdsByStatus(String status, Calendar date) {
		if (date == null)
			date = new GregorianCalendar(1990, 1, 1);
		String[] tables = { itemTable, logTable, valueTable, categoryTable };
		String[] cols = { itemTable + "." + itemIDColumn };
		String where = database.where(itemTable + "." + itemIDColumn, logTable
				+ "." + itemIDColumn, DBConnect.EQ, DBConnect.COLUMN)
				+ " AND "
				+ database.where(logTable + "." + valueIDColumn, valueTable
						+ "." + valueIDColumn, DBConnect.EQ, DBConnect.COLUMN)
				+ " AND "
				+ database.where(logTable + "." + categoryIDColumn,
						categoryTable + "." + categoryIDColumn, DBConnect.EQ,
						DBConnect.COLUMN)
				+ " AND "
				+ database.where(valueTable + "." + valueColumn, status,
						DBConnect.EQ, DBConnect.IS)
				+ " AND "
				+ database.where(categoryTable + "." + valueColumn, "status",
						DBConnect.EQ, DBConnect.IS)
				+ "and exists (select log.item_id from log "
				+ "inner join log_names on log.category_id=log_names.category_id "
				+ "where log_items.date_column >= '"
				+ Converter.formatCalendar(date, "yyyy-MMM-dd")
				+ "' and "
				+ "log_names.val='Order.class' "
				+ "and log.item_id=log_items.item_id)";
		Data data = database.select(tables, cols, where);
		data.reset();
		List<Number> ids = new LinkedList<Number>();
		while (data.next()) {
			Number n = (Number) data.getColumnValue(0);
			if (n != null)
				ids.add(n);
		}
		return ids;
	}

	private Order loadOrderItems(Order order, SecurityModel security) {
		String[] tables = { logTable, categoryTable, valueTable };
		String[] cols = { logTable + "." + itemIDColumn };
		String where = database.where(categoryTable + "." + categoryIDColumn,
				logTable + "." + categoryIDColumn, DBConnect.EQ,
				DBConnect.COLUMN)
				+ " AND "
				+ database.where(valueTable + "." + valueIDColumn, logTable
						+ "." + valueIDColumn, DBConnect.EQ, DBConnect.COLUMN)
				+ " AND "
				+ database.where(categoryTable + "." + valueColumn, "orderNo",
						DBConnect.EQ, DBConnect.IS)
				+ " AND "
				+ database.where(valueTable + "." + valueColumn, order
						.getOrderNo(), DBConnect.EQ, DBConnect.IS)
				+ " AND "
				+ database.where(logTable + "." + itemIDColumn, order.getId(),
						DBConnect.NOTEQ, DBConnect.NUMBER);
		Data data = database.select(tables, cols, where);
		data.reset();
		String[] sortBy = { "file", "ALL" };
		while (data.next()) {
			OrderItem item = (OrderItem) getLogItem(sortBy, Converter
					.getInt(data.getColumnValue(cols[0])), new OrderItem());
			if (item != null && item.getValue("product_id") != null) {
				item.setProduct(dbUtil.getProductFamilyFromDescription(
						order.getValue("family")).getProduct(
						item.getValue("product_id"), security));
				order.addItem(item);
			}
		}
		order.setSecurity(security);
		return order;
	}

	/***************************************************************************
	 * Finds a single LogItem object given an id number.
	 * 
	 * @param sample
	 *            Sample LogItem object holding the category names that we want
	 *            out of the database.
	 * @param sortBy
	 *            Array of Strings representing the categories the item should
	 *            be sorted by.
	 * @param id
	 *            Database ID # of LogItem.
	 * @return LogItem object.
	 **************************************************************************/
	private LogItem getLogItem(String[] sortBy, int id, LogItem newItem) {
		Set contains = new HashSet();
		for (int x = 0; x < sortBy.length; contains.add(sortBy[x++]))
			;
		newItem.setSortBy(sortBy).setId(id);
		contains.remove("date");
		String name, value;
		GregorianCalendar cal = new GregorianCalendar();
		String[] tables = { logTable, itemTable, categoryTable, valueTable };
		String[] cols = { dateColumn, categoryTable + "." + valueColumn,
				valueTable + "." + valueColumn };
		String where = database.where(itemTable + "." + itemIDColumn, logTable
				+ "." + itemIDColumn, DBConnect.EQ, DBConnect.COLUMN)
				+ " AND "
				+ database.where(categoryTable + "." + categoryIDColumn,
						logTable + "." + categoryIDColumn, DBConnect.EQ,
						DBConnect.COLUMN)
				+ " AND "
				+ database.where(valueTable + "." + valueIDColumn, logTable
						+ "." + valueIDColumn, DBConnect.EQ, DBConnect.COLUMN)
				+ " AND "
				+ database.where(itemTable + "." + itemIDColumn, id,
						DBConnect.EQ, DBConnect.NUMBER);
		Data data = database.select(tables, cols, where);
		data.reset();
		boolean firstRun = true, isNull = true;
		while (data.next()) {
			name = (String) data.getColumnValue(cols[1]);
			value = (String) data.getColumnValue(cols[2]);
			if (isNull && value != null)
				isNull = false;
			if (contains.contains(name) || contains.contains("ALL"))
				newItem.setValue(name, value);
			if (firstRun) {
				cal.setTime((Date) data.getColumnValue(cols[0]));
				newItem.setDateTime(cal);
				firstRun = false;
			}
		}
		if (isNull)
			return null;
		else
			return newItem;
	}

	private int[] getDateRange(GregorianCalendar from, GregorianCalendar to) {
		String[] tables = { itemTable };
		String[] cols = { itemIDColumn };
		String where = null;
		if (from != null) {
			where = database.where(dateColumn, database
					.getDateForComparison(from), DBConnect.GTEQ,
					DBConnect.NUMBER);
			if (to != null)
				where = where
						+ " AND "
						+ database.where(dateColumn, database
								.getDateForComparison(to), DBConnect.LTEQ,
								DBConnect.NUMBER);
		} else if (to != null)
			where = database
					.where(dateColumn, database.getDateForComparison(to),
							DBConnect.LTEQ, DBConnect.NUMBER);
		Data data = database.select(tables, cols, where);
		data.reset();
		int[] retVal = new int[data.size()];
		int count = 0;
		while (data.next())
			retVal[count++] = ((Integer) data.getColumnValue(cols[0]))
					.intValue();
		return retVal;
	}

	/***************************************************************************
	 * Handles the internal logic for finding the log items for each individual
	 * query object.
	 * 
	 * @param q
	 *            One QueryItem object.
	 * @return an OrderedIntList of found log item ID's.
	 **************************************************************************/
	private OrderedIntList getThisQuery(QueryItem q) {
		Iterator valuesIt;
		String value;
		OrderedIntList temp = new OrderedIntList();
		String category = q.getCategory();
		Set values = q.getValues();
		boolean eachFirst = true, and = q.isAnd();
		int logic = q.getExternalLogic();
		if ((q.getCompareType() != DBConnect.IN && q.getCompareType() != DBConnect.NOTIN)
				|| values.size() > 200 || and) {
			valuesIt = values.iterator();
			while (valuesIt.hasNext()) {
				value = (String) valuesIt.next();
				if (eachFirst || !and) {
					temp.add(find(category, value, q.getCompareType(), q
							.getSearchType()));
					eachFirst = false;
				} else
					temp = temp.and(find(category, value, q.getCompareType(), q
							.getSearchType()));
			}
		} else
			temp.add(find(category, (String[]) values.toArray(new String[0]), q
					.getCompareType(), q.getSearchType()));
		return temp;
	}

	/***************************************************************************
	 * Searches the log tables and returns an OrderedIntList of Log item ID's.
	 * 
	 * @param category
	 *            Name of category to search for.
	 * @param value
	 *            Value to search for.
	 * @param ct
	 *            CompareType.
	 * @param st
	 *            SearchType.
	 **************************************************************************/
	private OrderedIntList find(String category, String value, int ct, int st) {
		String[] tables = { logTable, categoryTable, valueTable };
		String[] cols = { itemIDColumn };
		String where = database.where(categoryTable + "." + categoryIDColumn,
				logTable + "." + categoryIDColumn, DBConnect.EQ,
				DBConnect.COLUMN)
				+ " AND "
				+ database.where(valueTable + "." + valueIDColumn, logTable
						+ "." + valueIDColumn, DBConnect.EQ, DBConnect.COLUMN)
				+ " AND "
				+ database.where(categoryTable + "." + valueColumn, category,
						DBConnect.EQ, DBConnect.IS)
				+ " AND "
				+ database.where(valueTable + "." + valueColumn, value, ct, st);
		Data data = database.select(tables, cols, where);
		data.reset();
		OrderedIntList temp = new OrderedIntList();
		while (data.next())
			temp.add(((Integer) data.getColumnValue(cols[0])).intValue());
		return temp;
	}

	/***************************************************************************
	 * Searches the log tables and returns a OrderedIntList of Log item ID's.
	 * 
	 * @param category
	 *            Name of category to search for.
	 * @param value
	 *            Array of values to search for.
	 * @param ct
	 *            CompareType.
	 * @param st
	 *            SearchType.
	 **************************************************************************/
	private OrderedIntList find(String category, String[] value, int ct, int st) {
		String[] tables = { logTable, categoryTable, valueTable };
		String[] cols = { itemIDColumn };
		String where = database.where(categoryTable + "." + categoryIDColumn,
				logTable + "." + categoryIDColumn, DBConnect.EQ,
				DBConnect.COLUMN)
				+ " AND "
				+ database.where(valueTable + "." + valueIDColumn, logTable
						+ "." + valueIDColumn, DBConnect.EQ, DBConnect.COLUMN)
				+ " AND "
				+ database.where(categoryTable + "." + valueColumn, category,
						DBConnect.EQ, DBConnect.IS)
				+ " AND "
				+ database.where(valueTable + "." + valueColumn, value, ct, st);
		Data data = database.select(tables, cols, where);
		data.reset();
		OrderedIntList temp = new OrderedIntList();
		while (data.next())
			temp.add(((Integer) data.getColumnValue(cols[0])).intValue());
		return temp;
	}

	@CoinjemaDependency(type = "logDatabase", method = "logDatabase")
	public void setDatabase(DBConnect d) {
		database = d;
	}

	private DBConnect database;

	static String categoryTable = "log_names";

	static String valueTable = "log_values";

	static String itemTable = "log_items";

	static String logTable = "log";

	static String itemIDColumn = "item_id";

	static String valueIDColumn = "value_id";

	static String categoryIDColumn = "category_id";

	static String dateColumn = "date_column";

	static String valueColumn = "val";

	public static final String TIME = "date_logged";

	class Logger extends Thread {

		public Logger() {
			start();
		}

		public synchronized void run() {
			Object queued = null;
			String date, category;
			while (!stop) {
				try {
					queued = getQueued();
					while (queued != null) {
						logIt(queued);
						queued = getQueued();
					}
				} catch (Throwable e1) {
					log.error("Failed to record a log item: " + queued, e1);
				}
				try {
					wait();
				} catch (InterruptedException e) {
				}
			}
		}

		public synchronized void logIt(Object queued) {
			String[] cols = { itemIDColumn, categoryIDColumn, valueIDColumn };
			String[] vals = new String[3];
			String category;
			if (queued instanceof LogItem) {
				LogItem item = (LogItem) queued;
				Map stuff = item.getItem();
				int name, value, id = addItem(stuff, item.getId(), item
						.getDateTime());
				item.setId(id);
				persist(cols, vals, stuff, id);
				if (item instanceof OrderItem
						&& item.getValue("product_id") == null) {
					category = "product_id";
					name = addName(category);
					value = addValue(String.valueOf(((OrderItem) item)
							.getProduct(dbUtil.getAdmin()).getId()));
					vals[0] = String.valueOf(id);
					vals[1] = String.valueOf(name);
					vals[2] = String.valueOf(value);
					database.insert(logTable, cols, vals);
				}
			} else if (queued instanceof Order) {
				Order order = (Order) queued;
				Map<String, String> stuff = new HashMap<String, String>(order
						.getInfoMap());
				stuff.put("user", order.getUser().getUsername());
				stuff.put("orderNo", order.getOrderNo());
				stuff.put("merchant", order.getMerchant().getName());
				stuff.put("status", order.getStatus().value());
				stuff.put("action", "order");
				stuff.put("Order.class", order.getClass().getName());
				if (order.getShippingAddress() != null)
					stuff.put("shipping_address", String.valueOf(order
							.getShippingAddress().getId()));
				else
					stuff.put("shipping_address", "0");
				int id = addItem(stuff, order.getId(), order.getDateTime());
				order.setId(id);
				persist(cols, vals, stuff, id);
				for (OrderItem item : order) {
					item.setValue("orderNo", order.getOrderNo());
					logIt(item);
				}
			}
		}

		private synchronized void persist(String[] cols, String[] vals,
				Map stuff, int id) {
			String category;
			int name;
			int value;
			Iterator it = stuff.keySet().iterator();
			while (it.hasNext()) {
				category = (String) it.next();
				name = addName(category);
				value = addValue((String) stuff.get(category));
				vals[0] = "" + id;
				vals[1] = "" + name;
				vals[2] = "" + value;
				database.insert(logTable, cols, vals);
			}
		}

		/**
		 * Changed to add or update an already existing item.
		 * 
		 * @param item
		 * @return
		 */
		private synchronized int addItem(Map<String, String> item, int id,
				Calendar t) {
			Functions.javaLog("Adding log item with id = " + id);
			if (id < 0) {
				Functions.javaLog("Creating log item with id = " + id);
				Functions.javaLog("Creating a new log item: "
						+ database.cleanString(database.getDate(t)));
				return addItem(t);
			} else {
				Functions.javaLog("updating log item with id = " + id);
				database.delete(logTable, "item_id=" + id);
				return id;
			}
		}

		private synchronized int addItem(Calendar t) {
			int id = getNewId();
			Functions.javaLog("New id = " + id);
			String[] cols = { itemIDColumn, dateColumn };
			String[] vals = { String.valueOf(id),
					database.cleanString(database.getDate(t)) };
			database.insert(itemTable, cols, vals);
			return id;
		}

		private synchronized int getNewId() {
			Functions.javaLog("Getting new log id from generator");
			String[] tables = { "RDB$DATABASE" };
			String[] cols = { "gen_id(log_items_gen,1)" };
			Data data;
			data = database.select(tables, cols, "1>0");
			database.updateTable("RDB$DATABASE");
			data.reset();
			if (data.next())
				return ((Number) data.getColumnValue(cols[0])).intValue();
			return -1;
		}

		private synchronized int addName(String t) {
			int retVal = -1;
			String[] cols = { valueColumn };
			String[] vals = { database.cleanString(t) };
			if ((retVal = getNameID(t)) == -1) {
				database.insert(categoryTable, cols, vals);
				retVal = getNameID(t);
			}
			return retVal;
		}

		private synchronized int getNameID(String t) {
			String[] tables = { categoryTable };
			String[] cols = { categoryIDColumn };
			String where = database.where(valueColumn, t, DBConnect.EQ,
					DBConnect.IS);
			Data data;
			data = database.select(tables, cols, where);
			data.reset();
			if (data.next())
				return ((Integer) data.getColumnValue(cols[0])).intValue();
			return -1;
		}

		private synchronized int addValue(String t) {
			if (t.length() > 252)
				t = t.substring(0, 252);
			int retVal = -1;
			String[] cols = { valueColumn };
			String[] vals = { database.cleanString(t) };
			if ((retVal = getValueID(t)) == -1) {
				database.insert(valueTable, cols, vals);
				retVal = getValueID(t);
			}
			return retVal;
		}

		private synchronized int getValueID(String t) {
			String[] tables = { valueTable };
			String[] cols = { valueIDColumn };
			String where = database.where(valueColumn, t, DBConnect.EQ,
					DBConnect.IS);
			Data data;
			data = database.select(tables, cols, where);
			data.reset();
			if (data.next())
				return ((Integer) data.getColumnValue(cols[0])).intValue();
			return -1;
		}
	}

	@CoinjemaDependency(type = "dbutil", method = "dbutil")
	public void setDbUtil(DatabaseUtilities dbUtil) {
		this.dbUtil = dbUtil;
	}

	@CoinjemaDependency(type = "productService", method = "productService")
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}
}
