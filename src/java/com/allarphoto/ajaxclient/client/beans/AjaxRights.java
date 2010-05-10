package com.allarphoto.ajaxclient.client.beans;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AjaxRights implements IsSerializable {

	boolean admin = false, download = false, read = false, order = false,
			upload = false, download_orig = false;

	public AjaxRights() {
	}

	public AjaxRights(boolean ad, boolean re, boolean or, boolean down,
			boolean down_orig, boolean up) {
		admin = ad;
		read = re;
		order = or;
		download = down;
		download_orig = down_orig;
		upload = up;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isDownload() {
		return download;
	}

	public void setDownload(boolean download) {
		this.download = download;
	}

	public boolean isOrder() {
		return order;
	}

	public void setOrder(boolean order) {
		this.order = order;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public boolean isUpload() {
		return upload;
	}

	public void setUpload(boolean upload) {
		this.upload = upload;
	}

	public boolean hasAnyRight() {
		return admin || read || order || download || download_orig || upload;
	}

	public boolean isDownload_orig() {
		return download_orig;
	}

	public void setDownload_orig(boolean download_orig) {
		this.download_orig = download_orig;
	}

}
