package com.allarphoto.ajaxclient.client.beans;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AjaxProductField implements IsSerializable {

	String name, type, family;

	int searchOrder, displayOrder;

	public AjaxProductField() {

	}

	public AjaxProductField(String n, String f, int t, int so, int diso) {
		name = n;
		family = f;
		type = getStringType(t);
		searchOrder = so;
		displayOrder = diso;
	}

	public String getStringType(int t) {
		String typ = "";
		switch (t) {
		case 1:
			typ = "Category";
			break;
		case 2:
			typ = "Description";
			break;
		case 9:
			typ = "Expired";
			break;
		case 8:
			typ = "Numerical";
			break;
		case 6:
			typ = "Primary";
			break;
		case -1:
			typ = "Protected";
			break;
		case 11:
			typ = "Tag";
			break;
		}
		return typ;
	}

	public int getTypeOf() {
		if (type.equals("Category"))
			return 1;
		else if (type.equals("Description"))
			return 2;
		else if (type.equals("Expired"))
			return 9;
		else if (type.equals("Numerical"))
			return 8;
		else if (type.equals("Primary"))
			return 6;
		else if (type.equals("Protected"))
			return -1;
		else if (type.equals("Tag"))
			return 11;
		else
			return 0;
	}

	public AjaxProductField(String n, String f, String t, int so, int diso) {
		name = n;
		family = f;
		type = t;
		searchOrder = so;
		displayOrder = diso;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSearchOrder() {
		return searchOrder;
	}

	public void setSearchOrder(int searchOrder) {
		this.searchOrder = searchOrder;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
