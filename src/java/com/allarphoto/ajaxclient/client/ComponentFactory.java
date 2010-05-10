package com.allarphoto.ajaxclient.client;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Widget;
import com.allarphoto.ajaxclient.client.admin.LazerMenu;

public abstract class ComponentFactory implements IsSerializable {

	public abstract LazerMenu getMainMenu();

	public abstract Widget getToolbar();

	public abstract Widget getSplashTop();

	public abstract Widget getFooter();

	public abstract CommandFactory getCommands();

	public abstract Widget[] getTopComponentStack();

	public abstract Widget[] getBottomComponentStack();

	public abstract String getStartingSection();

	public abstract CommandFactory regetCommands();

	public abstract void setSectionTitle(String title);

	public abstract Widget createComponent(String widgetClass, Object[] args);

	public abstract String getIconFolder();

	public abstract boolean isCommandsSet();

}
