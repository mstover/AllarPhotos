package com.allarphoto.abi.actions;

import java.util.Map;

import org.coinjema.context.CoinjemaDynamic;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.allarphoto.servlet.actionhandler.ActionHandlerBase;

public class AbiSetup extends ActionHandlerBase {

	public void performAction(HandlerData info) throws ActionException {
		try {
			if (info.getAppBean("abiCategoryImages") == null) {
				info.setAppBean("abiCategoryImages", getCategoryImages());
			}
		} catch (Exception e) {
			getLog().debug("Couldn't find config object", e);
		}
	}

	public String getName() {
		return "abi";
	}

	@CoinjemaDynamic(alias = "categoryImages")
	public Map<String, String> getCategoryImages() {
		return null;
	}

}
