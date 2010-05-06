/***********************************************************************************************************************
 * Copyright 1999 (c) Lazer Incorporated All code contained in this file is the property of Lazer Incorporated. All
 * rights reserved.
 **********************************************************************************************************************/

package com.lazerinc.ecommerce;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import strategiclibrary.util.Converter;

import com.lazerinc.application.CartObject;
import com.lazerinc.application.Product;
import com.lazerinc.beans.LogItem;
import com.lazerinc.utils.Right;

public class KodakDAIOrderModel extends LazerwebOrderModel {

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
		float mbytes = 0, tempbytes = 0;
		Iterator it = cart.iterator();
		CartObject temp;
		Product product = null;
		int numberOrdered = 0, pixPerInch = 1;
		while (it.hasNext()) {
			temp = (CartObject) it.next();
			product = temp.getProduct();
			if (temp.getInstructions().containsKey("order")) {
				int pixelsPerInch = Converter.getInt(controller
						.getConfigValue("pixes_per_inch"), 327);
				double bytes = 0;
				if (!"Sample Pictures".equals(product.getValue("Image Type"))
						|| (bytes = Converter.getDouble(product
								.getValue("File Size"), 0) / 1024 / 1024) < .000001) {
					if ((bytes = Converter.getDouble(product
							.getValue("Hires File Size"), 0) / 1024 / 1024) < .000001) {
						log.info("pixels per inch = " + pixelsPerInch);
						bytes = ((Number) product.getValue("Width"))
								.doubleValue()
								* ((Number) product.getValue("Height"))
										.doubleValue();
						bytes = bytes * pixelsPerInch * pixelsPerInch;
						bytes = bytes / 2.255E20;
						log.info("calculated size (" + product.getDisplayName()
								+ " = " + bytes);
					} else
						log.info("hi res size (" + product.getDisplayName()
								+ " = " + bytes);
				} else
					log.info("sample picture size (" + product.getDisplayName()
							+ " = " + bytes);
				ret.setCost(product, (float) (bytes * .39));
				mbytes += bytes;
			}
		}
		int cd = (int) mbytes / 500 + 1;
		cd *= 12;
		ret.setShipping(product.getProductFamily(), (float) cd);
		return ret;
	}

	protected void additionalDownloadValues(Map global, LogItem logItem) {
		logItem.setValue("target use", global.get("download_usage").toString());
	}

	@Override
	protected Order createOrderObject(Merchant merchant) {
		return new KodakDaiOrder(merchant, user);
	}

	protected void checkIfOrder(OrderResponse orderResponse, LinkedList orders,
			CartObject cartObject) {
		if (cartObject.getInstructions().containsKey("order")
				&& cartObject.getProduct().getProductFamily()
						.getProductExpirationTester().hasPermission(
								cartObject.getProduct(), Right.ORDER, security)) {
			orders.add(cartObject);
			for (String value : cartObject.getInstructions().get("order")) {
				if ("ftp".equals(value) || "cd".equals(value)) {
					orderResponse.addInfo("Delivery Format", value);
					orderResponse.addInfo(cartObject.getProduct(),
							"Delivery Format", value);
				} else {
					orderResponse.addInfo(cartObject.getProduct(), "File Type",
							value);
				}
			}
		}
	}

}
