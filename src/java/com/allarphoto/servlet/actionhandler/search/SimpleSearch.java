package com.allarphoto.servlet.actionhandler.search;

import static com.allarphoto.servlet.ActionConstants.ACTION_SIMPLE_SEARCH;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;
import strategiclibrary.util.Converter;

import com.allarphoto.client.beans.ProductSetBean;
import com.allarphoto.client.beans.SearchCategoryBean;
import com.allarphoto.client.beans.UserBean;
import com.allarphoto.ecommerce.ProductSet;
import com.allarphoto.servlet.RequestConstants;

/**
 * A simple keyword search action.
 * 
 * @author Michael Stover
 * @version 1.0
 * @action actio_simple_search
 * @requestParam request_simple_search The search string used for the simple
 *               search. It can be several keyword separated by spaces.
 * @bean SearchCategoryBean searchCategories A bean that represents the category
 *       tree of the products that match the simple search criteria. The user
 *       can continue to narrow down their search using this bean.
 * @bean ProductSetBean productsFound Holds the information about the list of
 *       products that match the current search criteria.
 */

public class SimpleSearch extends AbstractSearchAction {

	public SimpleSearch() {
	}

	public String getErrorPage(Map parm1) {
		return null;
	}

	public void performAction(HandlerData actionInfo)
			throws com.allarphoto.client.exceptions.FatalException,
			com.allarphoto.client.exceptions.InformationalException,
			ActionException {
		SearchCategoryBean categories = getSearchCategoryBean(actionInfo);
		ProductSetBean productBean = getProductsFound(actionInfo);
		UserBean userBean = getUserBean(actionInfo);
		String searchString = actionInfo
				.getParameter(RequestConstants.REQUEST_SIMPLE_SEARCH);
		String searchCat = "Date Posted";
		if (searchString.startsWith("mod:")) {
			searchString = searchString.substring(4);
			searchCat = "Date Modified";
		}
		Calendar dateSearch = Converter.getCalendar(searchString, null);
		ProductSet newSet = null;
		if (dateSearch == null) {
			newSet = actionInfo.getParameter("search_type", "and")
					.equals("and") ? searchUtil.simpleSearch(productBean,
					searchString, userBean.getPermissions()) : searchUtil
					.simpleOrSearch(productBean, searchString, userBean
							.getPermissions());
		} else {
			Set criteria = new HashSet();
			criteria.add(searchUtil.getQueryItem(searchCat,
					new String[] { searchString }));
			newSet = searchUtil.search(productBean, criteria, userBean
					.getPermissions(), false);
		}
		searchUtil.refreshCategories(newSet, categories, userBean
				.getPermissions());
	}

	public String getName() {
		return ACTION_SIMPLE_SEARCH;
	}
}