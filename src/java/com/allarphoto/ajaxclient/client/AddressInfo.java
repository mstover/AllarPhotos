package com.allarphoto.ajaxclient.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.allarphoto.ajaxclient.client.beans.AjaxAddress;

public interface AddressInfo extends RemoteService {

	String[] getStates();

	String[] getCountries();

	AjaxAddress[] getMyAddresses();

	boolean updateAddress(AjaxAddress a);

}
