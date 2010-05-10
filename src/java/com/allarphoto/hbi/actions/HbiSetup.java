package com.allarphoto.hbi.actions;

import java.util.Map;

import org.coinjema.context.CoinjemaDynamic;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.allarphoto.servlet.actionhandler.ActionHandlerBase;

public class HbiSetup extends ActionHandlerBase {

	public HbiSetup() {
		super();
	}

	public void performAction(HandlerData info) throws ActionException {
		try {
			if (info.getAppBean("hbiCategoryImages") == null) {
				info.setAppBean("hbiCategoryImages", getCategoryImages());
			}
			if (info.getAppBean("imgTypeIcons") == null) {
				info.setAppBean("imgTypeIcons", getImgTypeImages());
			}
		} catch (Exception e) {
			getLog().debug("Couldn't find config object", e);
		}
	}

	public String getName() {
		return "hbi";
	}

	@CoinjemaDynamic(alias = "categoryImages")
	public Map<String, String> getCategoryImages() {
		return null;
	}

	@CoinjemaDynamic(alias = "imageTypeIcons")
	public Map<String, String> getImgTypeImages() {
		return null;
	}

}
