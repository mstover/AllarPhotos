package com.allarphoto.ajaxclient.client;

import com.google.gwt.user.client.rpc.RemoteService;

public interface UploadService extends RemoteService {

	public boolean isUploadDone();

	/**
	 * The first element of the array is an array of required categories. The
	 * second element is an array of optional categories.
	 * 
	 * @return
	 */
	public String[][] getMetaCategories();

	public String[] getPrimaryNames();

	public boolean uploadMeta(String[][] metadata);

}
