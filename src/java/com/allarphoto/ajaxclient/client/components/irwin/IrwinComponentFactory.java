package com.allarphoto.ajaxclient.client.components.irwin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.allarphoto.ajaxclient.client.AjaxSystem;
import com.allarphoto.ajaxclient.client.CommandFactory;
import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.SwappablePanel;
import com.allarphoto.ajaxclient.client.UpdateListener;
import com.allarphoto.ajaxclient.client.admin.LazerMenu;
import com.allarphoto.ajaxclient.client.beans.AjaxCart;
import com.allarphoto.ajaxclient.client.beans.AjaxOrder;
import com.allarphoto.ajaxclient.client.beans.AjaxProduct;
import com.allarphoto.ajaxclient.client.beans.AjaxProductFamily;
import com.allarphoto.ajaxclient.client.beans.Request;
import com.allarphoto.ajaxclient.client.components.OrderDisplay;
import com.allarphoto.ajaxclient.client.components.PopupSupportRequest;
import com.allarphoto.ajaxclient.client.components.SearchCenter;
import com.allarphoto.ajaxclient.client.components.SearchTree;
import com.allarphoto.ajaxclient.client.components.SouthCartThumbnailPanel;
import com.allarphoto.ajaxclient.client.components.SouthShoppingCart;
import com.allarphoto.ajaxclient.client.components.wa.WANumberSelector;
import com.allarphoto.ajaxclient.client.factory.DefaultComponentFactory;

public class IrwinComponentFactory extends DefaultComponentFactory {

	transient Collection brandIcons;

	public IrwinComponentFactory() {
		brandIcons = new ArrayList();
	}

	public Widget[] getTopComponentStack() {
		return new Widget[] { getMainMenu(), getToolbar() };
	}

	public String getStartingSection() {
		return "Browse irwin";
	}

	public Widget getToolbar() {
		final FlowPanel toolbar = new FlowPanel();
		Services.getServices().libraryInfoService.getLibraries("read",
				new AsyncCallback() {
				
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					public void onSuccess(Object result) {
						if (((AjaxProductFamily[]) result).length > 0) {
							for (int i = 0; i < ((AjaxProductFamily[]) result).length; i++) {
								if (((AjaxProductFamily[]) result)[i]
										.getFamilyName().equals("irwin")) {
									
									/**** BRAND BUTTONS START HERE ****/
									// Allison Brand Button
									Image icon = new Image(
											"irwin/images/logo_allison.gif");
									icon.addClickListener(createFolderJump(
											((AjaxProductFamily[]) result)[i],
											new String[] { "Active",
													"Allison" }));
									toolbar.add(icon);
									icon.addStyleName("brand-icon");
									brandIcons.add(icon);
									icon
											.setTitle("Jump to Allison section of library");
									// BernzOmatic Brand Button
									icon = new Image(
										"irwin/images/logo_bernzomatic.gif");
									icon.addClickListener(createFolderJump(
											((AjaxProductFamily[]) result)[i],
											new String[] { "Active",
												"BernzOmatic" }));
									toolbar.add(icon);
									icon.addStyleName("brand-icon");
									brandIcons.add(icon);
									icon
										.setTitle("Jump to BernzOmatic section of library");
									// Bulldog Brand Button
									icon = new Image(
											"irwin/images/logo_bulldog.gif");
									icon
											.addClickListener(createFolderJump(
													((AjaxProductFamily[]) result)[i],
													new String[] { "Active",
															"Bulldog" }));
									toolbar.add(icon);
									icon.addStyleName("brand-icon");
									brandIcons.add(icon);
									icon
											.setTitle("Jump to Bulldog section of library");
									// IRWIN Brand Button
									icon = new Image(
											"irwin/images/logo_IRWIN.gif");
									icon
											.addClickListener(createFolderJump(
													((AjaxProductFamily[]) result)[i],
													new String[] { "Active",
															"IRWIN" }));
									toolbar.add(icon);
									icon.addStyleName("brand-icon");
									brandIcons.add(icon);
									icon
											.setTitle("Jump to IRWIN section of library");
									// Marples Brand Button
									icon = new Image(
											"irwin/images/logo_marples.gif");
									icon
											.addClickListener(createFolderJump(
													((AjaxProductFamily[]) result)[i],
													new String[] { "Active",
															"Marples" }));
									toolbar.add(icon);
									icon.addStyleName("brand-icon");
									brandIcons.add(icon);
									icon
											.setTitle("Jump to Marples section of library");
									// Record Brand Button
									icon = new Image(
											"irwin/images/logo_record.gif");
									icon
											.addClickListener(createFolderJump(
													((AjaxProductFamily[]) result)[i],
													new String[] { "Active",
															"Record" }));
									toolbar.add(icon);
									icon.addStyleName("brand-icon");
									brandIcons.add(icon);
									icon
											.setTitle("Jump to Record section of library");
									// Rubbermaid Brand Button
									icon = new Image(
											"irwin/images/logo_rubbermaid.gif");
									icon.addClickListener(createFolderJump(
											((AjaxProductFamily[]) result)[i],
											new String[] { "Active",
													"Rubbermaid" }));
									toolbar.add(icon);
									icon.addStyleName("brand-icon");
									brandIcons.add(icon);
									icon
											.setTitle("Jump to Rubbermaid section of library");
									// STRAIT-LINE Brand Button
									icon = new Image(
											"irwin/images/logo_straitline.gif");
									icon.addClickListener(createFolderJump(
											((AjaxProductFamily[]) result)[i],
											new String[] { "Active",
													"STRAIT-LINE" }));
									toolbar.add(icon);
									icon.addStyleName("brand-icon");
									brandIcons.add(icon);
									icon
											.setTitle("Jump to Strait-Line section of library");
									// SHUR-LINE Brand Button
									icon = new Image(
											"irwin/images/logo_shur-line.gif");
									icon.addClickListener(createFolderJump(
											((AjaxProductFamily[]) result)[i],
											new String[] { "Active",
													"SHUR-LINE" }));
									toolbar.add(icon);
									icon.addStyleName("brand-icon");
									brandIcons.add(icon);
									icon
											.setTitle("Jump to Shur-Line section of library");
									// Private-Label Brand Button
									icon = new Image(
											"irwin/images/logo_private-label.gif");
									icon.addClickListener(createFolderJump(
											((AjaxProductFamily[]) result)[i],
											new String[] { "Active",
													"Private Label" }));
									toolbar.add(icon);
									icon.addStyleName("brand-icon");
									brandIcons.add(icon);
									icon
											.setTitle("Jump to Private Label section of library");
									// Mechanicals Brand Button
									// NOTE: Need to make this special privileged section.
									if( true) {
										icon = new Image(
										"irwin/images/logo_mechs.gif");
										icon.addClickListener(createFolderJump(
												((AjaxProductFamily[]) result)[i],
												new String[] { "Active",
													"Mechanicals" }));
										toolbar.add(icon);
										icon.addStyleName("brand-icon");
										icon.addStyleName("brand-mechs");
										brandIcons.add(icon);
										icon
											.setTitle("Jump to Mechanicals section of library");
									} //
								}
							}
						}

					}

				});
		toolbar.addStyleName("centered");
		return toolbar;

	}

