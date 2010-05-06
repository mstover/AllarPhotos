package com.lazerinc.servlet.actionhandler.commerce;

import static com.lazerinc.servlet.ActionConstants.ACTION_GET_ORDER_DETAILS;
import static com.lazerinc.servlet.RequestConstants.REQUEST_ORDER_NUMBER;
import strategiclibrary.service.webaction.HandlerData;

import com.lazerinc.application.SecurityModel;
import com.lazerinc.client.beans.UserBean;
import com.lazerinc.client.exceptions.LazerwebException;
import com.lazerinc.ecommerce.Order;
import com.lazerinc.utils.Resource;
import com.lazerinc.utils.Right;

/**
 * After all the instructions have been set in the users shopping cart, this
 * action will cause the execution of the ordering instructions.
 * 
 * @author Administrator
 * @action action_execute_order
 * @requestParam ShipTo Adds information about who to ship the order to.
 * @requestParam SoldTo Adds information about who is being billed for the
 *               order.
 * @requestParam accountName Adds information about which account the order is
 *               for.
 * @requestParam instructions Adds special, custom instructions to the order
 * @requestParam dlOnly True or False indicating whether the order represents
 *               only downloading of files.
 * @bean ShoppingCartBean cart The information built up in the shopping cart is
 *       used to fulfill the order. Additional information may be set in the
 *       order. After execution, the shopping cart is cleared of all products
 *       that participated in the order.
 * @bean OrderResponseBean orderResponse An order response bean is created that
 *       holds all the information regarding the results of executing the order.
 */
public class GetOrderDetails extends ApproveOrder {

	/**
	 * @see com.lazerinc.servlet.actionhandler.ActionHandler#performAction(HandlerData)
	 */
	public void performAction(HandlerData actionInfo) throws LazerwebException {
		String orderNumber = actionInfo.getParameter(REQUEST_ORDER_NUMBER);

		Order order = dbLogger.getOrder(orderNumber,
				getCurrentUserPerms(actionInfo));
		if (order == null)
			throw new LazerwebException(LazerwebException.EMPTY_ORDER);
		order.setSecurity(getCurrentUserPerms(actionInfo));
		SecurityModel perms = this.getCurrentUserPerms(actionInfo);
		UserBean userBean = getUserBean(actionInfo);
		String datatable = dbUtil.getProductFamilyFromDescription(
				order.getValue("family")).getTableName();
		if (order.getUser().getUsername().equals(userBean.getUsername())
				|| perms.getPermission(datatable, Resource.DATATABLE,
						Right.ADMIN)) {
			actionInfo.setRequestBean("order", order);
			if (perms.getPermission("all", Resource.DATABASE, Right.ADMIN))
				actionInfo.setRequestBean("canFulfill", Boolean.TRUE);
			else
				actionInfo.setRequestBean("canFulfill", Boolean.FALSE);
			getLog().info(order.toString());
		} else
			throw new LazerwebException(LazerwebException.INVALID_PERMISSION);

	}

	/**
	 * @see com.lazerinc.servlet.actionhandler.ActionHandler#getActionName()
	 */
	public String getName() {
		return ACTION_GET_ORDER_DETAILS;
	}
}
