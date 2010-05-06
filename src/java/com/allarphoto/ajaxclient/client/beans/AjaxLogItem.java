package com.lazerinc.ajaxclient.client.beans;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.lazerinc.ajaxclient.client.AjaxSystem;

public class AjaxLogItem implements IsSerializable {

	private String date;

	private int id = -1;

	/**
	 * 
	 * @gwt.typeArgs <java.lang.String,java.lang.String>
	 */
	private java.util.Map item = new HashMap();

	public AjaxLogItem() {

	}

	public void setValue(String key, String value) {
		item.put(key, value);
	}

	public String getValue(String key) {
		return (String) item.get(key);
	}

	public String getDate() {
		return date;
	}

	public void setDate(String d) {
		date = d;
	}

	public String[] getKeys() {
		return AjaxSystem.toStringArray(item.keySet());
	}
}
