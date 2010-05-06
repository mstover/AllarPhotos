package com.lazerinc.irwin;

import static com.lazerinc.ecommerce.Order.Status.AWAITING_APPROVAL;

import java.util.Collection;

import com.lazerinc.application.Fulfillment;
import com.lazerinc.beans.OrderItem;
import com.lazerinc.ecommerce.CommerceUser;
import com.lazerinc.ecommerce.LazerwebFulfillment;
import com.lazerinc.ecommerce.Merchant;
import com.lazerinc.ecommerce.Order;

public class NewellOrder extends Order {

	@Override
	protected Fulfillment createFulfillmentModel() {
		LazerwebFulfillment fulfill = new NewellFulfillment(getUser());
		initFulfilment(fulfill);
		return fulfill;
	}

	public NewellOrder() {
		setStatus(AWAITING_APPROVAL);
	}

	public NewellOrder(Merchant merchant, CommerceUser user) {
		super(merchant, user);
		setStatus(AWAITING_APPROVAL);
	}

	public NewellOrder(Collection<OrderItem> items) {
		super(items);
		setStatus(AWAITING_APPROVAL);
	}

}
