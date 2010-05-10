package com.allarphoto.ecommerce;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaDynamic;
import org.coinjema.context.CoinjemaObject;

import strategiclibrary.service.notification.NotificationService;

import com.allarphoto.application.Controller;
import com.allarphoto.application.Fulfillment;
import com.allarphoto.application.Product;
import com.allarphoto.application.SecureComponent;
import com.allarphoto.application.SecurityModel;
import com.allarphoto.beans.OrderItem;
import com.allarphoto.category.ProductField;
import com.allarphoto.server.ResourceService;
import com.allarphoto.utils.Functions;
import com.allarphoto.utils.Resource;
import com.allarphoto.utils.Right;
import com.allarphoto.utils.TinyUrl;

@CoinjemaObject(type = "fulfiller")
public class LazerwebFulfillment implements Fulfillment, SecureComponent {
	protected NotificationService notifier;

	protected static String nl = "\n"; // System.getProperty("line.separator");

	protected static Logger log = LazerwebFulfillment.getLogger();

	protected ResourceService resService;

	@CoinjemaDynamic(alias = "log4j")
	private static Logger getLogger() {
		return null;
	}

	@CoinjemaDependency(type = "resourceService")
	public void setResourceService(ResourceService rs) {
		resService = rs;
	}

	protected static final String FULFILLMENT_LAZERINC_COM = "fulfillment@lazerinc.com";

	protected CommerceUser user;

	protected Controller controller;

	protected SecurityModel security;

	public LazerwebFulfillment(CommerceUser user) {
		this.user = user;
	}

	public void setSecurity(SecurityModel s) {
		security = s;
	}

	@CoinjemaDependency(method = "emailService")
	public void setNotifier(NotificationService s) {
		notifier = s;
	}

