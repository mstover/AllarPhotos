package com.allarphoto.ajaxclient.client.components;

import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public abstract class EnterKeyListener implements KeyboardListener {

	public void onKeyDown(Widget sender, char keyCode, int modifiers) {
	}

	public void onKeyPress(Widget sender, char keyCode, int modifiers) {
		TextBox t = (TextBox) sender;
		if (keyCode == (char) KeyboardListener.KEY_ENTER) {
			onEnterPress(sender);
		}

	}

	public void onKeyUp(Widget sender, char keyCode, int modifiers) {
	}

	protected abstract void onEnterPress(Widget source);

}
