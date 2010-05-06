package com.lazerinc.ecommerce;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import com.lazerinc.beans.OrderItem;
import com.lazerinc.utils.Functions;
import com.lazerinc.utils.Right;

public class KodakDaiFulfillment extends LazerwebFulfillment {

	@Override
	public boolean notifyMerchant(Order order, Merchant merchant) {
		StringBuffer retMessage = new StringBuffer();
		String subject = "Product order from LazerWeb";
		boolean error = false;
		retMessage.append("We have received your order for the items below."
				+ nl);
		retMessage.append("If you have any questions, you may contact us at "
				+ merchant.getFulfillmentEmail() + nl + nl);
		error = error || !appendProductListing(order, retMessage, true);
		GregorianCalendar cal = new GregorianCalendar();
		retMessage.append(nl + "Date Ordered: " + (cal.get(Calendar.MONTH) + 1)
				+ "/" + cal.get(Calendar.DAY_OF_MONTH) + "/"
				+ cal.get(Calendar.YEAR) + nl + nl);
		retMessage.append("User info:" + nl);
		retMessage.append("Name: " + user.getFirstName() + " ");
		if (null != user.getMiddleInitial()
				&& !user.getMiddleInitial().equals("N/A")) {
			retMessage.append(user.getMiddleInitial() + " ");
		}
		retMessage.append(user.getLastName() + nl);
		retMessage.append("Phone: " + user.getPhone() + nl);
		retMessage.append("Fax: " + user.getFax() + nl);
		retMessage.append("Company: " + user.getCompany().getName() + nl);
		retMessage.append("Email: " + user.getEmailAddress() + nl);
		retMessage.append("Shipping Address:" + nl);
		retMessage.append("\t" + order.getShippingAddress().getAddress1() + nl);
		if (order.getShippingAddress().getAddress2() != null
				&& !order.getShippingAddress().getAddress2().equals("")
				&& !order.getShippingAddress().getAddress2().equals("N/A"))
			retMessage.append("\t" + order.getShippingAddress().getAddress2()
					+ nl);
		retMessage.append("\t" + order.getShippingAddress().getCity() + ", "
				+ order.getShippingAddress().getState().getCode() + " "
				+ order.getShippingAddress().getZip() + nl);
		retMessage.append("\t" + order.getShippingAddress().getCountry() + nl);
		retMessage.append(nl);
		if (error) {
			retMessage.insert(0,
					"ATTENTION!! ERRORS with some files below. Please read file "
							+ "list below for details." + nl);
		}
		try {
			notifier.sendMessage(new String[] { user.getEmailAddress() },
					merchant.getFulfillmentEmail(), subject, "text/plain",
					retMessage.toString());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public KodakDaiFulfillment(CommerceUser user) {
		super(user);
	}

	@Override
	protected boolean appendProductListing(Order order, StringBuffer message,
			boolean listRejectedFiles) {
		message.append("Ordered Products (from " + order.getValue("family")
				+ ") and associated instructions:" + nl);
		boolean error = false;
		for (OrderItem item : order) {
			if (listRejectedFiles
					|| !"rejected".equals(item.getValue("status"))) {
				if (hasPermission(item.getProduct(security), Right.ORDER)) {
					message.append("\t" + item.getValue("file") + nl);
					for (Map.Entry<String, String> entry : item
							.getProductValues()) {
						if (filterOrderProductInfo(entry))
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
		return !error;
	}

	protected boolean filterOrderProductInfo(Map.Entry<String, String> info) {
		return !info.getKey().equals("Delivery Format")
				&& !info.getKey().equals("cost")
				&& !info.getKey().equals("order");
	}

	public boolean notifyFulfillment(Order order, Merchant merchant) {
		Calendar cal = new GregorianCalendar();
		boolean errorFlag = false;
		String subject = "Product order from LazerWeb";
		StringBuffer message = new StringBuffer();
		message.append("Order Number: ").append(order.getOrderNo()).append(nl)
				.append(nl);
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
		message.append("Shipping Address:" + nl);
		message.append("\t" + order.getShippingAddress().getAddress1() + nl);
		if (order.getShippingAddress().getAddress2() != null
				&& !order.getShippingAddress().getAddress2().equals("")
				&& !order.getShippingAddress().getAddress2().equals("N/A"))
			message
					.append("\t" + order.getShippingAddress().getAddress2()
							+ nl);
		message.append("\t" + order.getShippingAddress().getCity() + ", "
				+ order.getShippingAddress().getState().getCode() + " "
				+ order.getShippingAddress().getZip() + nl);
		message.append("\t" + order.getShippingAddress().getCountry() + nl);
		message.append(nl);
		message.append(
				"Total Bill amount: " + order.getInfoMap().get("Total Cost"))
				.append(nl);

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
			notifier.sendMessage(new String[] { FULFILLMENT_LAZERINC_COM },
					user.getEmailAddress(), subject, "text/plain", message
							.toString());
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	@Override
	protected void appendOrderInfo(Order order, String headerMsg,
			StringBuffer message) {
		message.append(nl + headerMsg + nl);
		for (Map.Entry<String, String> info : order.infoIterator()) {
			if (filterOrderInfo(info)) {
				String tempInstr = Functions.stripString(Functions.stripString(
						info.getValue(), "["), "]");
				message.append(info.getKey() + ": " + tempInstr + nl);
			}
		}
	}

}
