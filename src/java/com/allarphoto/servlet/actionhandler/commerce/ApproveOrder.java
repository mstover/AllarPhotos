package com.lazerinc.servlet.actionhandler.commerce;

import static com.lazerinc.servlet.ActionConstants.ACTION_APPROVE_ORDER;

import org.coinjema.context.CoinjemaDependency;

import strategiclibrary.service.webaction.HandlerData;

import com.lazerinc.application.SecurityModel;
import com.lazerinc.beans.OrderItem;
import com.lazerinc.client.beans.UserBean;
import com.lazerinc.client.exceptions.LazerwebException;
import com.lazerinc.ecommerce.Order;
import com.lazerinc.servlet.actionhandler.ActionHandlerBase;
import com.lazerinc.utils.DatabaseLogger;
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
public class ApproveOrder extends ActionHandlerBase {
	protected DatabaseLogger dbLogger;

	@CoinjemaDependency(type = "databaseLogger", method = "databaseLogger")
	public void setDbLogger(DatabaseLogger dl) {
		dbLogger = dl;
	}

	/**
	 * @see com.lazerinc.servlet.actionhandler.ActionHandler#performAction(HandlerData)
	 */
	public void performAction(HandlerData actionInfo) throws LazerwebException {
		Order order = getOrder(actionInfo);
		verifyValidOrder(order);
		String actionTaken = actionInfo.getParameter("approve_choice");
		order.setValue("approval-comment", actionInfo
				.getParameter("approval_comment"));
		UserBean user = (UserBean) actionInfo.getUserBean("user");
		order.setValue("Approver", user.getLastName() + ", "
				+ user.getFirstName());
		SecurityModel perms = this.getCurrentUserPerms(actionInfo);
		String datatable = dbUtil.getProductFamilyFromDescription(
				order.getValue("family")).getTableName();
		if (perms.getPermission(datatable, Resource.DATATABLE, Right.ADMIN)) {
			try {
				if (isFullApproval(actionInfo)) {
					setApprovalOnItems(actionInfo, order, true);
					order.fulfill();
					actionInfo.setRequestBean("approvalResult",
							"Order Successfully Approved");
				} else if (isPartialApproval(actionInfo)) {
					setApprovalOnItems(actionInfo, order, true);
					order.confirmPartial();
					actionInfo.setRequestBean("approvalResult",
							"Order Successfully Approved (Partial)");
				} else {
					setApprovalOnItems(actionInfo, order, false);
					actionInfo.setRequestBean("approvalResult",
							"Order Rejected.  Notification sent to user.");
				}
			} finally {
				dbLogger.addOrder(order);
			}
		} else
			throw new LazerwebException(LazerwebException.INVALID_PERMISSION);
	}

	protected void verifyValidOrder(Order order) throws LazerwebException {
		if (order == null || order.getValue("family") == null)
			throw new LazerwebException(LazerwebException.INVALID_ORDER);
	}

	private boolean isPartialApproval(HandlerData actionInfo) {
		return actionInfo.getParameter("file_reject") != null
				&& (actionInfo.getParameter("approve_choice").equals(
						"Approve Partial Order") || actionInfo.getParameter(
						"approve_choice").equals("Approve Entire Order"));
	}

	private boolean isFullApproval(HandlerData actionInfo) {
		return actionInfo.getParameter("file_reject") == null
				&& (actionInfo.getParameter("approve_choice").equals(
						"Approve Partial Order") || actionInfo.getParameter(
						"approve_choice").equals("Approve Entire Order"));
	}

	protected Order getOrder(HandlerData actionInfo) {
		Order order = (Order) actionInfo.getRequestBean("order");
		return order;
	}

	private void setApprovalOnItems(HandlerData actionInfo, Order order,
			boolean approve) {
		if (approve) {
			order.setStatus(Order.Status.AWAITING_FULFILLMENT);
			boolean confirmationNeeded = false;
			for (OrderItem item : order) {
				for (String reject : actionInfo
						.getParameterValues("file_reject")) {
					if (item.getValue("file").equals(reject)) {
						confirmationNeeded = true;
						item.setValue("status", "rejected");
					}
				}
			}
			if (confirmationNeeded)
				order.setStatus(Order.Status.AWAITING_CONFIRMATION);
		} else {
			order.setStatus(Order.Status.REJECTED);
		}
	}

	/**
	 * @see com.lazerinc.servlet.actionhandler.ActionHandler#getActionName()
	 */
	public String getName() {
		return ACTION_APPROVE_ORDER;
	}
}
