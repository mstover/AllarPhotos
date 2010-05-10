package com.allarphoto.servlet.actionhandler.admin;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.allarphoto.ecommerce.CommerceUser;
import com.allarphoto.ecommerce.UserGroup;
import com.allarphoto.security.VirtualGroupBasedSecurity;
import com.allarphoto.server.UserService;
import com.allarphoto.servlet.actionhandler.ActionHandlerBase;

public class GetGroups extends ActionHandlerBase {

	public GetGroups() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Get all groups for given list of users.
	 */
	public void performAction(HandlerData actionInfo) throws ActionException {
		UserService ugd = getUgd();
		Set<UserGroup> groups = new HashSet<UserGroup>();
		getGroupsForUsers(actionInfo, ugd, groups);
		int[] groupIds = actionInfo.getParameterValuesAsInt("group_id", 0);
		for (int id : groupIds) {
			groups.add(ugd.getGroup(id));
		}
		actionInfo.setRequestBean("combinedSecurity",
				new VirtualGroupBasedSecurity(groups));
		actionInfo.setRequestBean("groups", groups);
		actionInfo.setRequestBean("users", ugd.getUsers(groups));
	}

	private void getGroupsForUsers(HandlerData actionInfo, UserService ugd,
			Set<UserGroup> groups) {
		int[] userids = actionInfo.getParameterValuesAsInt("user_id", 0);
		boolean first = true;
		Set<UserGroup> commonGroups = new HashSet<UserGroup>();
		for (int id : userids) {
			getLog().info("Looking for user with id = " + id);
			CommerceUser user = ugd.getUser(id);
			if (user != null) {
				Collection<UserGroup> theseGroups = user.getGroups();
				groups.addAll(theseGroups);
				if (first) {
					commonGroups.addAll(theseGroups);
					first = false;
				} else {
					Iterator<UserGroup> commonIter = commonGroups.iterator();
					while (commonIter.hasNext()) {
						UserGroup g = commonIter.next();
						if (!theseGroups.contains(g)) {
							commonIter.remove();
						}
					}
				}
			}
		}
		actionInfo.setRequestBean("commonGroups", commonGroups);
	}

	public String getName() {
		return "get_groups";
	}

}
