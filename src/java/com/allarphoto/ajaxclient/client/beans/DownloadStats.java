package com.allarphoto.ajaxclient.client.beans;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DownloadStats implements IsSerializable {

	String total;

	String from, to;

	DownloadUserStat[] userTotals;

	public DownloadStats() {
	}

	public String fromDate() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String toDate() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getTotal() {
		return total;
	}

	public void setUserMap(DownloadUserStat[] ut) {
		userTotals = ut;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public DownloadUserStat[] getUsers() {
		return userTotals;
	}

}
