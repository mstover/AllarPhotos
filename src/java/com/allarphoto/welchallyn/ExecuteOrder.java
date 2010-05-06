package com.lazerinc.welchallyn;

import strategiclibrary.service.webaction.HandlerData;

import com.lazerinc.client.beans.ShoppingCartBean;
import com.lazerinc.client.exceptions.LazerwebException;

public class ExecuteOrder extends
		com.lazerinc.servlet.actionhandler.commerce.ExecuteOrder {

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
