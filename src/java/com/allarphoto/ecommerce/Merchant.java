/***********************************************************************************************************************
 * Copyright 1999 (c) Lazer Incorporated All code contained in this file is the property of Lazer Incorporated. All
 * rights reserved.
 **********************************************************************************************************************/

package com.lazerinc.ecommerce;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;

import com.lazerinc.beans.City;
import com.lazerinc.beans.Country;
import com.lazerinc.beans.State;

public class Merchant implements Serializable {
	private static final long serialVersionUID = 1;

	public Merchant() {
	}

	void readObject(ObjectInputStream ois) throws ClassNotFoundException,
			IOException {
		ois.defaultReadObject();
	}

	void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
	}

	public void setMerchantID(String newMerchantID) {
		merchantID = newMerchantID;
	}

	public String getMerchantID() {
		return merchantID;
	}

	public void setName(String newName) {
		name = newName;
	}

	public String getName() {
		return name;
	}

	public void setOrderingEmail(String newOrderingEmail) {
		orderingEmail = newOrderingEmail;
	}

	public String getOrderingEmail() {
		return orderingEmail;
	}

	public void setPhone(String newPhone) {
		phone = newPhone;
	}

	public String getPhone() {
		return phone;
	}

	public void setFax(String newFax) {
		fax = newFax;
	}

	public String getFax() {
		return fax;
	}

	public void setAddress1(String newAddress1) {
		address1 = newAddress1;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress2(String newAddress2) {
		address2 = newAddress2;
	}

	public String getAddress2() {
		return address2;
	}

	public void setCity(City newCity) {
		city = newCity;
	}

	public City getCity() {
		return city;
	}

	public void setState(State newState) {
		state = newState;
	}

	public State getState() {
		return state;
	}

	public void setCountry(Country newCountry) {
		country = newCountry;
	}

	public Country getCountry() {
		return country;
	}

	public void setCreditCards(int[] newCreditCards) {
		creditCards = newCreditCards;
	}

	public int[] getCreditCards() {
		return creditCards;
	}

	public void setFulfillmentEmail(String newFulfillmentEmail) {
		fulfillmentEmail = newFulfillmentEmail;
	}

	public String getFulfillmentEmail() {
		return fulfillmentEmail;
	}

	public void setOrderProcessing(int newOrderProcessing) {
		orderProcessing = newOrderProcessing;
	}

	public int getOrderProcessing() {
		return orderProcessing;
	}

	public void setZip(String newZip) {
		zip = newZip;
	}

	public String getZip() {
		return zip;
	}

	public void setMerchantModelClass(String newMerchantModelClass) {
		merchantModelClass = newMerchantModelClass;
	}

	public String getMerchantModelClass() {
		return merchantModelClass;
	}

	public void setTaxRate(float newTaxRate) {
		taxRate = newTaxRate;
	}

	public float getTaxRate() {
		return taxRate;
	}

	public void addTaxedState(String state) {
		taxedStates.add(state);
	}

	public boolean isTaxedState(String state) {
		return taxedStates.contains(state);
	}

	public void setProductFamilies(String[] newProductFamilies) {
		productFamilies = newProductFamilies;
	}

	public String[] getProductFamilies() {
		return productFamilies;
	}

	private String merchantID;

	private String name;

	private String orderingEmail;

	private String phone;

	private String fax;

	private String address1;

	private String address2;

	private City city;

	private State state;

	private Country country;

	private String fulfillmentEmail;

	private int orderProcessing;

	private int[] creditCards;

	private String zip;

	public static final int MANUAL = 1;

	public static final int AUTO = 2;

	public static final int VISA = 3;

	public static final int MASTERCARD = 4;

	public static final int DISCOVER = 5;

	public static final int AMEX = 6;

	private String merchantModelClass;

	private float taxRate;

	private java.util.Set taxedStates = new HashSet();

	private String[] productFamilies;
}
