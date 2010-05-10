package com.allarphoto.ajaxclient.client.beans;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.HTML;

public class AjaxAddress implements IsSerializable {

	public String address1, address2, city, state, country, zip, attn, phone;

	public AjaxCompany company;

	boolean inUse;

	int id;

	public AjaxAddress() {
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAttn() {
		return attn;
	}

	public void setAttn(String attn) {
		this.attn = attn;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public AjaxCompany getCompany() {
		return company;
	}

	public void setCompany(AjaxCompany company) {
		this.company = company;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String toString() {
		return getAttn() + ": " + getAddress1() + " " + getAddress2() + " "
				+ getCity() + ", " + getState() + " " + getZip() + " "
				+ getCountry();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result
				+ ((address1 == null) ? 0 : address1.hashCode());
		result = PRIME * result
				+ ((address2 == null) ? 0 : address2.hashCode());
		result = PRIME * result + ((attn == null) ? 0 : attn.hashCode());
		result = PRIME * result + ((city == null) ? 0 : city.hashCode());
		result = PRIME * result
				+ ((company == null) ? 0 : company.getName().hashCode());
		result = PRIME * result + ((country == null) ? 0 : country.hashCode());
		result = PRIME * result + ((phone == null) ? 0 : phone.hashCode());
		result = PRIME * result + ((state == null) ? 0 : state.hashCode());
		result = PRIME * result + ((zip == null) ? 0 : zip.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		final AjaxAddress other = (AjaxAddress) obj;
		if (address1 == null) {
			if (other.address1 != null)
				return false;
		} else if (!address1.equals(other.address1))
			return false;
		if (address2 == null) {
			if (other.address2 != null)
				return false;
		} else if (!address2.equals(other.address2))
			return false;
		if (attn == null) {
			if (other.attn != null)
				return false;
		} else if (!attn.equals(other.attn))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.getName().equals(other.company.getName()))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (zip == null) {
			if (other.zip != null)
				return false;
		} else if (!zip.equals(other.zip))
			return false;
		return true;
	}

	public HTML toHTML() {
		StringBuffer buf = new StringBuffer();
		if (getAttn() != null)
			buf.append(getAttn()).append("<br>");
		if (getCompany() != null)
			buf.append(getCompany().getName()).append("<br>");
		if (getAddress1() != null)
			buf.append(getAddress1()).append("<br>");
		if (getAddress2() != null && getAddress2().length() > 0
				&& !getAddress2().equals("N/A"))
			buf.append(getAddress2()).append("<br>");
		if (getCity() != null)
			buf.append(getCity()).append(", ");
		if (getState() != null)
			buf.append(getState()).append(" ");
		if (getZip() != null)
			buf.append(getZip()).append("<br>");
		if (getCountry() != null)
			buf.append(getCountry());
		return new HTML(buf.toString());
	}

	public boolean isInUse() {
		return inUse;
	}
	
	public boolean isValid() {
		return address1 != null && address1.length() > 0 && city != null && city.length() > 0
				&& country != null && country.length() > 0 && zip != null && zip.length() > 0 &&
				state != null && state.length() > 0 && attn != null && attn.length() > 0;
	}
	
	public String getInvalidMessage() {
		StringBuffer buf = new StringBuffer("Please include a valid <br>");
		if(address1 == null || address1.length() <= 0) buf.append("Street Address<br>");
		if(city == null || city.length() <= 0) buf.append("City<br>");
		if(country == null || country.length() <= 0) buf.append("Country<br>");
		if(zip == null || zip.length() <= 0) buf.append("Postal Code<br>");
		if(state == null || state.length() <= 0) buf.append("State or Province<br>");
		if(attn == null || attn.length() <= 0) buf.append("Recipient Name<br>");
		buf.append("in your address");
		return buf.toString();		
	}

	public void setInUse(boolean inUse) {
		this.inUse = inUse;
	}

}
