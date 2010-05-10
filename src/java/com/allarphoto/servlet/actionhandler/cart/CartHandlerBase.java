package com.allarphoto.servlet.actionhandler.cart;

import static com.allarphoto.servlet.RequestConstants.REQUEST_PRODUCT;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.allarphoto.application.Product;
import com.allarphoto.application.SecurityModel;
import com.allarphoto.client.beans.ProductBean;
import com.allarphoto.client.beans.ShoppingCartBean;
import com.allarphoto.client.exceptions.LazerwebException;
import com.allarphoto.client.util.ShoppingCartUtil;
import com.allarphoto.servlet.actionhandler.ActionHandlerBase;
import com.allarphoto.utils.Functions;

/**
 * Title: Lazerweb Description: Lazerweb - Lazer Inc. version 3.0 Copyright:
 * Copyright (c) 2001 Company: Lazer Inc.
 * 
 * @author Michael Stover
 * @version 1.0
 */

public abstract class CartHandlerBase extends ActionHandlerBase {
	protected ShoppingCartUtil cartUtil = new ShoppingCartUtil();

	protected Product extractProduct(String productAndFamily,
			HandlerData actionInfo) throws LazerwebException {
		try {
			String[] pAndF = Functions.split(productAndFamily, "|");
			return dbUtil.getProductFamily(pAndF[1]).getProduct(pAndF[0],
					this.getCurrentUserPerms(actionInfo));
		} catch (NumberFormatException e) {
			throw new LazerwebException(LazerwebException.INCOMPLETE_INFO, e);
		}
	}

	protected void addInstructionsToProduct(HandlerData actionInfo,
			String[] products, Map instructions) throws ActionException {
		ShoppingCartBean cart = cartUtil.getCart(actionInfo);
		for (int i = 0; i < products.length; i++) {
			Product product = extractProduct(products[i], actionInfo);
			addInstructionToProduct(actionInfo, instructions, cart, product);
		}
	}

	private void addInstructionToProduct(HandlerData info, Map instructions,
			ShoppingCartBean cart, Product product) {
		if (product != null) {
			ProductBean bean = createProductBean(product);
			Collection<String[]> downloadableTypes = bean
					.getDownloadableTypes(getCurrentUserPerms(info));
			Iterator iter = instructions.keySet().iterator();
			while (iter.hasNext()) {
				String item = (String) iter.next();
				if (item.equals("order")
						&& bean.isOrderable(getCurrentUserPerms(info)))
					cart.addInstruction(product, item, (String) instructions
							.get(item));
				else if (item.equals("download")) {
					String downloadType = (String) instructions.get(item);
					for (String[] allowed : downloadableTypes) {
						if (allowed[0].equals(downloadType)) {
							cart.addInstruction(product, item,
									(String) instructions.get(item));
							break;
						}
					}
				}
			}
		}
	}

	protected ProductBean createProductBean(Product p) {
		try {
			ProductBean bean = p.getProductFamily().getProductBeanClass()
					.newInstance();
			bean.setProduct(p);
			return bean;
		} catch (Exception e) {
			return null;
		}
	}

	protected void clearInstructions(HandlerData actionInfo)
			throws ActionException {
		ShoppingCartBean cart = cartUtil.getCart(actionInfo);
		String[] productsToClear = actionInfo
				.getParameterValues(REQUEST_PRODUCT);
		for (int i = 0; i < productsToClear.length; i++) {
			Product product = extractProduct(productsToClear[i], actionInfo);
			cart.removeInstruction(product, "order");
			cart.removeInstruction(product, "download");
			cart.removeInstruction(product, "special");
			cart.removeInstruction(product, "regular");
		}
	}

	protected void addInstructionsToProduct(HandlerData actionInfo,
			Collection<Product> products, Map instructions)
			throws ActionException {
		ShoppingCartBean cart = cartUtil.getCart(actionInfo);
		for (Product product : products) {
			addInstructionToProduct(actionInfo, instructions, cart, product);
		}
	}

	protected void addInstructionsToCart(HandlerData actionInfo,
			Map instructions) throws ActionException {
		ShoppingCartBean cart = cartUtil.getCart(actionInfo);
		Iterator iter = instructions.keySet().iterator();
		while (iter.hasNext()) {
			String item = (String) iter.next();
			cart.addInstruction(item, (String) instructions.get(item));
		}
	}

}