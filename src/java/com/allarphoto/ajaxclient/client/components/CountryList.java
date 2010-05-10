package com.allarphoto.ajaxclient.client.components;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;
import com.allarphoto.ajaxclient.client.AjaxSystem;
import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.components.basic.ComboBox;

public class CountryList extends ComboBox {

	public static CountryList countries = new CountryList();

	public CountryList() {
		super();
		init2();
	}

	private void init2() {
		addItem("");
		final ComboBox t = this;

		Services.getServices().addressInfo.getCountries(new AsyncCallback() {

			public void onFailure(Throwable caught) {
			}

			public void onSuccess(Object result) {
				String[] countries = (String[]) result;
				AjaxSystem.addToComboList(t, countries);
			}

		});
	}

	public String getCountry() {
		return getItemText(getSelectedIndex());
	}

	public void setCountry(String country) {
		setSelectedIndex(AjaxSystem.findComboIndexBinarySearch(this, country));
	}
}
