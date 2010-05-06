package com.lazerinc.ajaxclient.client.beans;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class AreaField extends FormField implements IsSerializable {

	int rows = 0, cols = 0;

	transient private TextArea text;

	public AreaField() {

	}

	public AreaField(String n) {
		super(n);
	}

	public AreaField(String n, int r, int c) {
		super(n);
		rows = r;
		cols = c;
	}

	public String getText() {
		return text.getText();
	}

	public Widget createField() {
		if (text == null) {
			text = new TextArea();
			text.addChangeListener(new ChangeListener() {

				public void onChange(Widget arg0) {
					value = text.getText();
				}

			});
			text.setName(name);
			if (cols > 0)
				text.setCharacterWidth(cols);
			if (rows > 0)
				text.setHeight(cols + "em");
		}
		return text;
	}

	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + getName().hashCode();
		result = PRIME * result + cols;
		result = PRIME * result + rows;
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		try {
			final AreaField other = (AreaField) obj;
			if (getName() == null) {
				if (other.getName() != null)
					return false;
			} else if (!getName().equals(other.getName()))
				return false;
			if (cols != other.cols)
				return false;
			if (rows != other.rows)
				return false;
			return true;
		} catch (RuntimeException e) {
			return false;
		}
	}

	public int getCols() {
		return cols;
	}

	public void setCols(int cols) {
		this.cols = cols;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

}
