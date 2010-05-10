package com.allarphoto.servlet.actionhandler.commerce;

import static com.allarphoto.servlet.ActionConstants.ACTION_MARK_ORDER_FULFILLED;
import strategiclibrary.service.webaction.HandlerData;

import com.allarphoto.application.SecurityModel;
import com.allarphoto.client.exceptions.LazerwebException;
import com.allarphoto.ecommerce.Order;
import com.allarphoto.utils.Resource;
import com.allarphoto.utils.Right;

public class MarkOrderFulfilled extends ApproveOrder {

	public String getName() {
		return ACTION_MARK_ORDER_FULFILLED;
	}

	public void performAction(HandlerData actionInfo) throws LazerwebException {
		Order order = getOrder(actionInfo);
		verifyValidOrder(order);
		SecurityModel perms = this.getCurrentUserPerms(actionInfo);
		if (perms.getPermission("all", Resource.DATABASE, Right.ADMIN)) {
			order.setStatus(Order.Status.FULFILLED);
			dbLogger.addOrder(order);
		} else
			throw new LazerwebException(LazerwebException.INVALID_PERMISSION);

	}

}
