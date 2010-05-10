package com.allarphoto.ajaxclient.client.admin;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
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

public class GroupAdminTreeActions implements TreeListener, DeleteListener {

	Panel controlPanel;

	Tree groupTree;

	public GroupAdminTreeActions() {
		super();
	}

	public GroupAdminTreeActions(Tree groupTree) {
		this.groupTree = groupTree;
	}

	public void onTreeItemSelected(final TreeItem item) {
		if (item.getParentItem() == null) {
			GroupControlPanel gcp = new GroupControlPanel(item.getText());
			if (item.getChildCount() == 0) {
				BusyPopup.waitFor("Loading Users");
				Services.getServices().userInfoService.getUsers(item.getText(),
						new AsyncCallback() {

							public void onFailure(Throwable caught) {
								new PopupWarning(caught.toString());
								BusyPopup.done("Loading Users");
							}

							public void onSuccess(Object result) {
								AjaxUser[] users = (AjaxUser[]) result;
								for (int i = 0; i < users.length; i++) {
									TreeItem uit = new TreeItem(users[i]
											.getFullName());
									uit.setUserObject(users[i]);
									uit.addStyleName("user-name-tree-item");
									item.addItem(uit);
								}
								BusyPopup.done("Loading Users");
							}

						});
			}
			controlPanel = new VerticalPanel();
			controlPanel.add(AjaxSystem.getTitle("Group: " + item.getText()));
			HorizontalPanel hp = new HorizontalPanel();
			hp.add(new Button("Delete Group", new ClickListener() {

				public void onClick(Widget sender) {
					deleteGroup(item);
				}

			}));
			final TextBox nameEdit = new TextBox();
			nameEdit.setText(item.getText());
			hp.add(nameEdit);
			nameEdit.addKeyboardListener(new EnterKeyListener() {

				protected void onEnterPress(Widget source) {
					changeGroupName(item, nameEdit);
				}

			});
			hp.add(new Button("Change Group Name", new ClickListener() {

				public void onClick(Widget sender) {
					changeGroupName(item, nameEdit);
				}

			}));
			hp.add(new Button("Create User Spreadsheet", new ClickListener() {

				public void onClick(Widget sender) {
					Window.open(
							"/lazerweb/group_export.vtl?action=group_export&group_name="
									+ ((AjaxGroup) item.getUserObject())
											.getName(), "group_export", "");// createUserSpreadsheet((AjaxGroup)item.getUserObject());
				}

			}));
			hp.setSpacing(10);
			controlPanel.add(hp);
			VerticalPanel vp = new VerticalPanel();
			final TextArea description = new TextArea();
			hp = new HorizontalPanel();
			hp.add(AjaxSystem.getLabel("Description:"));
			description.setText(((AjaxGroup) item.getUserObject())
					.getDescription());
			description.setCharacterWidth(80);
			hp.add(new Button("Update Group Description", new ClickListener() {

				public void onClick(Widget arg0) {
					Services.getServices().userInfoService
							.updateGroupDescription(item.getText(), description
									.getText(), new AsyncCallback() {

								public void onFailure(Throwable arg0) {
									new PopupWarning(
											"Failed to update group description");

								}

								public void onSuccess(Object arg0) {
									if (!((Boolean) arg0).booleanValue()) {
										new PopupWarning(
												"Failed to update group description");
										description.setText(((AjaxGroup) item
												.getUserObject())
												.getDescription());
									} else {
										((AjaxGroup) item.getUserObject())
												.setDescription(description
														.getText());
									}

								}
							});
				}

			}));
			vp.add(hp);
			hp.setSpacing(20);
			vp.add(description);
			vp.setSpacing(1);
			controlPanel.add(vp);
			controlPanel.add(AjaxSystem.getTitle("Manage Permissions"));
			controlPanel.add(gcp);
			((VerticalPanel) controlPanel)
					.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
			Services.getServices().mainPanel.setCenter(controlPanel);
		} else {
			controlPanel = new UserControlPanel(getGroupList(), (AjaxUser) item
					.getUserObject());
			Services.getServices().mainPanel.setCenter(controlPanel);
			((UserControlPanel) controlPanel).addDeleteListener(this);
		}
	}

