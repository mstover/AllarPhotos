package com.lazerinc.ajaxclient.client.beans;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class SelectField extends FormField implements IsSerializable {

	private transient ListBox text;

	String[] values;

	public SelectField() {
	}

	public SelectField(String n, String[] v) {
		super(n);
		values = v;
	}

	public SelectField(String n, int s, String[] v) {
		super(n, s);
		values = v;
	}

	public String[] getValues() {
		return values;
	}

	public void setValues(String[] values) {
		this.values = values;
	}

	public Widget createField() {
		if (text == null) {
			text = new ListBox();
			for (int i = 0; i < values.length; i++)
				text.addItem(values[i]);
			text.addChangeListener(new ChangeListener() {

				public void onChange(Widget arg0) {
					value = getText();
				}

			});
			text.setName(name);
			if (size > 0)
				text.setVisibleItemCount(size);
		}
		return text;
	}

	public String getText() {
		return text.getItemText(text.getSelectedIndex());
	}

	public String getValue() {
		return super.getValue();
	}

}
