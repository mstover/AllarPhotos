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

// Title: LoginBean
// Author: Michael Stover
// Company: Lazer Inc.
package com.allarphoto.beans;

public class LoginBean {

	public LoginBean() {
	}

	public String getUsername() {
		return username;
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

	public boolean checkUser(com.allarphoto.dbtools.LoginDB database) {
		userID = database.checkUser(username, password);
		if (userID != -1)
			return true;
		else
			return false;
	}

	public int getUserID() {
		return userID;
	}

	/***************************************************************************
	 * Method checks if user is a valid logged-in user.
	 * 
	 * @return True if user is valid and logged in, false otherwise.
	 **************************************************************************/
	public boolean validUser() {
		if (userID != -1)
			return true;
		else
			return false;
	}

	private String username;

	private String password;

	private int userID;
}
