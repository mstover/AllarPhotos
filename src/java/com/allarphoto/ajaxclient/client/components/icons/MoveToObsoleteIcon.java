package com.allarphoto.ajaxclient.client.components.icons;

import com.allarphoto.ajaxclient.client.beans.AjaxProduct;

public class MoveToObsoleteIcon extends MoveToActiveIcon {

	public MoveToObsoleteIcon() {
		addStyleName("button-icon");
		init();
	}

	public MoveToObsoleteIcon(AjaxProduct p) {
		super(p);
	}

	protected String getNewDir() {
		return "Obsolete";
	}

	public String getIconUrl() {
		return "move_obsolete.gif";
	}

	public String getToolTip() {
		return "Move Image to Obsolete";
	}

}
