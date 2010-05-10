package com.allarphoto.ajaxclient.client.fragments;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.allarphoto.ajaxclient.client.AjaxSystem;
import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.beans.AjaxAddress;

public class AddressFragment implements EntryPoint {

	List usedAddrs;

	AjaxAddress[] addresses;

	VerticalPanel mainPanel;

	boolean created;

	public void onModuleLoad() {
		init();
	}

	protected void init() {
		RootPanel.get("address_fragment").clear();
		mainPanel = new VerticalPanel();
		usedAddrs = new ArrayList();
		final ListBox addressChoices = new ListBox();
		Label store = AjaxSystem.getLabel("Stored Addresses:");
		final Label link = AjaxSystem.getLabel("Manage Addresses");
		link.addStyleName("linked");
		final HorizontalPanel hp = new HorizontalPanel();
		hp.add(store);
		hp.add(link);
		mainPanel.add(hp);
		mainPanel.add(addressChoices);
		created = false;
		Services.getServices().addressInfo.getMyAddresses(new AsyncCallback() {

			public void onFailure(Throwable arg0) {
			}

			public void onSuccess(Object arg0) {
				addressChoices.addItem("");
				addresses = (AjaxAddress[]) arg0;
				addressChoices.addChangeListener(new ChangeListener() {

					public void onChange(Widget lb) {
						int index = ((ListBox) lb).getSelectedIndex() - 1;
						if (index >= 0) {
							AjaxAddress addr = (AjaxAddress) usedAddrs
									.get(index);
							setAddressIntoForm(addr.getAttn(), addr
									.getAddress1(), addr.getAddress2(), addr
									.getCity(), addr.getState(), addr
									.getCountry(), addr.getPhone(), addr
									.getCompany().getName(), addr.getZip());
						}
					}

				});
				for (int i = 0; i < addresses.length; i++) {
					if (addresses[i].isInUse()) {
						addressChoices.addItem(addresses[i].toString());
						usedAddrs.add(addresses[i]);
					}
				}
				hp.setWidth("100%");
				link.addClickListener(new ClickListener() {

					public void onClick(Widget sender) {
						if (!created) {
							created = true;
							mainPanel.add(createAddressControlPanel());
						}
					}

				});

			}

		});
		hp.setCellHorizontalAlignment(store, HasHorizontalAlignment.ALIGN_LEFT);
		hp.setCellHorizontalAlignment(link, HasHorizontalAlignment.ALIGN_RIGHT);
		RootPanel.get("address_fragment").add(mainPanel);
	}

	protected Widget createAddressControlPanel() {
		final VerticalPanel vp = new VerticalPanel();
		Grid g = new Grid(addresses.length + 1, 2);
		g.setWidget(0, 0, AjaxSystem.getLabel("Active?"));
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
			}

		}));
		return vp;
	}

	private native void setAddressIntoForm(String attn, String address_1,
			String address_2, String city, String stateName,
			String countryName, String phone, String company, String zip)/*-{
	 var f = top.document.getElementById("addr_form");
	 f.phone.value=""+phone;
	 f.elements['company.name'].value=company;
	 f.attn.value=attn;
	 f.address_1.value=address_1;
	 f.address_2.value=address_2;
	 f.zip.value=zip;
	 f.elements['City.name'].value=city;
	 for (var i = 0; i < f.elements['State.name'].options.length; i++) {
	 if(f.elements['State.name'].options[i].selected) f.elements['State.name'].options[i].selected = false;
	 if(f.elements['State.name'].options[i].value==stateName)
	 {
	 f.elements['State.name'].options[i].selected=true;
	 break;
	 }
	 }
	 for (var i = 0; i < f.elements['Country.name'].options.length; i++) {
	 if(f.elements['Country.name'].options[i].selected) f.elements['Country.name'].options[i].selected = false;
	 if(f.elements['Country.name'].options[i].value==countryName)
	 {
	 f.elements['Country.name'].options[i].selected=true;
	 break;
	 }
	 }
	 }-*/;

}
