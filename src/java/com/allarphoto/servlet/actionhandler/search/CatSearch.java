package com.lazerinc.servlet.actionhandler.search;

import static com.lazerinc.servlet.ActionConstants.ACTION_CATEGORY_SEARCH;
import static com.lazerinc.servlet.RequestConstants.REQUEST_CATEGORY_PREFIX;
import static com.lazerinc.servlet.RequestConstants.REQUEST_HISTORY_INDEX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.lazerinc.application.SecurityModel;
import com.lazerinc.client.beans.ProductSetBean;
import com.lazerinc.client.beans.SearchCategoryBean;
import com.lazerinc.client.beans.UserBean;
import com.lazerinc.client.exceptions.FatalException;
import com.lazerinc.client.exceptions.InformationalException;
import com.lazerinc.ecommerce.ProductSet;
import com.lazerinc.utils.Resource;
import com.lazerinc.utils.Right;

/**
 * Title: Lazerweb Description: Lazerweb - Lazer Inc. version 3.0 Copyright:
 * Copyright (c) 2001 Company: Lazer Inc.
 * 
 * @author Michael Stover
 * @version 1.0
 * @action action_category_search
 * @requestParam request_category_prefix Any request parameters with this prefix
 *               will be treated as search instructions. The parameter name
 *               (minus the prefix) is a compound value with parts separated by
 *               the '|' character. The value(s) of the parameter are values
 *               chosen for that category. To illustrate:
 *               <p>
 * 
 * <pre>
 *     A description of the compound name value is:
 *      &lt;prefix&gt;&lt;category_name&gt;| &lt;compareType&gt;| &lt;searchType&gt;| &lt;[Internal:]and|or&gt;| &lt;[External:]and|or|andnot&gt;
 *      An example name:
 *      request_category_prefixBrand|0|1|or|and
 *      This gets interpreted as follows: 
 *      The &quot;request_category_prefix&quot; is removed.  
 *      - &quot;Brand&quot; is taken as the name of the category in question.  
 *      - The next value is an integer code indicating
 *      how the chosen values should be compared (EQ=0,NOTEQ=1,GT=2,GTEQ=3,LT=4,
 *      LTEQ=5,IN=6,7,LIKE=8,NOTLIKE=9,NULL=10)
 *      - The third value is an integer code representing the type of search 
 *      (CONTAINS=0,IS=1,STARTSWITH=2,ENDSWITH=3,CONTAINSCASEINSENSITIVE=4,
 *      ISCASEINSENSITIVE=5,STARTSWITHCASEINSENSITIVE=6,ENDSWITHCASEINSENSITIVE=7,
 *      NUMBER=8,COLUMN=9,STRING=10)
 *      - The fourth value indicates whether all the values chosen for that category
 *      have to match or just one (ie, 'and' or 'or).
 *      - The fifth value indicates whether any matching product have to match this
 *      choice and any other category choices or just one of the category options selected.
 * </pre>
 * 
 * @requestParam request_history_index The search bean keeps a history of
 *               product sets that matched the search criteria at each step of
 *               the way. By setting this "history index", the user can jump to
 *               previous search results and take a different path.
 * @bean SearchCategoryBean searchCategories This beans holds the tree of search
 *       categories available for the user to search on. The top level of the
 *       tree holds the searchable categories and the leaves of each represent
 *       the possible values for each category.
 * @bean ProductSetBean productsFound The products that are in the current
 *       search set. Given the previous seach criteria chosen by the user, this
 *       is the set of products that match.
 */

public class CatSearch extends AbstractSearchAction {

	public CatSearch() {
	}

	public String getErrorPage(Map parm1) {
		return null;
	}

	public void performAction(HandlerData actionInfo) throws FatalException,
			InformationalException, ActionException {
		SearchCategoryBean categories = getSearchCategoryBean(actionInfo);
		Set search = new HashSet();
		String[] values;
		Iterator iter = actionInfo.getParamNames().iterator();
		while (iter.hasNext()) {
			String item = (String) iter.next();
			if (item.startsWith(REQUEST_CATEGORY_PREFIX)) {
				values = actionInfo.getParameterValues(item);
				item = item.substring(REQUEST_CATEGORY_PREFIX.length());
				if (values.length > 0 && !values[0].equals("choose one"))
					search.add(searchUtil.getQueryItem(item, values));
			}
		}
		UserBean userBean = getUserBean(actionInfo);
		String[] libraries = getController().getConfigValue("product_tables")
				.split(",");
		ProductSetBean productBean = getProductsFound(actionInfo);
		try {
			int hist = actionInfo.getParameterAsInt(REQUEST_HISTORY_INDEX, -1);
			if (hist == -11
					|| productBean.getFirstProductSet() == null
					|| isIncomplete(productBean, libraries,
							getCurrentUserPerms(actionInfo))) {
				getLog().debug(
						"Getting initial product set for "
								+ Arrays.asList(libraries));
				productBean.clear();
				for (String library : libraries) {
					if (library != null)
						searchUtil.getInitialSet(library, productBean, userBean
								.getPermissions());
				}
			} else if (hist >= 0) {
				productBean.setHistoryIndex(hist);
				productBean.clear(hist);
			}
		} catch (NumberFormatException e) {
		}
		getLog().debug("Searching with " + search);
		ProductSet newSet = searchUtil.search(productBean, search, userBean
				.getPermissions(), actionInfo.hasParam("toggle", emptyStrings));
		if (newSet != null)
			searchUtil.refreshCategories(newSet, categories, userBean
					.getPermissions());
		actionInfo.setRequestBean("canAccessExpired", canAccessExpired(
				actionInfo, libraries[0]));
	}

	private boolean canAccessExpired(HandlerData actionInfo, String library) {
		return getCurrentUserPerms(actionInfo).getPermission(library,
				Resource.DATATABLE, Right.ADMIN)
				|| getCurrentUserPerms(actionInfo).getPermission("all",
						Resource.DATABASE, Right.ADMIN);
	}

	private boolean isIncomplete(ProductSetBean productBean,
			String[] libraries, SecurityModel perms) {
		for (String library : libraries) {
			if (library != null)
				if (perms
						.getPermission(library, Resource.DATATABLE, Right.READ)
						&& productBean.getFirstProductSet().getProductList(
								library) == null)
					return true;
		}
		if (productBean.getFirstProductSet().getProductFamilies().size() > libraries.length)
			return true;
		return false;
	}

	public String getName() {
		return ACTION_CATEGORY_SEARCH;
	}
}