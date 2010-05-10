package com.allarphoto.ecommerce;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.allarphoto.beans.OrderItem;
import com.allarphoto.utils.Functions;

public class KodakInkFulfillment extends LazerwebFulfillment {

	public KodakInkFulfillment(CommerceUser user) {
		super(user);
		// TODO Auto-generated constructor stub
	}

	protected boolean appendProductListing(Order order, StringBuffer message,
			boolean listRejectedFiles) {
		message.append("Ordered Products from " + order.getValue("family")
				+ ":" + nl);
		for (OrderItem item : order) {
			if (listRejectedFiles
					|| !"rejected".equals(item.getValue("status"))) {
				String filename = item.getValue("file");
				if (filename.endsWith(".jpg"))
					message
							.append(
									filename
											.substring(0, filename.length() - 4))
							.append(",");
				else
					message.append(filename).append(nl);
				message.append(Functions.stripString(Functions.stripString(item
						.getProductValue("quantity"), "]"), "["));
				message.append(nl);
			}
		}
		return true;
	}

	@Override
	public boolean notifyFulfillment(Order order, Merchant merchant) {
		String host = controller.getConfigValue("email_host");
		String subject = "Inkjet Sample order from Lazer, Inc.";
		StringBuffer message = new StringBuffer();
		message.append("User info:" + nl + nl);
		message.append("Name: " + user.getFirstName() + " "
				+ user.getMiddleInitial() + " " + user.getLastName() + nl);
		message.append("Phone: " + user.getPhone() + nl);
		message.append("Fax: " + user.getFax() + nl);
		message.append("Company: " + user.getCompany().getName() + nl);
		message.append("Email: " + user.getEmailAddress() + nl);
		message.append("Shipping Address:" + nl);
		message.append("\t" + order.getShippingAddress().getAddress1() + nl);
		if (order.getShippingAddress().getAddress2() != null
				&& !order.getShippingAddress().getAddress2().equals(""))
			message
					.append("\t" + order.getShippingAddress().getAddress2()
							+ nl);
		message.append("\t" + order.getShippingAddress().getCity() + ", "
				+ order.getShippingAddress().getState().getCode() + " "
				+ order.getShippingAddress().getZip() + nl);
		message.append("\t" + order.getShippingAddress().getCountry() + nl);
		message.append(nl);
		appendProductListing(order, message, true);

		appendOrderInfo(order, "", message);
		GregorianCalendar cal = new GregorianCalendar();
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

	@Override
	public boolean notifyMerchant(Order order, Merchant merchant) {
		// do not send response to user.
		return true;
	}

	@Override
	protected void appendOrderInfo(Order order, String headerMsg,
			StringBuffer message) {
		// added for special instructions
		if (order.getInfoMap().get("special instructions") != null) {
			String specInst = order.getInfoMap().get("special instructions");
			if (specInst.startsWith("[") && specInst.endsWith("]")) {
				specInst = specInst.substring(1, specInst.length() - 1);
			}
			message.append(nl + "Special instructions: " + specInst + nl + nl);
		}
	}

}
