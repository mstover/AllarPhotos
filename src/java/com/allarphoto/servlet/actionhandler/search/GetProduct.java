package com.allarphoto.servlet.actionhandler.search;

import static com.allarphoto.servlet.ActionConstants.ACTION_GET_PRODUCT;
import static com.allarphoto.servlet.RequestConstants.REQUEST_PRODUCT_FAMILY;
import static com.allarphoto.servlet.RequestConstants.REQUEST_PRODUCT_ID;

import java.util.Map;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.allarphoto.application.Product;
import com.allarphoto.client.beans.ProductBean;
import com.allarphoto.client.beans.ShoppingCartBean;
import com.allarphoto.client.exceptions.LazerwebException;

/**
 * Retrieves a specific product and stores its information in a bean.
 * 
 * @author Michael Stover
 * @version 1.0
 * @action action_get_product
 * @requestParam request_product_id The id of the product to be retrieved.
 * @requestParam request_product_family The family of the product to be
 *               retrieved.
 * @bean ProductBean productBean The product bean stores all the information
 *       available about the product requested.
 */

public class GetProduct extends AbstractSearchAction {

	public GetProduct() {
	}

	public String getErrorPage(Map parm1) {
		return null;
	}

	public void performAction(HandlerData actionInfo) throws LazerwebException,
			ActionException {
		Product p = searchUtil.getProduct(actionInfo
				.getParameter(REQUEST_PRODUCT_FAMILY), actionInfo
				.getParameter(REQUEST_PRODUCT_ID),
				getCurrentUserPerms(actionInfo));
		ProductBean productBean = p.getProductFamily().getProductBean(p);
		actionInfo.setUserBean("productBean", productBean);
		String prod = productBean.getPrimary();
		String fam = productBean.getProductFamily().getTableName();
		productBean.setInCart(false);
		ShoppingCartBean cartBean = actionInfo.getUserBean("cart",
				ShoppingCartBean.class);
		if (null != cartBean.getProduct(prod, fam)) {
			getLog().debug(
					"Setting inCart attribute for "
							+ cartBean.getProduct(prod, fam).getPrimary());
			productBean.setInCart(true);
		} else {
			getLog().debug("Setting inCart attriburte failed for " + prod);
		}
	}

	public String getName() {
		return ACTION_GET_PRODUCT;
	}
}