	private ClickListener createFolderJump(final AjaxProductFamily family,
			final String[] initialSelection) {
		return new ClickListener() {

			public void onClick(Widget sender) {
				unselectBrandIcons();
				sender.addStyleName("selected");
				SearchCenter sc = new SearchCenter(AjaxSystem.getLabel(""));
				SearchTree st = (SearchTree) createComponent("SearchTree",
						new Object[] { sc, family.getDescriptiveName(),
								initialSelection });
				Services.getServices().mainPanel.setScreen("Browse "
						+ family.getDescriptiveName(), sc, st, null,
						createComponent("ShoppingCart", new Object[] {
								Services.getServices().cart, sc }));
			}

		};
	}

	public void jumpTo(String folderName) {
		Iterator iter = brandIcons.iterator();
		while (iter.hasNext()) {
			Image icon = (Image) iter.next();
			if (icon.getTitle() != null
					&& icon.getTitle().indexOf(folderName) > -1) {
				unselectBrandIcons();
				icon.addStyleName("selected");
				break;
			}
		}
	}

	public void unselectBrandIcons() {
		Iterator iter = brandIcons.iterator();
		while (iter.hasNext()) {
			Image icon = (Image) iter.next();
			icon.removeStyleName("selected");
		}
	}

	public void setSectionTitle(String title) {
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
																new IrwinSearchResultsViewer(
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
		cmd.add("Help", getHelpCommand());
		cmd.add("User Info", getUserEditScreen());
		cmd.add("support", getSupportScreen());
		cmd.add("logoff",getLogoffCmd());
		return cmd;
	}

	protected native void redirectForLogin() /*-{
	 window.open("irwin","_top");
	 }-*/;

	protected Command getLoginScreen() {
		return new Command() {
			public void execute() {
				redirectForLogin();
			}
		};
	}

	public Command getHelpCommand() {
		return new Command() {

			public void execute() {
				sendUserToHelp();

			}

		};
	}

	protected native void sendUserToHelp() /*-{
	 window.open("irwin/help.pdf","_help");
	 }-*/;

	public LazerMenu getMainMenu() {
		return new IrwinMenu(getCommands());
	}

	public String getIconFolder() {
		return "icons_irwin";
	}

	public Widget createComponent(String classname, Object[] args) {
		if ("SearchTree".equals(classname)) {
			return new IrwinSearchTree((SwappablePanel) args[0],
					(String) args[1], (args.length > 2 ? (String[]) args[2]
							: new String[] { "" }));
		} else if ("ShoppingCart".equals(classname)) {
			return new SouthShoppingCart((AjaxCart) args[0],
					(SearchCenter) args[1]);
		} else if ("SearchResultsViewer".equals(classname)) {
			return new IrwinSearchResultsViewer((String[][]) args[0],
					(String) args[1]);
		} else if ("NumberSelector".equals(classname)) {
			if (args.length == 5)
				return new WANumberSelector(((Integer) args[0]).intValue(),
						((Integer) args[1]).intValue(), ((Integer) args[2])
								.intValue(), (ClickListener) args[3],
						((Integer) args[4]).intValue());
			else
				return new WANumberSelector((int[]) args[0],
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

		}  else if("OrderDisplay".equals(classname))
		{
			if(args.length == 1) return new NewellOrderDisplay((String)args[0]);
			else return new NewellOrderDisplay((AjaxOrder)args[0],(UpdateListener)args[1]);
		} else
			return super.createComponent(classname, args);
	}

	protected Command getSupportScreen() {
		return new Command() {
			public void execute() {
				new IrwinPopupSupportRequest().init();
			}
		};
	}

}
