package com.allarphoto.irwin;

import static com.allarphoto.ecommerce.Order.Status.AWAITING_APPROVAL;

import java.util.Collection;

import com.allarphoto.application.Fulfillment;
import com.allarphoto.beans.OrderItem;
import com.allarphoto.ecommerce.CommerceUser;
import com.allarphoto.ecommerce.LazerwebFulfillment;
import com.allarphoto.ecommerce.Merchant;
import com.allarphoto.ecommerce.Order;

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
