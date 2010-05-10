package com.allarphoto.ajaxclient.client.components;

import java.util.Iterator;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.allarphoto.ajaxclient.client.AjaxSystem;
import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.UpdateListener;
import com.allarphoto.ajaxclient.client.beans.AjaxOrder;
import com.allarphoto.ajaxclient.client.beans.AjaxOrderItem;
import com.allarphoto.ajaxclient.client.beans.AjaxProduct;

public class OrderDisplay extends VerticalPanel {
	AjaxOrder order;

	FlexTable properties = new FlexTable();

	UpdateListener listener;

	public OrderDisplay(String orderNo) {
		init();
		retrieveOrder(orderNo);
	}

	public OrderDisplay(AjaxOrder o) {
		order = o;
		init();
		populateDisplay();
	}

	public OrderDisplay(AjaxOrder o, UpdateListener ul) {
		this(o);
		listener = ul;
	}

	private void retrieveOrder(final String orderNo) {
		BusyPopup.waitFor("Loading Order " + orderNo);
		Services.getServices().orderService.getOrder(orderNo,
				new AsyncCallback() {

					public void onFailure(Throwable arg0) {
						BusyPopup.done("Loading Order " + orderNo);

					}

					public void onSuccess(Object arg0) {
						order = (AjaxOrder) arg0;
						populateDisplay();
						BusyPopup.done("Loading Order " + orderNo);
					}

				});
	}

	private void init() {
		add(AjaxSystem.getTitle("Order Details"));
		add(properties);
	}

