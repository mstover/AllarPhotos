package com.lazerinc.servlet.actionhandler.admin.keyword;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.coinjema.util.Tuple;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;
import strategiclibrary.service.webaction.ServletHandlerData;

import com.lazerinc.client.beans.ProductSetBean;
import com.lazerinc.servlet.actionhandler.search.AbstractSearchAction;

public class FileExport extends AbstractSearchAction {

	public FileExport() {
	}

	public String getName() {
		return "keyword_file_export";
	}

	public void performAction(HandlerData info) throws ActionException {

		ProductSetBean productBean = getProductsFound(info);
		Tuple<Collection<String>, Collection<Map<String, Collection<String>>>> export = productService
				.exportKeywords(productBean.getCurrentProductSet()
						.getProductList());
		info.setRequestBean("export", export.second);
		info.setRequestBean("headers", export.first);
		info.setRequestBean("primaryDelim", "\t");
		info.setRequestBean("secondaryDelim", "|");
		((ServletHandlerData) info).getResponse().setContentType("text/csv");

	}

}
