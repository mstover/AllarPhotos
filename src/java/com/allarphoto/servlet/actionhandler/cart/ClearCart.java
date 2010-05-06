package com.lazerinc.servlet.actionhandler.cart;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.lazerinc.client.beans.ShoppingCartBean;
import com.lazerinc.client.exceptions.LazerwebException;
import com.lazerinc.ecommerce.CommerceUser;
import com.lazerinc.ecommerce.UserProperties;

public class ClearCart extends CartHandlerBase {

	public ClearCart() {
		super();
	}

	public void performAction(HandlerData actionInfo) throws LazerwebException,
			ActionException {
		ShoppingCartBean cart = cartUtil.getCart(actionInfo);
		getLog().info("Clearing cart");
		cart.clear();
		String serialCart = cart.saveCartToString();
		CommerceUser user = this.getUserBean(actionInfo).getUser();
		user.setProperty(UserProperties.SHOPPING_CART, serialCart);
		getUgd().updateUser(user, this.getCurrentUserPerms(actionInfo), true);
	}

	public String getName() {
		return "clear_cart";
	}

}
