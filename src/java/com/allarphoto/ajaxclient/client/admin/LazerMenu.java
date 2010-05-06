package com.lazerinc.ajaxclient.client.admin;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;
import com.lazerinc.ajaxclient.client.AjaxSystem;
import com.lazerinc.ajaxclient.client.CommandFactory;
import com.lazerinc.ajaxclient.client.Services;
import com.lazerinc.ajaxclient.client.beans.AjaxGroup;
import com.lazerinc.ajaxclient.client.beans.AjaxProductFamily;
import com.lazerinc.ajaxclient.client.components.BusyPopup;
import com.lazerinc.ajaxclient.client.components.Legend;
import com.lazerinc.ajaxclient.client.components.PopupWarning;
import com.lazerinc.ajaxclient.client.components.SearchCenter;
import com.lazerinc.ajaxclient.client.components.ShoppingCart;

public class LazerMenu extends HorizontalPanel {
	protected final static int SUPER_ADMIN = 0;

	protected final static int GROUP_ADMIN = 1;

	protected final static int ADMIN = 2;

	protected final static int UPLOADER = 3;

	protected final static int LIBRARIES = 5;

	protected MenuBar libraryMenu, systemMenu, libraries;

	protected int menuCount;

	protected boolean isSuperAdmin, canUpload, isAdmin, isGroupAdmin,
			isUploadAdmin;

	protected MenuBar mainBar;

	protected boolean[] asyncCalls;

	protected boolean isLoggedIn;

	public LazerMenu(CommandFactory c) {
		super();
		mainBar = new MenuBar(false);
		menuCount = 0;
		asyncCalls = new boolean[] { false, false, false, false };
		isUploadAdmin = false;
		init(c);
	}

	public LazerMenu(boolean vertical, CommandFactory c) {
		super();
		mainBar = new MenuBar(vertical);
		menuCount = 0;
		asyncCalls = new boolean[] { false, false, false, false };
		init(c);
	}

	protected void addMenuItems(CommandFactory cmd) {
		addLeftHeader(cmd);
		addStyleName("top-menu");
		add(mainBar);
		addReturn(cmd);
		addBrowseMenu(cmd);
		addMyStuffMenu(cmd);
		addGroupMenu(cmd);
		addLibraryMenu(cmd);
		addSystemMenu(cmd);
		addRightHeader(cmd);
		addHelpMenu(cmd);
		addSupportFormLink(cmd);
		addLogoff(cmd);
		Services.getServices().mainPanel.redraw();
	}

	protected void initSubMenus() {
		createLibraryMenu();
		libraries = new MenuBar(true);
		libraries.addStyleName("sub-menu");
	}

	protected void init(final CommandFactory cmd) {
		initSubMenus();
		BusyPopup.waitFor("Loading...");
		initAsyncCalls(cmd);
		Timer t = new Timer() {

			public void run() {
				if (isAsyncCallsDone(cmd)) {
					BusyPopup.done("Loading...");
					isLoggedIn = Services.getServices().user != null;
					addMenuItems(cmd);
				} else
					schedule(500);

			}

		};
		t.schedule(500);
	}

	protected boolean isAsyncCallsDone(CommandFactory cmd) {
		for (int i = 0; i < asyncCalls.length; i++) {
			if (!asyncCalls[i])
				return false;
		}
		return true;
	}

