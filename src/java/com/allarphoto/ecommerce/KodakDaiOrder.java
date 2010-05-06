package com.lazerinc.ecommerce;

import java.util.Collection;

import com.lazerinc.application.Fulfillment;
import com.lazerinc.beans.OrderItem;

public class KodakDaiOrder extends Order {
	private static final long serialVersionUID = 1;

	public KodakDaiOrder() {
		super();
	}

	public KodakDaiOrder(Merchant merchant, CommerceUser user) {
		super(merchant, user);
	}

	public KodakDaiOrder(Collection<OrderItem> items) {
		super(items);
	}

	@Override
	protected Fulfillment createFulfillmentModel() {
		Fulfillment fulfill = new KodakDaiFulfillment(getUser());
		initFulfilment(fulfill);
		return fulfill;
	}

}
