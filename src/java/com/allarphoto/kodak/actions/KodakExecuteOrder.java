package com.allarphoto.kodak.actions;

import org.coinjema.context.CoinjemaContext;

import strategiclibrary.service.webaction.HandlerData;

import com.allarphoto.client.beans.ShoppingCartBean;
import com.allarphoto.client.exceptions.InformationalException;
import com.allarphoto.client.exceptions.LazerwebException;
import com.allarphoto.servlet.actionhandler.commerce.ExecuteOrder;

public class KodakExecuteOrder extends ExecuteOrder {

	public String getName() {
		return "kodak_execute_order";
	}

	public void performAction(HandlerData info) throws LazerwebException {
		ShoppingCartBean cart = (ShoppingCartBean) info.getBean("cart");
		if (!info.hasParam("order_confirmed")) {
			cart.clearGlobalInstructions();
			String downloadUsage = info.getParameter("download_usage_text",
					info
							.getParameter("download_usage", null,
									this.emptyStrings), this.emptyStrings);
			String orderUsage = info.getParameter("order_usage_text", info
					.getParameter("order_usage", null, this.emptyStrings),
					this.emptyStrings);
			if (downloadUsage != null)
				cart.addInstruction("download_usage", downloadUsage);
			else if (cart.isDownloadRequest())
				throw new LazerwebException("NoUsageException");
			if (orderUsage != null) {
				cart.addInstruction("order_usage", orderUsage);
				determineImageFormat(cart, orderUsage);
			} else if (cart.isOrderRequest())
				throw new LazerwebException("NoUsageException");
			cart.addInstruction("charge_no", info.getParameter("charge_no",
					null, emptyStrings));
			cart.addInstruction("special instructions", info.getParameter(
					"instructions", null, emptyStrings));
			cart.addInstruction("usage_agree", info.getParameter("usage_agree",
					null, emptyStrings));
		}
		checkUsageAgree(cart);
		checkChargeCode(cart);
		if (!info.hasParam("order_confirmed")) {
			checkTradeshow(cart);
			checkBadUsageChoice(cart);
		}
		executeOrder(info, cart);
	}

	protected void determineImageFormat(ShoppingCartBean cart, String orderUsage) {
		if (orderUsage.indexOf("Collateral") > -1
				|| orderUsage.indexOf("Merchandiser") > -1
				|| orderUsage.indexOf("Point-Of-Sale") > -1
				|| orderUsage.indexOf("Press") > -1
				|| orderUsage.indexOf("Advertisement") > -1
				|| orderUsage.indexOf("Packaging") > -1) {
			cart.addInstruction("image format", "CMYK");
		} else if (orderUsage.indexOf("Camera") > -1
				|| orderUsage.indexOf("Tradeshow") > -1
				|| orderUsage.indexOf("Training") > -1
				|| orderUsage.indexOf("Web") > -1
				|| orderUsage.indexOf("Presentation") > -1) {
			cart.addInstruction("image format", "RGB");
		} else {
			cart.addInstruction("image format", "unknown");
		}
	}

	protected void checkTradeshow(ShoppingCartBean cart)
			throws LazerwebException {
		if (cart.containsInstructionString("download_usage", "tradeshow")
				|| cart.containsInstructionString("order_usage", "tradeshow")) {
			throw new InformationalException("TradeshowWarning");
		}
	}

	protected void checkBadUsageChoice(ShoppingCartBean cart)
			throws LazerwebException {
		if (cart.containsInstructionString("download_usage", "collateral")
				|| cart.containsInstructionString("download_usage",
						"merchandisers")
				|| cart.containsInstructionString("download_usage", "press")
				|| cart.containsInstructionString("download_usage",
						"advertisement")
				|| cart.containsInstructionString("download_usage",
						"point-of-sale")
				|| cart
						.containsInstructionString("download_usage",
								"packaging")) {
			throw new InformationalException("BadDownloadUsageWarning");
		}
		getLog().debug("Order usages = " + cart.getInstruction("order_usage"));
		if (cart.containsInstructionString("order_usage", "web")
				|| cart.containsInstructionString("order_usage",
						"presentations")) {
			throw new InformationalException("BadOrderUsageWarning");
		}
	}

	protected void checkChargeCode(ShoppingCartBean cart)
			throws LazerwebException {
		if (cart.isOrderRequest() && !cart.hasInstruction("charge_no")) {
			throw new LazerwebException("NoChargeNoException");
		}
	}

	protected void checkUsageAgree(ShoppingCartBean cart)
			throws LazerwebException {
		if (!cart.hasInstruction("usage_agree"))
			throw new LazerwebException("UsageAgreeException");
	}

}
