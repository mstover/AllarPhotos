package com.lazerinc.ajaxclient.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CustomServiceAsync {

	public void getComponentFactory(AsyncCallback acb);

	public void getLibraryList(AsyncCallback acb);
}
