package com.allarphoto.ajaxclient.client;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public interface MainPanel extends Redrawable {

	public void recreateMenus();

	public void setCenter(Widget w);

	public void setWest(Widget w);

	public void setEast(Widget w);

	public void setScreen(String title, Widget center, Widget west,
			Widget east, Widget south);

	public Widget getWest();

	public Widget getCenter();

	public RootPanel getTopParent();

	public int getCenterWidth();

	public int getCenterHeight();

	public int getAbsoluteCenterLeft();

	public int getAbsoluteCenterTop();

	public int getEastWidth();

	public int getSouthWidth();
}
