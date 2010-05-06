package com.lazerinc.ajaxclient.client.factory;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuItem;
import com.lazerinc.ajaxclient.client.CommandFactory;
import com.lazerinc.ajaxclient.client.admin.LazerMenu;
import com.lazerinc.ajaxclient.client.components.BusyPopup;

public class MccMenu extends LazerMenu {
	public MccMenu(boolean vertical, CommandFactory c) {
		super(vertical, c);
	}

	public MccMenu(CommandFactory c) {
		super(c);
	}

	protected void addMyStuffMenu(CommandFactory cmd) {
	}

	protected void addReturn(CommandFactory cmd) {
	}

	protected void addLeftHeader(CommandFactory cmd) {
		Image logo = new Image("mcc/images/mcc-logo.jpg");
		add(logo);
	}

	protected void addRightHeader(CommandFactory cmd) {
		Image banner = new Image("mcc/images/mcc-logo-tag.jpg");
		add(banner);
		setCellHorizontalAlignment(banner, HasHorizontalAlignment.ALIGN_RIGHT);
		setCellHorizontalAlignment(mainBar, HasHorizontalAlignment.ALIGN_LEFT);
	}
}
