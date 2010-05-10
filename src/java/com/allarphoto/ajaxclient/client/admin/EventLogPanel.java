package com.allarphoto.ajaxclient.client.admin;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.allarphoto.ajaxclient.client.AjaxSystem;
import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.beans.AjaxLogItem;
import com.allarphoto.ajaxclient.client.components.BusyPopup;
import com.allarphoto.ajaxclient.client.components.DatePicker;
import com.allarphoto.ajaxclient.client.components.PopupWarning;

public class EventLogPanel extends DockPanel {

	DatePicker from, to;

	ListBox actions, categories, values;

	ScrollPanel scroll;

	FlexTable table;

	public EventLogPanel() {
		init();

	}

	private void init() {
		actions = new ListBox();
		actions.addItem("");
		categories = new ListBox();
		categories.addChangeListener(getCategoryChangeListener());
		values = new ListBox();
		Services.getServices().eventService.getActions(new AsyncCallback() {

			public void onFailure(Throwable arg0) {
				new PopupWarning("Failed to get action list");

			}

			public void onSuccess(Object result) {
				String[] actionList = (String[]) result;
				for (int i = 0; i < actionList.length; i++) {
					actions.addItem(actionList[i]);
				}
			}

		});
		actions.addChangeListener(getActionChangeListener());
		HorizontalPanel northPanel = new HorizontalPanel();
		northPanel.add(AjaxSystem.getLabel("From:"));
		from = new DatePicker();
		to = new DatePicker();
		northPanel.add(from);
		northPanel.add(AjaxSystem.getLabel("To:"));
		northPanel.add(to);
		northPanel.add(AjaxSystem.getLabel("Action:"));
		northPanel.add(actions);
		northPanel.add(AjaxSystem.getLabel("Category:"));
		northPanel.add(categories);
		northPanel.add(AjaxSystem.getLabel("Value:"));
		northPanel.add(values);
		northPanel.setSpacing(10);
		northPanel.add(new Button("Go", new ClickListener() {

			public void onClick(Widget arg0) {
				getEventList();
			}
		}));
		add(northPanel, DockPanel.NORTH);

	}

	private ChangeListener getActionChangeListener() {
		return new ChangeListener() {
			public void onChange(Widget w) {
				if (from.getText().length() == 0 || to.getText().length() == 0) {
					DeferredCommand.add(new Command() {

						public void execute() {
							new PopupWarning(
									"Please choose a valid date range before continuing.");
						}

					});
					return;
				}
				String action = actions.getItemText(actions.getSelectedIndex());
				if (action == null || action.length() == 0) {
					categories.clear();
					values.clear();
				} else {
					BusyPopup.waitFor("Getting Categories");
					Services.getServices().eventService.getCategories(action,
							from.getText(), to.getText(), new AsyncCallback() {

								public void onFailure(Throwable arg0) {
									new PopupWarning(arg0.toString());
									BusyPopup.done("Getting Categories");

								}

								public void onSuccess(Object result) {
									String[] cats = (String[]) result;
									categories.clear();
									values.clear();
									categories.addItem("");
									for (int i = 0; i < cats.length; i++)
										categories.addItem(cats[i]);
									BusyPopup.done("Getting Categories");
								}

							});
				}
			}
		};
	}

	private ChangeListener getCategoryChangeListener() {
		return new ChangeListener() {
			public void onChange(Widget w) {
				if (from.getText().length() == 0 || to.getText().length() == 0) {
					DeferredCommand.add(new Command() {

						public void execute() {
							new PopupWarning(
									"Please choose a valid date range before continuing.");
						}

					});
					return;
				}
				String action = actions.getItemText(actions.getSelectedIndex());
				String cat = categories.getItemText(categories
						.getSelectedIndex());
				if (cat == null || cat.length() == 0) {
					values.clear();
				} else {
					BusyPopup.waitFor("Getting Values");
					Services.getServices().eventService.getValues(action, cat,
							from.getText(), to.getText(), new AsyncCallback() {

								public void onFailure(Throwable arg0) {
									new PopupWarning(arg0.toString());
									BusyPopup.done("Getting Values");
								}

								public void onSuccess(Object result) {
									String[] vals = (String[]) result;
									values.clear();
									values.addItem("");
									for (int i = 0; i < vals.length; i++)
										values.addItem(vals[i]);
									BusyPopup.done("Getting Values");
								}

							});
				}
			}
		};
	}

