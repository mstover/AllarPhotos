package com.lazerinc.ajaxclient.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.lazerinc.ajaxclient.client.beans.AjaxProductFamily;

public interface CustomService extends RemoteService {

	public ComponentFactory getComponentFactory();

	public AjaxProductFamily[] getLibraryList();

}
