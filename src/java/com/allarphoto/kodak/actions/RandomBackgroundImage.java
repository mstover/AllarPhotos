package com.lazerinc.kodak.actions;

import org.coinjema.context.CoinjemaDynamic;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.lazerinc.servlet.actionhandler.ActionHandlerBase;

public class RandomBackgroundImage extends ActionHandlerBase {

	public String getName() {
		return "kdk_background";
	}

	public void performAction(HandlerData info) throws ActionException {
		String[] images = getBackgroundImages();
		info.setUserBean("backgroundImg",
				images[(int) (Math.random() * images.length)]);
	}

	@CoinjemaDynamic(alias = "backgroundImages")
	public String[] getBackgroundImages() {
		return new String[0];
	}
}
