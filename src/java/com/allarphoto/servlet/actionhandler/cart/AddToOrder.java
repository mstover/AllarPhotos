package com.lazerinc.servlet.actionhandler.cart;

import static com.lazerinc.servlet.ActionConstants.ACTION_ORDER_ITEMS;

import java.util.HashMap;
import java.util.Map;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

/*******************************************************************************
 * This action handler adds ordering instructions to
 * 
 * @author Michael Stover
 * @created January 31, 2002
 * @version 1.0
 * @action action_order_items
 * @requestParam addToOrder.order A list of products to order. The products are
 *               specified in a compound value of the following format:
 *               <p>
 *               &lt;Product Family&gt;|&lt;Product ID&gt;
 * @requestParam addToOrder.download_product A list of products to download. The
 *               products are specified in a compound value.
 * @requestParam addToOrder.download_pdf A list of products to be downloaded as
 *               PDF files.
 * @requestParam addToOrder.download_jpg A list of products to be downloaded as
 *               JPG files.
 * @requestParam request_product A list of products to be modified. It is
 *               necessary to advise the action handler of all the products to
 *               be modified so that their previous values can be cleared first.
 *               The products are specified in the values as a compound value.
 *               The compound format is:
 *               <p>
 *               &lt;Product Family&gt;|&lt;Product ID&gt;
 * @bean ShoppingCartBean cart The shopping cart bean is modified to reflect the
 *       ordering changes and deletions.
 ******************************************************************************/

public class AddToOrder extends CartHandlerBase {

	public final static String ORDER = "addToOrder.order";

	public final static String DOWNLOAD_PRODUCT = "addToOrder.download_product";

	public final static String DOWNLOAD_PDF = "addToOrder.download_pdf";

	public final static String DOWNLOAD_JPG = "addToOrder.download_jpg";

	public final static String DOWNLOAD_INSTR = "addToOrder.download_";

	/***************************************************************************
	 * Constructor for the AddToOrder object
	 **************************************************************************/
	public AddToOrder() {
	}

	/***************************************************************************
	 * Gets the ActionName attribute of the AddToOrder object
	 * 
	 * @return The ActionName value
	 **************************************************************************/
	public String getName() {
		return ACTION_ORDER_ITEMS;
	}

	/***************************************************************************
	 * Description of the Method
	 * 
	 * @param actionInfo
	 *            Description of Parameter
	 * @exception com.lazerinc.client.exceptions.InformationalException
	 *                Description of Exception
	 * @exception com.lazerinc.client.exceptions.FatalException
	 *                Description of Exception
	 **************************************************************************/
	public void performAction(HandlerData actionInfo) throws ActionException {
		clearInstructions(actionInfo);
		addOrderInstructions(actionInfo);
		addDownloadInstructions(actionInfo);

	}

	/***************************************************************************
	 * Adds a feature to the OrderInstructions attribute of the AddToOrder
	 * object
	 * 
	 * @param actionInfo
	 *            The feature to be added to the OrderInstructions attribute
	 **************************************************************************/
	protected void addOrderInstructions(HandlerData actionInfo)
			throws ActionException {
		String[] products = actionInfo.getParameterValues(ORDER);
		if (products == null) {
			return;
		}
		Map instructions = new HashMap();
		instructions.put("order", "");
		addInstructionsToProduct(actionInfo, products, instructions);
	}

	protected void addDownloadInstructions(HandlerData actionInfo)
			throws ActionException {
		for (Object name : actionInfo.getParamNames()) {
			if (name.toString().startsWith(DOWNLOAD_INSTR)) {
				String format = name.toString().substring(
						DOWNLOAD_INSTR.length());
				String[] products = actionInfo.getParameterValues(name
						.toString());
				Map instructions = new HashMap();
				String downloadValue = null;
				String extra = "regular";
				if (format.length() > 0 && !format.equals("product")) {
					extra = "special";
					downloadValue = format;
				}
				instructions.put("download", downloadValue);
				instructions.put(extra, null);
				addInstructionsToProduct(actionInfo, products, instructions);
			}
		}
	}

}