	private void createUserSpreadsheet(AjaxGroup ag) {
		final PopupPanel userSpreadsheet = new PopupPanel(false);
		BusyPopup.waitFor("Loading Users");
		Services.getServices().userInfoService.getUsers(ag.getName(),
				new AsyncCallback() {

					public void onFailure(Throwable caught) {
						new PopupWarning(caught.toString());
						BusyPopup.done("Loading Users");
					}

					public void onSuccess(Object result) {
						VerticalPanel vp = new VerticalPanel();
						AjaxUser[] users = (AjaxUser[]) result;
						Grid userInfo = new Grid(users.length + 1, 8);
						int row = 0;
						int col = 0;
						userInfo.setWidget(row, col++, AjaxSystem
								.getLabel("Username"));
						userInfo.setWidget(row, col++, AjaxSystem
								.getLabel("Last Name"));
						userInfo.setWidget(row, col++, AjaxSystem
								.getLabel("First Name"));
						userInfo.setWidget(row, col++, AjaxSystem
								.getLabel("Company"));
						userInfo.setWidget(row, col++, AjaxSystem
								.getLabel("Address"));
						userInfo.setWidget(row, col++, AjaxSystem
								.getLabel("City"));
						userInfo.setWidget(row, col++, AjaxSystem
								.getLabel("State"));
						userInfo.setWidget(row, col++, AjaxSystem
								.getLabel("Country"));
						userInfo.getRowFormatter().addStyleName(row, "header");
						userInfo.addStyleName("user-grid");
						userInfo.setCellSpacing(0);
						userInfo.setCellPadding(0);
						col = 0;
						row++;
						for (int i = 0; i < users.length; i++) {
							userInfo.setHTML(row, col++, "&nbsp;"
									+ users[i].getUsername());
							userInfo.setHTML(row, col++, "&nbsp;"
									+ users[i].getLastName());
							userInfo.setHTML(row, col++, "&nbsp;"
									+ users[i].getFirstName());
							userInfo.setHTML(row, col++, "&nbsp;"
									+ users[i].getCompany());
							userInfo.setHTML(row, col++, "&nbsp;"
									+ users[i].getBillAddress1());
							userInfo.setHTML(row, col++, "&nbsp;"
									+ users[i].getBillCity());
							userInfo.setHTML(row, col++, "&nbsp;"
									+ users[i].getBillState());
							userInfo.setHTML(row, col++, "&nbsp;"
									+ users[i].getBillCountry());
							row++;
							col = 0;
						}
						vp.add(new Button("Close", new ClickListener() {
							public void onClick(Widget sender) {
								userSpreadsheet.hide();
							}

						}));
						vp.add(userInfo);
						userSpreadsheet.add(vp);
						userSpreadsheet.setPopupPosition(100, 100);
						userSpreadsheet.show();
						userSpreadsheet.addStyleName("user-spreadsheet");
						BusyPopup.done("Loading Users");
					}

				});
	}

	public void deleted(String item) {
		groupTree.getSelectedItem().getParentItem().removeItem(
				groupTree.getSelectedItem());
	}

	private AjaxGroup[] getGroupList() {
		AjaxGroup[] groups = new AjaxGroup[groupTree.getItemCount()];
		for (int i = 0; i < groupTree.getItemCount(); i++) {
			groups[i] = (AjaxGroup) groupTree.getItem(i).getUserObject();
		}
		return groups;
	}

	private void deleteGroup(final TreeItem item) {
		if (item.getChildCount() != 0) {
			new PopupWarning("Cannot delete a group that has members");
			return;
		}
		BusyPopup.waitFor("Deleting Group...");
		Services.getServices().userInfoService.deleteGroup(item.getText(),
				new AsyncCallback() {

					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					public void onSuccess(Object result) {
						if (((Boolean) result).booleanValue()) {
							groupTree.removeItem(item);
							Services.getServices().mainPanel
									.setCenter(new FlowPanel());
							BusyPopup.done("Deleting Group...");
						} else {
							new PopupWarning("Failed to delete group");
							BusyPopup.done("Deleting Group...");
						}
					}
				});
	}

	private void changeGroupName(final TreeItem item, final TextBox nameEdit) {
		for (int i = 0; i < groupTree.getItemCount(); i++) {
			if (nameEdit.getText().equals(groupTree.getItem(i).getText())) {
				new PopupWarning("A group with that name already exists");
				nameEdit.setText(item.getText());
				return;
			}
		}

		BusyPopup.waitFor("Changing Group Name");
		Services.getServices().userInfoService.updateGroupName(item.getText(),
				nameEdit.getText(), new AsyncCallback() {

					public void onFailure(Throwable caught) {
						BusyPopup.done("Changing Group Name");
						new PopupWarning("Failed to change group name");
					}

					public void onSuccess(Object result) {
						if (((Boolean) result).booleanValue()) {
							item.setText(nameEdit.getText());
						} else {
							new PopupWarning("Failed to change group name");
						}
						BusyPopup.done("Changing Group Name");
					}

				});
	}

	public void onTreeItemStateChanged(TreeItem item) {
		// TODO Auto-generated method stub

	}

}
