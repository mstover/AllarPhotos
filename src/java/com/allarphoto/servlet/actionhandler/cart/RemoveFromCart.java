package com.allarphoto.servlet.actionhandler.cart;

import static com.allarphoto.servlet.ActionConstants.ACTION_REMOVE_FROM_CART;
import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.allarphoto.client.beans.ShoppingCartBean;
import com.allarphoto.client.exceptions.LazerwebException;

/**
 * Title: Lazerweb Description: Lazerweb - Lazer Inc. version 3.0 Copyright:
 * Copyright (c) 2001 Company: Lazer Inc.
 * 
 * @author Michael Stover
 * @version 1.0
 * @action action_remove_from_cart
 * @requestParam removeFromCart.remove A list of products to remove. Each value
 *               is a compound value in the following format:
 *               <p>
 *               &lt;Product Family&gt;|&lt;Product ID&gt;
 * @bean ShoppingCartBean cart The user's shopping cart is modified.
 */

public class RemoveFromCart extends CartHandlerBase {

	public final static String REMOVE = "removeFromCart.remove";

	public RemoveFromCart() {
	}

	public void performAction(HandlerData actionInfo) throws ActionException {
		ShoppingCartBean cart = cartUtil.getCart(actionInfo);
		removeFromShoppingCart(cart, actionInfo);
	}

	protected void removeFromShoppingCart(ShoppingCartBean cart,
			HandlerData actionInfo) throws LazerwebException {
		String[] products = actionInfo.getParameterValues(REMOVE);
		if (products == null) {
			return;
		}
		for (int x = 0; x < products.length; x++) {
			cart.remove(extractProduct(products[x], actionInfo));
		}
	}

	public String getName() {
		return ACTION_REMOVE_FROM_CART;
	}
}