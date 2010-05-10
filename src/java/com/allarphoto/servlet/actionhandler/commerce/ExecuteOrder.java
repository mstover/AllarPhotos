package com.allarphoto.servlet.actionhandler.commerce;

import static com.allarphoto.servlet.ActionConstants.ACTION_EXECUTE_ORDER;

import java.io.File;
import java.util.Iterator;

import strategiclibrary.service.webaction.HandlerData;
import strategiclibrary.service.webaction.ServletHandlerData;

import com.allarphoto.application.OrderingService;
import com.allarphoto.application.Product;
import com.allarphoto.application.ServiceGateway;
import com.allarphoto.client.beans.OrderResponseBean;
import com.allarphoto.client.beans.ShoppingCartBean;
import com.allarphoto.client.beans.UserBean;
import com.allarphoto.client.exceptions.InformationalException;
import com.allarphoto.client.exceptions.LazerwebException;
import com.allarphoto.ecommerce.OrderResponse;
import com.allarphoto.ecommerce.ProductFamily;
import com.allarphoto.servlet.actionhandler.ActionHandlerBase;

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
public class ExecuteOrder extends ActionHandlerBase {

	public static final String SHIP_TO = "ShipTo";

	public static final String SOLD_TO = "SoldTo";

	public static final String ACCOUNT_NAME = "accountName";

	public static final String DOWNLOAD_ONLY = "dlOnly";

	public static final String INSTRUCTIONS = "instructions";

	/**
	 * @see com.allarphoto.servlet.actionhandler.ActionHandler#performAction(HandlerData)
	 */
	public void performAction(HandlerData actionInfo) throws LazerwebException {
		ShoppingCartBean cart = (ShoppingCartBean) actionInfo.getBean("cart");

		cart.addInstruction("special instructions", actionInfo
				.getParameter(INSTRUCTIONS));
		addCustomInstructions(cart, actionInfo);

		getLog().debug(
				"ExecuteOrder: cart contents: " + cart.saveCartToString());
		executeOrder(actionInfo, cart);
	}

	protected void executeOrder(HandlerData actionInfo, ShoppingCartBean cart)
			throws InformationalException, LazerwebException {
		UserBean userBean = getUserBean(actionInfo);
		if (cart.getInstruction("special instructions") != null
				&& cart.getInstruction("special instructions").toString()
						.length() > 252) {
			cart.clearGlobalInstructions();
			throw new LazerwebException("InvalidOrderInfo");
		}
		if (userBean.getClientType() == null) {
			userBean.setClientType(getClient(actionInfo));
		}
		OrderResponse response = generateOrderResponse(cart, userBean,
				actionInfo);
		OrderResponseBean orderBean = new OrderResponseBean();
		actionInfo.setRequestBean("orderResponse", orderBean);
		orderBean.setResponse(response);
		if (checkDownloadFiles(actionInfo, response)) {
			removeProductsFromCart(actionInfo, response);
		} else {
			throw new InformationalException(
					LazerwebException.BAD_DOWNLOAD_FILE);
		}
		cart.clearGlobalInstructions();
		cart.clearInstructions();
	}

	protected void addCustomInstructions(ShoppingCartBean cart, HandlerData info)
			throws LazerwebException {

	}

	protected OrderResponse generateOrderResponse(ShoppingCartBean cart,
			UserBean userBean, HandlerData info) throws LazerwebException {
		if (cart.getInstruction("special instructions") != null
				&& cart.getInstruction("special instructions").toString()
						.length() > 252) {
			cart.clearGlobalInstructions();
			throw new LazerwebException("InvalidOrderInfo");
		}
		if (info.getBean("orderShipAddress") != null)
			cart.getGlobalInstructions().put("orderShipAddress",
					info.getBean("orderShipAddress"));
		else
			cart.getGlobalInstructions().put("orderShipAddress",
					userBean.getUser().getShippingAddress());
		OrderResponse response = new OrderResponse();
		OrderingService model;
		Iterator it = cart.getFamilyLists().keySet().iterator();
		ProductFamily pf;
		while (it.hasNext()) {
			pf = (ProductFamily) it.next();
			try {
				getLog().debug(
						"ExecuteOrder: Adding info to order response for family "
								+ pf.getTableName());
				model = ServiceGateway.getOrderService(pf.getOrderModelClass());
				model.setUser(userBean.getUser());
				model.setSecurity(userBean.getPermissions());
				getLog().debug(
						"ExecuteOrder: order model being used: "
								+ model.getClass().getName());
				response.add(model.execute(cart.getFamilyLists().get(pf), cart
						.getGlobalInstructions()));
			} catch (Exception e) {
				getLog().error("Error in ExecuteOrder: " + e.getMessage(), e);
			}
		}
		return response;
	}

	protected void removeProductsFromCart(HandlerData actionInfo,
			OrderResponse response) {
		ShoppingCartBean cart = (ShoppingCartBean) actionInfo
				.getUserBean("cart");
		if (cart != null) {
			for (Product p : response.getProducts().keySet()) {
				cart.remove(p);
			}
		}
	}

	protected boolean checkDownloadFiles(HandlerData actionInfo,
			OrderResponse response) {
		boolean retVal = true;
		Iterator it = response.getInfoIterator();
		String tempInstr, tempFile;
		while (it.hasNext()) {
			tempInstr = (String) it.next();
			if (tempInstr.startsWith("download_file")) {
				tempFile = getController().getConfigValue("download_dir")
						+ response.getInfo(tempInstr);
				getLog()
						.debug(
								"ExecuteOrder: Checking for download file: "
										+ tempFile);
				File testFile = new File(tempFile);
				if (!testFile.exists()) {
					getLog().debug(
							"ExecuteOrder: Could not find download file: "
									+ tempFile);
					retVal = false;
				} else {
					getLog().debug(
							"ExecuteOrder: Located download file: " + tempFile);
				}
			}
		}
		return retVal;
	}

	protected String getClient(HandlerData actionInfo) {
		String client;
		client = ((ServletHandlerData) actionInfo).getRequest().getHeader(
				"user-agent");
		if (client.indexOf("mac") > -1 || client.indexOf("Mac") > -1)
			client = "MAC";
		else if (client.indexOf("Win") > -1)
			client = "WIN";
		else
			client = "OTHER";
		return client;
	}

	/**
	 * @see com.allarphoto.servlet.actionhandler.ActionHandler#getActionName()
	 */
	public String getName() {
		return ACTION_EXECUTE_ORDER;
	}
}
