package com.allarphoto.ajaxclient.client.factory;

import java.util.List;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import com.allarphoto.ajaxclient.client.AjaxSystem;
import com.allarphoto.ajaxclient.client.CommandFactory;
import com.allarphoto.ajaxclient.client.ComponentFactory;
import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.SwappablePanel;
import com.allarphoto.ajaxclient.client.UpdateListener;
import com.allarphoto.ajaxclient.client.admin.EventLogPanel;
import com.allarphoto.ajaxclient.client.admin.GroupAddPanel;
import com.allarphoto.ajaxclient.client.admin.GroupAdminTreeActions;
import com.allarphoto.ajaxclient.client.admin.LazerMenu;
import com.allarphoto.ajaxclient.client.admin.UploadManager;
import com.allarphoto.ajaxclient.client.admin.UserAccessAdminPanel;
import com.allarphoto.ajaxclient.client.admin.UserAdminPanel;
import com.allarphoto.ajaxclient.client.admin.UserControlPanel;
import com.allarphoto.ajaxclient.client.admin.library.LibraryTreeActions;
import com.allarphoto.ajaxclient.client.beans.AjaxAccessRequest;
import com.allarphoto.ajaxclient.client.beans.AjaxCart;
import com.allarphoto.ajaxclient.client.beans.AjaxGroup;
import com.allarphoto.ajaxclient.client.beans.AjaxOrder;
import com.allarphoto.ajaxclient.client.beans.AjaxProduct;
import com.allarphoto.ajaxclient.client.beans.AjaxProductFamily;
import com.allarphoto.ajaxclient.client.beans.Request;
import com.allarphoto.ajaxclient.client.components.BusyPopup;
import com.allarphoto.ajaxclient.client.components.CartThumbnailPanel;
import com.allarphoto.ajaxclient.client.components.CheckoutPanel;
import com.allarphoto.ajaxclient.client.components.LoginPanel;
import com.allarphoto.ajaxclient.client.components.NumberSelector;
import com.allarphoto.ajaxclient.client.components.OrderDisplay;
import com.allarphoto.ajaxclient.client.components.OrderFilter;
import com.allarphoto.ajaxclient.client.components.PopupSupportRequest;
import com.allarphoto.ajaxclient.client.components.ProductToolbar;
import com.allarphoto.ajaxclient.client.components.SearchCenter;
import com.allarphoto.ajaxclient.client.components.SearchResultsViewer;
import com.allarphoto.ajaxclient.client.components.SearchTree;
import com.allarphoto.ajaxclient.client.components.ShoppingCart;
import com.allarphoto.ajaxclient.client.components.ThumbnailPanel;
import com.allarphoto.ajaxclient.client.components.UserTipDisplay;
import com.allarphoto.ajaxclient.client.components.VerifyScreen;
import com.allarphoto.ajaxclient.client.uploader.TreeControls;
import com.allarphoto.ajaxclient.client.uploader.Uploader;

