package com.allarphoto.ajaxclient.client.components;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.allarphoto.ajaxclient.client.Services;

public class DownloadLink extends HTML {
	String filename;

	String dir;

	public DownloadLink() {
		addStyleName("download-link");
		init();
	}

	public DownloadLink(String arg0) {
		filename = arg0;
		addStyleName("download-link");
		init();
	}

	private void init() {
		this.setHTML(filename);
		Services.getServices().orderService.getDownloadDir(new AsyncCallback() {

			public void onFailure(Throwable arg0) {
				// TODO Auto-generated method stub

			}

			public void onSuccess(Object arg0) {
				dir = arg0.toString();
				setHTML("<a href='" + dir + filename + "'>" + filename + "</a>");
			}

		});
	}

	public String getLinkUrl() {
		return dir + filename;
	}

}
