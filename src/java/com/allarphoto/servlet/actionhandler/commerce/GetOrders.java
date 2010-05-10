package com.allarphoto.servlet.actionhandler.commerce;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import strategiclibrary.service.webaction.HandlerData;
import strategiclibrary.util.Converter;

import com.allarphoto.application.SecurityModel;
import com.allarphoto.client.beans.DateBean;
import com.allarphoto.client.beans.UserBean;
import com.allarphoto.client.exceptions.LazerwebException;
import com.allarphoto.ecommerce.DatabaseUtilities;
import com.allarphoto.ecommerce.Order;
import com.allarphoto.utils.Resource;
import com.allarphoto.utils.Right;

public class GetOrders extends ApproveOrder {

	public GetOrders() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void performAction(HandlerData actionInfo) throws LazerwebException {
		String status = actionInfo.getParameter("order_status");
		if (status == null)
			status = Order.Status.AWAITING_APPROVAL.value();
		Calendar selectedDate = Converter.getCalendar(actionInfo
				.getParameter("order_date"), new DateBean().lastYear());
		Collection<Order> orders = dbLogger.getOrders(status, selectedDate,
				getCurrentUserPerms(actionInfo));
		filterByPermissions(orders, getCurrentUserPerms(actionInfo), dbUtil,
				getUserBean(actionInfo));
		if (!(orders instanceof List)) {
			orders = new LinkedList<Order>(orders);
		}
		Collections.sort((List) orders);
		actionInfo.setRequestBean("orders", orders);
		actionInfo.setRequestBean("status", status);
		actionInfo.setRequestBean("selectedDate", selectedDate);
	}

	protected void filterByPermissions(Collection<Order> orders,
			SecurityModel perms, DatabaseUtilities dbUtil, UserBean userBean) {
		Iterator<Order> iter = orders.iterator();
		while (iter.hasNext()) {
			Order order = iter.next();
			String datatable = dbUtil.getProductFamilyFromDescription(
					order.getValue("family")).getTableName();
			if (!order.getUser().getUsername().equals(userBean.getUsername())
					&& !perms.getPermission(datatable, Resource.DATATABLE,
							Right.ADMIN)) {
				iter.remove();
			}
		}
	}

	public String getName() {
		return "action_get_orders";
	}

}
