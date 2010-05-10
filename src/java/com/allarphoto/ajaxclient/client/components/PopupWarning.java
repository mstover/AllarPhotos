package com.allarphoto.ajaxclient.client.components;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;

public class PopupWarning extends PopupPanel {

	public PopupWarning(String warning) {
		super(true);
		add(new HTML(warning));
		addStyleName("popup-info");
		setPopupPosition(Window.getClientWidth() / 3,
				Window.getClientHeight() / 3);
		setVisible(true);
		show();
	}

}
