package com.allarphoto.ajaxclient.client.admin;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.TreeListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.allarphoto.ajaxclient.client.AjaxSystem;
import com.allarphoto.ajaxclient.client.DeleteListener;
import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.beans.AjaxGroup;
import com.allarphoto.ajaxclient.client.beans.AjaxUser;
import com.allarphoto.ajaxclient.client.components.BusyPopup;
import com.allarphoto.ajaxclient.client.components.EnterKeyListener;
import com.allarphoto.ajaxclient.client.components.PopupWarning;

public class UserAdminPanel extends DockPanel implements DeleteListener {
	AjaxGroup[] groups;

	Tree userList;

	AjaxUser currentSelectedUser;

	GroupPanel groupBox;

	TabPanel controlTab;

	UserControlPanel ucp;

	public UserAdminPanel() {
	}

	public UserAdminPanel(AjaxGroup[] groups) {
		this.groups = groups;
		init();
	}

	private void init() {
		ListBox groupChoice = new ListBox();
		groupChoice.addItem("");
		groupChoice.setSelectedIndex(0);
		groupChoice.addChangeListener(getGroupChoiceAction());
		AjaxSystem.addToList(groupChoice, groups);
		VerticalPanel vp = new VerticalPanel();
		vp.add(AjaxSystem.getLabel("Choose Group"));
		vp.add(groupChoice);
		HorizontalPanel controls = new HorizontalPanel();
		controls.add(vp);
		vp = new VerticalPanel();
		vp.add(AjaxSystem.getLabel("Search For User"));
		TextBox userSearch = new TextBox();
		userSearch.addKeyboardListener(getUserSearchAction());
		vp.add(userSearch);
		controls.add(vp);
		vp = new VerticalPanel();
		userList = new Tree();
		userList.addTreeListener(getUserSelectAction());
		ScrollPanel scroller = new ScrollPanel(userList);
		vp.add(controls);
		vp.add(scroller);
		scroller.setHeight(((int) (Window.getClientHeight() * .85)) + "px");
		scroller.setWidth("100%");
		add(vp, DockPanel.WEST);
		groupBox = new GroupPanel(groups, "Add or Remove User From Groups",
				getGroupSelectionListener());
		controlTab = new TabPanel();
		controlTab.add(groupBox, "Modify&nbsp;Groups", true);
		ucp = new UserControlPanel();
		ucp.addDeleteListener(this);
		controlTab.add(ucp, "Edit&nbsp;User", true);
		controlTab.selectTab(0);
		controlTab.setWidth("100%");
		controlTab.getTabBar().setWidth("100%");
		controlTab.addTabListener(getTabListener());
		vp.setWidth("100%");
		add(controlTab, DockPanel.CENTER);
		setSpacing(20);
		setWidth("100%");
		setHeight("100%");
	}

	public void deleted(String item) {
		for (int i = 0; i < userList.getItemCount(); i++) {
			TreeItem node = userList.getItem(i);
			if (((AjaxUser) node.getUserObject()).getUsername().equals(item)) {
				userList.removeItem(node);
				break;
			}
		}

	}

	private ClickListener getGroupSelectionListener() {
		return new ClickListener() {

			public void onClick(Widget sender) {
				final CheckBox selGroup = (CheckBox) sender;
				if (currentSelectedUser != null
						&& !stateMatches(selGroup, currentSelectedUser)) {
					Services.getServices().userInfoService.toggleUserToGroup(
							currentSelectedUser.getUsername(), selGroup
									.getText(), new AsyncCallback() {

								public void onFailure(Throwable caught) {
									new PopupWarning(
											"Failed to change group membership");
								}

								public void onSuccess(Object result) {
									if (!((Boolean) result).booleanValue())
										new PopupWarning(
												"Failed to change group membership");
									else
										currentSelectedUser
												.toggleGroupMembership(selGroup
														.getText());
								}

							});
				}

			}

		};
	}

	private boolean stateMatches(CheckBox selGroup, AjaxUser user) {
		if (selGroup.isChecked()) {
			return user.belongsToGroup(selGroup.getText());
		} else
			return !user.belongsToGroup(selGroup.getText());
	}

	private ChangeListener getGroupChoiceAction() {
		return new ChangeListener() {

			public void onChange(Widget sender) {
				currentSelectedUser = null;
				userList.removeItems();
				BusyPopup.waitFor("Loading Users");
				Services.getServices().userInfoService.getUsers(
						((ListBox) sender).getItemText(((ListBox) sender)
								.getSelectedIndex()), new AsyncCallback() {

							public void onFailure(Throwable caught) {
								BusyPopup.done("Loading Users");
								new PopupWarning("Failed to load Users "
										+ caught.toString());
							}

							public void onSuccess(Object result) {
								if (((AjaxUser[]) result).length == 0)
									new PopupWarning("No users found");
								else
									showUserList((AjaxUser[]) result);
								BusyPopup.done("Loading Users");
							}

						});

			}

		};
	}

	private TreeListener getUserSelectAction() {
		return new TreeListener() {

			public void onTreeItemSelected(TreeItem item) {
				selectUser((AjaxUser) item.getUserObject());
			}

			public void onTreeItemStateChanged(TreeItem item) {
				// TODO Auto-generated method stub

			}

		};
	}

	private TabListener getTabListener() {
		return new TabListener() {

			public boolean onBeforeTabSelected(SourcesTabEvents sender,
					int tabIndex) {
				return true;
			}

			public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
				if (tabIndex == 1 && currentSelectedUser != null) {
					if (ucp.getUser() == null
							|| !ucp.getUser().equals(currentSelectedUser))
						ucp.setGroupsAndUser(groups, currentSelectedUser);
				}
			}
		};
	}

	private KeyboardListener getUserSearchAction() {
		return new EnterKeyListener() {

			protected void onEnterPress(Widget source) {
				TextBox searchText = (TextBox) source;
				userList.removeItems();
				currentSelectedUser = null;
				BusyPopup.waitFor("Searching Users");
				Services.getServices().userInfoService.searchUsers(searchText
						.getText(), true, new AsyncCallback() {

					public void onFailure(Throwable caught) {
						BusyPopup.done("Searching Users");
						new PopupWarning("Failed to load Users "
								+ caught.toString());
					}

					public void onSuccess(Object result) {
						if (((AjaxUser[]) result).length == 0)
							new PopupWarning("No users found");
						else
							showUserList((AjaxUser[]) result);
						BusyPopup.done("Searching Users");
					}
				});
			}

		};
	}

	private void showUserList(AjaxUser[] users) {
		for (int i = 0; i < users.length; i++) {
			TreeItem la = new TreeItem(users[i].getFullName());
			la.setUserObject(users[i]);
			userList.addItem(la);
			la.addStyleName("text");
		}
	}

	public void selectUser(AjaxUser u) {
		currentSelectedUser = u;
		groupBox.setSelectedGroups(currentSelectedUser.getGroups());
		if (ucp.getUser() == null)
			ucp.setGroupsAndUser(groups, currentSelectedUser);
		else
			ucp.setUser(currentSelectedUser);
		if (!isAttached())
			controlTab.selectTab(1);
	}

}
