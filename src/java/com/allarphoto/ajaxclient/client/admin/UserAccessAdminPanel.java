package com.lazerinc.ajaxclient.client.admin;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.TreeListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lazerinc.ajaxclient.client.AjaxSystem;
import com.lazerinc.ajaxclient.client.Services;
import com.lazerinc.ajaxclient.client.beans.AjaxAccessRequest;
import com.lazerinc.ajaxclient.client.components.BusyPopup;
import com.lazerinc.ajaxclient.client.components.PopupWarning;

public class UserAccessAdminPanel extends DockPanel {

	VerticalPanel control = new VerticalPanel();

	public UserAccessAdminPanel() {
	}

	public UserAccessAdminPanel(AjaxAccessRequest[] requests) {
		init(requests);

	}

	private void init(AjaxAccessRequest[] requests) {
		if (requests.length == 0) {
			add(
					AjaxSystem
							.getTitle("There are no pending access requests currently"),
					DockPanel.NORTH);
		} else {
			Tree requestTree = new Tree();
			for (int i = 0; i < requests.length; i++) {
				TreeItem item = new TreeItem(requests[i].getLastName() + ", "
						+ requests[i].getFirstName());
				item.setUserObject(requests[i]);
				requestTree.addItem(item);
			}
			requestTree.addTreeListener(getTreeListener());
			ScrollPanel scroller = new ScrollPanel(requestTree);
			scroller.setHeight(((int) (Window.getClientHeight() * .85)) + "px");
			add(scroller, DockPanel.WEST);
			control.add(AjaxSystem.getTitle(""));
			control.setWidth("100%");
			control.setSpacing(10);
			add(control, DockPanel.CENTER);
			setSpacing(20);
			setWidth("100%");
			setHeight("100%");
		}

	}

	private TreeListener getTreeListener() {
		return new TreeListener() {

			public void onTreeItemSelected(TreeItem item) {
				AjaxAccessRequest request = (AjaxAccessRequest) item
						.getUserObject();
				control.clear();
				control.add(AjaxSystem.getLabel(request.getLastName() + ", "
						+ request.getFirstName() + "(" + request.getEmail()
						+ ") Requests Access to "
						+ request.getResourceDescription()));
				if (request.getCompany() != null
						&& request.getCompany().length() > 0)
					addLabelAndValue(control,"Company:",request.getCompany());
				if (request.getPhone() != null
						&& request.getPhone().length() > 0)
					addLabelAndValue(control,"Phone:",request.getPhone());
				if (request.getAddress1() != null)
					addLabelAndValue(control,"Address 1:",request.getAddress1());
				if (request.getAddress2() != null)
					addLabelAndValue(control,"Address 2:",request.getAddress2());
				if (request.getCity() != null)
					addLabelAndValue(control,"City:",request.getCity());
				if (request.getState() != null)
					addLabelAndValue(control,"State/Province:",request.getState());
				if (request.getCountry() != null)
					addLabelAndValue(control,"Country:",request.getCountry());
				if (request.getZip() != null)
					addLabelAndValue(control,"Postal Code (Zip):",request.getZip());
				if (request.getReason() != null
						&& request.getReason().length() > 0)
					addLabelAndValue(control,"Reason:",request.getReason());
				control
						.add(AjaxSystem
								.getLabel("Choose a group to add this user to in order to grant this request"));
				if (request.getUsername() == null
						|| request.getUsername().length() == 0) {
					control
							.add(AjaxSystem
									.getText("The requester does not currently have an account.  Granting this request will create a new account"));
				}
				ListBox groupBox = new ListBox();
				for (int i = 0; i < request.getGroups().length; i++) {
					if(!request.getGroups()[i].equals("admin"))
					groupBox.addItem(request.getGroups()[i]);
				}
				TextArea feedback = new TextArea();
				HorizontalPanel hp = new HorizontalPanel();
				hp.add(AjaxSystem.getLabel("Choose Group:"));
				hp.add(groupBox);
				control.add(hp);
				hp = new HorizontalPanel();
				hp.add(new Button("Grant Request", getGrantRequester(groupBox,
						item, feedback)));
				hp.add(new Button("Reject Request", getRejectRequester(item,
						feedback)));
				hp.setSpacing(10);
				control.add(hp);
				hp = new HorizontalPanel();
				hp.setSpacing(10);
				hp
						.add(AjaxSystem
								.getLabel("If rejecting, please provide feedback for the user"));
				feedback.setCharacterWidth(50);
				feedback.setHeight("8em");
				hp.add(feedback);
				control.add(hp);
			}

			public void onTreeItemStateChanged(TreeItem item) {
				// TODO Auto-generated method stub

			}

		};
	}
	
	protected void addLabelAndValue(Panel p,String label,String value)
	{
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(AjaxSystem.getLabel(label));
		hp.add(AjaxSystem.getText(value));
		p.add(hp);
	}

	private ClickListener getRejectRequester(final TreeItem item,
			final TextArea feedback) {
		return new ClickListener() {

			public void onClick(Widget sender) {
				if (feedback.getText().length() == 0) {
					new PopupWarning(
							"You must provide an explanation for rejecting the request.");
					return;
				}
				BusyPopup.waitFor("Reject Request");
				Services.getServices().userInfoService.rejectAccessRequest(
						((AjaxAccessRequest) item.getUserObject()).getId(),
						feedback.getText(), new AsyncCallback() {

							public void onFailure(Throwable caught) {
								new PopupWarning(caught.toString());
								BusyPopup.done("Reject Request");

							}

							public void onSuccess(Object result) {
								if (((Boolean) result).booleanValue()) {
									item.getTree().removeItem(item);
									control.clear();
								} else {
									new PopupWarning("Failed to Reject Request");
								}
								BusyPopup.done("Reject Request");
							}

						});
			}

		};
	}

	private ClickListener getGrantRequester(final ListBox groupBox,
			final TreeItem item, final TextArea feedback) {
		return new ClickListener() {

			public void onClick(Widget sender) {
				BusyPopup.waitFor("Granting Request");
				Services.getServices().userInfoService.grantAccessRequest(
						((AjaxAccessRequest) item.getUserObject()).getId(),
						groupBox.getItemText(groupBox.getSelectedIndex()),
						feedback.getText(), new AsyncCallback() {

							public void onFailure(Throwable caught) {
								new PopupWarning(caught.toString());
								BusyPopup.done("Granting Request");

							}

							public void onSuccess(Object result) {
								if (((Boolean) result).booleanValue()) {
									item.getTree().removeItem(item);
									control.clear();
								} else {
									new PopupWarning("Failed to Grant Request");
								}
								BusyPopup.done("Granting Request");
							}

						});
			}

		};
	}

}
