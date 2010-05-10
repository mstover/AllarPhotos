package com.allarphoto.fineart;

import static com.allarphoto.ecommerce.Order.Status.AWAITING_APPROVAL;

import java.util.Collection;

import com.allarphoto.application.Fulfillment;
import com.allarphoto.beans.OrderItem;
import com.allarphoto.ecommerce.CommerceUser;
import com.allarphoto.ecommerce.LazerwebFulfillment;
import com.allarphoto.ecommerce.Merchant;
import com.allarphoto.ecommerce.Order;

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
