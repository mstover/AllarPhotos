package com.allarphoto.ajaxclient.client.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AjaxOrder implements IsSerializable {

	String date;

	String orderNo;

	AjaxAddress shippingAddress;

	String status;

	String merchant;

	AjaxUser user;

	boolean approve, confirm, fulfill;

	/**
	 * @gwt.typeArgs <com.allarphoto.ajaxclient.client.beans.AjaxOrderItem>
	 */
	List items = new ArrayList();

	/**
	 * @gwt.typeArgs <java.lang.String,java.lang.String>
	 * 
	 */
	Map values = new HashMap();

	public AjaxOrder() {
		// TODO Auto-generated constructor stub
	}

	public void addItem(AjaxOrderItem item) {
		items.add(item);
	}

	public void addValue(String k, String v) {
		values.put(k, v);
	}

	public String getValue(String k) {
		return (String) values.get(k);
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List getItems() {
		return items;
	}

	public void setItems(List items) {
		this.items = items;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public AjaxAddress getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(AjaxAddress shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public AjaxUser getUser() {
		return user;
	}

	public void setUser(AjaxUser user) {
		this.user = user;
	}

	public Map getValues() {
		return values;
	}

	public void setValues(Map values) {
		this.values = values;
	}

	public String getMerchant() {
		return merchant;
	}

	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}

	public boolean isApprove() {
		return approve;
	}

	public void setApprove(boolean approve) {
		this.approve = approve;
	}

	public boolean isConfirm() {
		return confirm;
	}

	public void setConfirm(boolean confirm) {
		this.confirm = confirm;
	}

	public boolean isFulfill() {
		return fulfill;
	}

	public void setFulfill(boolean fulfill) {
		this.fulfill = fulfill;
	}

}
