package com.allarphoto.kodak.actions;

import com.allarphoto.client.beans.ShoppingCartBean;
import com.allarphoto.client.exceptions.InformationalException;
import com.allarphoto.client.exceptions.LazerwebException;

public class KProExecuteOrder extends KodakExecuteOrder {

	@Override
	public String getName() {
		return "kpro_execute_order";
	}

	protected void determineImageFormat(ShoppingCartBean cart, String orderUsage) {
		if (orderUsage.indexOf("Collateral") > -1
				|| orderUsage.indexOf("Editorialer") > -1
				|| orderUsage.indexOf("Point-Of-Sale") > -1
				|| orderUsage.indexOf("Press") > -1
				|| orderUsage.indexOf("Advertisement") > -1
				|| orderUsage.indexOf("Direct Mail") > -1
				|| orderUsage.indexOf("Product") > -1) {
			cart.addInstruction("image format", "CMYK");
		} else if (orderUsage.indexOf("Sample") > -1
				|| orderUsage.indexOf("Tradeshow") > -1
				|| orderUsage.indexOf("Training") > -1
				|| orderUsage.indexOf("Web") > -1
				|| orderUsage.indexOf("Presentation") > -1) {
			cart.addInstruction("image format", "RGB");
		} else {
			cart.addInstruction("image format", "unknown");
		}
	}

	protected void checkBadUsageChoice(ShoppingCartBean cart)
			throws LazerwebException {
		getLog().debug("Order usages = " + cart.getInstruction("order_usage"));
		if (cart.containsInstructionString("order_usage", "web")
				|| cart.containsInstructionString("order_usage",
						"presentations")
				|| cart.containsInstructionString("order_usage", "training")) {
			throw new InformationalException("BadOrderUsageWarning");
		}
	}

}
