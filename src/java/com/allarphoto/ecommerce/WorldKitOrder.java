package com.lazerinc.ecommerce;

import java.util.Collection;

import com.lazerinc.application.Fulfillment;
import com.lazerinc.beans.OrderItem;

public class WorldKitOrder extends Order {

	public WorldKitOrder() {
		super();
		setStatus(Status.FULFILLED);
	}

	public WorldKitOrder(Merchant merchant, CommerceUser user) {
		super(merchant, user);
		setStatus(Status.FULFILLED);
	}

	public WorldKitOrder(Collection<OrderItem> items) {
		super(items);
	}

	@Override
	protected Fulfillment createFulfillmentModel() {
		WorldKitchenFulfillment fulfill = new WorldKitchenFulfillment(getUser());
		initFulfilment(fulfill);
		return fulfill;
	}

}
