package com.allarphoto.hbi.actions;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.coinjema.context.CoinjemaDynamic;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;
import strategiclibrary.service.webaction.ServletHandlerData;

import com.allarphoto.client.exceptions.LazerwebException;
import com.allarphoto.ecommerce.ProductFamily;
import com.allarphoto.servlet.actionhandler.ActionHandlerBase;
import com.allarphoto.utils.Resource;

public class HbiRedirect extends ActionHandlerBase {

	public HbiRedirect() {
	}

	public String getName() {
		return "hbi_redirect";
	}

	public void performAction(HandlerData info) throws ActionException {
		Collection<Resource> families = this.getCurrentUserPerms(info)
				.getAvailableResourceList(Resource.DATATABLE);
		if (families.size() == 1) {
			String tablename = families.iterator().next().getName();
			Map<String, String> categories = getCategoryImages();
			ProductFamily family = this.dbUtil.getProductFamily(tablename);
			String dir = categories.get(tablename + "_dir");
			try {
				((ServletHandlerData) info)
						.getResponse()
						.sendRedirect(
								dir
										+ "/catsearch.jsp?request_history_index=-11&request_browse_set=0&request_category_prefixArchive|0|1|or|and=Active&request_category_prefixLibrary+Name|0|1|or|and="
										+ family.getDescriptiveName());
			} catch (IOException e) {
				throw new LazerwebException();
			}
		}

	}

	@CoinjemaDynamic(alias = "categoryImages")
	public Map<String, String> getCategoryImages() {
		return null;
	}

}
