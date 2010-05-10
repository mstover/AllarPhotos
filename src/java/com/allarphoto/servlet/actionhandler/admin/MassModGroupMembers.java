package com.allarphoto.servlet.actionhandler.admin;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.allarphoto.beans.User;
import com.allarphoto.client.beans.UserBean;
import com.allarphoto.ecommerce.UserGroup;
import com.allarphoto.server.UserService;
import com.allarphoto.servlet.actionhandler.ActionHandlerBase;

public class MassModGroupMembers extends ActionHandlerBase {

	public MassModGroupMembers() {
		super();
	}

	public void performAction(HandlerData info) throws ActionException {
		getLog().debug("Mass modifying group membership");
		int[] modGroups = info.getParameterValuesAsInt("mod_group_id", 0);
		int[] modUsers = info.getParameterValuesAsInt("mod_user_id", 0);
		UserService ugd = getUgd();
		UserBean user = this.getUserBean(info);
		if (modGroups != null && modGroups.length > 0) {
			int[] selectedUsers = info
					.getParameterValuesAsInt("add_user_id", 0);
			int[] unselectedUsers = info.getParameterValuesAsInt(
					"remove_user_id", 0);
			for (int groupId : modGroups) {
				UserGroup group = ugd.getGroup(groupId);
				for (int userId : selectedUsers) {
					User u = ugd.getUser(userId);
					ugd.addUserToGroup(u.getUsername(), group.getName(), user
							.getPermissions());
					addMessage("User " + u.getLastName() + ", "
							+ u.getFirstName() + " added to group "
							+ group.getName(), null, info);
				}
			}
			for (int groupId : modGroups) {
				UserGroup group = ugd.getGroup(groupId);
				for (int userId : unselectedUsers) {
					User u = ugd.getUser(userId);
					ugd.removeUserFromGroup(u.getUsername(), group.getName(),
							user.getPermissions());
					addMessage("User " + u.getLastName() + ", "
							+ u.getFirstName() + " removed from group "
							+ group.getName(), null, info);
				}
			}
		} else if (modUsers != null && modUsers.length > 0) {
			int[] selectedGroups = info.getParameterValuesAsInt("add_group_id",
					0);
			int[] unselectedGroups = info.getParameterValuesAsInt(
					"remove_group_id", 0);
			for (int userId : modUsers) {
				User u = ugd.getUser(userId);
				for (int groupId : selectedGroups) {
					UserGroup group = ugd.getGroup(groupId);
					ugd.addUserToGroup(u.getUsername(), group.getName(), user
							.getPermissions());
					addMessage("User " + u.getLastName() + ", "
							+ u.getFirstName() + " added to group "
							+ group.getName(), null, info);
				}
			}
			for (int userId : modUsers) {
				User u = ugd.getUser(userId);
				for (int groupId : unselectedGroups) {
					UserGroup group = ugd.getGroup(groupId);
					ugd.removeUserFromGroup(u.getUsername(), group.getName(),
							user.getPermissions());
					addMessage("User " + u.getLastName() + ", "
							+ u.getFirstName() + " removed from group "
							+ group.getName(), null, info);
				}
			}
		}
	}

	public String getName() {
		return "mod_group_members";
	}

}
