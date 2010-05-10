package com.allarphoto.servlet.actionhandler.search;

import static com.allarphoto.servlet.ActionConstants.ACTION_SET_NEXT_PRODUCT;
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

public class SetNextProduct extends AbstractSearchAction {

	public SetNextProduct() {
	}

	public String getErrorPage(Map parm1) {
		return null;
	}

	public void performAction(HandlerData actionInfo) throws LazerwebException,
			ActionException {
		getLog().debug("performing action: " + ACTION_SET_NEXT_PRODUCT);
		ProductBean currentProductBean = (ProductBean) actionInfo
				.getUserBean("productBean");
		ProductBean productBean = (ProductBean) actionInfo
				.getUserBean("nextProductBean");
		if (null == currentProductBean
				&& actionInfo.hasParam(REQUEST_PRODUCT_FAMILY)
				&& actionInfo.hasParam(REQUEST_PRODUCT_ID)) {
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
		Product p = getNextProduct(productList, currentProductBean);
		if (p != null) {
			productBean = p.getProductFamily().getProductBean(p);
			actionInfo.setUserBean("nextProductBean", productBean);
		} else
			actionInfo.removeUserBean("nextProductBean");
		// if(p == null) productBean.clear();
		/*
		 * int newDisplaySet = (int) ((currentIndex) /
		 * productList.getPagingSize()); getLog().debug("new set = " +
		 * newDisplaySet); if (productList.getPageNo() != newDisplaySet) {
		 * productList.setPageNo(newDisplaySet); }
		 */
	}

	protected Product getNextProduct(List listOfItems,
			ProductBean currentProduct) {
		if (listOfItems != null && listOfItems.size() > 0
				&& listOfItems.get(0) instanceof Product) {
			int currentIndex = listOfItems.indexOf(currentProduct.getProduct());
			if (currentIndex + 1 < listOfItems.size())
				return (Product) listOfItems.get(currentIndex + 1);
			else
				return null;
		} else if (listOfItems != null && listOfItems.size() > 0
				&& listOfItems.get(0) instanceof CartObject) {
			boolean found = false;
			;
			for (CartObject o : (List<CartObject>) listOfItems) {
				if (found)
					return o.getProduct();
				if (o.getProduct().equals(currentProduct.getProduct())) {
					found = true;
				}
			}
		}
		return null;
	}

	public String getName() {
		return ACTION_SET_NEXT_PRODUCT;
	}
}