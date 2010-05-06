package com.lazerinc.ecommerce;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class WorldKitchenFulfillment extends LazerwebFulfillment {

	public WorldKitchenFulfillment(CommerceUser user) {
		super(user);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean notifyFulfillment(Order order, Merchant merchant) {
		this.log.info("World Kitchen notiffy fulfillment to  "
				+ merchant.getName());
		Calendar cal = new GregorianCalendar();
		boolean errorFlag = false;
		String host = controller.getConfigValue("email_host");
		String subject = "Product order from LazerWeb";
		String from = user.getEmailAddress();
		StringBuffer message = new StringBuffer();
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
		message.append("Fax: " + user.getFax() + nl);
		message.append("Company: " + user.getCompany().getName() + nl);
		message.append("Email: " + user.getEmailAddress() + nl);
		if (!order.getShippingAddress().equals(user.getShippingAddress())) {
			message.append("Shipping Address:" + nl);
			message
					.append("\t" + order.getShippingAddress().getAddress1()
							+ nl);
			if (order.getShippingAddress().getAddress2() != null
					&& !order.getShippingAddress().getAddress2().equals(""))
				message.append("\t" + order.getShippingAddress().getAddress2()
						+ nl);
			message.append("\t"
					+ order.getShippingAddress().getCity().getName() + ", "
					+ order.getShippingAddress().getState().getCode() + " "
					+ order.getShippingAddress().getZip() + nl);
			message.append("\t"
					+ order.getShippingAddress().getCountry().getName() + nl);
		} else
			message
					.append("Shipping address is the same as billing address above."
							+ nl);
		message.append(nl);

		errorFlag = errorFlag || !appendProductListing(order, message, false);
		// added for special instructions
		appendOrderInfo(order, "", message);
		// (cal.get(Calendar.MONTH)+1) corrects a bug in java where
		// Calendar.MONTH = {0...11}
		message.append("Date Ordered: " + (cal.get(Calendar.MONTH) + 1) + "/"
				+ cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR)
				+ nl);
		if (errorFlag) {
			message.insert(0,
					"ATTENTION!! ERRORS with some files below. Please read file "
							+ "list carefully before fullfilling order." + nl
							+ "=============================================="
							+ nl);
		}

		try {
			notifier.sendMessage(
					new String[] { merchant.getFulfillmentEmail() }, user
							.getEmailAddress(), subject, "text/plain", message
							.toString());
			return true;
		} catch (Exception e) {
			return false;
		}

	}
}
