package com.lazerinc.ajaxclient.client.factory;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class HbiComponentFactory extends DefaultComponentFactory {

	public HbiComponentFactory() {
	}

	public Widget getFooter() {
		return super.getFooter();
	}

	public Widget getSplashTop() {
		HorizontalPanel splash = new HorizontalPanel();
		splash.setWidth("100%");
		Image logo = new Image("hbi/images/hbi-logo.gif");
		Image banner = new Image("hbi/images/hanes-brands-logo.gif");
		splash.add(logo);
		splash.setCellHorizontalAlignment(logo,
				HasHorizontalAlignment.ALIGN_LEFT);
		splash.add(banner);
		splash.setCellHorizontalAlignment(banner,
				HasHorizontalAlignment.ALIGN_RIGHT);
		splash.addStyleName("hb_splash");
		return splash;
	}

	public Widget[] getBottomComponentStack() {
		return new Widget[0];
	}

	public Widget[] getTopComponentStack() {
		return new Widget[] { getMainMenu(), getSplashTop(), getSectionTitle() };
	}

}
