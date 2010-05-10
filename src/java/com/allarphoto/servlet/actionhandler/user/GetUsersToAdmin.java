package com.allarphoto.servlet.actionhandler.user;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.allarphoto.beans.User;
import com.allarphoto.client.beans.UserBean;
import com.allarphoto.ecommerce.UserGroup;
import com.allarphoto.server.UserService;
import com.allarphoto.servlet.actionhandler.ActionHandlerBase;
import com.allarphoto.utils.Resource;
import com.allarphoto.utils.Right;
import static com.allarphoto.utils.Right.*;
import com.allarphoto.utils.Rights;

public class GetUsersToAdmin extends ActionHandlerBase {

	public GetUsersToAdmin() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void performAction(HandlerData actionInfo) throws ActionException {
		UserService ugd = getUgd();
		UserBean user = getUserBean(actionInfo);
		Set<User> commonUsers = new HashSet<User>();
		if (actionInfo.hasParam("group_id")) {
			boolean first = true;
			Collection<UserGroup> groups = new LinkedList<UserGroup>();
			for (int id : actionInfo.getParameterValuesAsInt("group_id", 0)) {
				if (id > 0) {
					UserGroup g = ugd.getGroup(id);
					if (user.getPermissions().getPermission(g.getName(),
							Resource.GROUP, ADMIN))
						groups.add(ugd.getGroup(id));
					first = getCommonUsers(commonUsers, first, g.getUsers());
				}
			}
			actionInfo.setRequestBean("viewingGroups", groups);
			actionInfo.setRequestBean("users", ugd.getUsers(groups));
		} else {
			boolean first = true;
			Collection<UserGroup> groups = new LinkedList<UserGroup>();
			for (UserGroup g : user.getGroups()) {
				if (user.getPermissions().getPermission(g.getName(),
						Resource.GROUP, ADMIN))
					groups.add(g);
				first = getCommonUsers(commonUsers, first, g.getUsers());
			}
			if (groups.size() == 0) {
				for (Resource res : user.getPermissions()
						.getAvailableResourceList(Resource.GROUP, ADMIN)) {
					groups.add(getUgd().getGroup(res.getName()));
				}
			}
			actionInfo.setRequestBean("viewingGroups", groups);
			actionInfo.setRequestBean("users", ugd.getUsers(groups));
		}
		actionInfo.setRequestBean("commonUsers", commonUsers);
	}

	private boolean getCommonUsers(Set<User> commonUsers, boolean first,
			Collection<? extends User> users) {
		if (first) {
			commonUsers.addAll(users);
			first = false;
		} else {
			Set<User> temp = new HashSet<User>(commonUsers);
			temp.removeAll(users);
			commonUsers.removeAll(temp);
		}
		return first;
	}

	public String getName() {
		return "get_users_to_admin";
	}

}
