package com.allarphoto.ajaxclient.client.components.wa;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.allarphoto.ajaxclient.client.AjaxSystem;
import com.allarphoto.ajaxclient.client.CommandFactory;
import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.admin.LazerMenu;
import com.allarphoto.ajaxclient.client.beans.AjaxProductFamily;
import com.allarphoto.ajaxclient.client.components.BusyPopup;
import com.allarphoto.ajaxclient.client.components.SearchCenter;

public class WAMenu extends LazerMenu {
	protected boolean addBrowseMenu = true;

	public WAMenu(boolean vertical, CommandFactory c) {
		super(vertical, c);
		// TODO Auto-generated constructor stub
	}

	public WAMenu(CommandFactory c) {
		super(c);
		// TODO Auto-generated constructor stub
	}

	protected void addLeftHeader(CommandFactory cmd) {
		Image logo = new Image("wa/images/wa-logo.gif");
		logo.addStyleName("libLogo");
		add(logo);
	}
	
	/* --- clipboard for addRightHeader
	HTML pwdBy = new HTML("Powered by LazerWeb&trade;");
	pwdBy.addStyleName("poweredBy");
	add(pwdBy);
	*/
	
	// From Super
	/*
	HTML legend = new HTML("Icon<br/>Reference");
	legend.addStyleName("legend");
	add(legend);
	setCellHorizontalAlignment(legend, HasHorizontalAlignment.ALIGN_RIGHT);
	legend.addMouseListener(getLegendListener());
	*/
	
	protected void addRightHeader(CommandFactory cmd) {
		super.addRightHeader(cmd);
	}

	protected void addReturn(CommandFactory cmd) {
		setCellHorizontalAlignment(mainBar, HasHorizontalAlignment.ALIGN_LEFT);
	}

	protected void addBrowseMenu(CommandFactory cmd) {
		if(addBrowseMenu) super.addBrowseMenu(cmd);
	}
	
	/*
	protected void addHelpMenu(final CommandFactory cmd) {
		MenuItem item = new MenuItem("Help", cmd.get("Help"));
		item.setTitle("Jump to the Help section");
		mainBar.addItem(item);
	}
	*/

	protected void addMyStuffMenu(final CommandFactory cmd) {
		if (isLoggedIn) {
			MenuItem item;
			item = new MenuItem("My Account", getMyStuff(cmd));
			item
					.setTitle("View details and information specific to your account");
			mainBar.addItem(item);
		}
	}
	
	protected void addSupportFormLink(CommandFactory cmd) {
		MenuItem item = new MenuItem("User Tips", cmd.get("usertips"));
		item.setTitle("User Tips for WelchAllyn online library.");
		mainBar.addItem(item);
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
														Services.getServices().factory
																.createComponent(
																		"ShoppingCart",
																		new Object[] {
																				Services
																						.getServices().cart,
																				sc }),
														null);
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

}
