package com.lazerinc.ajaxclient.client.beans;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AjaxGroup implements IsSerializable {
	String name;

	String description;

	AjaxPermissions perms = new AjaxPermissions();

	public AjaxGroup() {
	}

	public AjaxGroup(String n, String d) {
		name = n;
		description = d;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void addRight(AjaxResource r, AjaxRights rights) {
		perms.addRight(r, rights);
	}

	public AjaxResource[] getResources(int type) {
		return perms.getResources(type);
	}

	public AjaxRights getRights(AjaxResource r) {
		return perms.getRights(r);
	}

	public boolean hasAnyRight(AjaxResource res) {
		return perms.hasAnyRight(res);
	}

	public boolean hasPermission(AjaxResource r, String right) {
		return perms.hasPermission(r, right);
	}

	public void updateRight(String right, AjaxResource res, boolean updateValue) {
		perms.updateRight(right, res, updateValue);
	}

}
