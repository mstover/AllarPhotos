/*******************************************************************************
 * Copyright (C) 1999 Michael Stover This program is free software; you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version. This program is distributed
 * in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details. You should have received
 * a copy of the GNU General Public License along with this program; if not,
 * write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA. Michael Stover can be reached via email at
 * mstover1@rochester.rr.com or via snail mail at 130 Corwin Rd. Rochester, NY
 * 14610 The following exception to this license exists for Lazer Incorporated:
 * Lazer Inc is excepted from all limitations and requirements stipulated in the
 * GPL. Lazer Inc. is the only entity allowed this limitation. Lazer does have
 * the right to sell this exception, if they choose, but they cannot grant
 * additional exceptions to any other entities.
 ******************************************************************************/

// Title: User
// Author: Michael Stover
// Company: Lazer Inc.
package com.allarphoto.beans;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.coinjema.context.CoinjemaDynamic;

import strategiclibrary.service.cache.CacheService;

import com.allarphoto.cached.DatabaseObject;
import com.allarphoto.ecommerce.UserGroup;
import com.allarphoto.utils.Functions;

public class User implements Serializable, Comparable<User>, DatabaseObject {
	private static final long serialVersionUID = 1;

	public User() {
		properties = new HashMap();
	}

	protected String username = com.allarphoto.dbtools.DBConnect.DEFAULT;

	protected String password = com.allarphoto.dbtools.DBConnect.DEFAULT;

	protected String firstName = com.allarphoto.dbtools.DBConnect.DEFAULT;

	protected String lastName = com.allarphoto.dbtools.DBConnect.DEFAULT;

	protected String emailAddress = com.allarphoto.dbtools.DBConnect.DEFAULT;

	protected String phone = com.allarphoto.dbtools.DBConnect.DEFAULT;

	protected String fax = com.allarphoto.dbtools.DBConnect.DEFAULT;

	protected String addressLine1 = com.allarphoto.dbtools.DBConnect.DEFAULT;

	protected String addressLine2 = com.allarphoto.dbtools.DBConnect.DEFAULT;

	protected City city = new City("");

	protected State state = new State("");

	protected String zip = com.allarphoto.dbtools.DBConnect.DEFAULT;

	protected String passwordConfirm = com.allarphoto.dbtools.DBConnect.DEFAULT;

	protected com.allarphoto.beans.Company company = new Company();

	protected int userID;

	protected Map properties;

	public String getUsername() {
		return username;
	}

	public String getProperty(String key) {
		return (String) properties.get(key);
	}

	public void setProperty(String key, Object prop) {
		if (key != null)
			key = key.trim();
		properties.put(key, prop);
	}

	public String getFullName() {
		return getLastName() + ", " + getFirstName();
	}

	public Iterator propertyNameIterator() {
		return properties.keySet().iterator();
	}

	public void setUsername(String newUsername) {
		username = newUsername;
	}

	public void setPassword(String newPassword) {
		password = newPassword;
	}

	public String getPassword() {
		return password;
	}

