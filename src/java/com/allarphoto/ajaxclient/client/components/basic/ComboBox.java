package com.lazerinc.ajaxclient.client.components.basic;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.lazerinc.ajaxclient.client.components.icons.ComboEditIcon;

public class ComboBox extends HorizontalPanel {

	ListBox list;

	TextBox text;

	ComboEditIcon edit;

	String label;

	boolean multiple;

	public ComboBox() {
		this("New Value:", false);
	}

	public ComboBox(boolean m) {
		this("New Value:", m);
	}

	public ComboBox(String label) {
		this(label, false);
	}

	public ComboBox(String label, boolean m) {
		list = new ListBox();
		list.addItem("");
		text = new TextBox();
		this.label = label;
		edit = new ComboEditIcon(this, label);
		multiple = m;
		list.setMultipleSelect(multiple);
		if (multiple) {
			list.setItemSelected(0, false);
			list.setVisibleItemCount(5);
		}
		init();
	}

	protected void init() {
		add(list);
		add(edit);
	}

	public String getLabel() {
		return label;
	}

	public String getText() {
		if (text.getText().trim().length() > 0)
			return text.getText();
		else
			return list.getItemText(list.getSelectedIndex());
	}

	public TextBox getTextBox() {
		return text;
	}

	public void valueEntered() {
		if (text.getText().trim().length() > 0) {
			list.insertItem(text.getText(), 1);
			list.setItemSelected(1, true);
			text.setText("");
		}
	}

	public void addItem(String item, String value) {
		if (item != null && !item.equals("")) {
			list.addItem(item, value);
			text.setWidth(list.getOffsetWidth() + "px");
		}
	}

	public void addItem(String item) {
		if (item != null && !item.equals("")) {
			list.addItem(item);
		}
	}

	public void clear() {
		list.clear();
	}

	public int getItemCount() {
		return list.getItemCount();
	}

	public String getItemText(int index) {
		return list.getItemText(index);
	}

	public int getSelectedIndex() {
		return list.getSelectedIndex();
	}

	public void removeItem(int index) {
		list.removeItem(index);
	}

	public void setItemSelected(int index, boolean selected) {
		list.setItemSelected(index, selected);
	}

	public void setItemText(int index, String text) {
		list.setItemText(index, text);
	}

	public void setSelectedIndex(int index) {
		list.setSelectedIndex(index);
	}

	public boolean isItemSelected(int index) {
		return list.isItemSelected(index);
	}

	public boolean isMultipleSelect() {
		return list.isMultipleSelect();
	}

}
