package com.lazerinc.ajaxclient.client.components;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lazerinc.ajaxclient.client.AjaxSystem;
import com.lazerinc.ajaxclient.client.Services;
import com.lazerinc.ajaxclient.client.beans.AjaxUser;

public class LoginPanel extends VerticalPanel {

	public LoginPanel() {
		init();
	}

	private void init() {
		HorizontalPanel usernamePanel = new HorizontalPanel();
		final TextBox username = new TextBox();
		usernamePanel.add(AjaxSystem.getLabel("Username:"));
		usernamePanel.add(username);
		add(usernamePanel);
		HorizontalPanel passwordPanel = new HorizontalPanel();
		passwordPanel.add(AjaxSystem.getLabel("Password:"));
		final PasswordTextBox password = new PasswordTextBox();
		passwordPanel.add(password);
		add(passwordPanel);
		setSpacing(10);
		usernamePanel.setSpacing(10);
		passwordPanel.setSpacing(10);
		setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		HorizontalPanel buttonPanel = new HorizontalPanel();
		final CheckBox rememberMe = new CheckBox("Remember Me");
		buttonPanel.add(rememberMe);
		buttonPanel.add(new Button("Login", new ClickListener() {

			public void onClick(Widget arg0) {
				Services.getServices().userInfoService.login(
						username.getText(), password.getText(), rememberMe
								.isChecked(), new AsyncCallback() {

							public void onFailure(Throwable arg0) {
								new PopupWarning("Failed to Login");
							}

							public void onSuccess(Object result) {
								if (result != null) {
									Services.getServices().user = (AjaxUser) result;
									Services.getServices().getUserPerms();
									Services.getServices().getShoppingCart();
									Services.getServices().mainPanel
											.recreateMenus();
								} else
									new PopupWarning("Failed to Login");
							}

						});
			}

		}));
		buttonPanel.setSpacing(10);
		add(buttonPanel);

		Label forgotPassword = AjaxSystem.getLabel("Forgot Your Password?");
		forgotPassword.addStyleName("linked");
		add(forgotPassword);
		forgotPassword.addClickListener(getForgotPasswordClick());

		Label accessRequest = AjaxSystem.getLabel("Request Access");
		accessRequest.addStyleName("linked");
		add(accessRequest);
		accessRequest.addClickListener(getAccessClick());
	}

	private ClickListener getForgotPasswordClick() {
		return new ClickListener() {

			public void onClick(Widget arg0) {
				new PasswordReminderPopup();

			}

		};
	}

	private ClickListener getAccessClick() {
		return new ClickListener() {

			public void onClick(Widget arg0) {
				new AccessRequestPopup();

			}

		};
	}

}
