package com.allarphoto.ajaxclient.client.components;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.allarphoto.ajaxclient.client.AjaxSystem;
import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.beans.AjaxUser;

public class UserTipDisplay {

	public void init() {
		userTipPanel = new PopupPanel();
		userTipPanel.add(createuserTipPanel());
		userTipPanel.setPopupPosition(Window.getClientWidth() / 3, Window
				.getClientHeight() / 5);
		userTipPanel.setWidth(AjaxSystem.getWidthToWindowLeft(Window
				.getClientWidth() / 3 + 10)
				+ "px");
		userTipPanel.show();
	}

	PopupPanel userTipPanel;
	HTML userTips;

	protected Widget createuserTipPanel() {
		VerticalPanel vp = new VerticalPanel();
		vp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vp
				.add(AjaxSystem
						.getTitle("Library User Tips"));
		vp.addStyleName("usertips-panel");
		HorizontalPanel hp = new HorizontalPanel();
		userTips = new HTML();
		userTips.setHTML(getUserTips());
		hp.add(userTips);
		hp.add(new Button("Close", new ClickListener() {
			public void onClick(Widget sender) {
				userTipPanel.hide();
			}
		}));
		vp.add(hp);
		return vp;
	}
	
	protected String getUserTips() {
		String myTips = "<h1>This is where my User Tips will go!</h1>";
		myTips += "<p>Give the man a fish and he may feed his family.</p>";
		myTips += "<p>Teach a man to fish and he may feed his village.</p>";
		return myTips;
	}

}
