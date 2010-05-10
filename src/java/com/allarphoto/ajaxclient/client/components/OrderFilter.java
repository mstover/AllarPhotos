package com.allarphoto.ajaxclient.client.components;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.TreeListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.allarphoto.ajaxclient.client.AjaxSystem;
import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.UpdateListener;
import com.allarphoto.ajaxclient.client.beans.AjaxOrder;
import com.allarphoto.ajaxclient.client.beans.AjaxProductFamily;

public class OrderFilter extends VerticalPanel implements UpdateListener {
	DatePicker sinceDate;

	ListBox status;

	ListBox family;

	Tree orders;

	String username;

	public OrderFilter() {
		init();
	}

	public OrderFilter(String date) {
		init();
		populate(date);
	}

	public OrderFilter(String date, String u) {
		init();
		populateUsername(date, u);
	}

	public OrderFilter(String date, String st, boolean not) {
		init();
		populate(date, st);
	}

	private void populate(String date) {
		sinceDate.setText(date);
	}

	private void populateUsername(String date, String u) {
		sinceDate.setText(date);
		username = u;
		getOrders();
	}

	private void populate(String date, String st) {
		sinceDate.setText(date);
		status.setSelectedIndex(AjaxSystem.findIndex(status, st));
		getOrders();
	}

	public void update(AjaxOrder o) {
		orders.getSelectedItem().setUserObject(o);

	}

	private void init() {
		sinceDate = new DatePicker();
		sinceDate.addChangeListener(getDateListener());
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(AjaxSystem.getText("Since Date:"));
		hp.add(sinceDate);
		add(hp);
		hp = new HorizontalPanel();
		hp.add(AjaxSystem.getText("Status:"));
		status = new ListBox();
		status.addItem("");
		status.addItem("awaiting_approval");
		status.addItem("awaiting_confirmation");
		status.addItem("awaiting_fulfillment");
		status.addItem("fulfilled");
		status.addItem("rejected");
		status.addItem("cancelled");
		status.addChangeListener(getStatusListener());
		hp.add(status);
		add(hp);
		hp = new HorizontalPanel();
		hp.add(AjaxSystem.getText("Library:"));
		setupLibraryList();
		family.addChangeListener(getStatusListener());
		hp.add(family);
		add(hp);
		add(AjaxSystem.getLabel("Orders: "));
		orders = new Tree();
		orders.addTreeListener(getTreeListener());
		add(orders);

	}

	private TreeListener getTreeListener() {
		final UpdateListener t = this;
		return new TreeListener() {

			public void onTreeItemSelected(TreeItem arg0) {
				Services.getServices().mainPanel.setCenter(Services.getServices().factory.createComponent("OrderDisplay",new Object[]{arg0.getUserObject(), t}));

			}

			public void onTreeItemStateChanged(TreeItem arg0) {
				// TODO Auto-generated method stub

			}

		};
	}

	private ChangeListener getDateListener() {
		return new ChangeListener() {

			public void onChange(Widget arg0) {
				if (status.getItemText(status.getSelectedIndex()).length() > 0) {
					getOrders();
				}

			}

		};
	}

	private ChangeListener getStatusListener() {
		return new ChangeListener() {

			public void onChange(Widget arg0) {
				getOrders();

			}

		};
	}

	private void setupLibraryList() {
		family = new ListBox();
		family.addItem("");
		Services.getServices().libraryInfoService.getLibraries("admin",
				new AsyncCallback() {

					public void onFailure(Throwable arg0) {
						new PopupWarning("Failed to retrieve library list "
								+ arg0.toString());

					}

					public void onSuccess(Object arg0) {
						AjaxProductFamily[] libList = (AjaxProductFamily[]) arg0;
						for (int i = 0; i < libList.length; i++) {
							AjaxProductFamily f = libList[i];
							family.addItem(f.getDescriptiveName());
						}
					}

				});
	}

	private void getOrders() {
		if (status.getItemText(status.getSelectedIndex()).length() > 0) {
			if (family.getItemText(family.getSelectedIndex()).length() > 0) {
				BusyPopup.waitFor("Retrieving Orders");
				Services.getServices().orderService.getLibraryOrders(family
						.getItemText(family.getSelectedIndex()), status
						.getItemText(status.getSelectedIndex()), sinceDate
						.getText(), getOrderCallback());
			} else {
				BusyPopup.waitFor("Retrieving Orders");
				Services.getServices().orderService.getOrders(status
						.getItemText(status.getSelectedIndex()), sinceDate
						.getText(), getOrderCallback());
			}
		} else if (username != null) {

			BusyPopup.waitFor("Retrieving Orders");
			Services.getServices().orderService.getUserOrders(username,
					sinceDate.getText(), getOrderCallback());
		}
	}

	private AsyncCallback getOrderCallback() {
		return new AsyncCallback() {

			public void onFailure(Throwable arg0) {
				BusyPopup.done("Retrieving Orders");
				new PopupWarning("Error retrieving orders " + arg0.toString());
			}

			public void onSuccess(Object arg0) {
				List orderList = (List) arg0;
				Iterator iter = orderList.iterator();
				orders.removeItems();
				while (iter.hasNext()) {
					AjaxOrder o = (AjaxOrder) iter.next();
					if (o.getOrderNo().equals("More..."))
						new PopupWarning(
								"More than 100 orders found, please narrow your search criteria");
					TreeItem item = new TreeItem(o.getDate()
							+ " - "
							+ o.getUser().getLastName()
							+ ", "
							+ o.getUser().getFirstName()
							+ "("
							+ o.getOrderNo().substring(
									o.getOrderNo().indexOf(".")) + "):"
							+ o.getValue("family"));
					item.setUserObject(o);
					orders.addItem(item);
				}
				BusyPopup.done("Retrieving Orders");
				if (orderList.size() == 0) {
					new PopupWarning("No orders found");
				}

			}
		};
	}

}
