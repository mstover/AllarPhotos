package com.allarphoto.ajaxclient.client.components;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;
import com.allarphoto.ajaxclient.client.AjaxSystem;
import com.allarphoto.ajaxclient.client.Services;

public class StateList extends ListBox {
	public static StateList states = new StateList();

	public StateList() {
		init();
	}

	private void init() {
		addItem("");
		final ListBox t = this;

		Services.getServices().addressInfo.getStates(new AsyncCallback() {

			public void onFailure(Throwable caught) {
			}

			public void onSuccess(Object result) {
				String[] states = (String[]) result;
				AjaxSystem.addToList(t, states);
			}

		});
	}

	public String getState() {
		return getItemText(getSelectedIndex());
	}

	public void setState(String state) {
		setSelectedIndex(AjaxSystem.findIndexBinarySearch(this, state));
	}

}
