package com.lazerinc.irwin;

import java.util.Map;

import com.lazerinc.beans.OrderItem;
import com.lazerinc.ecommerce.CommerceUser;
import com.lazerinc.ecommerce.LazerwebFulfillment;
import com.lazerinc.ecommerce.Merchant;
import com.lazerinc.ecommerce.Order;
import com.lazerinc.utils.Right;
import com.lazerinc.utils.TinyUrl;

public class NewellFulfillment extends LazerwebFulfillment {

	public NewellFulfillment(CommerceUser user) {
		super(user);
	}

	@Override
	protected TinyUrl createTinyUrl(Order order) {
		return new TinyUrl("http://" + controller.getConfigValue("web_host")
				+ "/lazerweb/admin.jsp?section=ViewOrders");
	}

	public boolean sendApprovalRequest(Order order, Merchant merchant) {
		String subject = "Approval Request - Product order from LazerWeb";
		StringBuffer msg = new StringBuffer(
				"***** ORDER RECEIVED FOR THE ONLINE LIBRARY *****" + nl + nl);
		msg
				.append("This order requires approval before it will be fulfilled.  Please visit the provided link to approve the order: "
						+ nl + nl);

		msg.append(createTinyUrl(order).getUrl());
		msg.append(nl).append(nl);
		appendProductListing(order, msg, true);
		appendOrderInfo(order, "", msg);
		try {
			notifier.sendMessage(
					new String[] { merchant.getFulfillmentEmail() },
					FULFILLMENT_LAZERINC_COM, subject, "text/plain", msg
							.toString());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	protected boolean filterOrderInfo(Map.Entry<String, String> info) {
		return !info.getKey().equals("WIN") && !info.getKey().equals("MAC")
				&& !info.getKey().equals("family")
				&& !info.getKey().equals("Order.class")
				&& !info.getKey().equals("orderNo")
				&& !info.getKey().equals("user")
				&& !info.getKey().equals("merchant")
				&& !info.getKey().equals("status");
	}

	protected boolean appendProductListing(Order order, StringBuffer message,
			boolean listRejectedFiles) {
		boolean error = false;
		message.append("Ordered Products (from " + order.getValue("family")
				+ ") and associated instructions:" + nl);
		for (OrderItem item : order) {
			if (listRejectedFiles
					|| !"rejected".equals(item.getValue("status"))) {
				if (hasPermission(item.getProduct(security), Right.ORDER)) {
					message.append("\t" + item.getValue("file"));
					if ("rejected".equals(item.getValue("status")))
						message.append(" - rejected");
					message.append(nl);
					for (Map.Entry<String, String> entry : item
							.getProductValues()) {
						if (!entry.getKey().toLowerCase().equals("cost"))
							message.append("\t\t").append(entry.getKey())
									.append(": ").append(entry.getValue())
									.append(nl);
					}
				} else {
					error = true;
					message.append("ERROR:\t" + item.getValue("file") + nl
							+ "no permissions to order" + nl);
				}
			}
		}
		message.append("-------------------------------------------- " + nl);
		return !error;
	}

}
