package com.lazerinc.ajaxclient.client.components.abi;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.lazerinc.ajaxclient.client.AjaxSystem;
import com.lazerinc.ajaxclient.client.CommandFactory;
import com.lazerinc.ajaxclient.client.Services;
import com.lazerinc.ajaxclient.client.admin.LazerMenu;
import com.lazerinc.ajaxclient.client.beans.AjaxProductFamily;
import com.lazerinc.ajaxclient.client.components.SearchCenter;

public class ABIMenu extends LazerMenu {
	
	protected boolean addBrowseMenu = false;

	public ABIMenu(CommandFactory c) {
		super(c);
		// TODO Auto-generated constructor stub
	}

	public ABIMenu(boolean vertical, CommandFactory c) {
		super(vertical, c);
		// TODO Auto-generated constructor stub
	}

	protected void addBrowseMenu(CommandFactory cmd) {
		if(addBrowseMenu) super.addBrowseMenu(cmd);
	}

	protected void loadLibraryMenu(final CommandFactory cmd) {
		Services.getServices().libraryInfoService.getLibraries("read",
				new AsyncCallback() {

					public void onFailure(Throwable caught) {
						asyncCalls[LIBRARIES] = true;
					}

					public void onSuccess(Object result) {
						int items = 0;
						if (((AjaxProductFamily[]) result).length > 0) {
							final AjaxProductFamily[] families = (AjaxProductFamily[]) result;
							for (int i = 0; i < families.length; i++) {
								final int index = i;
								libraries.addItem(families[i]
										.getDescriptiveName(), new Command() {

									public void execute() {
										SearchCenter sc = new SearchCenter(
												AjaxSystem.getLabel(""));
										Services.getServices().mainPanel
												.setScreen(
														"Browse "
																+ families[index]
																		.getDescriptiveName(),
														sc,
														Services.getServices().factory
																.createComponent(
																		"SearchTree",
																		new Object[] {
																				sc,
																				families[index]
																						.getDescriptiveName() }),
														null,
														Services.getServices().factory
																.createComponent(
																		"ShoppingCart",
																		new Object[] {
																				Services
																						.getServices().cart,
																				sc }));
									}

								});
								items++;
							}
						}
						if (items > 1) {
							addBrowseMenu = true;
						} else {

							Services.getServices().libraryInfoService
									.getLibraries("admin", new AsyncCallback() {

										public void onFailure(Throwable caught) {

										}

										public void onSuccess(Object result) {
											if (((AjaxProductFamily[]) result).length > 0) {
												addBrowseMenu = true;
											}
										}

									});
						}
						asyncCalls[LIBRARIES] = true;
					}

				});
	}

	protected void addHelpMenu(final CommandFactory cmd) {
		MenuItem item = new MenuItem("Help", cmd.get("Help"));
		item.setTitle("Jump to the Help section");
		mainBar.addItem(item);
	}

	protected void addRightHeader(CommandFactory cmd) {
		super.addRightHeader(cmd);
	}

	protected void addLeftHeader(CommandFactory cmd) {
		setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		Image logo = new Image("abi/images/abi_wht_sm.gif");
		add(logo);
	}

	protected void addGroupMenu(CommandFactory cmd) {
		if (isLoggedIn) {
			if (isSuperAdmin) {
				MenuItem item = new MenuItem("Groups", getGroupMenu(cmd));
				item.setTitle("Admin groups and group permissions");
				mainBar.addItem(item);
			}
			if(isGroupAdmin) {
				MenuItem item = new MenuItem("Users", getUserMenu(cmd));
				item.setTitle("Modify, add, and delete users");
				mainBar.addItem(item);
			}
		}
	}

	protected void addReturn(CommandFactory cmd) {
	}

}
