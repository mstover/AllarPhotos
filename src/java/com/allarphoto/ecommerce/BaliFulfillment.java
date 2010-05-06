package com.lazerinc.ecommerce;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.lazerinc.beans.OrderItem;
import com.lazerinc.utils.TinyUrl;

public class BaliFulfillment extends LazerwebFulfillment {

	public BaliFulfillment(CommerceUser user) {
		super(user);
	}

	@Override
	public boolean notifyFulfillment(Order order, Merchant merchant) {
		Calendar cal = new GregorianCalendar();
		String subject = "Product order from LazerWeb";
		StringBuffer message = new StringBuffer(
				"***** ORDER FROM THE ONLINE LIBRARY *****" + nl + nl);
		messageBasics(order, message);

		appendProductListing(order, message, false);
		// added for special instructions
		appendOrderInfo(order, "User supplied instructions:", message);
		// (cal.get(Calendar.MONTH)+1) corrects a bug in java where
		// Calendar.MONTH = {0...11}
		message.append("Date Ordered: " + (cal.get(Calendar.MONTH) + 1) + "/"
				+ cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR)
				+ nl);
		try {
			notifier.sendMessage(new String[] { FULFILLMENT_LAZERINC_COM },
					user.getEmailAddress(), subject, "text/plain", message
							.toString());
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	private void messageBasics(Order order, StringBuffer message) {
		message.append("Order Number: " + order.getOrderNo() + nl + nl);
		message.append("\tUser info:" + nl);
		message.append("Name: " + user.getFirstName() + " "
				+ user.getMiddleInitial() + " " + user.getLastName() + nl);
		message.append("Billing Address:" + nl);
		message.append("\t" + user.getBillAddress1() + nl);
		if (user.getBillAddress2() != null
				&& !user.getBillAddress2().equals(""))
			message.append("\t" + user.getBillAddress2() + nl);
		message.append("\t" + user.getBillCity() + ", " + user.getBillState()
				+ " " + user.getBillZip() + nl);
		message.append("\t" + user.getBillCountry() + nl);
		message.append("Phone: " + user.getPhone() + nl);
		message.append("Company: " + user.getCompany().getName() + nl);
		message.append("Email: " + user.getEmailAddress() + nl);
		log.info("order address = " + order.getShippingAddress() + " user = "
				+ user);
		if (!order.getShippingAddress().equals(user.getShippingAddress())) {
			message.append("Shipping Address:" + nl);
			message
					.append("ATTN: " + order.getShippingAddress().getAttn()
							+ nl);
			message.append("Company: "
					+ order.getShippingAddress().getCompany() + nl);
			message.append("Shipping Phone: "
					+ order.getShippingAddress().getPhone() + nl);
			message
					.append("\t" + order.getShippingAddress().getAddress1()
							+ nl);
			if (order.getShippingAddress().getAddress2() != null
					&& !order.getShippingAddress().getAddress2().equals(""))
				message.append("\t" + order.getShippingAddress().getAddress2()
						+ nl);
			message.append("\t" + order.getShippingAddress().getCity() + ", "
					+ order.getShippingAddress().getState().getCode() + " "
					+ order.getShippingAddress().getZip() + nl);
			message.append("\t" + order.getShippingAddress().getCountry() + nl);
		} else
			message
					.append("Shipping address is the same as billing address above."
							+ nl);
		message.append(nl);
	}

	@Override
	public boolean sendApprovalRequest(Order order, Merchant merchant) {
		String subject = "Approval Request - Product order from LazerWeb";
		StringBuffer msg = new StringBuffer(
				"***** ORDER RECEIVED FOR THE ONLINE LIBRARY *****" + nl + nl);
		msg
				.append("This order requires approval before it will be fulfilled.  Please visit the provided link to approve the order: "
						+ nl + nl);

		msg.append(new TinyUrl("http://"
				+ controller.getConfigValue("web_host")
				+ controller.getConfigValue("href-base")
				+ "view_order.jsp?request_order_number=" + order.getOrderNo())
				.getUrl());
		msg.append(nl).append(nl);
		msg.append("***** ORDER DETAILS *****").append(nl).append(nl);
		messageBasics(order, msg);
		appendProductListing(order, msg, true);
		appendOrderInfo(order, "User supplied instructions:", msg);
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

	protected String getFromEmail(CommerceUser user) {
		return FULFILLMENT_LAZERINC_COM;
	}

	@Override
	protected boolean appendProductListing(Order order, StringBuffer message,
			boolean listRejectedFiles) {
		message.append("Ordered Products (from " + order.getValue("family")
				+ "):" + nl);
		for (OrderItem item : order) {
			if (listRejectedFiles
					|| !"rejected".equals(item.getValue("status"))) {
				message.append("\t" + item.getValue("file"));
				if ("rejected".equals(item.getValue("status")))
					message.append(" - rejected");
				message.append(nl);
			}
		}
		return true;
	}

	@Override
	public boolean notifyMerchant(Order order, Merchant merchant) {
		Calendar cal = new GregorianCalendar();
		String emailAddress = merchant.getFulfillmentEmail();
		String host = controller.getConfigValue("email_host");
		String subject = "Product order from LazerWeb";
		String from = user.getEmailAddress();
		StringBuffer retMessage = new StringBuffer(
				"***** ORDER RECEIVED FOR THE ONLINE LIBRARY *****" + nl + nl);

		retMessage.append("We have received your order for the items below."
				+ nl);
		retMessage.append("If you have any questions, you may contact us at "
				+ emailAddress + nl + nl);
		retMessage.append("Order Number: " + order.getOrderNo() + nl + nl);

		appendProductListing(order, retMessage, false);
		// added for special instructions

		appendOrderInfo(order, "Your supplied instructions:", retMessage);
		retMessage.append(nl + "Date Ordered: " + (cal.get(Calendar.MONTH) + 1)
				+ "/" + cal.get(Calendar.DAY_OF_MONTH) + "/"
				+ cal.get(Calendar.YEAR) + nl + nl);
		retMessage.append("User info:" + nl);
		retMessage.append("\t" + "Name: " + user.getFirstName() + " ");
		if (null != user.getMiddleInitial()
				&& !user.getMiddleInitial().equals("N/A")) {
			retMessage.append(user.getMiddleInitial() + " ");
		}
		retMessage.append(user.getLastName() + nl);
		retMessage.append("\t" + "Phone: " + user.getPhone() + nl);
		retMessage
				.append("\t" + "Company: " + user.getCompany().getName() + nl);
		retMessage.append("\t" + "Email: " + user.getEmailAddress() + nl);
		retMessage.append(nl + "Shipping Address:" + nl);
		retMessage.append("\t" + "Company: "
				+ order.getShippingAddress().getCompany() + nl);
		retMessage.append("\t" + "ATTN: "
				+ order.getShippingAddress().getAttn() + nl);
		retMessage.append("\t" + "Phone: "
				+ order.getShippingAddress().getPhone() + nl);
		retMessage.append("\t" + order.getShippingAddress().getAddress1() + nl);
		if (order.getShippingAddress().getAddress2() != null
				&& !order.getShippingAddress().getAddress2().equals(""))
			retMessage.append("\t" + order.getShippingAddress().getAddress2()
					+ nl);
		retMessage.append("\t" + order.getShippingAddress().getCity() + ", "
				+ order.getShippingAddress().getState().getCode() + " "
				+ order.getShippingAddress().getZip() + nl);
		retMessage.append("\t" + order.getShippingAddress().getCountry() + nl);
		retMessage.append(nl);
		try {
			notifier.sendMessage(new String[] { user.getEmailAddress() },
					merchant.getFulfillmentEmail(), subject, "text/plain",
					retMessage.toString());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
