package com.allarphoto.ajaxclient.client.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AjaxOrderResponse implements IsSerializable {

	/**
	 * @gwt.typeArgs <java.lang.String>
	 */
	List orderNos;

	List families = new ArrayList();

	/**
	 * @get.typeArgs <java.lang.String,java.lang.String>
	 */
	Map info;

	public AjaxOrderResponse(Map i) {
		info = i;
	}

	public AjaxOrderResponse() {

	}

	public Map getInfo() {
		return info;
	}

	public void setInfo(Map info) {
		this.info = info;
	}

	public List getOrderNos() {
		return orderNos;
	}

	public void setOrderNos(List orderNo) {
		this.orderNos = new ArrayList(orderNo);
	}

	public String toString() {
		return info.toString();
	}

	public void addFamily(String family) {
		families.add(family);
	}

	public List getFamilies() {
		return families;
	}

}
