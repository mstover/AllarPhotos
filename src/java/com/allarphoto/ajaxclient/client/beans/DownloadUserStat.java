package com.lazerinc.ajaxclient.client.beans;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DownloadUserStat implements IsSerializable, Comparable {

	public int compareTo(Object o) {
		DownloadUserStat other = (DownloadUserStat) o;
		long diff = other.getSize() - getSize();
		if (diff == 0)
			return 0;
		else
			return diff > 0 ? 1 : -1;
	}

	String name;

	String sizeString;

	long size;

	public DownloadUserStat() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DownloadUserStat(String n, String ss, long s) {
		name = n;
		sizeString = ss;
		size = s;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getSizeString() {
		return sizeString;
	}

	public void setSizeString(String sizeString) {
		this.sizeString = sizeString;
	}

}
