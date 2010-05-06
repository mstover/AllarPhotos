package com.lazerinc.servlet.actionhandler.commerce;

import strategiclibrary.service.webaction.HandlerData;

import com.lazerinc.client.beans.ShoppingCartBean;
import com.lazerinc.client.exceptions.LazerwebException;

public class WorldKitExecuteOrder extends ExecuteOrder {

	@Override
	protected void addCustomInstructions(ShoppingCartBean cart, HandlerData info)
			throws LazerwebException {
		if (cart.isOrderRequest())
			if (!info.hasParam("costCenter", this.emptyStrings))
				throw new LazerwebException("InvalidOrderInfo");
		cart.addInstruction("costCenter", info.getParameter("costCenter"));
	}

	@Override
	public String getName() {
		return "worldkit_execute_order";
	}

}
