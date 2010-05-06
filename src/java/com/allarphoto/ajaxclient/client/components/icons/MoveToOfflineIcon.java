package com.lazerinc.ajaxclient.client.components.icons;

import com.lazerinc.ajaxclient.client.beans.AjaxProduct;

public class MoveToOfflineIcon extends MoveToActiveIcon {

	public MoveToOfflineIcon() {
		addStyleName("button-icon");
		init();
	}

	public MoveToOfflineIcon(AjaxProduct p) {
		super(p);
	}

	protected String getNewDir() {
		return "Offline";
	}

	public String getIconUrl() {
		return "move_offline.gif";
	}

	public String getToolTip() {
		return "Move Image to Offline";
	}

}
