package com.lazerinc.ajaxclient.client.components;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lazerinc.ajaxclient.client.AjaxSystem;
import com.lazerinc.ajaxclient.client.Services;
import com.lazerinc.ajaxclient.client.beans.AjaxUser;

public class PopupSupportRequest {

	public void init() {
		supportPanel = new PopupPanel();
		supportPanel.add(createSupportPanel());
		supportPanel.setPopupPosition(Window.getClientWidth() / 3, Window
				.getClientHeight() / 5);
		supportPanel.setWidth(AjaxSystem.getWidthToWindowLeft(Window
				.getClientWidth() / 3 + 10)
				+ "px");
		supportPanel.show();
	}

	PopupPanel supportPanel;

	TextBox nameBox, emailBox;

	TextArea commentBox;

	protected Widget createSupportPanel() {
		VerticalPanel vp = new VerticalPanel();
		vp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vp
				.add(AjaxSystem
						.getTitle("Send a question or comment to the system admininstrators"));
		vp.addStyleName("support-panel");
		Grid g = new Grid(3, 2);
		g.setWidget(0, 0, AjaxSystem.getLabel("Your Name"));
		AjaxUser user = Services.getServices().user;
		nameBox = new TextBox();
		nameBox.setWidth("25em");
		if (user != null)
			nameBox.setText(user.getFirstName() + " " + user.getLastName());
		g.setWidget(0, 1, nameBox);
		g.setWidget(1, 0, AjaxSystem.getLabel("Your Email"));
		emailBox = new TextBox();
		emailBox.setWidth("25em");
		if (user != null)
			emailBox.setText(user.getEmailAddress());
		g.setWidget(1, 1, emailBox);
		g.setWidget(2, 0, AjaxSystem.getLabel("Question/Comment"));
		commentBox = new TextArea();
		g.setWidget(2, 1, commentBox);
		commentBox.setWidth("25em");
		commentBox.setVisibleLines(8);
		vp.add(g);
		g.setWidth("95%");
		Button send = new Button("Send", getSendListener());
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(send);
		hp.add(new Button("Cancel", new ClickListener() {

			public void onClick(Widget sender) {
				supportPanel.hide();
			}

		}));
		vp.add(hp);
		addTimeframeInfo(vp);
		return vp;
	}

	protected void addTimeframeInfo(VerticalPanel vp) {
		vp.add(AjaxSystem.getLabel("You should receive a reply within 24 hours of posting"));
	}

	protected ClickListener getSendListener() {
		return new ClickListener() {

			public void onClick(Widget sender) {
				Services.getServices().userInfoService.askQuestion(nameBox
						.getText(), emailBox.getText(), commentBox.getText(),
						new AsyncCallback() {

							public void onFailure(Throwable caught) {
								supportPanel.hide();
								new PopupWarning("Failed to send message");

							}

							public void onSuccess(Object result) {
								supportPanel.hide();
								if (((Boolean) result).booleanValue())
									new PopupWarning("Question/Comment Sent!");
								else
									new PopupWarning("Failed to send message");
							}

						});

			}

		};
	}

}
