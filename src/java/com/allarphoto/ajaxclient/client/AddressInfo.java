package com.lazerinc.ajaxclient.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.lazerinc.ajaxclient.client.beans.AjaxAddress;

public interface AddressInfo extends RemoteService {

	String[] getStates();

	String[] getCountries();

	AjaxAddress[] getMyAddresses();

	boolean updateAddress(AjaxAddress a);

}
