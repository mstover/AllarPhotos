package com.lazerinc.ajaxclient.client.beans;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.lazerinc.ajaxclient.client.components.icons.BaseIcon;

public class Request implements IsSerializable {

	transient BaseIcon backIcon;

	public Request() {

	}

	public Request(AjaxProduct p, BaseIcon i, String t) {
		product = p;
		backIcon = i;
		text = t;
	}

	AjaxProduct product;

	boolean isDownload;

	String filetype;

	String text;

	public String getFiletype() {
		return filetype;
	}

	public BaseIcon getBackIcon() {
		return backIcon;
	}

	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}

	public boolean isDownload() {
		return isDownload;
	}

	public void setDownload(boolean isDownload) {
		this.isDownload = isDownload;
	}

	public AjaxProduct getProduct() {
		return product;
	}

	public void setProduct(AjaxProduct product) {
		this.product = product;
	}

	public Request(AjaxProduct p, boolean id, String ft, BaseIcon i, String t) {
		product = p;
		isDownload = id;
		filetype = ft;
		backIcon = i;
		text = t;
	}

	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result
				+ ((filetype == null) ? 0 : filetype.hashCode());
		result = PRIME * result + (isDownload ? 1231 : 1237);
		result = PRIME * result + ((product == null) ? 0 : product.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		final Request other = (Request) obj;
		if (filetype == null) {
			if (other.filetype != null)
				return false;
		} else if (!filetype.equals(other.filetype))
			return false;
		if (isDownload != other.isDownload)
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		return true;
	}

	public String toString() {
		return text;
	}

}