	protected void initAsyncCalls(CommandFactory cmd) {
		Services.getServices().userInfoService.isAdmin(new AsyncCallback() {

			public void onFailure(Throwable caught) {
				isSuperAdmin = false;
				asyncCalls[SUPER_ADMIN] = true;
			}

			public void onSuccess(Object result) {
				isSuperAdmin = ((Boolean) result).booleanValue();
				asyncCalls[SUPER_ADMIN] = true;
			}
		});

		Services.getServices().libraryInfoService.getLibraries("upload",
				new AsyncCallback() {

					public void onFailure(Throwable caught) {
						canUpload = false;
						asyncCalls[UPLOADER] = true;

					}

					public void onSuccess(Object result) {
						if (((AjaxProductFamily[]) result).length > 0) {
							canUpload = true;
						} else
							canUpload = false;
						asyncCalls[UPLOADER] = true;

					}

				});

		Services.getServices().libraryInfoService.getLibraries("admin",
				new AsyncCallback() {

					public void onFailure(Throwable caught) {
						isAdmin = false;
						asyncCalls[ADMIN] = true;
					}

					public void onSuccess(Object result) {
						AjaxProductFamily[] families = (AjaxProductFamily[]) result;
						if (families.length > 0) {
							for (int i = 0; i < families.length; i++)
								if (families[i].isRemoteManaged())
									isUploadAdmin = true;
							isAdmin = true;
						} else
							isAdmin = false;
						asyncCalls[ADMIN] = true;
					}

				});

		Services.getServices().userInfoService.getGroups(true,
				new AsyncCallback() {

					public void onFailure(Throwable caught) {
						isGroupAdmin = false;
						asyncCalls[GROUP_ADMIN] = true;
					}

					public void onSuccess(Object result) {
						if (((AjaxGroup[]) result).length > 0) {
							isGroupAdmin = true;
						} else
							isGroupAdmin = false;
						asyncCalls[GROUP_ADMIN] = true;
					}

				});

		loadLibraryMenu(cmd);
	}

	protected void addHelpMenu(CommandFactory cmd) {
	}

	protected void addMyStuffMenu(final CommandFactory cmd) {
		if (isLoggedIn) {
			MenuItem item;
			item = new MenuItem("My Stuff", getMyStuff(cmd));
			item
					.setTitle("View details and information specific to your account");
			mainBar.addItem(item);
		}
	}

	protected void addBrowseMenu(final CommandFactory cmd) {
		if (isLoggedIn) {
			MenuItem item = new MenuItem("Browse", libraries);
			item.setTitle("Choose a library of images to search and browse");
			mainBar.addItem(item);
		}
	}

	protected void addLeftHeader(CommandFactory cmd) {
	}

	protected void addRightHeader(CommandFactory cmd) {
		HTML legend = new HTML("Icon<br/>Reference");
		legend.addStyleName("legend");
		add(legend);
		setCellHorizontalAlignment(legend, HasHorizontalAlignment.ALIGN_RIGHT);
		legend.addMouseListener(getLegendListener());
	}

	protected MouseListener getLegendListener() {
		return new MouseListener() {
			Legend l = new Legend();

			public void onMouseDown(Widget sender, int x, int y) {
				// TODO Auto-generated method stub

			}

			public void onMouseEnter(Widget sender) {
				l.setVisible(true);
				l.show();

			}

			public void onMouseLeave(Widget sender) {
				l.setVisible(false);
				l.hide();

			}

			public void onMouseMove(Widget sender, int x, int y) {
				// TODO Auto-generated method stub

			}

			public void onMouseUp(Widget sender, int x, int y) {
				// TODO Auto-generated method stub

			}

		};
	}

	protected void addReturn(final CommandFactory cmd) {
		MenuItem item = new MenuItem("Return", cmd.get("Return"));
		item.setTitle("Return to previous browser location (like back button)");
		mainBar.addItem(item);
	}

	protected void addLogoff(CommandFactory cmd) {
		if (isLoggedIn) {
			MenuItem item = new MenuItem("Log Off", cmd.get("logoff"));
			item.setTitle("End your session");
			mainBar.addItem(item);
		}
	}

	protected void addSupportFormLink(CommandFactory cmd) {
		MenuItem item = new MenuItem("Ask Webmaster", cmd.get("support"));
		item.setTitle("Ask a question or send feedback about the site");
		mainBar.addItem(item);
	}

	protected void addGroupMenu(CommandFactory cmd) {
		if (isLoggedIn) {
			if (isGroupAdmin) {
				MenuItem item = new MenuItem("Groups", getGroupMenu(cmd));
				item.setTitle("Admin groups and group permissions");
				mainBar.addItem(item);
				item = new MenuItem("Users", getUserMenu(cmd));
				item.setTitle("Modify, add, and delete users");
				mainBar.addItem(item);
			}
		}
	}

