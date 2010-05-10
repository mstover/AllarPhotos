/***********************************************************************************************************************
 * Copyright 1999 (c) Lazer Incorporated All code contained in this file is the property of Lazer Incorporated. All
 * rights reserved.
 **********************************************************************************************************************/
package com.allarphoto.ecommerce;

import static com.allarphoto.ecommerce.OrderFailedException.APPROVAL;
import static com.allarphoto.ecommerce.OrderFailedException.CANCELLED;
import static com.allarphoto.ecommerce.OrderFailedException.CONFIRMATION;
import static com.allarphoto.ecommerce.OrderFailedException.FULFILLMENT;
import static com.allarphoto.ecommerce.OrderFailedException.MERCHANT;
import static com.allarphoto.ecommerce.OrderFailedException.REJECT;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.collections.MultiMap;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaDynamic;
import org.coinjema.context.CoinjemaObject;

import strategiclibrary.service.cache.CacheService;
import strategiclibrary.util.Converter;

import com.allarphoto.application.Fulfillment;
import com.allarphoto.application.SecureComponent;
import com.allarphoto.application.SecurityModel;
import com.allarphoto.beans.Address;
import com.allarphoto.beans.OrderItem;

@CoinjemaObject
public class Order implements Serializable, SecureComponent,
		Iterable<OrderItem>, Comparable<Order> {
	private static final long serialVersionUID = 1;

	CacheService cache;

	public static enum Status {
		FULFILLED {

			public String value() {
				return "fulfilled";
			}
		},
		AWAITING_APPROVAL {

			public String value() {
				return "awaiting_approval";
			}
		},
		AWAITING_CONFIRMATION {

			public String value() {
				return "awaiting_confirmation";
			}
		},
		REJECTED {

			public String value() {
				return "rejected";
			}
		},
		CANCELLED {

			public String value() {
				return "cancelled";
			}
		},
		AWAITING_FULFILLMENT {

			public String value() {
				return "awaiting_fulfillment";
			}
		};

		public abstract String value();

		public String toString() {
			return value();
		}

		public static Status getStatus(String val) {
			if (FULFILLED.value().equals(val))
				return FULFILLED;
			else if (AWAITING_APPROVAL.value().equals(val))
				return AWAITING_APPROVAL;
			else if (AWAITING_CONFIRMATION.value().equals(val))
				return AWAITING_CONFIRMATION;
			else if (AWAITING_FULFILLMENT.value().equals(val))
				return AWAITING_FULFILLMENT;
			else if (CANCELLED.value().equals(val))
				return CANCELLED;
			else if (REJECTED.value().equals(val))
				return REJECTED;
			return FULFILLED;
		}
	}

	int id = -1;

	Collection<OrderItem> items;

	Collection<OrderItem> rawItems;

	Merchant merchant;

	Address shippingAddress;

	CommerceUser user;

	SecurityModel security;

	String orderNo;

	Calendar dateTime;

	private Status status;

	Map<String, String> orderMap = new HashMap<String, String>();

	public Order() {
		items = new TreeSet<OrderItem>();
		setStatus(Status.AWAITING_FULFILLMENT);
		dateTime = new GregorianCalendar();
	}

	public Order(Merchant merchant, CommerceUser user) {
		this();
		this.merchant = merchant;
		this.user = user;
	}

	public Order(Collection<OrderItem> items) {
		this();
		rawItems = items;
	}

	public void addItem(OrderItem item) {
		items.add(item);
	}

	/**
	 * Gets a value set just on the order as a whole. This is the beginning of
	 * the right way to do things, but these values do not get persisted to the
	 * database yet.
	 * 
	 * @param key
	 * @return
	 */
	public String getValue(String key) {
		return orderMap.get(key);
	}

	public void setValue(String key, String val) {
		orderMap.put(key, val);
	}

	public void setItemValue(int productId, String key, String val,
			SecurityModel security) {
		for (OrderItem item : items) {
			if (item.getProduct(security).getId() == productId) {
				item.setValue(key, val);
				break;
			}
		}
	}

	/**
	 * Returns false if no approval is necessary, and does nothing. Returns true
	 * if approval is needed and takes care of starting aproval process.
	 * 
	 * @param response
	 * @param costReport
	 * @return
	 */
	public void fulfill() {
		Fulfillment fulfill = createFulfillmentModel();
		if (getStatus().equals(Status.AWAITING_FULFILLMENT)
				|| getStatus().equals(Status.FULFILLED)) {
			if (!fulfill.notifyFulfillment(this, getMerchant()))
				throw new OrderFailedException(FULFILLMENT);
			if (!fulfill.notifyMerchant(this, getMerchant()))
				throw new OrderFailedException(MERCHANT);
		} else if (getStatus().equals(Status.AWAITING_APPROVAL)) {
			if (!fulfill.sendApprovalRequest(this, getMerchant()))
				throw new OrderFailedException(APPROVAL);
		} else if (getStatus().equals(Status.AWAITING_CONFIRMATION)) {
			if (!fulfill.sendConfirmationRequest(this, getMerchant()))
				throw new OrderFailedException(CONFIRMATION);
		}
	}

	protected Fulfillment createFulfillmentModel() {
		LazerwebFulfillment fulfill = new LazerwebFulfillment(getUser());
		initFulfilment(fulfill);
		return fulfill;
	}

	protected void initFulfilment(Fulfillment fulfill) {
		fulfill.setSecurity(security);
	}

	public Iterator<OrderItem> iterator() {
		return items.iterator();
	}

	public Collection<OrderItem> getItems() {
		return items;
	}

	public Iterable<Map.Entry<String, String>> infoIterator() {
		return getInfoMap().entrySet();
	}

	public Set<Map.Entry<String, String>> getInfoSet() {
		return getInfoMap().entrySet();
	}

	/**
	 * Gets the map of properties set on the first log item. A hack that needs
	 * to be fixed.
	 * 
	 * @return
	 */
	public Map<String, String> getInfoMap() {
		return orderMap;
	}

	public Merchant getMerchant() {
		return merchant;
	}

	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}

	public CommerceUser getUser() {
		return user;
	}

	public void setUser(CommerceUser user) {
		this.user = user;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public void approveOrder() {
	}

	public void reject() {
		Fulfillment fulfill = createFulfillmentModel();
		if (!fulfill.sendRejectNotification(this, getMerchant()))
			throw new OrderFailedException(REJECT);
	}

	public void cancel() {
		Fulfillment fulfill = createFulfillmentModel();
		if (!fulfill.sendCancellationNotification(this, getMerchant()))
			throw new OrderFailedException(CANCELLED);
	}

	public void confirmPartial() {
		Fulfillment fulfill = createFulfillmentModel();
		if (!fulfill.sendConfirmationRequest(this, getMerchant()))
			throw new OrderFailedException(CONFIRMATION);
	}

	public void setSecurity(SecurityModel security) {
		this.security = security;

	}

	public OrderItem newItem() {
		OrderItem item = new OrderItem();
		this.addItem(item);
		return item;
	}

	public void setStatus(Status st) {
		status = st;
	}

	public Status getStatus() {
		return status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Calendar getDateTime() {
		return dateTime;
	}

	public void setDateTime(Calendar dateTime) {
		this.dateTime = dateTime;
	}

	public int compareTo(Order o) {
		if (o == null)
			return -1;
		int comp = 0;
		comp = orderMap.get("family").compareTo(o.getInfoMap().get("family"));
		if (comp == 0)
			comp = getDateTime().compareTo(o.getDateTime());
		if (comp == 0) {
			if (getOrderNo() == null && o.getOrderNo() == null) {
				comp = 0;
			} else if (getOrderNo() == null)
				comp = 1;
			else if (o.getOrderNo() == null)
				comp = -1;
			else
				comp = getOrderNo().compareTo(o.getOrderNo());
		}
		return comp;
	}

	@CoinjemaDependency(type = "cacheService", method = "cacheService", order = CoinjemaDependency.Order.LAST)
	public void setCache(CacheService cache) {
		this.cache = cache;
		if (rawItems != null) {
			setItems(rawItems);
			rawItems = null;
		}
	}

	public void setItems(Collection<OrderItem> rawItems) {
		for (OrderItem item : rawItems) {
			if (item.getValue("file") != null) {
				addItem(item);
			} else {
				setOrderNo(item.getOrderNo());
				setDateTime(item.getDateTime());
				setId(item.getId());
				setMerchant(cache.getCache(Merchant.class).getCachedObject(
						"name", item.getValue("merchant")));
				setShippingAddress(cache.getCache(Address.class)
						.getCachedObject(
								"id",
								Converter.getInt(item
										.getValue("shipping_address"), -1)));
				setStatus(Status.getStatus(item.getValue("status")));
				setUser(cache.getCache(CommerceUser.class).getCachedObject(
						"username", item.getValue("user")));
				for (Object key : item.getItem().keySet()) {
					if (key != null && !key.equals("user")
							&& !key.equals("shipping_address")
							&& !key.equals("merchant") && !key.equals("status")
							&& !key.equals("orderNo"))
						setValue((String) key, item.getValue((String) key));
				}
			}
		}
		for (OrderItem item : getItems()) {
			item.setValue("family", getValue("family"));
		}
	}

	protected CacheService getCache() {
		return cache;
	}

	public Address getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(Address shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public static Collection<Order> loadOrders(Collection<OrderItem> items) {
		MultiMap mm = new MultiHashMap();
		for (OrderItem item : items) {
			mm.put(item.getOrderNo(), item);
		}
		List<Order> orders = new LinkedList<Order>();
		for (Object key : mm.keySet()) {
			Collection<OrderItem> oneOrderItems = (Collection<OrderItem>) mm
					.get(key);
			for (OrderItem item : oneOrderItems) {
				if (item.getValue("Order.class") != null) {
					try {
						Order o = (Order) Class.forName(
								item.getValue("Order.class")).newInstance();
						o.setItems(oneOrderItems);
						orders.add(o);
						break;
					} catch (Exception e) {
						throw new RuntimeException(
								"Failed to load order for orderId="
										+ item.getOrderNo(), e);
					}
				}
			}
		}
		return orders;
	}
}
