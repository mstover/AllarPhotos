package com.lazerinc.servlet.actionhandler.cart;

import static com.lazerinc.servlet.ActionConstants.ACTION_ADD_TO_CART;
import static com.lazerinc.servlet.RequestConstants.REQUEST_PRODUCT_FAMILY;
import static com.lazerinc.servlet.RequestConstants.REQUEST_PRODUCT_ID;

import java.util.Map;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.lazerinc.client.beans.ShoppingCartBean;
import com.lazerinc.client.util.SearchUtil;

/**
 * Title: Lazerweb Description: Lazerweb - Lazer Inc. version 3.0 Copyright:
 * Copyright (c) 2001 Company: Lazer Inc.
 * 
 * @author Michael Stover
 * @version 1.0
 * @action action_add_to_cart
 * @requestParam request_product_family The name of the family product for the
 *               product you want added to the shopping cart.
 * @requestParam request_product_if The id number of the product you want added
 *               to the shopping cart.
 * @bean ShoppingCartBean cart The user's shopping cart hold all the products
 *       they've chosen.
 */

public class AddToCart extends CartHandlerBase {
	SearchUtil searchUtil = new SearchUtil();

	public AddToCart() {
	}

	public String getErrorPage(Map parm1) {
		return null;
	}

	public void performAction(HandlerData actionInfo) throws ActionException {
		ShoppingCartBean cart = cartUtil.getCart(actionInfo);

		cart.add(searchUtil.getProduct(actionInfo
				.getParameter(REQUEST_PRODUCT_FAMILY), actionInfo
				.getParameter(REQUEST_PRODUCT_ID),
				getCurrentUserPerms(actionInfo)));

	}

	public String getName() {
		return ACTION_ADD_TO_CART;
	}
}