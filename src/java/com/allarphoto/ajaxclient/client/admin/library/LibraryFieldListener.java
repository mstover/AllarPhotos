package com.allarphoto.ajaxclient.client.admin.library;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.beans.AjaxProductFamily;
import com.allarphoto.ajaxclient.client.beans.AjaxProductField;

public class LibraryFieldListener implements TableListener {
	Services services = Services.getServices();

	int curRow = -1, curCol = -1;

	AjaxProductFamily family;

	public LibraryFieldListener(AjaxProductFamily f) {
		family = f;
	}

	public void onCellClicked(SourcesTableEvents sender, int row, int cell) {
		FlexTable table = (FlexTable) sender;
		if("deleted".equals(table.getRowFormatter().getStylePrimaryName(row))) return;/**/
		clearCurrentSelection(table, row, cell);
		makeWidget(table, row, cell);
	}

	private void clearCurrentSelection(FlexTable table, int row, int col) {
		if (row == curRow && col == curCol)
			return;
		if (curCol == 3 || curCol == 4) {
			Widget w = table.getWidget(curRow, curCol);
			if (w instanceof TextBox) {
				TextBox t = (TextBox) w;
				updateField(table, curRow, curCol, t);
				table.setText(curRow, curCol, t.getText());
			}
		} else if (curCol == 2) {
			Widget w = table.getWidget(curRow, curCol);
			if (w instanceof ListBox) {
				ListBox l = (ListBox) w;
				updateField(table, curRow, curCol, l);
				table.setText(curRow, curCol, l.getItemText(l
						.getSelectedIndex()));
			}
		}
		curRow = -1;
		curCol = -1;
	}

	private void makeWidget(final FlexTable table, final int row, final int col) {
		if (row == curRow && col == curCol)
			return;
		if (row >= 1) {
			curRow = row;
			curCol = col;
			if (col == 3 || col == 4) {
				TextBox t = new TextBox();
				t.setText(table.getText(row, col));
				table.setWidget(row, col, t);
				t.setFocus(true);
				t.addKeyboardListener(new KeyboardListener() {

					public void onKeyDown(Widget sender, char keyCode,
							int modifiers) {
					}

					public void onKeyPress(Widget sender, char keyCode,
							int modifiers) {
						TextBox t = (TextBox) sender;
						if (keyCode == (char) KeyboardListener.KEY_ENTER) {
							clearCurrentSelection(table, -11, -11);
						}

					}

					public void onKeyUp(Widget sender, char keyCode,
							int modifiers) {
					}

				});
			} else if (col == 2 && !table.getText(row, col).equals("Primary")) {
				String curValue = table.getText(row, col);
				ListBox l = new ListBox();
				l.addItem("Category");
				if (curValue.equals("Category"))
					l.setSelectedIndex(0);
				l.addItem("Description");
				if (curValue.equals("Description"))
					l.setSelectedIndex(1);
				l.addItem("Numerical");
				if (curValue.equals("Numerical"))
					l.setSelectedIndex(2);
				l.addItem("Protected");
				if (curValue.equals("Protected"))
					l.setSelectedIndex(3);
				l.addItem("Tag");
				if (curValue.equals("Tag"))
					l.setSelectedIndex(4);
				l.addChangeListener(new ChangeListener() {

					public void onChange(Widget sender) {
						clearCurrentSelection(table, -11, -11);
					}

				});
				table.setWidget(row, col, l);
			}
		}
	}

	private void updateField(final FlexTable table, final int row,
			final int col, ListBox l) {
		family.getFields()[row - 1] = new AjaxProductField(table
				.getText(row, 1), family.getFamilyName(), l.getItemText(l
				.getSelectedIndex()), Integer.parseInt(table.getText(row, 4)),
				Integer.parseInt(table.getText(row, 3)));
		services.libraryInfoService.updateField(family.getFields()[row - 1]
				.getName(), family.getFamilyName(), family.getFields()[row - 1]
				.getType(), family.getFields()[row - 1].getDisplayOrder(),
				family.getFields()[row - 1].getSearchOrder(),
				new AsyncCallback() {

					public void onFailure(Throwable caught) {

					}

					public void onSuccess(Object result) {
					}

				});
	}

	private void updateField(final FlexTable table, final int row,
			final int col, TextBox t) {
		family.getFields()[row - 1] = new AjaxProductField(table
				.getText(row, 1), family.getFamilyName(),
				table.getText(row, 2), (col == 4) ? Integer.parseInt(t
						.getText()) : Integer.parseInt(table.getText(row, 4)),
				(col == 3) ? Integer.parseInt(t.getText()) : Integer
						.parseInt(table.getText(row, 3)));
		services.libraryInfoService.updateField(family.getFields()[row - 1]
				.getName(), family.getFamilyName(), family.getFields()[row - 1]
				.getType(), family.getFields()[row - 1].getDisplayOrder(),
				family.getFields()[row - 1].getSearchOrder(),
				new AsyncCallback() {

					public void onFailure(Throwable caught) {

					}

					public void onSuccess(Object result) {
					}

				});
	}

}
