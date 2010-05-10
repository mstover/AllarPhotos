package com.allarphoto.servlet.actionhandler.search;

import static com.allarphoto.servlet.ActionConstants.ACTION_SET_PREVIOUS_PRODUCT;
import static com.allarphoto.servlet.RequestConstants.REQUEST_PRODUCT_FAMILY;
import static com.allarphoto.servlet.RequestConstants.REQUEST_PRODUCT_ID;

import java.util.List;
import java.util.Map;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.allarphoto.application.CartObject;
import com.allarphoto.application.Product;
import com.allarphoto.client.beans.ProductBean;
import com.allarphoto.client.exceptions.LazerwebException;
import com.allarphoto.utils.PagedList;

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

public class SetPreviousProduct extends SetNextProduct {

	public SetPreviousProduct() {
	}

	public String getErrorPage(Map parm1) {
		return null;
	}

	public void performAction(HandlerData actionInfo) throws LazerwebException,
			ActionException {
		getLog().debug("performing action: " + ACTION_SET_PREVIOUS_PRODUCT);
		ProductBean currentProductBean = (ProductBean) actionInfo
				.getUserBean("productBean");
		ProductBean productBean = (ProductBean) actionInfo
				.getUserBean("previousProductBean");
		if (null == currentProductBean.getProduct()) {
			Product p = searchUtil.getProduct(actionInfo
					.getParameter(REQUEST_PRODUCT_FAMILY), actionInfo
					.getParameter(REQUEST_PRODUCT_ID),
					getCurrentUserPerms(actionInfo));
			currentProductBean = p.getProductFamily().getProductBean(p);
			actionInfo.setUserBean("productBean", currentProductBean);
		}
		getLog().debug("current product is " + currentProductBean.getPrimary());
		PagedList productList = (PagedList) actionInfo.getBean(actionInfo
				.getParameter("listName", "browseProducts"));
		Product p = getPreviousProduct(productList, currentProductBean);
		if (p != null) {
			productBean = p.getProductFamily().getProductBean(p);
			actionInfo.setUserBean("previousProductBean", productBean);
		} else
			actionInfo.removeUserBean("previousProductBean");
	}

	protected Product getPreviousProduct(List listOfItems,
			ProductBean currentProduct) {
		if (listOfItems != null && listOfItems.size() > 0
				&& listOfItems.get(0) instanceof Product) {
			int currentIndex = listOfItems.indexOf(currentProduct.getProduct());
			if (currentIndex > 0)
				return (Product) listOfItems.get(currentIndex - 1);
			else
				return null;
		} else if (listOfItems != null && listOfItems.size() > 0
				&& listOfItems.get(0) instanceof CartObject) {
			boolean found = false;
			Product cached = null;
			for (CartObject o : (List<CartObject>) listOfItems) {
				if (found)
					return cached;
				if (o.getProduct().equals(currentProduct.getProduct())) {
					found = true;
				} else
					cached = o.getProduct();
			}
			return cached;
		}
		return null;
	}

	public String getName() {
		return ACTION_SET_PREVIOUS_PRODUCT;
	}
}