	private void getEventList() {
		if (from.getText().length() == 0 || to.getText().length() == 0) {
			new PopupWarning(
					"Please choose a valid date range before continuing.");
			return;
		}
		BusyPopup.waitFor("Getting Events");
		String value = "";
		String category = "";
		if (values.getSelectedIndex() >= 0)
			value = values.getItemText(values.getSelectedIndex());
		if (categories.getSelectedIndex() >= 0)
			category = categories.getItemText(categories.getSelectedIndex());
		Services.getServices().eventService.getEvents(actions
				.getItemText(actions.getSelectedIndex()), category, value, from
				.getText(), to.getText(), new AsyncCallback() {

			public void onFailure(Throwable arg0) {
				new PopupWarning(arg0.toString());
				BusyPopup.done("Getting Events");

			}

			public void onSuccess(Object result) {
				if (table != null)
					remove(scroll);
				table = new FlexTable();
				table.setCellSpacing(5);
				table.setCellPadding(5);
				AjaxLogItem[] items = (AjaxLogItem[]) result;
				String[] keys = createHeader(actions.getItemText(actions
						.getSelectedIndex()), items);
				for (int i = 0; i < items.length; i++) {
					createRow(i + 1, keys, items[i]);
				}
				scroll = new ScrollPanel(table);
				scroll.setHeight(((int) (Window.getClientHeight() * .90))
						+ "px");
				add(scroll, DockPanel.CENTER);
				BusyPopup.done("Getting Events");
			}

		});
	}

	private void createRow(int row, String[] keys, AjaxLogItem item) {
		int count = 0;
		table.setText(row, count++, item.getDate());
		table.setText(row, count++, item.getValue("user"));
		table.getRowFormatter().addStyleName(row,
				(row % 2 == 0 ? "even" : "odd"));
		for (int i = 0; i < keys.length; i++) {
			if (!keys[i].equals("user") && !keys[i].equals("action")) {
				table.setText(row, count++, item.getValue(keys[i]));
				// table.getCellFormatter().addStyleName(row, count++, "text");
			}
		}
	}

	private String[] createHeader(String action, AjaxLogItem[] items) {
		List keys = new ArrayList();
		int count = 0;
		table.setText(0, count++, "Date");
		table.setText(0, count++, "User");
		for (int j = 0; j < items.length; j++) {
			AjaxLogItem item = items[j];
			String[] names = item.getKeys();
			for (int i = 0; i < names.length; i++) {
				if (!keys.contains(names[i]) && !names[i].equals("user")
						&& !names[i].equals("action")) {
					table.setText(0, count++, capitalize(names[i]));
					// table.getCellFormatter().addStyleName(0, count++,
					// "text");
					keys.add(names[i]);
				}
			}
		}
		table.getRowFormatter().addStyleName(0, "header");
		return AjaxSystem.toStringArray(keys);
	}

	private String capitalize(String cap) {
		try {
			cap = cap.replaceAll("_", " ");
			if (cap.length() > 1) {
				cap = cap.substring(0, 1).toUpperCase() + cap.substring(1);
				int index = 1;
				for (int x = index; x < cap.length() - 1; x++) {
					if (cap.charAt(x) == ' ') {
						cap = cap.substring(0, x + 1)
								+ cap.substring(x + 1, x + 2).toUpperCase()
								+ cap.substring(x + 2);
					}
				}
				return cap;
			} else
				return cap;
		} catch (Exception e) {
			new PopupWarning(e.toString());
			return cap;
		}
	}

}
