package com.allarphoto.ajaxclient.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.allarphoto.ajaxclient.client.beans.AjaxAddress;

public interface AddressInfoAsync {

	void getStates(AsyncCallback acb);

	void getCountries(AsyncCallback acb);

	void getMyAddresses(AsyncCallback acb);

	void updateAddress(AjaxAddress a, AsyncCallback acb);
}