public class DefaultComponentFactory extends ComponentFactory implements
		IsSerializable {

	transient CommandFactory cmd;

	protected boolean commandsSet = false;

	transient Label sectionTitle;

	public DefaultComponentFactory() {
	}

	public Widget getFooter() {
		return null;
	}

	public LazerMenu getMainMenu() {
		return new LazerMenu(getCommands());
	}

	public Widget getSplashTop() {
		return null;
	}

	public String getStartingSection() {
		return null;
	}

	public String getIconFolder() {
		return "icon24";
	}

	public Widget getToolbar() {
		return null;
	}

	public CommandFactory regetCommands() {
		cmd = createCommandFactory();
		return cmd;
	}

	public CommandFactory getCommands() {
		if (cmd == null)
			cmd = createCommandFactory();
		return cmd;
	}

	public Widget[] getBottomComponentStack() {
		return new Widget[0];
	}

	public boolean isCommandsSet() {
		return commandsSet;
	}

	protected Label getSectionTitle() {
		if (sectionTitle == null) {
			sectionTitle = new Label("");
			sectionTitle.addStyleName("page-title");
		}
		return sectionTitle;
	}

	public void setSectionTitle(String title) {
		sectionTitle.setText(title);

	}

	public Widget createComponent(String classname, Object[] args) {
		if ("SearchTree".equals(classname)) {
			return new SearchTree((SwappablePanel) args[0], (String) args[1],
					(args.length > 2 ? (String[]) args[2] : null));
		} else if ("VerifyScreen".equals(classname)) {
			return new VerifyScreen((AjaxCart) args[0], (CheckoutPanel) args[1]);
		} else if ("NumberSelector".equals(classname)) {
			if (args.length == 5)
				return new NumberSelector(((Integer) args[0]).intValue(),
						((Integer) args[1]).intValue(), ((Integer) args[2])
								.intValue(), (ClickListener) args[3],
						((Integer) args[4]).intValue());
			else
				return new NumberSelector((int[]) args[0],
						(ClickListener) args[1], ((Integer) args[2]).intValue());
		} else if ("ProductToolbar".equals(classname)) {
			return new ProductToolbar((AjaxProduct) args[0]);
		} else if ("ShoppingCart".equals(classname)) {
			return new ShoppingCart((AjaxCart) args[0], (SearchCenter) args[1]);
		} else if ("SearchResultsViewer".equals(classname)) {
			return new SearchResultsViewer((String[][]) args[0],
					(String) args[1]);
		} else if ("CartThumbnailPanel".equals(classname)) {
			if (args.length == 3)
				return new CartThumbnailPanel((AjaxProduct) args[0],
						((Boolean) args[1]).booleanValue(), ((Integer) args[2])
								.intValue());
			else if (args.length == 2)
				if (args[0] instanceof AjaxProduct)
					return new CartThumbnailPanel((AjaxProduct) args[0],
							((Boolean) args[1]).booleanValue());
				else
					return new CartThumbnailPanel((Request) args[0],
							((Integer) args[1]).intValue());
			else
				return new CartThumbnailPanel((Request) args[0]);

		} else if("OrderDisplay".equals(classname))
		{
			if(args.length == 1) return new OrderDisplay((String)args[0]);
			else return new OrderDisplay((AjaxOrder)args[0],(UpdateListener)args[1]);
		}else if("ThumbnailPanel".equals(classname))
		{
			if(args.length == 3) return new ThumbnailPanel((AjaxProduct)args[0],((Boolean)args[1]).booleanValue(),((Integer)args[2]).intValue());
			else return new ThumbnailPanel((Request)args[0],((Integer)args[1]).intValue());
		}
		return null;
	}

	public Widget[] getTopComponentStack() {
		return new Widget[] { getMainMenu(), getSectionTitle() };
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
																		new ShoppingCart(
																				Services
																						.getServices().cart,
																				sc),
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
		cmd.add("PendingOrders", getPendingOrdersScreen());
		cmd.add("User Info", getUserEditScreen());
		cmd.add("support", getSupportScreen());
		cmd.add("usertips", getUserTipDisplay());
		cmd.add("logoff",getLogoffCmd());
		return cmd;
	}

	protected Command getLoginScreen() {
		return new Command() {

			public void execute() {
				Services.getServices().mainPanel.setScreen("Login",
						new LoginPanel(), null, null, null);
			}

		};
	}

	protected Command getAccessRequestManager() {
		return new Command() {

			public void execute() {
				BusyPopup.waitFor("Loading User Access Request Screen");
				Services.getServices().userInfoService.getAccessRequests(true,
						new AsyncCallback() {

							public void onFailure(Throwable caught) {
								Services.getServices().mainPanel.setScreen(
										"Failed Request", new Label(caught
												.toString()), null, null, null);
								BusyPopup
										.done("Loading User Access Request Screen");
							}

							public void onSuccess(Object result) {
								Services.getServices().mainPanel.setScreen(
										"Manage User Access Requests",
										new UserAccessAdminPanel(
												(AjaxAccessRequest[]) result),
										null, null, null);
								BusyPopup
										.done("Loading User Access Request Screen");
							}
						});
			}

		};
	}

	protected Command getAdminUserCmd() {
		return new Command() {

			public void execute() {
				BusyPopup.waitFor("Loading User Admin Screen");
				new UserControlPanel();
				Services.getServices().userInfoService.getGroups(true,
						new AsyncCallback() {

							public void onFailure(Throwable caught) {
								Services.getServices().mainPanel.setScreen(
										"Failed Request", new Label(caught
												.toString()), null, null, null);
								BusyPopup.done("Loading User Admin Screen");
							}

							public void onSuccess(Object result) {
								Services.getServices().mainPanel
										.setScreen("User Admin Screen",
												new UserAdminPanel(
														(AjaxGroup[]) result),
												null, null, null);
								BusyPopup.done("Loading User Admin Screen");
							}
						});
			}

		};
	}

	protected Command getUploadManager() {
		return new Command() {

			public void execute() {
				BusyPopup.waitFor("Loading recent uploads");
				Services.getServices().libraryInfoService
						.getPendingUploads(new AsyncCallback() {

							public void onFailure(Throwable caught) {
								Services.getServices().mainPanel.setScreen(
										"Failed Request", new Label(caught
												.toString()), null, null, null);
								BusyPopup.done("Loading recent uploads");
							}

							public void onSuccess(Object result) {
								Services.getServices().mainPanel.setScreen(
										"Manage Upload Packages",
										new UploadManager((List) result), null,
										null, null);
								BusyPopup.done("Loading recent uploads");
							}

						});
			}

		};
	}

	protected Command getEventLogScreen() {
		return new Command() {

			public void execute() {
				Services.getServices().mainPanel.setScreen("Event Log",
						new EventLogPanel(), null, null, null);
			}
		};
	}

	protected Command getAdminGroupCmd() {
		return new Command() {

			public void execute() {
				BusyPopup.waitFor("Loading Group Admin Screen");
				new UserControlPanel();
				Services.getServices().userInfoService.getGroups(true,
						new AsyncCallback() {

							public void onFailure(Throwable caught) {
								Services.getServices().mainPanel.setScreen(
										"Failed Request", new Label(caught
												.toString()), null, null, null);
								BusyPopup.done("Loading Group Admin Screen");
							}

							public void onSuccess(Object result) {
								AjaxGroup[] groups = (AjaxGroup[]) result;
								Tree userGroupTree = new Tree();
								for (int i = 0; i < groups.length; i++) {
									TreeItem item = new TreeItem(groups[i]
											.getName());
									item.setUserObject(groups[i]);
									userGroupTree.addItem(item);
								}
								userGroupTree
										.addTreeListener(new GroupAdminTreeActions(
												userGroupTree));
								Services.getServices().mainPanel.setScreen(
										"Manage Groups", new FlowPanel(),
										userGroupTree, null, null);
								BusyPopup.done("Loading Group Admin Screen");
							}
						});
			}

		};
	}

	protected Command getAddGroupCmd() {
		return new Command() {

			public void execute() {
				BusyPopup.waitFor("Loading Group Add Screen");
				Services.getServices().userInfoService.getGroups(true,
						new AsyncCallback() {

							public void onFailure(Throwable caught) {
								Services.getServices().mainPanel.setScreen(
										"Failed Request", new Label(caught
												.toString()), null, null, null);
								BusyPopup.done("Loading Group Add Screen");
							}

							public void onSuccess(Object result) {
								Services.getServices().mainPanel
										.setScreen("Create A New Group",
												new GroupAddPanel(
														(AjaxGroup[]) result),
												null, null, null);
								BusyPopup.done("Loading Group Add Screen");
							}
						});
			}

		};
	}

	protected native void returnUser(String url) /*-{
	 window.open(url,"_top");
	 }-*/;

	protected native void logoffUser() /*-{
	 window.open("admin.jsp?action=action_logout","_top");
	 }-*/;

	protected Command getReturnCmd() {
		return new Command() {
			public void execute() {
				Services.getServices().userInfoService
						.getCallingUrl(new AsyncCallback() {

							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}

							public void onSuccess(final Object result) {
								returnUser((String) result);
							}

						});
			}
		};

	}

	protected Command getLogoffCmd() {
		return new Command() {
			public void execute() {
				logoffUser();
			}
		};

	}

	protected Command getImageUploaderCmd() {
		return new Command() {

			public void execute() {
				BusyPopup.waitFor("Loading Image Uploader");
				TreeControls treeControl = new TreeControls();
				Uploader up = new Uploader(treeControl);
				Services.getServices().mainPanel.setScreen(
						"Upload Images To An On-Line Library", up, treeControl
								.getLibraryTree(), null, null);
				BusyPopup.done("Loading Image Uploader");
			}

		};
	}

	protected Command getUserAddCmd() {
		return new Command() {

			public void execute() {
				BusyPopup.waitFor("Loading User Add Screen");
				Services.getServices().userInfoService.getGroups(true,
						new AsyncCallback() {

							public void onFailure(Throwable caught) {
								Services.getServices().mainPanel.setScreen(
										"Failed Request", new Label(caught
												.toString()), null, null, null);
								BusyPopup.done("Loading User Add Screen");
							}

							public void onSuccess(Object result) {
								Services.getServices().mainPanel.setScreen(
										"Add A User", new UserControlPanel(
												(AjaxGroup[]) result), null,
										null, null);
								BusyPopup.done("Loading User Add Screen");
							}
						});

			}

		};
	}

	protected Command getViewOrdersScreen() {
		return new Command() {
			public void execute() {
				Services.getServices().mainPanel.setScreen("View Orders",
						new HTML(""), new OrderFilter("LAST_MONTH",
								"awaiting_approval", true), null, null);
			}
		};
	}

	protected Command getMyOrdersScreen() {

		return new Command() {
			public void execute() {
				Services.getServices().mainPanel.setScreen("My Orders",
						new HTML(""), new OrderFilter("LAST_WEEK", Services
								.getServices().user.getUsername()), null, null);
			}
		};
	}

	protected Command getPendingOrdersScreen() {

		return new Command() {
			public void execute() {
				Services.getServices().mainPanel.setScreen("My Orders",
						new HTML(""), new OrderFilter("LAST_YEAR",
								"awaiting_fulfillment", true), null, null);
			}
		};
	}

	protected Command getSupportScreen() {
		return new Command() {
			public void execute() {
				new PopupSupportRequest().init();
			}
		};
	}

	protected Command getUserTipDisplay() {
		return new Command() {
			public void execute() {
				new UserTipDisplay().init();
			}
		};
	}
	
	protected Command getLibraryAdminCmd() {
		return new Command() {

			public void execute() {
				BusyPopup.waitFor("Loading Library Admin Screen");
				Services.getServices().libraryInfoService.getLibraries("admin",
						new AsyncCallback() {

							public void onFailure(Throwable caught) {
								Services.getServices().mainPanel.setScreen(
										"Failed Request", new Label(caught
												.toString()), null, null, null);
								BusyPopup.done("Loading Library Admin Screen");
							}

							public void onSuccess(Object result) {
								Tree libraryTree = new Tree();
								AjaxProductFamily[] libs = (AjaxProductFamily[]) result;
								for (int i = 0; i < libs.length; i++) {
									TreeItem item = new TreeItem(libs[i]
											.getDescriptiveName());
									item.setUserObject(libs[i]);
									libraryTree.addItem(item);
									item.addItem("View Download Stats");
								}
								Services.getServices().mainPanel.setScreen(
										"Manage Libraries", null, libraryTree,
										null, null);
								libraryTree
										.addTreeListener(new LibraryTreeActions());
								BusyPopup.done("Loading Library Admin Screen");
							}
						});
			}

		};
	}

	public Command getUserEditScreen() {
		return new Command() {
	
			public void execute() {
				BusyPopup.waitFor("Loading User Admin Screen");
				new UserControlPanel();
				Services.getServices().userInfoService.getGroups(true,
						new AsyncCallback() {
	
							public void onFailure(Throwable caught) {
								Services.getServices().mainPanel.setScreen(
										"Failed Request", new Label(caught
												.toString()), null, null, null);
								BusyPopup.done("Loading User Admin Screen");
							}
	
							public void onSuccess(Object result) {
								UserAdminPanel uap = new UserAdminPanel(
										(AjaxGroup[]) result);
								uap.selectUser(Services.getServices().user);
								Services.getServices().mainPanel.setScreen(
										"User Admin Screen", uap, null, null,
										null);
								BusyPopup.done("Loading User Admin Screen");
							}
						});
			}
	
		};
	}

}
