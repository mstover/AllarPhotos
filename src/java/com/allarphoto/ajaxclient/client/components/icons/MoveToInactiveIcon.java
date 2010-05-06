package com.lazerinc.ajaxclient.client.components.icons;

import com.lazerinc.ajaxclient.client.beans.AjaxProduct;

public class MoveToInactiveIcon extends MoveToActiveIcon {

	public MoveToInactiveIcon() {
		addStyleName("button-icon");
		init();
	}

	public MoveToInactiveIcon(AjaxProduct p) {
		super(p);
	}

	protected String getNewDir() {
		return "Inactive";
	}

	public String getIconUrl() {
		return "move_obsolete.gif";
	}

	public String getToolTip() {
		return "Move Image to Inactive";
	}

}
