package com.allarphoto.servlet.actionhandler.user;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaDynamic;

import strategiclibrary.service.notification.NotificationService;
import strategiclibrary.service.template.TemplateService;
import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.allarphoto.client.beans.UserBean;
import com.allarphoto.client.exceptions.InformationalException;
import com.allarphoto.client.exceptions.LazerwebException;
import com.allarphoto.ecommerce.CommerceUser;
import com.allarphoto.ecommerce.UserGroup;
import com.allarphoto.server.UserService;
import com.allarphoto.servlet.actionhandler.ActionHandlerBase;
import com.allarphoto.utils.Functions;
import com.allarphoto.utils.Resource;
import com.allarphoto.utils.WebBean;

public class AddUser extends ActionHandlerBase {
	NotificationService notifier;

	public AddUser() {
		super();
	}

	@CoinjemaDependency(method = "emailService")
	public void setNotifier(NotificationService s) {
		notifier = s;
	}

	public void performAction(HandlerData actionInfo) throws ActionException {
		CommerceUser newUser = new CommerceUser();
		UserService userService = getUgd();
		WebBean.setValues(newUser, actionInfo);
		actionInfo.setFlashBean("newUser", newUser);
		if (newUser.getLastName() == null || newUser.getLastName().length() < 1
				|| newUser.getFirstName() == null
				|| newUser.getFirstName().length() < 1
				|| newUser.getEmailAddress() == null
				|| newUser.getEmailAddress().length() < 1) {
			throw new InformationalException(
					LazerwebException.INCOMPLETE_USER_INFO);
		}
		if (newUser.getUsername() == null || newUser.getUsername().length() < 1) {
			newUser.setUsername(newUser.getFirstName().substring(0, 1)
					.toLowerCase()
					+ newUser.getLastName().toLowerCase());
		}
		if (newUser.getPassword() == null || newUser.getPassword().length() < 1) {
			newUser.setPassword(userService.createRandomPassword());
		}
		if (userService.getUser(newUser.getUsername()) != null) {
			throw new InformationalException(LazerwebException.DUP_USERNAME);
		}
		newUser.setPassword(newUser.getPassword());
		UserBean user = getUserBean(actionInfo);
		Set<UserGroup> groups = getGroupsForNewUser(actionInfo);
		getLog().info("permissions = " + user.getPermissions());
		userService.addUser(newUser, groups, user.getPermissions());
		emailInfo(newUser, actionInfo);
		this.addMessage(
				"User " + newUser.getUsername() + " successfully added",
				newUser, actionInfo);

	}

	protected Set<UserGroup> getGroupsForNewUser(HandlerData info) {
		UserService userService = getUgd();
		Set<UserGroup> groups = new HashSet<UserGroup>();
		for (int groupId : info.getParameterValuesAsInt("group_id", 0)) {
			getLog().info("group id = " + groupId);
			if (groupId > 0)
				groups.add(userService.getGroup(groupId));
		}
		return groups;
	}

	protected boolean emailInfo(CommerceUser user, HandlerData info) {
		boolean retVal = false;
		StringWriter msg = new StringWriter();
		Map values = new HashMap();
		values.put("request", info);
		values.put("config", getController().getProperties());
		values.put("user", user);
		values.put("url", "http://" + info.getBean("host"));
		List<String> libraries = new LinkedList<String>();
		for (Resource r : ugd.getSecurity(user).getAvailableResourceList(
				Resource.DATATABLE)) {
			libraries.add(dbUtil.getProductFamily(r.getName())
					.getDescriptiveName());
		}
		getTemplateService().mergeTemplate("add_user.vtl", values, msg);
		String message = msg.toString();
		try {
			notifier.sendMessage(new String[] { user.getEmailAddress() },
					"webmaster@lazerinc.com", info.getParameter(
							"email_subject", "Reply from Online Library"),
					"text/plain", message.toString());
			retVal = true;
		} catch (Exception e) {
			retVal = false;
			getLog().debug(e.getMessage());
		}
		return retVal;
	}

	public String getName() {
		return "add_user";
	}

	@CoinjemaDynamic(type = "velocityService", method = "emailTemplateService")
	public TemplateService getTemplateService() {
		return null;
	}

}
