package com.allarphoto.ajaxclient.client.components.icons;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class CloseIcon extends BaseIcon {

	PopupPanel close;

	public CloseIcon() {
		addStyleName("button-icon");
		init();
	}

	public CloseIcon(PopupPanel popup) {
		this();
		close = popup;
		init();
	}

	protected ClickListener createClickListener() {
		return new ClickListener() {

			public void onClick(Widget sender) {
				close.hide();
				close.setVisible(false);
			}

		};
	}

	public String getIconUrl() {
		return "close.gif";
	}

	public String getToolTip() {
		return "Close Popup";
	}

}
