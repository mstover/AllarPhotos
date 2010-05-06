package com.lazerinc.servlet.actionhandler.user;

import static com.lazerinc.servlet.ActionConstants.ACTION_MODIFY_USER;

import java.util.HashSet;
import java.util.Set;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.lazerinc.client.beans.UserBean;
import com.lazerinc.client.exceptions.FatalException;
import com.lazerinc.client.exceptions.InformationalException;
import com.lazerinc.client.exceptions.LazerwebException;
import com.lazerinc.ecommerce.CommerceUser;
import com.lazerinc.ecommerce.UserGroup;
import com.lazerinc.server.UserService;
import com.lazerinc.servlet.actionhandler.ActionHandlerBase;
import com.lazerinc.utils.Functions;
import com.lazerinc.utils.Resource;
import com.lazerinc.utils.WebBean;

/**
 * Allows the information about the current user to be modified.
 * 
 * @author Administrator
 * @action action_modify_user
 * @requestParam none There are no specific request parameters, but any
 *               parameters available whose names match properties of the User
 *               object will be set in the user and saved to the database.
 * @bean UserBean user The users information is updated.
 */
public class ModifyUser extends ActionHandlerBase {

	/**
	 * Modifies the current user from request parameters.
	 */
	public void performAction(HandlerData actionInfo) throws FatalException,
			InformationalException, ActionException {
		UserService ugd = getUgd();
		UserBean user = getUserBean(actionInfo);
		CommerceUser modify = ugd.getUser(actionInfo.getParameterAsInt(
				"userID", user.getUser().getUserID()));
		if (user.getPermissions().getPermission(modify.getUsername(),
				Resource.USER, "admin")) {
			String passwd = modify.getPassword();
			WebBean.setValues(modify, actionInfo);
			if (modify.getPassword() == null
					|| modify.getPassword().length() == 0
					|| passwd.equals(modify.getPassword()))
				modify.setPassword(passwd);
			UserService userService = getUgd();
			userService.updateUser(modify, user.getPermissions(), true);
			updateGroups(actionInfo, ugd, user, modify);
			addMessage("User " + modify.getFirstName() + " "
					+ modify.getLastName() + " updated", modify, actionInfo);
		} else
			throw new InformationalException(
					LazerwebException.INVALID_PERMISSION);
	}

	private void updateGroups(HandlerData actionInfo, UserService ugd,
			UserBean user, CommerceUser modify) {
		if (actionInfo.hasParam("group_id")) {
			int[] groupIds = actionInfo.getParameterValuesAsInt("group_id", -1);
			Set<UserGroup> setGroups = new HashSet<UserGroup>();
			for (int id : groupIds) {
				if (id > 0)
					setGroups.add(ugd.getGroup(id));
			}
			Set<UserGroup> current = new HashSet<UserGroup>(modify.getGroups());
			for (UserGroup g : current) {
				if (!setGroups.contains(g)) {
					ugd.removeUserFromGroup(modify.getUsername(), g.getName(),
							user.getPermissions());
				}
			}
			for (UserGroup g : setGroups) {
				ugd.addUserToGroup(modify.getUsername(), g.getName(), user
						.getPermissions());
			}
		}
	}

	/**
	 * @see com.lazerinc.servlet.actionhandler.ActionHandler#getActionName()
	 */
	public String getName() {
		return ACTION_MODIFY_USER;
	}
}
