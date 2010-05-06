package com.lazerinc.servlet.actionhandler.cart;

import static com.lazerinc.servlet.ActionConstants.ACTION_CHECK_CART_EMPTY;
import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.lazerinc.client.beans.ShoppingCartBean;
import com.lazerinc.client.exceptions.LazerwebException;

public class CheckCartEmpty extends CartHandlerBase {

	public CheckCartEmpty() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void performAction(HandlerData actionInfo) throws ActionException {
		ShoppingCartBean cart = cartUtil.getCart(actionInfo);
		if (cart.size() == 0)
			throw new LazerwebException(LazerwebException.EMPTY_CART);
		if (cart.getDownloadProducts().size() == 0
				&& cart.getOrderedProducts().size() == 0)
			throw new LazerwebException(LazerwebException.EMPTY_ORDER);

	}

	public String getName() {
		// TODO Auto-generated method stub
		return ACTION_CHECK_CART_EMPTY;
	}

}
