package com.lazerinc.servlet.actionhandler.commerce;

import static com.lazerinc.servlet.ActionConstants.ACTION_CONFIRM_ORDER;
import strategiclibrary.service.webaction.HandlerData;

import com.lazerinc.application.SecurityModel;
import com.lazerinc.client.beans.UserBean;
import com.lazerinc.client.exceptions.LazerwebException;
import com.lazerinc.ecommerce.Order;
import com.lazerinc.utils.Resource;
import com.lazerinc.utils.Right;

public class ConfirmOrder extends ApproveOrder {

	@Override
	public void performAction(HandlerData actionInfo) throws LazerwebException {
		Order order = getOrder(actionInfo);
		String actionTaken = actionInfo.getParameter("confirm_choice");
		UserBean userBean = getUserBean(actionInfo);
		SecurityModel perms = this.getCurrentUserPerms(actionInfo);
		verifyValidOrder(order);
		String datatable = dbUtil.getProductFamilyFromDescription(
				order.getValue("family")).getTableName();
		if (order.getUser().getUsername().equals(userBean.getUsername())
				|| perms.getPermission(datatable, Resource.DATATABLE,
						Right.ADMIN)) {
			try {
				if (actionTaken.matches(".*[fF]ulfill.*")) {
					order.setStatus(Order.Status.AWAITING_FULFILLMENT);
					order.fulfill();
					actionInfo.setRequestBean("confirmResult",
							"Order Will Be Fulfilled");
				} else {
					order.setStatus(Order.Status.CANCELLED);
					actionInfo.setRequestBean("confirmResult",
							"Order Has Been cancelled");
				}
			} finally {
				dbLogger.addOrder(order);
			}
		} else
			throw new LazerwebException(LazerwebException.INVALID_PERMISSION);
	}

	@Override
	public String getName() {
		return ACTION_CONFIRM_ORDER;
	}

}
