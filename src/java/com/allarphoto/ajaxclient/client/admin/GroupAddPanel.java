package com.lazerinc.ajaxclient.client.admin;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.lazerinc.ajaxclient.client.AjaxSystem;
import com.lazerinc.ajaxclient.client.Services;
import com.lazerinc.ajaxclient.client.beans.AjaxGroup;
import com.lazerinc.ajaxclient.client.components.BusyPopup;
import com.lazerinc.ajaxclient.client.components.EnterKeyListener;
import com.lazerinc.ajaxclient.client.components.PopupWarning;

public class GroupAddPanel extends DockPanel {
	GroupPanel groupPanel;

	TextBox newGroup;

	public GroupAddPanel() {
		super();
	}

	public GroupAddPanel(AjaxGroup[] groups) {
		this();
		init(groups);
	}

	private void init(final AjaxGroup[] groups) {
		this.clear();
		this.setSpacing(20);
		newGroup = new TextBox();
		newGroup.addKeyboardListener(new EnterKeyListener() {

			protected void onEnterPress(Widget source) {
				createNewGroup(groups);
			}
		});
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(AjaxSystem.getLabel("Group Name:"));
		hp.add(newGroup);
		hp.add(new Button("Add", new ClickListener() {

			public void onClick(Widget sender) {
				createNewGroup(groups);
			}

		}));
		groupPanel = new GroupPanel(groups, "Select Admin Groups For New Group");
		add(hp, DockPanel.WEST);
		add(groupPanel, DockPanel.CENTER);
	}

	private boolean exists(String group, AjaxGroup[] groups) {
		for (int i = 0; i < groups.length; i++) {
			if (groups[i].getName().equals(group))
				return true;
		}
		return false;
	}

	private void createNewGroup(final AjaxGroup[] groups) {
		if (exists(newGroup.getText(), groups)) {
			new PopupWarning("A group with that name already exists");
		} else {
			final String[] selectedGroups = groupPanel.getSelectedGroups();
			if (selectedGroups.length == 0)
				new PopupWarning(
						"You must select at least one existing group to have admin rights over the new group");
			else {
				BusyPopup.waitFor("Adding Group");
				Services.getServices().userInfoService.addGroup(newGroup
						.getText(), selectedGroups, new AsyncCallback() {

					public void onFailure(Throwable caught) {
						BusyPopup.done("Adding Group");
						new PopupWarning(caught.toString());

					}

					public void onSuccess(Object result) {
						BusyPopup.done("Adding Group");
						if (((Boolean) result).booleanValue())
							new PopupWarning("New Group: " + newGroup.getText()
									+ " successfully created");
						else
							new PopupWarning("Group creation failed");
						Services.getServices().userInfoService.getGroups(false,
								new AsyncCallback() {

									public void onFailure(Throwable caught) {
										new PopupWarning(caught.toString());
									}

									public void onSuccess(Object result) {
										init((AjaxGroup[]) result);
									}
								});
					}

				});

			}
		}
	}

}
