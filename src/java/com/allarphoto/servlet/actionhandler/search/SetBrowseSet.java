package com.lazerinc.servlet.actionhandler.search;

import static com.lazerinc.servlet.ActionConstants.ACTION_BROWSE_SET;
import static com.lazerinc.servlet.ActionConstants.ACTION_CATEGORY_SEARCH;
import static com.lazerinc.servlet.RequestConstants.REQUEST_BROWSE_SET;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;
import strategiclibrary.util.Converter;

import com.lazerinc.application.Product;
import com.lazerinc.client.beans.ProductSetBean;
import com.lazerinc.lazerweb.utils.ProductDateSort;
import com.lazerinc.utils.PagedList;

/**
 * The set of searched products is divided into pages. If their are 300 products
 * that currently match the search criteria, only one page at a time is
 * displayed to the user. By setting the "browse set" (ie, the viewed page), the
 * user can step through these pages of products. The number of products per
 * page is set in the properties for each customer as the "search_display_size".
 * 
 * @author Michael Stover
 * @version 1.0
 * @action action_browse_set
 * @requestParam request_browse_set An integer representing the page to view.
 *               The first page is 0, then 1, etc.
 * @bean ProductSetBean productsFound The active viewing page of the matching
 *       products is set.
 */

public class SetBrowseSet extends AbstractSearchAction {

	public SetBrowseSet() {
	}

	public String getErrorPage(Map parm1) {
		return null;
	}

	public void performAction(HandlerData actionInfo)
			throws com.lazerinc.client.exceptions.FatalException,
			com.lazerinc.client.exceptions.InformationalException,
			ActionException {
		getLog().debug("performing action: " + ACTION_BROWSE_SET);
		ProductSetBean productBean = getProductsFound(actionInfo);
		PagedList productList = actionInfo.getUserBean("browseProducts",
				PagedList.class);
		if (productBean.getCurrentProductSet() == null) {
			productBean.clear();
			getContainer().executeAction(ACTION_CATEGORY_SEARCH, actionInfo);
			productBean = getProductsFound(actionInfo);
		}
		if (actionInfo.getParameter("browseset_sortby", null) != null)
			actionInfo.setUserBean("browseset_sortby", actionInfo.getParameter(
					"browseset_sortby", null));
		else if (actionInfo.getUserBean("browseset_sortby") == null)
			actionInfo.setUserBean("browseset_sortby", this.getController()
					.getConfigValue("browseset_sortby"));
		getLog().info(
				"browseset_sortby = "
						+ this.getController().getConfigValue(
								"browseset_sortby"));
		String sortby = (String) actionInfo.getUserBean("browseset_sortby");
		List<Product> prods = productBean.getCurrentProductSet()
				.getProductList();
		if (sortby != null) {
			if (sortby.equals("date")) {
				Collections.sort(prods, new ProductDateSort());
			}
		}
		productList.setList(prods);
		productList.setPagingSize(Converter.getInt(getController()
				.getConfigValue("search_display_size")));
		if (actionInfo.hasParam(REQUEST_BROWSE_SET)) {
			getLog().debug("changing display set");
			productList.setPageNo(actionInfo.getParameterAsInt(
					REQUEST_BROWSE_SET, 0));
		}
	}

	public String getName() {
		return ACTION_BROWSE_SET;
	}
}