package com.allarphoto.servlet.actionhandler.search;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;
import strategiclibrary.util.Converter;

import com.allarphoto.client.beans.ProductSetBean;
import com.allarphoto.client.beans.SearchCategoryBean;

public class CheckNumImagesFound extends AbstractSearchAction {

	public CheckNumImagesFound() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void performAction(HandlerData actionInfo) throws ActionException {
		ProductSetBean foundImages = this.getProductsFound(actionInfo);
		SearchCategoryBean searchCategories = this
				.getSearchCategoryBean(actionInfo);
		if (foundImages.size() <= Converter.getInt(getController()
				.getConfigValue("default_product_display_number"))
				|| searchCategories.getSearchCategories() == null
				|| searchCategories.getSearchCategories().size() == 0) {
			throw new ActionException("TooFewImagesWarning");
		}
	}

	public String getName() {
		return "check_product_found_count";
	}

}
