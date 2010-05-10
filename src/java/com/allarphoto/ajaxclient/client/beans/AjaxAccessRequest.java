package com.allarphoto.ajaxclient.client.beans;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AjaxAccessRequest implements IsSerializable {
	String username, firstName, lastName, email, company, phone, reason;
	String address1,address2,city,state,country,zip;

	AjaxResource resource;

	String resourceDescription;

	int id;

	String[] groups;

	public AjaxAccessRequest() {
	}

	public AjaxAccessRequest(String username, AjaxResource res) {
		setUsername(username);
		setResource(res);
	}

	public AjaxAccessRequest(String firstName, String lastName, String email,
			AjaxResource res) {
		setFirstName(firstName);
		setLastName(lastName);
		setEmail(email);
		setResource(res);
	}

	public AjaxAccessRequest(String username, String firstName,
			String lastName, String email, AjaxResource res) {
		this(firstName, lastName, email, res);
		setUsername(username);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public AjaxResource getResource() {
		return resource;
	}

	public void setResource(AjaxResource resource) {
		this.resource = resource;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getResourceDescription() {
		return resourceDescription;
	}

	public void setResourceDescription(String resourceDescription) {
		this.resourceDescription = resourceDescription;
	}

	public String[] getGroups() {
		return groups;
	}

	public void setGroups(String[] groups) {
		this.groups = groups;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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

}
