package com.lazerinc.ajaxclient.client.beans;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.lazerinc.ajaxclient.client.AjaxSystem;

public class AjaxUser implements IsSerializable {
	private String middleInitial;

	private String billPhone;

	private String billAddress1;

	private String billAddress2;

	private String billState;

	private String billCity;

	private String billZip;

	private String billCountry;

	private String shipPhone;

	private String shipAddress1;

	private String shipAddress2;

	private String shipState;

	private String shipCity;

	private String shipZip;

	private String shipCountry;

	private String referrer;

	protected String username;

	protected String password;

	protected String firstName;

	protected String lastName;

	protected String emailAddress;

	protected String phone;

	protected String fax;

	protected String addressLine1;

	protected String addressLine2;

	protected String city;

	protected String state;

	protected String zip;

	protected String company;

	private String expiration;

	String[] groups;

	public String[] getGroups() {
		return groups;
	}

	public void setGroups(String[] groups) {
		this.groups = groups;
	}

	public AjaxUser() {
	}

	public void addGroup(String group) {
		String[] newGroups = new String[groups.length + 1];
		AjaxSystem.arraycopy(groups, 0, newGroups, 0, groups.length);
		newGroups[newGroups.length - 1] = group;
		groups = newGroups;

	}

	public boolean belongsToGroup(String group) {
		return AjaxSystem.findIndex(groups, group) >= 0;
	}

	public void removeGroup(String group) {
		int index = AjaxSystem.findIndex(groups, group);
		removeGroup(index);
	}

	private void removeGroup(int index) {
		if (index >= 0) {
			String[] newGroups = new String[groups.length - 1];
			AjaxSystem.arraycopy(groups, 0, newGroups, 0, index - 0);
			if (index + 1 < groups.length)
				AjaxSystem.arraycopy(groups, index + 1, newGroups, index,
						groups.length - (index + 1));
			groups = newGroups;
		}
	}

	public void toggleGroupMembership(String group) {
		int index = AjaxSystem.findIndex(groups, group);
		if (index >= 0)
			removeGroup(index);
		else
			addGroup(group);
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public String getFullName() {
		return getLastName() + ", " + getFirstName();
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getBillAddress1() {
		return billAddress1;
	}

	public void setBillAddress1(String billAddress1) {
		this.billAddress1 = billAddress1;
	}

	public String getBillAddress2() {
		return billAddress2;
	}

	public void setBillAddress2(String billAddress2) {
		this.billAddress2 = billAddress2;
	}

	public String getBillCity() {
		return billCity;
	}

	public void setBillCity(String billCity) {
		this.billCity = billCity;
	}

	public String getBillCountry() {
		return billCountry;
	}

	public void setBillCountry(String billCountry) {
		this.billCountry = billCountry;
	}

	public String getBillPhone() {
		return billPhone;
	}

	public void setBillPhone(String billPhone) {
		this.billPhone = billPhone;
	}

	public String getBillState() {
		return billState;
	}

	public void setBillState(String billState) {
		this.billState = billState;
	}

	public String getBillZip() {
		return billZip;
	}

	public void setBillZip(String billZip) {
		this.billZip = billZip;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleInitial() {
		return middleInitial;
	}

	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getReferrer() {
		return referrer;
	}

	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}

	public String getShipAddress1() {
		return shipAddress1;
	}

	public void setShipAddress1(String shipAddress1) {
		this.shipAddress1 = shipAddress1;
	}

	public String getShipAddress2() {
		return shipAddress2;
	}

	public void setShipAddress2(String shipAddress2) {
		this.shipAddress2 = shipAddress2;
	}

	public String getShipCity() {
		return shipCity;
	}

	public void setShipCity(String shipCity) {
		this.shipCity = shipCity;
	}

	public String getShipCountry() {
		return shipCountry;
	}

	public void setShipCountry(String shipCountry) {
		this.shipCountry = shipCountry;
	}

	public String getShipPhone() {
		return shipPhone;
	}

	public void setShipPhone(String shipPhone) {
		this.shipPhone = shipPhone;
	}

	public String getShipState() {
		return shipState;
	}

	public void setShipState(String shipState) {
		this.shipState = shipState;
	}

	public String getShipZip() {
		return shipZip;
	}

	public void setShipZip(String shipZip) {
		this.shipZip = shipZip;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getExpiration() {
		return expiration;
	}

	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}

}
