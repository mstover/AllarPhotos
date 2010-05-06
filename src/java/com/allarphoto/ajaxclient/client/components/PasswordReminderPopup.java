package com.lazerinc.ajaxclient.client.components;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lazerinc.ajaxclient.client.AjaxSystem;
import com.lazerinc.ajaxclient.client.Services;

public class PasswordReminderPopup extends PopupPanel {

	public PasswordReminderPopup() {
		super(false);
		init();
		addStyleName("popup-info");
		setPopupPosition(Window.getClientWidth() / 3,
				Window.getClientHeight() / 3);
		setVisible(true);
		show();
	}

	protected void init() {
		VerticalPanel main = new VerticalPanel();
		final TextBox username = new TextBox();
		final TextBox email = new TextBox();
		main.add(AjaxSystem.getText("Enter Username:"));
		main.add(username);
		main.add(AjaxSystem.getLabel("Or"));
		main.add(AjaxSystem.getText("Enter Email:"));
		main.add(email);
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(new Button("Send Password", new ClickListener() {

			public void onClick(Widget arg0) {
				if (username.getText().length() > 0) {
					Services.getServices().userInfoService.sendPassword(
							username.getText(), getCallback());
				} else if (email.getText().length() > 0) {
					Services.getServices().userInfoService
							.sendPasswordFromEmail(email.getText(),
									getCallback());
				} else
					new PopupWarning(
							"You must provide either your username or the email associated with your account");
			}

		}));
		hp.add(new Button("Cancel", new ClickListener() {
			public void onClick(Widget a) {
				hide();
			}
		}));
		hp.setSpacing(10);
		main.add(hp);
		main.setSpacing(5);
		add(main);
	}

	private AsyncCallback getCallback() {
		return new AsyncCallback() {

			public void onFailure(Throwable arg0) {
				new PopupWarning(
						"Failed to find user with the information provided.");

			}

			public void onSuccess(Object arg0) {
				if (((Boolean) arg0).booleanValue()) {
					new PopupWarning("Password successfully sent!");
					hide();
				} else
					new PopupWarning(
							"Failed to find user with the information provided.");

			}

		};
	}

}
