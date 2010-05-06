package com.lazerinc.servlet.actionhandler.commerce;

import static com.lazerinc.servlet.ActionConstants.ACTION_MARK_ORDER_FULFILLED;
import strategiclibrary.service.webaction.HandlerData;

import com.lazerinc.application.SecurityModel;
import com.lazerinc.client.exceptions.LazerwebException;
import com.lazerinc.ecommerce.Order;
import com.lazerinc.utils.Resource;
import com.lazerinc.utils.Right;

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
