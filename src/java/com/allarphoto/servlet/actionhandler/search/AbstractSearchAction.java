package com.allarphoto.servlet.actionhandler.search;

import org.coinjema.context.CoinjemaDynamic;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.allarphoto.client.beans.ProductBean;
import com.allarphoto.client.beans.ProductSetBean;
import com.allarphoto.client.beans.SearchCategoryBean;
import com.allarphoto.client.util.SearchUtil;
import com.allarphoto.servlet.actionhandler.ActionHandlerBase;

public abstract class AbstractSearchAction extends ActionHandlerBase {
	protected SearchUtil searchUtil = new SearchUtil();

	protected SearchCategoryBean getSearchCategoryBean(HandlerData info)
			throws ActionException {
		SearchCategoryBean bean = (SearchCategoryBean) info
				.getUserBean("searchCategories");
		Class<? extends SearchCategoryBean> expectedClass = getSearchCategoryBeanClass();
		if (bean == null || !bean.getClass().equals(expectedClass)) {
			info.removeUserBean("searchCategories");
			return info.getUserBean("searchCategories", expectedClass);
		}
		return bean;
	}

	protected ProductSetBean getProductsFound(HandlerData info)
			throws ActionException {
		return info.getUserBean("productsFound", ProductSetBean.class);
	}

	@CoinjemaDynamic(type = "searchCategoryBeanClass")
	protected Class<? extends SearchCategoryBean> getSearchCategoryBeanClass() {
		return null;
	}

}
