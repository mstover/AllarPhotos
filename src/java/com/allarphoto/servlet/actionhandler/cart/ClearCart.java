package com.allarphoto.servlet.actionhandler.cart;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.allarphoto.client.beans.ShoppingCartBean;
import com.allarphoto.client.exceptions.LazerwebException;
import com.allarphoto.ecommerce.CommerceUser;
import com.allarphoto.ecommerce.UserProperties;

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