	public boolean notifyFulfillment(Order order, Merchant merchant) {
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
			notifier.sendMessage(new String[] { FULFILLMENT_LAZERINC_COM },
					user.getEmailAddress(), subject, "text/plain", message
							.toString());
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	protected String getFromEmail(CommerceUser user) {
		return user.getEmailAddress();
	}

	protected boolean hasPermission(Product p, Right right) {
		boolean hasRight = security.getPermission(p.getProductFamily()
				.getTableName(), Resource.DATATABLE, right);
		for (ProductField field : p.getProductFamily().getFields()) {
			if (field.getType() < 0) {
				Resource res = resService
						.getResource(p.getProductFamily().getTableName() + "."
								+ field.getName() + "."
								+ p.getValue(field.getName()),
								Resource.PROTECTED_FIELD);
				hasRight = hasRight && security.getPermission(res, right);
			}
		}
		return hasRight;
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
						message.append("\t\t").append(entry.getKey()).append(
								": ").append(entry.getValue()).append(nl);
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

	protected void appendOrderInfo(Order order, String headerMsg,
			StringBuffer message) {
		message.append(nl + headerMsg + nl);
		for (Map.Entry<String, String> info : order.infoIterator()) {
			if (filterOrderInfo(info)) {
				String tempInstr = Functions.stripString(Functions.stripString(
						info.getValue(), "["), "]");
				if (headerMsg != null && headerMsg.length() > 0)
					message.append("\t");
				message.append(info.getKey()).append(": ").append(tempInstr)
						.append(nl);
			}
		}
	}

	protected boolean filterOrderInfo(Map.Entry<String, String> info) {
		return !info.getKey().equals("WIN") && !info.getKey().equals("MAC")
				&& !info.getKey().equals("family")
				&& !info.getKey().equals("Order.class")
				&& !info.getKey().equals("orderNo")
				&& !info.getKey().equals("user")
				&& !info.getKey().equals("Total Cost")
				&& !info.getKey().equals("merchant")
				&& !info.getKey().equals("status");
	}

	public boolean notifyMerchant(Order order, Merchant merchant) {
		Calendar cal = new GregorianCalendar();
		String host = controller.getConfigValue("email_host");
		String subject = "Product order from LazerWeb";
		String from = user.getEmailAddress();
		StringBuffer retMessage = new StringBuffer();
		boolean error = false;

		retMessage.append("We have received your order for the items below."
				+ nl);
		retMessage.append("If you have any questions, you may contact us at "
				+ merchant.getFulfillmentEmail() + nl + nl);

		error = error || !appendProductListing(order, retMessage, false);
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
		retMessage.append("Phone: " + user.getPhone() + nl);
		retMessage.append("Fax: " + user.getFax() + nl);
		retMessage.append("Company: " + user.getCompany().getName() + nl);
		retMessage.append("Email: " + user.getEmailAddress() + nl);
		retMessage.append("Shipping Address:" + nl);
		retMessage.append("\t" + order.getShippingAddress().getAddress1() + nl);
		if (order.getShippingAddress().getAddress2() != null
				&& !order.getShippingAddress().getAddress2().equals(""))
			retMessage.append("\t" + user.getShipAddress2() + nl);
		retMessage.append("\t" + order.getShippingAddress().getCity() + ", "
				+ order.getShippingAddress().getState().getCode() + " "
				+ order.getShippingAddress().getZip() + nl);
		retMessage.append("\t"
				+ order.getShippingAddress().getCountry().getName() + nl);
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

	public boolean sendApprovalRequest(Order order, Merchant merchant) {
		String subject = "Approval Request - Product order from LazerWeb";
		StringBuffer msg = new StringBuffer(
				"***** ORDER RECEIVED FOR THE ONLINE LIBRARY *****" + nl + nl);
		msg
				.append("This order requires approval before it will be fulfilled.  Please visit the provided link to approve the order: "
						+ nl + nl);

		msg.append(createTinyUrl(order)
				.getUrl());
		msg.append(nl).append(nl);
		appendProductListing(order, msg, true);
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

	protected TinyUrl createTinyUrl(Order order) {
		return new TinyUrl("http://"
				+ controller.getConfigValue("web_host")
				+ controller.getConfigValue("href-base")
				+ "view_order.jsp?request_order_number=" + order.getOrderNo());
	}

	public boolean sendConfirmationRequest(Order order, Merchant merchant) {
		String subject = "Confirm Partial Request - Product order from LazerWeb";
		StringBuffer msg = new StringBuffer(
				"***** ORDER RECEIVED FOR THE ONLINE LIBRARY *****" + nl + nl);
		msg
				.append("Your order has been partially approved.  Some files were rejected from the order for the following reason:");
		msg.append(nl).append(nl);
		msg.append(order.getValue("approval-comment"));
		msg.append(nl).append(nl);
		msg
				.append("Please visit the provided link to confirm you still want the order fulfilled: "
						+ nl + nl);

		msg.append(createTinyUrl(order)
				.getUrl());
		msg.append(nl).append(nl);
		appendProductListing(order, msg, true);
		try {
			notifier.sendMessage(new String[] { user.getEmailAddress() },
					merchant.getFulfillmentEmail(), subject, "text/plain", msg
							.toString());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean sendCancellationNotification(Order order, Merchant merchant) {
		String subject = "Order Cancellation - Product order from LazerWeb";
		StringBuffer msg = new StringBuffer(
				"***** ORDER CANCELLED FOR THE ONLINE LIBRARY *****" + nl + nl);
		msg.append("Your order has been cancelled per your request.");
		msg.append(nl).append(nl);
		appendOrderInfo(order, "Your Order", msg);
		appendProductListing(order, msg, true);
		try {
			notifier.sendMessage(new String[] { user.getEmailAddress() },
					merchant.getFulfillmentEmail(), subject, "text/plain", msg
							.toString());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean sendRejectNotification(Order order, Merchant merchant) {
		String subject = "Order Rejected - Product order from LazerWeb";
		StringBuffer msg = new StringBuffer(
				"***** ORDER REJECTED FOR THE ONLINE LIBRARY *****" + nl + nl);
		msg.append("Your order has been rejected for the following reason:");
		msg.append(nl).append(nl);
		msg.append(order.getValue("approval-comment"));
		msg.append(nl).append(nl);
		appendOrderInfo(order, "Your Order", msg);
		appendProductListing(order, msg, true);
		try {
			notifier.sendMessage(new String[] { user.getEmailAddress() },
					merchant.getFulfillmentEmail(), subject, "text/plain", msg
							.toString());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@CoinjemaDependency(type = "appController", method = "appController")
	public void setController(Controller controller) {
		this.controller = controller;
	}

}
