package com.lazerinc.beans;

import java.io.Serializable;

import com.lazerinc.cached.DatabaseObject;

public class Address implements Serializable, Comparable<Address>,
		DatabaseObject {
	private static final long serialVersionUID = 1;

	City city;

	State state;

	Country country;

	Company company;

	boolean inUse = true;

	String address1 = "", address2 = "", zip = "", phone = "", fax = "",
			attn = "";

	int id;

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = (address1 != null ? address1.trim() : "");
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = (address2 != null ? address2.trim() : "");
	}

	public String getAttn() {
		return attn;
	}

	public void setAttn(String attn) {
		this.attn = (attn != null ? attn.trim() : "");
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = (fax != null ? fax.trim() : "");
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = (phone != null ? phone.trim() : "");
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = (zip != null ? zip.trim() : "");
	}

	public Address() {
		super();
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((city == null) ? 0 : city.hashCode());
		result = PRIME * result + ((state == null) ? 0 : state.hashCode());
		result = PRIME * result + ((country == null) ? 0 : country.hashCode());
		result = PRIME * result + ((company == null) ? 0 : company.hashCode());
		result = PRIME * result
				+ ((address1 == null) ? 0 : address1.hashCode());
		result = PRIME * result
				+ ((address2 == null) ? 0 : address2.hashCode());
		result = PRIME * result + ((zip == null) ? 0 : zip.hashCode());
		result = PRIME * result + ((phone == null) ? 0 : phone.hashCode());
		result = PRIME * result + ((fax == null) ? 0 : fax.hashCode());
		result = PRIME * result + ((attn == null) ? 0 : attn.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Address other = (Address) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.equals(other.company))
			return false;
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
		if (zip == null) {
			if (other.zip != null)
				return false;
		} else if (!zip.equals(other.zip))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (fax == null) {
			if (other.fax != null)
				return false;
		} else if (!fax.equals(other.fax))
			return false;
		if (attn == null) {
			if (other.attn != null)
				return false;
		} else if (!attn.equals(other.attn))
			return false;
		return true;
	}

	public int compareTo(Address o) {
		if (o == null)
			return -1;
		int ret = getAttn() != null ? getAttn().compareTo(o.getAttn()) : 0;
		if (ret == 0)
			ret = getAddress1() != null ? getAddress1().compareTo(
					o.getAddress1()) : 0;
		if (ret == 0)
			ret = getAddress2() != null ? getAddress2().compareTo(
					o.getAddress2()) : 0;
		if (ret == 0)
			ret = getCity() != null ? getCity().compareTo(o.getCity()) : 0;
		if (ret == 0)
			ret = getState() != null ? getState().compareTo(o.getState()) : 0;
		return ret;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer("Address[");
		if (getId() > 0)
			buf.append(getId()).append(":");
		buf.append(getAttn()).append("\n").append(getAddress1()).append("\n");
		buf.append(getAddress2()).append("\n").append(getCity()).append(", ");
		buf.append(getZip()).append(" ").append(getState()).append(" ").append(
				getCountry());
		buf.append("\n").append(getPhone()).append("\n").append(getCompany());
		buf.append("]");
		return buf.toString();
	}

	public boolean isInUse() {
		return inUse;
	}

	public void setInUse(boolean inUse) {
		this.inUse = inUse;
	}

}
