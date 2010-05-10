package com.allarphoto.welchallyn;

import strategiclibrary.service.webaction.HandlerData;

import com.allarphoto.client.beans.ShoppingCartBean;
import com.allarphoto.client.exceptions.LazerwebException;

public class ExecuteOrder extends
		com.allarphoto.servlet.actionhandler.commerce.ExecuteOrder {

	public ExecuteOrder() {
	}

	@Override
	protected void addCustomInstructions(ShoppingCartBean cart, HandlerData info)
			throws LazerwebException {
		cart.addInstruction("Order Usage", info.getParameter("order_usage"));
		cart.addInstruction("digTrans", info.getParameter("digTrans",
				"No Digital Transfer (Send CD)"));
	}

	@Override
	public String getName() {
		return "welch_execute_order";
	}

}
