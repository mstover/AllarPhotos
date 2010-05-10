package com.allarphoto.ajaxclient.client.beans;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AjaxOrderItem implements IsSerializable {

	AjaxProduct product;

	boolean rejected = false;

	/**
	 * @gwt.typeArgs <java.lang.String,java.lang.String>
	 */
	Map values = new HashMap();

	public AjaxOrderItem() {

	}

	public AjaxProduct getProduct() {
		return product;
	}

	public void setProduct(AjaxProduct product) {
		this.product = product;
	}

	public void addValue(String k, String v) {
		values.put(k, v);
	}

	public Map getValues() {
		return values;
	}

	public String getValue(String k) {
		return (String) values.get(k);
	}

	public void setValues(Map values) {
		this.values = values;
	}

	public boolean isRejected() {
		return rejected;
	}

	public void setRejected(boolean rejected) {
		this.rejected = rejected;
	}

}
