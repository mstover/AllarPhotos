package com.allarphoto.ajaxclient.client.components;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.allarphoto.ajaxclient.client.AjaxSystem;
import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.beans.AjaxAddress;
import com.allarphoto.ajaxclient.client.beans.AjaxCart;

public class UserInfoCheckoutScreen extends VerticalPanel {
	ListBox addressChoices;

	AjaxAddress[] addresses;

	List usedAddrs;

	AddressForm addrForm;

	CheckoutPanel checkout;

	public UserInfoCheckoutScreen(AjaxCart cart, CheckoutPanel c) {
		checkout = c;
		init();
	}

	private void init() {
		clear();
		usedAddrs = new ArrayList();
		addressChoices = new ListBox();
		final Label store = AjaxSystem.getLabel("Stored Addresses:","header");
		store.addStyleName("linked");
		add(store);
		add(addressChoices);
		addrForm = new AddressForm();
		add(addrForm);
		BusyPopup.waitFor("Loading Your Addresses");
		Services.getServices().addressInfo.getMyAddresses(new AsyncCallback() {
			public void onFailure(Throwable arg0) {
				BusyPopup.done("Loading Your Addresses");

			}

			public void onSuccess(Object arg0) {
				addresses = (AjaxAddress[]) arg0;
				for (int i = 0; i < addresses.length; i++) {
					if (addresses[i].isInUse()) {
						if (usedAddrs.size() == 0)
							addrForm.populate(addresses[i]);
						addressChoices.addItem(addresses[i].toString());
						usedAddrs.add(addresses[i]);
					}
				}
				store.addClickListener(new ClickListener() {

					public void onClick(Widget sender) {
						checkout.workflowInsert(createAddressControlPanel());
					}

				});
				BusyPopup.done("Loading Your Addresses");

			}
		});
		addressChoices.addChangeListener(new ChangeListener() {

			public void onChange(Widget arg0) {
				int index = addressChoices.getSelectedIndex();
				if (index >= 0) {
					AjaxAddress addr = (AjaxAddress) usedAddrs.get(index);
					addrForm.populate(addr);
				}
			}

		});
	}

	protected Widget createAddressControlPanel() {
		VerticalPanel vp = new VerticalPanel();
		Grid g = new Grid(addresses.length + 1, 2);
		g.setWidget(0, 0, AjaxSystem.getLabel("In Use"));
		g.setText(0, 1, "");
		for (int i = 0; i < addresses.length; i++) {
			final AjaxAddress aa = addresses[i];
			CheckBox inuse = new CheckBox();
			inuse.addClickListener(new ClickListener() {

				public void onClick(Widget sender) {
					if (((CheckBox) sender).isChecked())
						aa.setInUse(true);
					else
						aa.setInUse(false);
					Services.getServices().addressInfo.updateAddress(aa,
							new AsyncCallback() {

								public void onFailure(Throwable caught) {

								}

								public void onSuccess(Object result) {
								}

							});
				}

			});
			inuse.setChecked(addresses[i].isInUse());
			g.setWidget(i + 1, 0, inuse);
			g.setText(i + 1, 1, addresses[i].toString());
		}
		vp.add(g);
		vp.add(new Button("Done", new ClickListener() {

			public void onClick(Widget sender) {
				init();
				checkout.endWorkflowinsert();
			}

		}));
		return vp;
	}

	public AjaxAddress getAddress() {
		return addrForm.getAddress();
	}

}