	protected void populateDisplay() {
		int row = 0;
		properties.setWidget(row, 0, AjaxSystem.getLabel("Order No:"));
		properties.setText(row++, 1, order.getOrderNo());
		properties.setWidget(row, 0, AjaxSystem.getLabel("Date Ordered:"));
		properties.setText(row++, 1, order.getDate());
		properties.setWidget(row, 0, AjaxSystem.getLabel("Merchant:"));
		properties.setText(row++, 1, order.getMerchant());
		properties.setWidget(row, 0, AjaxSystem.getLabel("Product Library:"));
		properties.setText(row++, 1, order.getValue("family"));
		properties.setWidget(row, 0, AjaxSystem.getLabel("Status:"));
		properties.setWidget(row++, 1, AjaxSystem.getLabel(order.getStatus(),
				"status-" + order.getStatus()));
		properties.setWidget(row, 0, AjaxSystem.getLabel("User:"));
		properties.setText(row++, 1, order.getUser().getLastName() + ", "
				+ order.getUser().getFirstName());
		properties.setWidget(row, 0, AjaxSystem.getLabel("Shipping Address:"));
		if (order.getShippingAddress() != null)
			properties.setWidget(row, 1, order.getShippingAddress().toHTML());
		else
			properties.setText(row, 1, "");
		properties.getRowFormatter().setVerticalAlign(row++,
				HasVerticalAlignment.ALIGN_TOP);
		Iterator iter = order.getValues().keySet().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			if (filterInfo(key)) {
				properties.setWidget(row, 0, AjaxSystem.getLabel(key));
				properties.setText(row++, 1, order.getValue(key));
			}
		}
		if (order.getStatus().equals("awaiting_approval") && order.isApprove()) {
			add(getApprovalButtons());
		} else if (order.getStatus().equals("awaiting_confirmation")
				&& order.isConfirm())
			add(getConfirmButtons());
		else if (order.getStatus().equals("awaiting_fulfillment")
				&& order.isFulfill())
			add(getFulfillButtons());
		Grid vp = new Grid(order.getItems().size(), 3);
		vp.addStyleName("order-display-table");
		iter = order.getItems().iterator();
		int count = 0;
		while (iter.hasNext()) {
			final AjaxOrderItem r = (AjaxOrderItem) iter.next();
			vp.setWidget(count, 0, OrderDisplay.createProductImage(r
					.getProduct()));
			vp.setWidget(count, 1, OrderDisplay.createUsageRightsPanel(r
					.getProduct()));
			if ((order.getStatus().equals("awaiting_approval") && order
					.isApprove())) {
				CheckBox box = new CheckBox("Reject");
				box.addClickListener(new ClickListener() {

					public void onClick(Widget arg0) {
						CheckBox b = (CheckBox) arg0;
						if (b.isChecked())
							r.setRejected(true);
						else
							r.setRejected(false);
					}

				});
				vp.setWidget(count, 2, box);
			} else if (r.isRejected()) {
				vp.setWidget(count, 2, AjaxSystem.getLabel("Rejected",
						"status-rejected"));
			} else
				vp.setText(count, 2, "");
			count++;
		}
		add(vp);
	}

	protected boolean filterInfo(String key) {
		return !key.equals("family") && !key.equals("Total Cost")
				&& !key.equals("Order.class") && !key.equals("action");
	}

	private Widget getApprovalButtons() {
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(new Button("Approve", getApproveAction()));
		hp.add(new Button("Partially Approve", getPartialApproveAction()));
		hp.add(new Button("Reject", getRejectAction()));
		hp.setSpacing(10);
		return hp;
	}

	private Widget getConfirmButtons() {
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(new Button("Confirm", getConfirmAction()));
		hp.add(new Button("Cancel", getCancelAction()));
		hp.setSpacing(10);
		return hp;
	}

	private Widget getFulfillButtons() {

		HorizontalPanel hp = new HorizontalPanel();
		hp.add(new Button("Mark Fulfilled", getFulfillAction()));
		hp.setSpacing(10);
		return hp;
	}

	private AsyncCallback updateOrder() {
		BusyPopup.waitFor("Updating Order");
		return new AsyncCallback() {

			public void onFailure(Throwable arg0) {
				new PopupWarning(arg0.toString());
				BusyPopup.done("Updating Order");

			}

			public void onSuccess(Object arg0) {
				order = (AjaxOrder) arg0;
				clear();
				init();
				populateDisplay();
				if(listener != null) listener.update(order);
				BusyPopup.done("Updating Order");
			}

		};
	}

	private ClickListener getApproveAction() {
		return new ClickListener() {

			public void onClick(Widget arg0) {
				Services.getServices().orderService.approveOrder(order,
						updateOrder());
			}

		};
	}

	private ClickListener getPartialApproveAction() {

		return new ClickListener() {

			public void onClick(Widget arg0) {
				Services.getServices().orderService.approveOrder(order,
						updateOrder());
			}

		};
	}

	private ClickListener getConfirmAction() {

		return new ClickListener() {

			public void onClick(Widget arg0) {
				Services.getServices().orderService.confirmOrder(order,
						updateOrder());
			}

		};
	}

	private ClickListener getCancelAction() {

		return new ClickListener() {

			public void onClick(Widget arg0) {
				Services.getServices().orderService.cancelOrder(order,
						updateOrder());
			}

		};
	}

	private ClickListener getFulfillAction() {
		return new ClickListener() {

			public void onClick(Widget arg0) {
				Services.getServices().orderService.fulfillOrder(order,
						updateOrder());
			}

		};
	}

	private ClickListener getRejectAction() {
		return new ClickListener() {

			public void onClick(Widget arg0) {
				Services.getServices().orderService.rejectOrder(order,
						updateOrder());
			}

		};
	}

	static public Widget createProductImage(AjaxProduct product) {
		VerticalPanel vp = new VerticalPanel();
		Image img = new Image();
		img.setUrl("/lazerweb/servlet/fetchpix?product_family="
				+ product.getFamilyName() + "&product_id=" + product.getId()
				+ "&thumb=true&mimetype=image/jpeg");
		if (product.getHeight() > product.getWidth()) {
			if (product.getHeight() > 80)
				img.setHeight("80px");
		} else {
			if (product.getWidth() > 80)
				img.setWidth("80px");
		}
		vp.add(img);
		vp.add(AjaxSystem.getText(product.getName()));
		vp.addStyleName("order-display");
		return vp;
	}

	static public Widget createUsageRightsPanel(AjaxProduct product) {
		VerticalPanel vp = new VerticalPanel();
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(AjaxSystem.getLabel("Usage Expiration:"));
		hp.add(AjaxSystem.getText(product.getValue("Usage Expiration")));
		vp.add(hp);
		hp = new HorizontalPanel();
		hp.add(AjaxSystem.getLabel("Usage Rights:"));
		hp.add(AjaxSystem.getText(product.getValue("Usage Rights")));
		vp.add(hp);
		hp = new HorizontalPanel();
		hp.add(AjaxSystem.getLabel("Photographer:"));
		hp.add(AjaxSystem.getText(product.getValue("Photographer")));
		vp.add(hp);
		hp = new HorizontalPanel();
		hp.add(AjaxSystem.getLabel("Model:"));
		hp.add(AjaxSystem.getText(product.getValue("Model")));
		vp.add(hp);
		vp.addStyleName("order-display");
		return vp;
	}

}
