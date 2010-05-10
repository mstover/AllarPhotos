/***********************************************************************************************************************
 * Copyright 1999 (c) Lazer Incorporated All code contained in this file is the property of Lazer Incorporated. All
 * rights reserved.
 **********************************************************************************************************************/

package com.allarphoto.ecommerce;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class CreditCard implements Serializable {
	private static final long serialVersionUID = 1;

	public CreditCard() {
	}

	void readObject(ObjectInputStream ois) throws ClassNotFoundException,
			IOException {
		ois.defaultReadObject();
	}

	void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
	}

	public void setId(int newId) {
		id = newId;
	}

	public int getId() {
		return id;
	}

	public String getTypeAsString() {
		switch (type) {
		case 1:
			return "Visa";
		case 2:
			return "MasterCard";
		case 3:
			return "Discover";
		case 4:
			return "American Express";
		}
		return "";
	}

	public void setType(int newType) {
		type = newType;
	}

	public int getType() {
		return type;
	}

	public void setNumber(String newNumber) {
		number = newNumber;
	}

	public String getNumber() {
		return number;
	}

	public void setExpDate(java.util.GregorianCalendar newExpDate) {
		expDate = newExpDate;
	}

	public java.util.GregorianCalendar getExpDate() {
		return expDate;
	}

	public void setFirstName(String newFirstName) {
		firstName = newFirstName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setLastName(String newLastName) {
		lastName = newLastName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setMiddleInitial(String newMiddleInitial) {
		middleInitial = newMiddleInitial;
	}

	public String getMiddleInitial() {
		return middleInitial;
	}

	private int id;

	private String number;

	private java.util.GregorianCalendar expDate;

	private String firstName;

	private String lastName;

	private String middleInitial;

	public static int VISA = 1;

	public static int MASTERCARD = 2;

	public static int DISCOVER = 3;

	public static int AMEX = 4;

	private int type;
}
