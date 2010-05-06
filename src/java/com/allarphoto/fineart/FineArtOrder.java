package com.lazerinc.fineart;

import static com.lazerinc.ecommerce.Order.Status.AWAITING_APPROVAL;

import java.util.Collection;

import com.lazerinc.application.Fulfillment;
import com.lazerinc.beans.OrderItem;
import com.lazerinc.ecommerce.CommerceUser;
import com.lazerinc.ecommerce.LazerwebFulfillment;
import com.lazerinc.ecommerce.Merchant;
import com.lazerinc.ecommerce.Order;

public class FineArtOrder extends Order {

	@Override
	protected Fulfillment createFulfillmentModel() {
		LazerwebFulfillment fulfill = new FineArtFulfillment(getUser());
		initFulfilment(fulfill);
		return fulfill;
	}

	public FineArtOrder() {
		setStatus(AWAITING_APPROVAL);
	}

	public FineArtOrder(Merchant merchant, CommerceUser user) {
		super(merchant, user);
		setStatus(AWAITING_APPROVAL);
	}

	public FineArtOrder(Collection<OrderItem> items) {
		super(items);
		setStatus(AWAITING_APPROVAL);
	}

}
