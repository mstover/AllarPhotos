package com.allarphoto.ecommerce;

import static com.allarphoto.ecommerce.Order.Status.AWAITING_APPROVAL;

import java.io.Serializable;
import java.util.Collection;

import com.allarphoto.application.Fulfillment;
import com.allarphoto.beans.OrderItem;

public class BaliOrder extends Order implements Serializable {
	private static final long serialVersionUID = 1;

	public BaliOrder() {
		super();
		setStatus(AWAITING_APPROVAL);
	}

	public BaliOrder(Merchant merchant, CommerceUser user) {
		super(merchant, user);
		setStatus(AWAITING_APPROVAL);
	}

	public BaliOrder(Collection<OrderItem> items) {
		super(items);
	}

	protected Fulfillment createFulfillmentModel() {
		Fulfillment fulfill = new BaliFulfillment(getUser());
		initFulfilment(fulfill);
		return fulfill;
	}

}
