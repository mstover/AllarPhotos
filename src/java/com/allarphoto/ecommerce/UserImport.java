// Title: Commerce
// Version:
// Copyright: Copyright (c) 1999
// Author: Michael Stover
// Company: Lazer inc.
// Description: Your description
package com.allarphoto.ecommerce;

import java.util.Arrays;
import java.util.Properties;

import org.coinjema.context.CoinjemaContext;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

import com.allarphoto.dbtools.DBConnect;
import com.allarphoto.dbtools.FirebirdDB;
import com.allarphoto.server.UserService;
import com.allarphoto.utils.Data;
import com.allarphoto.utils.Resource;

@CoinjemaObject
public class UserImport {
	UserService ugd;

	Properties props = new Properties();

	String group;

	DatabaseUtilities newDbUtil;

	DBConnect oldDatabase;

	DBConnect newDatabase;

	public UserImport() {
	}

	private void init() {

		oldDatabase = new FirebirdDB(new CoinjemaContext("old"));
		newDatabase = new FirebirdDB(new CoinjemaContext("new"));
	}

	public static void main(String[] args) {
		UserImport userImport = new UserImport();
		boolean success = true;
		userImport.init();
		if (success == false) {
			System.out
					.println("This class imports users of a given group from the old Lazerweb "
							+ "to the new Lazerweb database");
			System.out
					.println("Invalid command line arguments.  To use this class, please "
							+ "use the following syntax:");
			System.out
					.println("UserImport -old <old datasource> <old username> <old password> "
							+ "-new <new datasource> <new username> <new password> "
							+ "-group <group name>");
		} else
			userImport.importUsers();
	}

	public void importUsers() {
		String[] oldTables = { "users", "groups", "membership" };
		String[] cols = { "username", "passwrd" };
		String where = "users.user_id=membership.user_id AND groups.group_id=membership.group_id "
				+ "AND group_name='" + group + "'";
		Data data = oldDatabase.select(oldTables, cols, where);
		int groupID = addGroup(newDbUtil, group, newDatabase);
		System.out.println("groupID = " + groupID + " group=" + group);
		CommerceUser user;
		data.reset();
		while (data.next()) {
			user = new CommerceUser();
			user.setUsername((String) data.getColumnValue("username"));
			user.setPassword((String) data.getColumnValue("passwrd"));
			System.out.println("username = " + user.getUsername());
			getUgd()
					.addUser(
							user,
							Arrays.asList(new UserGroup[] { getUgd().getGroup(
									group) }), null);
		}
	}

	public int addGroup(DatabaseUtilities utils, String groupName,
			DBConnect database) {
		int groupID = utils.getGroupID(groupName);
		if (groupID == -1) {
			String table = "groups";
			String[] cols = { "name" };
			String[] vals = { database.cleanString(groupName) };
			database.insert("groups", cols, vals);
			utils.addResource(groupName, Resource.GROUP);
			groupID = utils.getGroupID(groupName);
		}
		return groupID;
	}

	private boolean invokedStandalone = false;

	protected UserService getUgd() {
		return ugd;
	}

	@CoinjemaDependency(type = "userService", method = "userService")
	public void setUgd(UserService ugd) {
		this.ugd = ugd;
	}

	@CoinjemaDependency(type = "dbutil", method = "dbutil")
	public void setDbUtil(DatabaseUtilities du) {
		newDbUtil = du;
	}
}
