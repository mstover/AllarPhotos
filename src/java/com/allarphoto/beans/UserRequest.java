package com.allarphoto.beans;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import strategiclibrary.service.sql.ObjectMappingService;

import com.allarphoto.application.SecurityModel;
import com.allarphoto.ecommerce.CommerceUser;
import com.allarphoto.ecommerce.UserGroup;
import com.allarphoto.server.UserService;
import com.allarphoto.utils.Resource;

public class UserRequest implements Serializable {

	Resource requestedResource;

	String username, email, firstName, lastName, company, phone, reason;
	String address1,address2,city,state,country,zip;

	int id;

	boolean newUser = false;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public UserRequest() {
	}

	public UserRequest(String username, Resource req) {
		this.username = username;
		requestedResource = req;
	}

	public UserRequest(String firstName, String lastName, String email,
			Resource res) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		requestedResource = res;
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

	public Resource getRequestedResource() {
		return requestedResource;
	}

	public void setRequestedResource(Resource requestedResource) {
		this.requestedResource = requestedResource;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isNewUser() {
		return newUser;
	}

	public void setNewUser(boolean newUser) {
		this.newUser = newUser;
	}

	public boolean reject(ObjectMappingService mapper) {
		delete(mapper);
		return true;
	}

	public boolean grant(ObjectMappingService mapper, UserService ugd,
			String group, SecurityModel perms) {
		CommerceUser u = ugd.getUser(getUsername());
		if (u == null) {
			u = ugd.getUserByEmail(getEmail());
		}
		if (u == null) {
			newUser = true;
			u = new CommerceUser();
			u.setFirstName(getFirstName());
			u.setLastName(getLastName());
			u.setEmailAddress(getEmail());
			u.setBillAddress1(getAddress1());
			u.setBillAddress2(getAddress2());
			u.setBillCity(new City(getCity()));
			u.setBillState(new State(getState()));
			u.setBillCountry(new Country(getCountry()));
			u.setBillZip(getZip());
			u.setPhone(getPhone());
			u.setCompany(new Company(getCompany()));
			if (getUsername() != null && getUsername().length() > 0)
				u.setUsername(getUsername());
			else {
				u.setUsername(getFirstName().substring(0, 1).toLowerCase()
						+ getLastName().toLowerCase());
				u.setPassword(ugd.createRandomPassword());
				int count = 1;
				while (ugd.getUser(u.getUsername()) != null)
					u.setUsername(u.getUsername() + (count++));
			}
			ugd.addUser(u, makeGroupList(group, ugd), perms);
			setUsername(u.getUsername());
		} else {
			ugd.addUserToGroup(u.getUsername(), group, perms);
		}
		setEmail(u.getEmailAddress());
		delete(mapper);
		return true;
	}

	private void delete(ObjectMappingService mapper) {
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("request", this);
		mapper.doUpdate("deleteUserRequest.sql", values);
	}

	private Collection<UserGroup> makeGroupList(String group, UserService ugd) {
		Collection<UserGroup> groups = new LinkedList<UserGroup>();
		groups.add(ugd.getGroup(group));
		return groups;
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
