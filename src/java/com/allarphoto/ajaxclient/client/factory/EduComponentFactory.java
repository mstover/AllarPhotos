package com.allarphoto.ajaxclient.client.factory;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;
import com.allarphoto.ajaxclient.client.AjaxSystem;
import com.allarphoto.ajaxclient.client.CommandFactory;
import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.SwappablePanel;
import com.allarphoto.ajaxclient.client.admin.LazerMenu;
import com.allarphoto.ajaxclient.client.beans.AjaxCart;
import com.allarphoto.ajaxclient.client.beans.AjaxProduct;
import com.allarphoto.ajaxclient.client.beans.AjaxProductFamily;
import com.allarphoto.ajaxclient.client.beans.Request;
import com.allarphoto.ajaxclient.client.components.CheckoutPanel;
import com.allarphoto.ajaxclient.client.components.SearchCenter;
import com.allarphoto.ajaxclient.client.components.SouthCartThumbnailPanel;
import com.allarphoto.ajaxclient.client.components.SouthShoppingCart;
import com.allarphoto.ajaxclient.client.components.edu.EduNumberSelector;
import com.allarphoto.ajaxclient.client.components.edu.EduProductToolbar;
import com.allarphoto.ajaxclient.client.components.mcc.MccSearchTree;
import com.allarphoto.ajaxclient.client.components.mcc.MccVerifyScreen;

public class EduComponentFactory extends DefaultComponentFactory {

	public Widget[] getBottomComponentStack() {
		// TODO Auto-generated method stub
		return super.getBottomComponentStack();
	}

	public Widget getFooter() {
		// TODO Auto-generated method stub
		return super.getFooter();
	}

	public LazerMenu getMainMenu() {
		return new EduMenu(getCommands());
	}

	public Widget getSplashTop() {
		return null;
	}

	public Widget[] getTopComponentStack() {
		return new Widget[] { getMainMenu() };
	}

	public String getStartingSection() {
		return "Browse edu_demo";
	}

	public String getIconFolder() {
		return "icons_edu";
	}

	public void setSectionTitle(String title) {
	}

	public Widget createComponent(String classname, Object[] args) {
		if ("SearchTree".equals(classname)) {
			return new MccSearchTree((SwappablePanel) args[0],
					(String) args[1], (args.length > 2 ? (String[]) args[2]
							: null));
		} else if ("VerifyScreen".equals(classname)) {
			return new MccVerifyScreen((AjaxCart) args[0],
					(CheckoutPanel) args[1]);
		} else if ("ShoppingCart".equals(classname)) {
			return new SouthShoppingCart((AjaxCart) args[0],
					(SearchCenter) args[1]);
		} else if ("NumberSelector".equals(classname)) {
			if (args.length == 5)
				return new EduNumberSelector(((Integer) args[0]).intValue(),
						((Integer) args[1]).intValue(), ((Integer) args[2])
								.intValue(), (ClickListener) args[3],
						((Integer) args[4]).intValue());
			else
				return new EduNumberSelector((int[]) args[0],
						(ClickListener) args[1], ((Integer) args[2]).intValue());
		} else if ("CartThumbnailPanel".equals(classname)) {
			if (args.length == 3)
				return new SouthCartThumbnailPanel((AjaxProduct) args[0],
						((Boolean) args[1]).booleanValue(), ((Integer) args[2])
								.intValue());
			else if (args.length == 2)
				if (args[0] instanceof AjaxProduct)
					return new SouthCartThumbnailPanel((AjaxProduct) args[0],
							((Boolean) args[1]).booleanValue());
				else
					return new SouthCartThumbnailPanel((Request) args[0],
							((Integer) args[1]).intValue());
			else
				return new SouthCartThumbnailPanel((Request) args[0]);
		} else if ("ProductToolbar".equals(classname)) {
			return new EduProductToolbar((AjaxProduct) args[0]);
		} else
			return super.createComponent(classname, args);
	}

	protected CommandFactory createCommandFactory() {
		commandsSet = false;
		final CommandFactory cmd = new CommandFactory();
		Services.getServices().libraryInfoService.getLibraries("read",
				new AsyncCallback() {

					public void onFailure(Throwable caught) {

					}

					public void onSuccess(Object result) {
						if (((AjaxProductFamily[]) result).length > 0) {
							final AjaxProductFamily[] families = (AjaxProductFamily[]) result;
							for (int i = 0; i < families.length; i++) {
								final int index = i;
								cmd
										.add("Browse "
												+ families[i].getFamilyName(),
												new Command() {

													public void execute() {
														SearchCenter sc = new SearchCenter(
																AjaxSystem
																		.getLabel(""));
														Services.getServices().mainPanel
																.setScreen(
																		"Browse "
																				+ families[index]
																						.getDescriptiveName(),
																		sc,
																		createComponent(
																				"SearchTree",
																				new Object[] {
																						sc,
																						families[index]
																								.getDescriptiveName() }),
																		null,
																		createComponent(
																				"ShoppingCart",
																				new Object[] {
																						Services
																								.getServices().cart,
																						sc }));
													}

												});
							}
						}
						commandsSet = true;
					}

				});
		cmd.add("AdminLibraries", getLibraryAdminCmd());
		cmd.add("AddUser", getUserAddCmd());
		cmd.add("UploadImages", getImageUploaderCmd());
		cmd.add("Return", getReturnCmd());
		cmd.add("AddGroup", getAddGroupCmd());
		cmd.add("AdminGroups", getAdminGroupCmd());
		cmd.add("AdminUsers", getAdminUserCmd());
		cmd.add("ManageUploads", getUploadManager());
		cmd.add("AccessRequests", getAccessRequestManager());
		cmd.add("Login", getLoginScreen());
		cmd.add("EventLog", getEventLogScreen());
		cmd.add("ViewOrders", getViewOrdersScreen());
		cmd.add("MyOrders", getMyOrdersScreen());
		cmd.add("PendingOrders", getPendingOrdersScreen());
		cmd.add("support", getSupportScreen());
		cmd.add("logoff",getLogoffCmd());
		return cmd;
	}

}
