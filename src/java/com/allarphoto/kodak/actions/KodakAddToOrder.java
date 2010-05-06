package com.lazerinc.kodak.actions;

import java.util.HashMap;
import java.util.Map;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.lazerinc.servlet.actionhandler.cart.AddToOrder;

public class KodakAddToOrder extends AddToOrder {
	public final static String KODAK_ORDER = "addToOrder.order_";

	@Override
	protected void addOrderInstructions(HandlerData actionInfo)
			throws ActionException {

		for (Object name : actionInfo.getParamNames()) {
			if (name.toString().startsWith(KODAK_ORDER)) {
				String format = name.toString().substring(KODAK_ORDER.length());
				String[] products = actionInfo.getParameterValues(name
						.toString());
				Map instructions = new HashMap();
				String[] instr = format.split("_");
				for (String in : instr) {
					instructions.clear();
					if (in != null && in.length() > 0) {
						instructions.put("order", in);
						addInstructionsToProduct(actionInfo, products,
								instructions);
					}
				}
			}
		}
	}

	@Override
	public String getName() {
		return "kodak_order_items";
	}

}
