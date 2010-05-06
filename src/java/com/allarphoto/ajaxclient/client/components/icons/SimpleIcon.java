package com.lazerinc.ajaxclient.client.components.icons;

import com.google.gwt.user.client.ui.ClickListener;

public class SimpleIcon extends BaseIcon {

	String myUrl;

	ClickListener action;

	String toolTip;

	public SimpleIcon() {
		addStyleName("button-icon");
	}

	public SimpleIcon(String url, ClickListener a, String tt) {
		super();
		myUrl = url;
		action = a;
		toolTip = tt;
		init();
	}

	protected ClickListener createClickListener() {
		return action;
	}

	public String getIconUrl() {
		return myUrl;
	}

	public String getToolTip() {
		return toolTip;
	}

}
