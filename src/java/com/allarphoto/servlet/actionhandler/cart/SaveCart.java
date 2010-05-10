package com.allarphoto.servlet.actionhandler.cart;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.allarphoto.client.beans.ShoppingCartBean;
import com.allarphoto.client.exceptions.LazerwebException;
import com.allarphoto.ecommerce.CommerceUser;
import com.allarphoto.ecommerce.UserProperties;

public class SaveCart extends CartHandlerBase {

	public SaveCart() {
		super();
	}

	public void performAction(HandlerData actionInfo) throws LazerwebException,
			ActionException {
		ShoppingCartBean cart = cartUtil.getCart(actionInfo);
		String serialCart = cart.saveCartToString();
		CommerceUser user = getUserBean(actionInfo).getUser();
		user.setProperty(UserProperties.SHOPPING_CART, serialCart);
		getUgd().updateUser(user, this.getCurrentUserPerms(actionInfo), true);
	}

	public String getName() {
		return "save_cart";
	}

}
