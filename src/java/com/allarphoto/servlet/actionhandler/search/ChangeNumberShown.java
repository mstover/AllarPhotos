package com.allarphoto.servlet.actionhandler.search;

import static com.allarphoto.servlet.ActionConstants.ACTION_CHANGE_NUMBER_SHOWN;
import static com.allarphoto.servlet.RequestConstants.REQUEST_CHANGE_NUMBER_SHOWN;

import java.util.Map;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.allarphoto.client.beans.ProductSetBean;

/**
 * This actin is used to change the number of products below which the user is
 * given the option of browsing the products directly. If the search set has
 * fewer than this number of products, the user may start browsing those
 * products.
 * 
 * @author Michael Stover
 * @version 1.0
 * @action action_change_number_shown
 * @requestParam request_change_number_shown An integer representing the new
 *               display size number.
 * @bean ProductSetBean productsFound The display size of the set of found
 *       products is set.
 */

public class ChangeNumberShown extends AbstractSearchAction {

	public ChangeNumberShown() {
	}

	public String getErrorPage(Map parm1) {
		return null;
	}

	public void performAction(HandlerData actionInfo)
			throws com.allarphoto.client.exceptions.FatalException,
			com.allarphoto.client.exceptions.InformationalException,
			ActionException {
		ProductSetBean productBean = getProductsFound(actionInfo);
		String changeTo = actionInfo.getParameter(REQUEST_CHANGE_NUMBER_SHOWN);
		if (changeTo == null) {
			productBean.setDisplaySize(Integer.parseInt(getController()
					.getConfigValue("number_of_products_shown")));
		} else {
			productBean.setDisplaySize(Integer.parseInt(changeTo));
		}
		productBean.setPageSize(Integer.parseInt(getController()
				.getConfigValue("search_display_size")));
	}

	public String getName() {
		return ACTION_CHANGE_NUMBER_SHOWN;
	}
}