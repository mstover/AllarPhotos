package com.allarphoto.servlet.actionhandler.cart;

import static com.allarphoto.servlet.ActionConstants.ACTION_SET_CART_SET;
import static com.allarphoto.servlet.RequestConstants.REQUEST_BROWSE_SET;
import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;
import strategiclibrary.util.Converter;

import com.allarphoto.application.CartObject;
import com.allarphoto.client.beans.ShoppingCartBean;
import com.allarphoto.client.exceptions.FatalException;
import com.allarphoto.client.exceptions.InformationalException;
import com.allarphoto.utils.PagedList;

/**
 * The shopping cart is divided into viewable sets (if the total number of
 * products is greater than the number allowed to be viewed on one page). By
 * setting the cart set number, the user can page through their shopping cart.
 * 
 * @author Administrator
 * @action action_set_cart_set
 * @requestParam request_cart_set This is the cart set number to be set.
 * @bean ShoppingCartBean cart The shopping cart is not modified, but the
 *       current view page is changed.
 */
public class SetCartSet extends CartHandlerBase {

	/**
	 * @see com.allarphoto.servlet.actionhandler.ActionHandler#performAction(HandlerData)
	 */
	public void performAction(HandlerData actionInfo) throws FatalException,
			InformationalException, ActionException {
		ShoppingCartBean cart = cartUtil.getCart(actionInfo);
		PagedList<CartObject> productList = actionInfo.getUserBean("cartList",
				PagedList.class);
		String[] productTables = getController().getConfigValue(
				"product_tables").split(",");
		boolean first = true;
		for (String pt : productTables) {
			if (pt != null) {
				getLog().debug("adding to paged list from library: " + pt);
				if (cart.getProductsByFamily(pt) != null) {
					if (first) {
						first = false;
						productList.setList(cart.getProductsByFamily(pt));
					} else
						productList.addAll(cart.getProductsByFamily(pt));
				}
				getLog().debug("added list = " + cart.getProductsByFamily(pt));
			}
		}

		productList.setPagingSize(Converter.getInt(getController()
				.getConfigValue("search_display_size")));
		if (actionInfo.hasParam(REQUEST_BROWSE_SET)) {
			getLog().debug("changing display set");
			productList.setPageNo(Integer.parseInt((String) actionInfo
					.getParameter(REQUEST_BROWSE_SET)));
		}
	}

	/**
	 * @see com.allarphoto.servlet.actionhandler.ActionHandler#getActionName()
	 */
	public String getName() {
		return ACTION_SET_CART_SET;
	}
}
