package com.allarphoto.ajaxclient.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.allarphoto.ajaxclient.client.beans.AjaxProductFamily;

public interface CustomService extends RemoteService {

	public ComponentFactory getComponentFactory();

	public AjaxProductFamily[] getLibraryList();

}
