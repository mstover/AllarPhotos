package com.lazerinc.servlet.actionhandler.cart;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.lazerinc.client.beans.ShoppingCartBean;
import com.lazerinc.client.exceptions.LazerwebException;
import com.lazerinc.ecommerce.CommerceUser;
import com.lazerinc.ecommerce.UserProperties;

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
