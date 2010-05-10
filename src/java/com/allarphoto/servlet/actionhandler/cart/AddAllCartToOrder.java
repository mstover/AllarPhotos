package com.allarphoto.servlet.actionhandler.cart;

import java.util.HashMap;
import java.util.Map;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

public class AddAllCartToOrder extends AddToOrder {

	/***************************************************************************
	 * Gets the ActionName attribute of the AddToOrder object
	 * 
	 * @return The ActionName value
	 **************************************************************************/
	public String getName() {
		return "add_cart_to_order";
	}

	/***************************************************************************
	 * Description of the Method
	 * 
	 * @param actionInfo
	 *            Description of Parameter
	 * @exception com.allarphoto.client.exceptions.InformationalException
	 *                Description of Exception
	 * @exception com.allarphoto.client.exceptions.FatalException
	 *                Description of Exception
	 **************************************************************************/
	public void performAction(HandlerData actionInfo) throws ActionException {
		cartUtil.getCart(actionInfo).clearInstructions();
		if (actionInfo.getParameter("order_type", "download")
				.equals("download")) {
			Map instructions = new HashMap();
			instructions.put("download", actionInfo.getParameter(
					"download_type", "jpg"));
			addInstructionsToProduct(actionInfo, cartUtil.getCart(actionInfo)
					.getProducts(), instructions);
		} else {
			Map instructions = new HashMap();
			instructions.put("order", "order");
			addInstructionsToProduct(actionInfo, cartUtil.getCart(actionInfo)
					.getProducts(), instructions);
		}

	}

}
