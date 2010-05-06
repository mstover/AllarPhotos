/***********************************************************************************************************************
 * Copyright 1999 (c) Lazer Incorporated All code contained in this file is the property of Lazer Incorporated. All
 * rights reserved.
 **********************************************************************************************************************/

package com.lazerinc.ecommerce;

import java.io.File;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.lazerinc.application.CartObject;
import com.lazerinc.application.Product;
import com.lazerinc.utils.Functions;
import com.lazerinc.utils.Right;

public class KodakInkOrderModel extends LazerwebOrderModel {

	private String specInst; // added for "special instructions"

	/***************************************************************************
	 * Calculates the estimated cost for this entire order. This assumes the
	 * collection is filled with CartObject objects.
	 * 
	 * @param products
	 *            Collection of CartObject's
	 * @param global
	 *            Set of global instructions for order.
	 * @return cost of all products in a CostReport object.
	 **************************************************************************/
	public CostReport getCost(Collection cart, Map global) {
		CostReport ret = new CostReport();
		Iterator it = cart.iterator();
		CartObject temp;
		Product product = null;
		int numberOrdered = 0, numberSides = 1;
		while (it.hasNext()) {
			temp = (CartObject) it.next();
			product = temp.getProduct();
			if (product.getProductFamily().getProductExpirationTester()
					.hasPermission(product, Right.ORDER, security)) {
				if (temp.getInstructions().containsKey("order")) {
					numberOrdered = temp.getQuantity();
					numberSides = 2;// ((Integer)product.getValue("Sides for
					// Calc")).intValue();

					ret.setCost(product, (float) (numberOrdered * numberSides));
					Functions.javaLog(product.getPrimary() + " "
							+ (numberOrdered * numberSides * 4));

				}
			}
		}
		ret.setShipping(product.getProductFamily(), 0);
		return ret;
	}

	@Override
	protected void saveOrderInfoFromglobal(OrderResponse response, Map global) {
		if (global.containsKey("special instructions")) {
			response.addInfo("special instructions", global.get(
					"special instructions").toString());
		}
	}

	/***************************************************************************
	 * Zips the download files in batches sent from dealWithDownload
	 * 
	 * @param filesToZip
	 *            Linked list of files to zip
	 * @param fileIncrement
	 *            Used to label each download file
	 **************************************************************************/
	@Override
	protected void dealWithZip(List filesToZip, int fileIncrement, Map global,
			OrderResponse response) {
		String filename = controller.getConfigValue("download_dir");
		GregorianCalendar cal = new GregorianCalendar();
		String rand = ugd.createRandomPassword().substring(0, 3);
		String downloadFile = user.getUsername() + "_"
				+ cal.get(Calendar.MONTH) + cal.get(Calendar.DATE) + "_" + rand;
		if (global.containsKey("MAC")) {
			downloadFile = downloadFile + ".sit";
			Functions.zipForMac((File[]) filesToZip.toArray(new File[0]),
					filename + "/" + downloadFile, controller
							.getConfigValue("dropstuff"));
		} else {
			downloadFile = downloadFile + ".zip";
			Functions.zip((File[]) filesToZip.toArray(new File[0]), filename,
					downloadFile);
		}
		response.addInfo("download_file" + fileIncrement, downloadFile);
	}

	@Override
	protected void checkIfOrder(OrderResponse orderResponse, LinkedList orders,
			CartObject cartObject) {
		if (cartObject.getInstructions().containsKey("order")) {
			orders.add(cartObject);
			orderResponse.addInfo(cartObject.getProduct(), "quantity", ""
					+ cartObject.getQuantity());
		}
	}

	@Override
	protected void checkIfDownload(OrderResponse response, LinkedList download,
			CartObject temp) {
		if (temp.getInstructions().containsKey("download")) {
			download.add(temp);
		}
	}

	@Override
	protected Order createOrderObject(Merchant merchant) {
		return new KodakInkOrder(merchant, user);
	}

	@Override
	protected void saveProductInstructionsFromCart(OrderResponse response,
			CartObject temp) {
		// do nothing
	}

	@Override
	protected void saveProductCostInfo(OrderResponse response,
			CostReport costReport, CartObject temp) {
		// do nothing
	}

	@Override
	protected void saveTotalOrderCost(OrderResponse response,
			CostReport costReport) {
		// do nothing
	}
}
