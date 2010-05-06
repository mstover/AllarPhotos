package com.lazerinc.ajaxclient.client.beans;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AjaxCompany implements IsSerializable {

	String name;

	String industry;

	public AjaxCompany() {
	}

	public AjaxCompany(String n, String i) {
		name = n;
		industry = i;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
