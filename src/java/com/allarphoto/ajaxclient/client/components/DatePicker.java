package com.allarphoto.ajaxclient.client.components;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;

public class DatePicker extends HorizontalPanel {

	TextBox field;

	public DatePicker() {
		init("");
	}

	public void addChangeListener(ChangeListener cl) {
		field.addChangeListener(cl);
	}

	public DatePicker(String initialValue) {
		init(initialValue);
	}

	private void init(String value) {
		field = new TextBox();
		add(field);
		String id = "date-field" + hashCode();
		DOM.setAttribute(field.getElement(), "id", id);
		add(new HTML(
				"<a href=\"javascript:show_calendar('"
						+ id
						+ "', window.top.document.getElementById('"
						+ id
						+ "').value);\"><img src='/lazerweb/iwimages/cal.gif' width='16' height='16' border='0' title='Click Here to Pick the timestamp' alt='Click Here to Pick the timestamp'></a>"));
	}

	public String getText() {
		return field.getText();
	}

	public void setText(String text) {
		field.setText(text);
	}

}
