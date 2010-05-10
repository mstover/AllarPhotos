package com.allarphoto.ajaxclient.client.components;

import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;

public class Reactive implements MouseListener {

	public void onMouseDown(Widget sender, int x, int y) {
		// TODO Auto-generated method stub

	}

	public void onMouseEnter(Widget sender) {
		sender.addStyleName("mouse_over");

	}

	public void onMouseLeave(Widget sender) {
		sender.removeStyleName("mouse_over");

	}

	public void onMouseMove(Widget sender, int x, int y) {
		// TODO Auto-generated method stub

	}

	public void onMouseUp(Widget sender, int x, int y) {
		// TODO Auto-generated method stub

	}

}
