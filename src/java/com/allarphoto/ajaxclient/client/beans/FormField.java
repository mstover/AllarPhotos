package com.allarphoto.ajaxclient.client.beans;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class FormField implements IsSerializable {

	String name;

	int size = 0;

	boolean required;

	String value;

	transient private TextBox text;

	public FormField() {

	}

	public FormField(String n) {
		name = n;
	}

	public FormField(String n, int s) {
		this(n);
		size = s;
	}

	public String getText() {
		return text.getText();
	}

	public String getValue() {
		return value;
	}

	public Widget createField() {
		if (text == null) {
			text = new TextBox();
			text.addChangeListener(new ChangeListener() {

				public void onChange(Widget arg0) {
					value = text.getText();
				}

			});
			text.setName(name);
			if (size > 0)
				text.setMaxLength(size);
		}
		return text;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((name == null) ? 0 : name.hashCode());
		result = PRIME * result + size;
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		try {
			final FormField other = (FormField) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (size != other.size)
				return false;
			return true;
		} catch (RuntimeException e) {
			return false;
		}
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

}