	protected MenuBar getMyStuff(final CommandFactory cmd) {
		final MenuBar myStuff = new MenuBar(true);
		myStuff.addStyleName("sub-menu");
		MenuItem item = new MenuItem("My Orders", cmd.get("MyOrders"));
		item.setTitle("View your orders");
		myStuff.addItem(item);
		item = new MenuItem("User Info", cmd.get("User Info"));
		item.setTitle("Edit your user information");
		myStuff.addItem(item);
		return myStuff;
	}

	protected void loadLibraryMenu(final CommandFactory cmd) {
		Services.getServices().libraryInfoService.getLibraries("read",
				new AsyncCallback() {

					public void onFailure(Throwable caught) {
						asyncCalls[LIBRARIES] = true;
					}

					public void onSuccess(Object result) {
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
														new ShoppingCart(
																Services
																		.getServices().cart,
																sc), null);
									}

								});
							}
						}
						asyncCalls[LIBRARIES] = true;
					}

				});
	}

	protected void addSystemMenu(CommandFactory cmd) {
		if (isSuperAdmin) {
			MenuBar systemMenu = new MenuBar(true);
			systemMenu.addStyleName("sub-menu");
			MenuItem item = new MenuItem("Event Log", cmd.get("EventLog"));
			item
					.setTitle("View events such as logins, orders, downloads, file uploads, file moves");
			systemMenu.addItem(item);
			item = new MenuItem("Pending Orders", cmd.get("PendingOrders"));
			item.setTitle("View orders awaiting fulfillment");
			systemMenu.addItem(item);
			item = new MenuItem("System", systemMenu);
			item.setTitle("System Administration functions");
			mainBar.addItem(item);
		}
	}

	protected void createLibraryMenu() {
		libraryMenu = new MenuBar(true);
		libraryMenu.addStyleName("sub-menu");
	}

	protected void addLibraryMenu(CommandFactory cmd) {
		if (isLoggedIn) {
			MenuItem item = new MenuItem("Libraries", libraryMenu);
			item
					.setTitle("Admin-level functions pertaining to library management");
			if(isSuperAdmin || canUpload || isAdmin) mainBar.addItem(item);
			if (canUpload) {
				item = new MenuItem("Upload Images", cmd.get("UploadImages"));
				item.setTitle("Upload files to an On-Line Library folder");
				libraryMenu.addItem(item);
			}
			if (isAdmin) {
				item = new MenuItem("Admin Libraries", cmd
						.get("AdminLibraries"));
				item
						.setTitle("Manage library categories and other library properties");
				libraryMenu.addItem(item);
				item = new MenuItem("View Orders", cmd.get("ViewOrders"));
				item.setTitle("View Orders currently in progress");
				libraryMenu.addItem(item);
				if (isUploadAdmin || isSuperAdmin) {
					item = new MenuItem("Manage Uploads", cmd
							.get("ManageUploads"));
					item.setTitle("Approve file uploads into new folders");
					libraryMenu.addItem(item);
				}
			}
		}
	}

	protected MenuBar getGroupMenu(CommandFactory cmd) {
		MenuBar groupMenu = new MenuBar(true);
		groupMenu.addStyleName("sub-menu");
		MenuItem item = new MenuItem("Admin Groups", cmd.get("AdminGroups"));
		item.setTitle("Admin group permissions and membership");
		groupMenu.addItem(item);
		item = new MenuItem("Add a Group", cmd.get("AddGroup"));
		item.setTitle("Add a new group to the system");
		groupMenu.addItem(item);
		return groupMenu;
	}

	protected MenuBar getUserMenu(CommandFactory cmd) {
		MenuBar userMenu = new MenuBar(true);
		userMenu.addStyleName("sub-menu");
		MenuItem item = new MenuItem("Admin Users", cmd.get("AdminUsers"));
		item.setTitle("Jump to screen to modify, create, or delete users");
		userMenu.addItem(item);
		item = new MenuItem("Add User", cmd.get("AddUser"));
		item.setTitle("Add a user to the system");
		userMenu.addItem(item);
		item = new MenuItem("Access Requests", cmd.get("AccessRequests"));
		item.setTitle("Approve or deny access requests");
		userMenu.addItem(item);
		return userMenu;
	}

}