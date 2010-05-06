package com.lazerinc.ajaxclient.client.components;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lazerinc.ajaxclient.client.AjaxSystem;
import com.lazerinc.ajaxclient.client.Services;
import com.lazerinc.ajaxclient.client.beans.AjaxProductFamily;

public class AccessRequestPopup extends PopupPanel {

	public AccessRequestPopup() {
		super(false);
		init();
		addStyleName("popup-info");
		setPopupPosition(10, 10);
		setVisible(true);
		show();
	}

	protected void init() {
		VerticalPanel main = new VerticalPanel();
		final TextBox username = new TextBox();
		main
				.add(AjaxSystem
						.getLabel("Your Username (if you have an account):"));
		main.add(username);
		final ListBox libraries = new ListBox();
		main.add(AjaxSystem.getLabel("Library You Need Access To (required):"));
		libraries.addItem("");
		libraries.setMultipleSelect(true);
		libraries.setVisibleItemCount(5);
		Services.getServices().customService
				.getLibraryList(new AsyncCallback() {

					public void onFailure(Throwable arg0) {
						// TODO Auto-generated method stub

					}

					public void onSuccess(Object arg0) {
						AjaxProductFamily[] families = (AjaxProductFamily[]) arg0;
						for (int i = 0; i < families.length; i++)
							libraries.addItem(families[i].getDescriptiveName());
					}

				});
		main.add(libraries);
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(AjaxSystem.getLabel("If you have no account:"));
		VerticalPanel newAccountPanel = new VerticalPanel();
		newAccountPanel.setSpacing(5);
		final TextBox firstName = new TextBox();
		final TextBox lastName = new TextBox();
		final TextBox email = new TextBox();
		final TextBox company = new TextBox();
		final TextBox phone = new TextBox();
		final TextArea reason = new TextArea();
		newAccountPanel.add(AjaxSystem
				.getText("New Account Request (all fields required):"));
		newAccountPanel.add(AjaxSystem.getLabel("First Name:", "required"));
		newAccountPanel.add(firstName);
		newAccountPanel.add(AjaxSystem.getLabel("Last Name:", "required"));
		newAccountPanel.add(lastName);
		newAccountPanel.add(AjaxSystem.getLabel("Email:", "required"));
		newAccountPanel.add(email);
		newAccountPanel.add(AjaxSystem.getLabel("Company:", "required"));
		newAccountPanel.add(company);
		newAccountPanel.add(AjaxSystem.getLabel("Phone:", "required"));
		newAccountPanel.add(phone);
		newAccountPanel.add(AjaxSystem.getLabel("Reason:", "required"));
		newAccountPanel.add(reason);
		newAccountPanel.addStyleName("new-account-panel");
		hp.add(newAccountPanel);
		hp.setSpacing(20);
		main.add(hp);
		hp = new HorizontalPanel();
		hp.add(new Button("Submit Request", new ClickListener() {

			public void onClick(Widget arg0) {
				if (libraries.getSelectedIndex() > 0
						&& (username.getText().length() > 0 || (firstName
								.getText().length() > 0
								&& lastName.getText().length() > 0
								&& email.getText().length() > 0
								&& company.getText().length() > 0
								&& phone.getText().length() > 0 && reason
								.getText().length() > 0))) {
					BusyPopup.waitFor("Requesting Access");
					Services.getServices().userInfoService
							.requestAccess(username.getText(),
									getSelectedLibraries(libraries), firstName
											.getText(), lastName.getText(),
									email.getText(), company.getText(), phone
											.getText(), reason.getText(),
									getCallback());
				} else
					new PopupWarning(
							"You must select at least one library and provide either a username of"
									+ " an existing account or all the required information for a new account");
			}

		}));
		hp.add(new Button("Cancel", new ClickListener() {
			public void onClick(Widget a) {
				hide();
			}
		}));
		hp.setSpacing(10);
		main.add(hp);
		main.setSpacing(10);
		add(main);
	}

	private List getSelectedLibraries(ListBox libraries) {
		List libs = new ArrayList();
		for (int i = 1; i < libraries.getItemCount(); i++)
			if (libraries.isItemSelected(i))
				libs.add(libraries.getItemText(i));
		return libs;
	}

	private AsyncCallback getCallback() {
		return new AsyncCallback() {

			public void onFailure(Throwable arg0) {
				new PopupWarning(
						"Failed to find user with the information provided.");
				BusyPopup.done("Requesting Access");

			}

			public void onSuccess(Object arg0) {
				if (((Boolean) arg0).booleanValue()) {
					new PopupWarning("Access Request successfully sent!");
					hide();
				} else
					new PopupWarning(
							"Failed to submit access request correctly.");
				BusyPopup.done("Requesting Access");
			}

		};
	}
}
