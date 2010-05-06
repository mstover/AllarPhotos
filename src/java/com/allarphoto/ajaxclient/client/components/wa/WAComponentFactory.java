package com.lazerinc.ajaxclient.client.components.wa;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;
import com.lazerinc.ajaxclient.client.AjaxSystem;
import com.lazerinc.ajaxclient.client.CommandFactory;
import com.lazerinc.ajaxclient.client.Services;
import com.lazerinc.ajaxclient.client.SwappablePanel;
import com.lazerinc.ajaxclient.client.admin.LazerMenu;
import com.lazerinc.ajaxclient.client.beans.AjaxCart;
import com.lazerinc.ajaxclient.client.beans.AjaxProduct;
import com.lazerinc.ajaxclient.client.beans.AjaxProductFamily;
import com.lazerinc.ajaxclient.client.beans.Request;
import com.lazerinc.ajaxclient.client.components.CheckoutPanel;
import com.lazerinc.ajaxclient.client.components.SearchCenter;
import com.lazerinc.ajaxclient.client.components.ShoppingCart;
import com.lazerinc.ajaxclient.client.components.ThumbnailPanel;
import com.lazerinc.ajaxclient.client.components.irwin.IrwinSearchResultsViewer;
import com.lazerinc.ajaxclient.client.factory.DefaultComponentFactory;

public class WAComponentFactory extends DefaultComponentFactory {

	public Widget[] getBottomComponentStack() {
		// TODO Auto-generated method stub
		return super.getBottomComponentStack();
	}

	public Widget getFooter() {
		// TODO Auto-generated method stub
		return super.getFooter();
	}

	public LazerMenu getMainMenu() {
		return new WAMenu(getCommands());
	}

	public Widget getSplashTop() {
		return null;
	}

	public Widget[] getTopComponentStack() {
		return new Widget[] { getMainMenu() };
	}

	public String getStartingSection() {
		return "Browse welch";
	}

	public String getIconFolder() {
		return "icons_wa";
	}

	public void setSectionTitle(String title) {
	}

	public Widget createComponent(String classname, Object[] args) {
		if ("SearchTree".equals(classname)) {
			return new WASearchTree((SwappablePanel) args[0], (String) args[1],
					(args.length > 2 ? (String[]) args[2] : new String[] { "" }));
		} else if ("ShoppingCart".equals(classname)) {
			return new ShoppingCart((AjaxCart) args[0], (SearchCenter) args[1]);
		} else if ("VerifyScreen".equals(classname)) {
			return new WAVerifyScreen((AjaxCart) args[0],
					(CheckoutPanel) args[1]);
		} else if ("NumberSelector".equals(classname)) {
			if (args.length == 5)
				return new WANumberSelector(((Integer) args[0]).intValue(),
						((Integer) args[1]).intValue(), ((Integer) args[2])
								.intValue(), (ClickListener) args[3],
						((Integer) args[4]).intValue());
			else
				return new WANumberSelector((int[]) args[0],
						(ClickListener) args[1], ((Integer) args[2]).intValue());
		} else if ("SearchResultsViewer".equals(classname)) {
			return new WaSearchResultsViewer((String[][]) args[0],
					(String) args[1]);
		} else if("ThumbnailPanel".equals(classname))
		{
			if(args.length == 3) return new WAThumbnailPanel((AjaxProduct)args[0],((Boolean)args[1]).booleanValue(),((Integer)args[2]).intValue());
			else return new WAThumbnailPanel((Request)args[0],((Integer)args[1]).intValue());
		}else
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
																new WaSearchResultsViewer(
																		null,
																		null));
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
																		createComponent(
																				"ShoppingCart",
																				new Object[] {
																						Services
																								.getServices().cart,
																						sc }),
																		null);
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
		cmd.add("User Info", getUserEditScreen());
		cmd.add("PendingOrders", getPendingOrdersScreen());
		cmd.add("usertips", getHelpCommand()); // getUserTipDisplay());
		cmd.add("support", getSupportScreen());
		cmd.add("logoff",getLogoffCmd());
		return cmd;
	}

	protected native void redirectForLogin() /*-{
	 window.open("/lazerweb/wa","_top");
	 }-*/;
	
	public Command getHelpCommand() {
		return new Command() {
			public void execute() {
				sendUserToHelp();
			}
		};
	}

	protected native void sendUserToHelp() /*-{
	 window.open("wa/help.jsp","_help");
	 }-*/;

	protected Command getLoginScreen() {
		return new Command() {
			public void execute() {
				redirectForLogin();
			}
		};
	}

}
