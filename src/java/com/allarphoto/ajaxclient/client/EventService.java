package com.allarphoto.ajaxclient.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.allarphoto.ajaxclient.client.beans.AjaxLogItem;

public interface EventService extends RemoteService {

	public String[] getActions();

	public AjaxLogItem[] getEvents(String action, String category,
			String value, String sinceDate, String toDate);

	public String[] getCategories(String action, String sinceDate, String toDate);

	public String[] getValues(String action, String category, String sinceDate,
			String toDate);

}
