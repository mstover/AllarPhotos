package com.allarphoto.ajaxclient.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UploadServiceAsync {

	public void isUploadDone(AsyncCallback acb);

	/**
	 * The first element of the array is an array of required categories. The
	 * second element is an array of optional categories.
	 * 
	 * @return
	 */
	public void getMetaCategories(AsyncCallback acb);

	public void getPrimaryNames(AsyncCallback acb);

	public void uploadMeta(String[][] metadata, AsyncCallback acb);
}
