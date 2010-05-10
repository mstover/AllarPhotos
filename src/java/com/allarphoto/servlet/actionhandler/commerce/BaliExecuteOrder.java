package com.allarphoto.servlet.actionhandler.commerce;

import static com.allarphoto.servlet.ActionConstants.ACTION_BALI_EXECUTE_ORDER;
import static com.allarphoto.servlet.RequestConstants.REQUEST_ACCOUNT_NAME;
import static com.allarphoto.servlet.RequestConstants.REQUEST_INSTRUCTIONS;
import static com.allarphoto.servlet.RequestConstants.REQUEST_PURPOSE;
import static com.allarphoto.servlet.RequestConstants.REQUEST_PURPOSE_OTHER;
import static com.allarphoto.servlet.RequestConstants.REQUEST_SHIP_TO;
import static com.allarphoto.servlet.RequestConstants.REQUEST_SOLD_TO;
import strategiclibrary.service.webaction.HandlerData;

import com.allarphoto.client.beans.OrderResponseBean;
import com.allarphoto.client.beans.ShoppingCartBean;
import com.allarphoto.client.beans.UserBean;
import com.allarphoto.client.exceptions.InformationalException;
import com.allarphoto.client.exceptions.LazerwebException;
import com.allarphoto.ecommerce.OrderResponse;

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
public class BaliExecuteOrder extends ExecuteOrder {

	/**
	 * @see com.allarphoto.servlet.actionhandler.ActionHandler#performAction(HandlerData)
	 */
	public void performAction(HandlerData actionInfo) throws LazerwebException {
		// verifyShipTo(actionInfo);
		// verifyPurpose(actionInfo);
		// Taken out temporarily to fix download error
		// verifyAccountName(actionInfo);
		ShoppingCartBean cart = (ShoppingCartBean) actionInfo.getBean("cart");
		// cart.addInstruction("ship
		// to",actionInfo.getParameter(REQUEST_SHIP_TO));
		// cart.addInstruction("sold
		// to",actionInfo.getParameter(REQUEST_SOLD_TO));
		cart.addInstruction("account name", actionInfo
				.getParameter(REQUEST_ACCOUNT_NAME));
		cart.addInstruction("intended use", actionInfo
				.getParameter("intended_use"));
		cart.addInstruction("special instructions", actionInfo
				.getParameter(REQUEST_INSTRUCTIONS));
		if (null != actionInfo.getParameter(REQUEST_PURPOSE_OTHER)) {
			cart.addInstruction("purpose", actionInfo
					.getParameter(REQUEST_PURPOSE_OTHER));
		} else {
			cart.addInstruction("purpose", actionInfo
					.getParameter(REQUEST_PURPOSE));
		}
		/*
		 * if(null != actionInfo.getParameter(PURPOSE) &&
		 * actionInfo.getParameter(PURPOSE).equals(OTHER_TRIGGER)) {
		 * cart.addInstruction("purpose",
		 * actionInfo.getParameter(OTHER_PURPOSE)); }else{
		 * cart.addInstruction("purpose",actionInfo.getParameter(PURPOSE)); }
		 */
		getLog().debug(
				"ExecuteOrder: cart contents: " + cart.saveCartToString());
		UserBean userBean = getUserBean(actionInfo);
		if (userBean.getClientType() == null) {
			userBean.setClientType(getClient(actionInfo));
		}
		OrderResponse response = generateOrderResponse(cart, userBean,
				actionInfo);
		if (response.getProducts().size() == 0) {
			throw new LazerwebException(LazerwebException.EMPTY_ORDER);
		}
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

	protected void verifyAccountName(HandlerData actionInfo)
			throws InformationalException {
		String accountName = actionInfo.getParameter(REQUEST_ACCOUNT_NAME);
		if (null == accountName || accountName.equals("")
				|| accountName.equals(" ") || (accountName.equals("."))) {
			throw new InformationalException("InvalidOrderInfo");
		}
	}

	protected void verifyShipTo(HandlerData actionInfo)
			throws InformationalException {
		String shipTo = actionInfo.getParameter(REQUEST_SHIP_TO);
		String soldTo = actionInfo.getParameter(REQUEST_SOLD_TO);
		String accountName = actionInfo.getParameter(REQUEST_ACCOUNT_NAME);
		if (((null == shipTo || null == soldTo || shipTo.equals("")
				|| soldTo.equals("") || shipTo.length() > 7 || soldTo.length() > 7) && (null == accountName || accountName
				.equals("")))
				&& actionInfo.getParameter(DOWNLOAD_ONLY).equals("false")) {
			throw new InformationalException("InvalidOrderInfo");
		}
	}

	protected void verifyPurpose(HandlerData actionInfo)
			throws InformationalException {
		String purpose = actionInfo.getParameter(REQUEST_PURPOSE);
		String otherPurp = actionInfo.getParameter(REQUEST_PURPOSE_OTHER);
		if ((null == purpose || purpose.equals("")
				|| purpose.equals("choose one") || (purpose.equals("other") && ((null == otherPurp) || otherPurp
				.equals(""))))
				&& actionInfo.getParameter(DOWNLOAD_ONLY).equals("false")) {
			throw new InformationalException("InvalidOrderInfo");
		}
	}

	/**
	 * @see com.allarphoto.servlet.actionhandler.ActionHandler#getActionName()
	 */
	public String getName() {
		return ACTION_BALI_EXECUTE_ORDER;
	}
}
