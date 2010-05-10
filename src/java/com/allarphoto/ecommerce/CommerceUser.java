/***********************************************************************************************************************
 * Copyright 1999 (c) Lazer Incorporated All code contained in this file is the property of Lazer Incorporated. All
 * rights reserved.
 **********************************************************************************************************************/

package com.allarphoto.ecommerce;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.allarphoto.beans.Address;
import com.allarphoto.beans.City;
import com.allarphoto.beans.Country;
import com.allarphoto.beans.Referrer;
import com.allarphoto.beans.State;
import com.allarphoto.beans.User;

public class CommerceUser extends User implements Serializable {
	private static final long serialVersionUID = 1;

	public CommerceUser() {
	}

	public boolean verify() {
		if (username != null && username.length() > 5 && password != null
				&& password.length() > 5 && password.equals(passwordConfirm)
				&& billAddress1 != null && billAddress1.length() > 1
				&& billCity != null && billCity.getName().length() > 1
				&& billState != null && billState.getName().length() > 1
				&& billZip != null && billZip.length() > 1
				&& billCountry != null && billCountry.getName().length() > 1
				&& firstName != null && firstName.length() > 1
				&& lastName != null && lastName.length() > 1
				&& emailAddress != null && emailAddress.indexOf("@") > 0
				&& phone != null && phone.length() > 1 && billAddress1 != null
				&& billAddress1.length() > 1 && billCity != null
				&& billCity.getName().length() > 1 && billState != null
				&& billState.getName().length() > 1 && billZip != null
				&& billZip.length() > 1 && billCountry != null
				&& billCountry.getName().length() > 1)
			return true;
		else
			return false;
	}

	void readObject(ObjectInputStream ois) throws ClassNotFoundException,
			IOException {
		ois.defaultReadObject();
	}

	void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
	}

	public void setMiddleInitial(String newMiddleInitial) {
		middleInitial = newMiddleInitial;
	}

	public Address getShippingAddress() {
		Address addr = new Address();
		addr.setAddress1(getShipAddress1());
		addr.setAddress2(getShipAddress2());
		addr.setAttn(getFirstName() + " " + getMiddleInitial() + " "
				+ getLastName());
		addr.setCity(getShipCity());
		addr.setCompany(getCompany());
		addr.setCountry(getShipCountry());
		addr.setState(getShipState());
		addr.setPhone(getShipPhone());
		addr.setZip(getShipZip());
		return addr;
	}

	public String getMiddleInitial() {
		return middleInitial;
	}

	public void setBillPhone(String newBillPhone) {
		billPhone = newBillPhone;
	}

	public String getBillPhone() {
		return billPhone;
	}

	public void setBillAddress1(String newBillAddress1) {
		billAddress1 = newBillAddress1;
	}

	public String getBillAddress1() {
		return billAddress1;
	}

	public void setBillAddress2(String newBillAddress2) {
		billAddress2 = newBillAddress2;
	}

	public String getBillAddress2() {
		return billAddress2;
	}

	public void setBillState(State newBillState) {
		billState = newBillState;
	}

	public State getBillState() {
		return billState;
	}

	public void setBillCity(City newBillCity) {
		billCity = newBillCity;
	}

	public City getBillCity() {
		return billCity;
	}

	public void setBillZip(String newBillZip) {
		billZip = newBillZip;
	}

	public String getBillZip() {
		return billZip;
	}

	public void setBillCountry(Country newBillCountry) {
		billCountry = newBillCountry;
	}

	public Country getBillCountry() {
		return billCountry;
	}

	public void setShipPhone(String newShipPhone) {
		shipPhone = newShipPhone;
	}

	public String getShipPhone() {
		return shipPhone;
	}

	public void setShipAddress1(String newShipAddress1) {
		shipAddress1 = newShipAddress1;
	}

	public String getShipAddress1() {
		return shipAddress1;
	}

	public void setShipAddress2(String newShipAddress2) {
		shipAddress2 = newShipAddress2;
	}

	public String getShipAddress2() {
		return shipAddress2;
	}

	public void setShipState(State newShipState) {
		shipState = newShipState;
	}

	public State getShipState() {
		return shipState;
	}

	public void setShipCity(City newShipCity) {
		shipCity = newShipCity;
	}

	public City getShipCity() {
		return shipCity;
	}

	public void setShipZip(String newShipZip) {
		shipZip = newShipZip;
	}

	public String getShipZip() {
		return shipZip;
	}

	public void setShipCountry(Country newShipCountry) {
		shipCountry = newShipCountry;
	}

	public Country getShipCountry() {
		return shipCountry;
	}

	public void setReferrer(Referrer newReferrer) {
		referrer = newReferrer;
	}

	public Referrer getReferrer() {
		return referrer;
	}

	public void setExpDate(Calendar d) {
		expDate = d;
	}

	public void setExpDate(java.util.Date d) {
		if (d != null) {
			expDate = new GregorianCalendar();
			expDate.setTime(d);
		} else
			expDate = null;
	}

	public Calendar getExpDate() {
		return expDate;
	}

	private Calendar expDate;

	private String middleInitial;

	private String billPhone;

	private String billAddress1;

	private String billAddress2;

	private State billState;

	private City billCity = new City("");

	private String billZip;

	private Country billCountry = new Country("");

	private String shipPhone;

	private String shipAddress1;

	private String shipAddress2;

	private State shipState = new State("");

	private City shipCity = new City("");

	private String shipZip;

	private Country shipCountry = new Country("");

	private Referrer referrer = new Referrer();
}