	public int getId() {
		return getUserID();
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

	public void setEmailAddress(String newEmailAddress) {
		emailAddress = newEmailAddress;
	}

	public String getEmailAddress() {
		return emailAddress;
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

	public void setAddressLine1(String newAddressLine1) {
		addressLine1 = newAddressLine1;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine2(String newAddressLine2) {
		addressLine2 = newAddressLine2;
	}

	public String getAddressLine2() {
		return addressLine2;
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

	public void setZip(String newZip) {
		zip = newZip;
	}

	public String getZip() {
		return zip;
	}

	public void setPasswordConfirm(String newPasswordConfirm) {
		passwordConfirm = newPasswordConfirm;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setUserID(int newUserID) {
		userID = newUserID;
	}

	public int getUserID() {
		return userID;
	}

	void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
	}

	void readObject(ObjectInputStream ois) throws ClassNotFoundException,
			IOException {
		ois.defaultReadObject();
	}

	public void setCompany(com.allarphoto.beans.Company newCompany) {
		company = newCompany;
	}

	public com.allarphoto.beans.Company getCompany() {
		return company;
	}

	public Collection<UserGroup> getGroups() {
		return getGroupCache().getCache(UserGroup.class).getCachedObjects(
				"user", this);
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result
				+ ((username == null) ? 0 : username.hashCode());
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
		final User other = (User) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	public int compareTo(User o) {
		int c = getLastName().toUpperCase().compareTo(
				o.getLastName().toUpperCase());
		if (c == 0)
			c = getFirstName().toUpperCase().compareTo(
					o.getFirstName().toUpperCase());
		if (c == 0)
			return getUserID() - o.getUserID();
		return c;
	}

	@CoinjemaDynamic(type = "cacheService")
	public CacheService getGroupCache() {
		return null;
	}

	/***************************************************************************
	 * Encrypts a string.
	 * 
	 * @param input
	 *            String to be enrypted.
	 * @return The encrypted string.
	 **************************************************************************/
	public static String encrypt(String input) {
		if (input == null || input.length() == 0)
			return "";
		String[] chars = new String[input.length()];
		StringBuffer number = new StringBuffer("");
		StringBuffer retVal = new StringBuffer("");
		char start;
		int count = -1;
		while (++count < input.length())
			chars[count] = Integer.toString((int) input.charAt(count));
		count = -1;
		while (++count < chars.length) {
			chars[count] = User.baseConvert(chars[count], 10, 8);
			if (chars[count].length() == 2)
				chars[count] = "00" + chars[count];
			else if (chars[count].length() == 3)
				chars[count] = "0" + chars[count];
		}
		count = -1;
		while (++count < chars.length)
			number.append(chars[count]);
		start = number.charAt(0);
		for (count = 3; count < number.length(); count += 3)
			number.setCharAt(count - 3, number.charAt(count));
		number.setCharAt(count - 3, start);
		chars = new String[number.length() / 2];
		for (count = 0; (count * 2 + 3) < number.length(); count++)
			chars[count] = number.toString().substring(count * 2 + 1,
					count * 2 + 3);
		chars[count] = number.toString()
				.substring(count * 2 + 1, count * 2 + 2)
				+ number.toString().substring(0, 1);
		count = -1;
		while (++count < chars.length)
			retVal.append((char) (32 + Integer.parseInt(chars[count])));
		return retVal.toString();
	}

	/***************************************************************************
	 * Decrypts a string.
	 * 
	 * @param input
	 *            String to be decrypted.
	 * @return The decrypted string.
	 **************************************************************************/
	public static String decrypt(String input) {
		if (input == null || input.length() == 0)
			return "";
		String[] chars = new String[input.length()];
		StringBuffer retVal = new StringBuffer("");
		StringBuffer number = new StringBuffer("");
		char end, start;
		for (int count = 0; count < input.length(); count++) {
			chars[count] = Integer.toString(((int) input.charAt(count)) - 32);
			if (chars[count].length() == 1)
				chars[count] = "0" + chars[count];
		}
		number.append(chars[chars.length - 1].substring(1, 2));
		for (int count = 0; count < chars.length - 1; count++)
			number.append(chars[count]);
		number.append(chars[chars.length - 1].substring(0, 1));
		end = number.charAt(0);
		for (int count = 0; count + 3 < number.length(); count += 3) {
			start = number.charAt(count + 3);
			number.setCharAt(count + 3, end);
			end = start;
		}
		number.setCharAt(0, end);
		chars = new String[number.length() / 4];
		for (int count = 0; (count * 4 + 4) <= number.length(); count++)
			chars[count] = number.toString()
					.substring(count * 4, count * 4 + 4);
		for (int count = 0; count < chars.length; count++)
			chars[count] = User.baseConvert(chars[count], 8, 10);
		for (int count = 0; count < chars.length; count++)
			retVal.append((char) Integer.parseInt(chars[count]));
		return retVal.toString();
	}

	/***************************************************************************
	 * Converts a given number from a given base to a different given base.
	 * 
	 * @param digits
	 *            Number to be converted.
	 * @param fromBase
	 *            Base to convert from.
	 * @param toBase
	 *            Base to convert to.
	 * @return Resulting int.
	 **************************************************************************/
	private static String baseConvert(String digits, int fromBase, int toBase) {
		int temp, num;
		StringBuffer retVal = new StringBuffer("");
		int place = 0;
		int count = digits.length();
		num = 0;
		if (fromBase != 10) {
			while (--count > -1) {
				temp = Integer.parseInt(digits.substring(count, count + 1));
				num += temp * Math.pow(fromBase, place);
				place++;
			}
		} else
			num = Integer.parseInt(digits);
		if (toBase != 10) {
			place = 1;
			while (num > 0) {
				temp = toBase;
				retVal.append("" + num % temp);
				num -= num % temp;
				num /= temp;
			}
			retVal.reverse();
		} else
			retVal = new StringBuffer("" + num);
		return retVal.toString();
	}

	public String getEncryptedPassword() {
		return encrypt(getPassword());
	}

	public void setEncryptedPassword(String encrypted) {
		setPassword(decrypt(encrypted));
	}
}
