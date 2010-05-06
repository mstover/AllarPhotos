package com.lazerinc.ajaxclient.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EventServiceAsync {

	public void getActions(AsyncCallback acb);

	public void getEvents(String action, String category, String value,
			String sinceDate, String toDate, AsyncCallback acb);

	public void getCategories(String action, String sinceDate, String toDate,
			AsyncCallback acb);

	public void getValues(String action, String category, String sinceDate,
			String toDate, AsyncCallback acb);

}
