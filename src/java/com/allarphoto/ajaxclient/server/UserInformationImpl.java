package com.lazerinc.ajaxclient.server;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaDynamic;

import strategiclibrary.service.cache.CacheService;
import strategiclibrary.service.notification.NotificationService;
import strategiclibrary.service.sql.ObjectMappingService;
import strategiclibrary.service.template.TemplateService;
import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;
import strategiclibrary.service.webaction.ServletHandlerData;
import strategiclibrary.util.Converter;

import com.lazerinc.ajaxclient.client.UserInformation;
import com.lazerinc.ajaxclient.client.beans.AjaxAccessRequest;
import com.lazerinc.ajaxclient.client.beans.AjaxGroup;
import com.lazerinc.ajaxclient.client.beans.AjaxPermissions;
import com.lazerinc.ajaxclient.client.beans.AjaxResource;
import com.lazerinc.ajaxclient.client.beans.AjaxRights;
import com.lazerinc.ajaxclient.client.beans.AjaxUser;
import com.lazerinc.application.SecurityModel;
import com.lazerinc.beans.City;
import com.lazerinc.beans.Company;
import com.lazerinc.beans.Country;
import com.lazerinc.beans.LogItem;
import com.lazerinc.beans.State;
import com.lazerinc.beans.User;
import com.lazerinc.beans.UserRequest;
import com.lazerinc.client.beans.GenericDataBean;
import com.lazerinc.client.beans.ShoppingCartBean;
import com.lazerinc.client.beans.UserBean;
import com.lazerinc.client.exceptions.LazerwebException;
import com.lazerinc.client.util.LogUtil;
import com.lazerinc.client.util.ShoppingCartUtil;
import com.lazerinc.ecommerce.CommerceUser;
import com.lazerinc.ecommerce.UserGroup;
import com.lazerinc.security.NoPermissions;
import com.lazerinc.server.ResourceService;
import com.lazerinc.server.UserService;
import com.lazerinc.utils.Resource;
import com.lazerinc.utils.Right;
import com.lazerinc.utils.Rights;
import com.lazerinc.utils.WebBean;

public class UserInformationImpl extends AbstractGwtServlet implements
		UserInformation {
	private static final long serialVersionUID = 1;

	UserService ugd;

	ObjectMappingService mapper;

	TemplateService templates;

	ResourceService resService;

	NotificationService emailer;

	CacheService cache;

	public ShoppingCartUtil cartUtil = new ShoppingCartUtil();

	public LogUtil logUtil = new LogUtil();

	public UserInformationImpl() {
		super();
	}

	public String getFullName() {
		HandlerData req = this.getThreadLocalHandlerData();
		UserBean user = (UserBean) req.getBean("user");
		if (user == null)
			return "No Name!";
		return user.getFirstName() + " " + user.getLastName();
	}

	public String getBrowser() {
		return (String) getThreadLocalHandlerData().getBean("browser");
	}

	public boolean requestAccess(String username, List libraries,
			String firstName, String lastName, String email, String company,
			String phone, String reason) {
		try {
			HandlerData info = getThreadLocalHandlerData();
			info.setRequestBean("library", (String[]) libraries
					.toArray(new String[0]));
			info.setRequestBean("username", username);
			info.setRequestBean("firstName", firstName);
			info.setRequestBean("lastName", lastName);
			info.setRequestBean("email", email);
			info.setRequestBean("phone", phone);
			info.setRequestBean("company", company);
			info.setRequestBean("reason", reason);
			Map<String, List<String>> emailsToLibraries = new HashMap<String, List<String>>();
			User requestingUser = null;
			if (info.getParameterValues("library").length == 0)
				throw new LazerwebException(LazerwebException.INCOMPLETE_INFO);
			for (String library : info.getParameterValues("library")) {
				UserRequest request = WebBean
						.setValues(new UserRequest(), info);
				if (request.getUsername() != null
						&& request.getUsername().length() > 0
						&& (request.getEmail() == null || request.getEmail()
								.length() == 0)) {
					requestingUser = ugd.getUser(request.getUsername());
					if (requestingUser == null)
						throw new LazerwebException(
								LazerwebException.NO_SUCH_USER);
				} else if (request.getUsername() != null
						&& request.getUsername().length() > 0) {
					requestingUser = ugd.getUser(request.getUsername());
					if (requestingUser == null)
						requestingUser = ugd.getUserByEmail(request.getEmail());
					if (requestingUser == null)
						throw new LazerwebException(
								LazerwebException.NO_SUCH_USER);
					setRequestUser(requestingUser, request);
				} else if (request.getEmail() != null
						&& request.getEmail().length() > 0) {
					if (request.getCompany() == null
							|| request.getReason() == null
							|| request.getPhone() == null
							|| request.getCompany().length() == 0
							|| request.getReason().length() == 0
							|| request.getPhone().length() == 0)
						throw new LazerwebException(
								LazerwebException.INCOMPLETE_INFO);
					requestingUser = ugd.getUserByEmail(request.getEmail());
					setRequestUser(requestingUser, request);
				} else
					throw new LazerwebException(
							LazerwebException.INCOMPLETE_USER_INFO);
				request.setRequestedResource(resService.getResource(dbutil
						.getProductFamilyFromDescription(library)
						.getTableName(), Resource.DATATABLE));
				String[] emails = getRecipients(request);
				for (String e : emails) {
					List<String> libraryList = emailsToLibraries.get(e);
					if (libraryList == null) {
						libraryList = new ArrayList<String>();
						emailsToLibraries.put(e, libraryList);
					}
					libraryList.add(dbutil.getProductFamilyFromDescription(
							library).getTableName());
				}
				Map<String, Object> values = new HashMap<String, Object>();
				values.put("request", request);
				values.put("url", "http://" + info.getBean("host"));
				mapper.doUpdate("newUserRequest.sql", values);
			}
			for (Entry<String, List<String>> entry : emailsToLibraries
					.entrySet()) {
				UserRequest[] requests = new UserRequest[entry.getValue()
						.size()];
				int count = 0;
				for (String library : entry.getValue()) {
					requests[count] = WebBean
							.setValues(new UserRequest(), info);
					requests[count].setRequestedResource(resService
							.getResource(library, Resource.DATATABLE));
					setRequestUser(requestingUser, requests[count]);
					getLog().info("Requesting user is " + requestingUser);
					count++;
				}
				Map<String, Object> values = new HashMap<String, Object>();
				String subject = "User Access Requested to On-Line Image Library";
				StringWriter msg = new StringWriter();
				values = new HashMap<String, Object>();
				values.put("request", requests[0]);
				values.put("requests", requests);
				values.put("dbutil", dbutil);
				values.put("url", "http://"
						+ info.getBean("host")
						+ ((ServletHandlerData) info).getRequest()
								.getContextPath()
						+ "/admin.jsp?section=AccessRequests");
				values.put("user", ugd.getUser(requests[0].getUsername()));
				getTemplateService().mergeTemplate("access_request.vtl",
						values, msg);
				emailer.sendMessage(new String[] { entry.getKey() },
						getEmailReplyTo(), subject, "text/plain", msg
								.toString());
			}
			return true;
		} catch (Exception e) {
			getLog().warn("Failed to request access", e);
			return false;
		}
	}

	@CoinjemaDynamic(alias = "systemEmailReplyTo")
	protected String getEmailReplyTo() {
		return "webmaster@lazerinc.com";
	}

	private void setRequestUser(User requestingUser, UserRequest request) {
		if (requestingUser != null) {
			request.setUsername(requestingUser.getUsername());
			request.setEmail(null);
			request.setFirstName(null);
			request.setLastName(null);
		}
	}

	public boolean askQuestion(String name, String email, String question) {
		HandlerData info = this.getThreadLocalHandlerData();
		info.getBean("host");
		emailer.sendMessage(new String[] { getWebmaster() }, email,
				"Question about " + info.getBean("host") + " on-line library",
				"text/plain", name + "(" + email + ") wrote:\n\n" + question);
		emailer
				.sendMessage(new String[] { email }, getWebmaster(),
						"Regarding your question about " + info.getBean("host")
								+ " on-line library", "text/plain",
						"Your message has been received.  Please expect a response within 24 hours.");
		return true;
	}

	private String[] getRecipients(UserRequest request) {
		Resource res = request.getRequestedResource();
		List<String> recips = new LinkedList<String>();
		for (User u : ugd.getAdmins(res)) {
			recips.add(u.getEmailAddress());
		}
		return (String[]) recips.toArray(new String[recips.size()]);
	}

	public boolean addGroup(String groupName, String[] adminGroups) {
		try {
			if (adminGroups.length > 0) {
				ugd.addGroup(groupName, adminGroups[0], currentPerms());
				for (int i = 1; i < adminGroups.length; i++) {
					ugd.updateGroupRights(ugd.getGroup(adminGroups[i]),
							resService.getResource(groupName, Resource.GROUP),
							Right.ADMIN, true, null, currentPerms());
				}
				return true;
			} else
				return false;
		} catch (Exception e) {
			getLog().warn("Failed to create group", e);
			return false;
		}
	}

	public boolean deleteGroup(String groupName) {
		try {
			if (ugd.getGroup(groupName).getUsers().size() == 0) {
				ugd.deleteGroup(groupName, currentPerms());
				return true;
			} else
				return false;
		} catch (Exception e) {
			getLog().warn("Failed to delete group", e);
			return false;
		}
	}

	protected boolean emailInfo(CommerceUser user) {
		if(!(currentPerms() instanceof NoPermissions) && !currentPerms().getPermission(user.getUsername(),Resource.USER, Right.ADMIN)) return false;
		boolean retVal = false;
		String nl = System.getProperty("line.separator");
		StringBuffer message = new StringBuffer();
		message.append("This message was sent by the Online Library" + nl + nl);

		String username = user.getUsername();
		message.append("Your username is " + username + nl);

		String password = user.getPassword();
		message.append("Your password is " + password);
		try {
			emailer.sendMessage(new String[] { user.getEmailAddress() },
					"webmaster@lazerinc.com", "Reply from Online Library",
					"text/plain", message.toString());
			retVal = true;
		} catch (Exception e) {
			retVal = false;
			getLog().debug(e.getMessage());
		}
		return retVal;
	}

	public boolean sendPassword(String username) {
		try {
			return emailInfo(ugd.getUser(username));
		} catch (Exception e) {
			getLog().warn("Failed to send password", e);
			return false;
		}
	}

	public boolean sendPasswordFromEmail(String email) {
		try {
			return emailInfo(ugd.getUserByEmail(email));
		} catch (Exception e) {
			getLog().warn("Failed to send password", e);
			return false;
		}
	}

	public AjaxAccessRequest[] getAccessRequests(boolean canAdmin) {
		try {
			Map<String, Object> values = new HashMap<String, Object>();
			values.put("user", ((UserBean) this.getThreadLocalHandlerData()
					.getBean("user")).getUser());
			Collection<UserRequest> requests = (Collection<UserRequest>) mapper
					.getObjects("getUserRequests.sql", values);
			AjaxAccessRequest[] ajaxRequests = new AjaxAccessRequest[requests
					.size()];
			int count = 0;
			for (UserRequest r : requests) {
				ajaxRequests[count] = new AjaxAccessRequest(r.getUsername(), r
						.getFirstName(), r.getLastName(), r.getEmail(),
						new AjaxResource(r.getRequestedResource().getName(), r
								.getRequestedResource().getType()));
				ajaxRequests[count].setCompany(r.getCompany());
				ajaxRequests[count].setPhone(r.getPhone());
				ajaxRequests[count].setReason(r.getReason());
				ajaxRequests[count].setId(r.getId());
				ajaxRequests[count].setAddress1(r.getAddress1());
				ajaxRequests[count].setAddress2(r.getAddress2());
				ajaxRequests[count].setCity(r.getCity());
				ajaxRequests[count].setState(r.getState());
				ajaxRequests[count].setCountry(r.getCountry());
				ajaxRequests[count].setZip(r.getZip());
				User u = ugd.getUser(r.getUsername());
				if (u != null) {
					ajaxRequests[count].setEmail(u.getEmailAddress());
					ajaxRequests[count].setFirstName(u.getFirstName());
					ajaxRequests[count].setLastName(u.getLastName());
				}
				ajaxRequests[count]
						.setResourceDescription(getResourceDescription(r
								.getRequestedResource()));
				ajaxRequests[count]
						.setGroups(getGroupList(getGroupChoicesForAccessRequest(
								r.getRequestedResource(), currentPerms())));
				count++;
			}
			return ajaxRequests;
		} catch (Exception e) {
			getLog().warn("Failed to get user access requests", e);
			return new AjaxAccessRequest[0];
		}
	}

	private void logRequestGrant(UserRequest request, UserBean grantor) {
		LogItem item = createRequestLogItem(request);
		item.setValue("grantor", grantor.getUsername());
		logUtil.logItem(item);
	}

	private void logRequestReject(UserRequest request, UserBean rejector) {
		LogItem item = createRequestLogItem(request);
		item.setValue("rejector", rejector.getUsername());
		logUtil.logItem(item);
	}

	private LogItem createRequestLogItem(UserRequest request) {
		LogItem item = new LogItem();
		item.setValue("action", "access request");
		item.setValue("user_email", request.getEmail());
		item.setValue("user_firstname", request.getFirstName());
		item.setValue("user_lastname", request.getLastName());
		item.setValue("username", request.getUsername());
		item.setValue("library_name", request.getRequestedResource().getName());
		return item;
	}

	public boolean grantAccessRequest(int requestId, String group,
			String feedback) {
		try {
			UserRequest request = new UserRequest();
			Map<String, Object> values = new HashMap<String, Object>();
			values.put("id", requestId);
			mapper.getObject("getUserRequests.sql", values, request);
			request.grant(mapper, ugd, group, currentPerms());
			logRequestGrant(request, (UserBean) getThreadLocalHandlerData()
					.getBean("user"));
			HandlerData info = getThreadLocalHandlerData();
			if (request.isNewUser()) {
				emailNewUser(ugd.getUser(request.getUsername()), info);
			} else {
				StringWriter msg = new StringWriter();
				values = new HashMap<String, Object>();
				values.put("request", request);
				values.put("url", "http://" + info.getBean("host"));
				values.put("resourceDescription",
						getResourceDescription(request.getRequestedResource()));
				getTemplateService().mergeTemplate("grant_access_request.vtl",
						values, msg);
				emailer.sendMessage(new String[] { request.getEmail() },
						"webmaster@lazerinc.com",
						"Your requested access has been granted", "text/plain",
						msg.toString());

			}
			return true;
		} catch (Exception e) {
			getLog().warn("Problem granting access", e);
			return false;
		}
	}

	public boolean deleteUser(String username) {
		try {
			return ugd.deleteUser(username, currentPerms());
		} catch (Exception e) {
			getLog().warn("Failed to delete user", e);
			return false;
		}
	}

	private void emailNewUser(CommerceUser user, HandlerData info) {
		Map<String, Object> values;
		StringWriter msg = new StringWriter();
		values = new HashMap<String, Object>();
		values.put("user", user);
		values.put("url", "http://" + info.getBean("host"));
		List<String> libraries = new LinkedList<String>();
		for (Resource r : ugd.getSecurity(user).getAvailableResourceList(
				Resource.DATATABLE)) {
			libraries.add(dbutil.getProductFamily(r.getName())
					.getDescriptiveName());
		}
		values.put("libraries", libraries);
		getTemplateService().mergeTemplate("add_user.vtl", values, msg);
		emailer.sendMessage(new String[] { user.getEmailAddress() },
				"webmaster@lazerinc.com",
				"Your requested account has been created", "text/plain", msg
						.toString());
	}

	public boolean rejectAccessRequest(int requestId, String feedback) {
		UserRequest request = new UserRequest();
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("id", requestId);
		mapper.getObject("getUserRequests.sql", values, request);
		request.reject(mapper);
		logRequestReject(request, (UserBean) getThreadLocalHandlerData()
				.getBean("user"));
		if ((request.getUsername() == null || request.getUsername().length() == 0)
				&& (request.getEmail() == null || request.getEmail().length() == 0))
			return true;
		if (request.getEmail() == null || request.getEmail().length() == 0)
			request.setEmail(ugd.getUser(request.getUsername())
					.getEmailAddress());
		StringWriter msg = new StringWriter();
		values = new HashMap<String, Object>();
		values.put("request", request);
		values.put("resourceDescription", getResourceDescription(request
				.getRequestedResource()));
		values.put("feedback", feedback);
		getTemplateService().mergeTemplate("reject_access_request.vtl", values,
				msg);
		emailer.sendMessage(new String[] { request.getEmail() },
				"webmaster@lazerinc.com",
				"Your requested access has been rejected", "text/plain", msg
						.toString());

		return true;
	}

	private Collection<UserGroup> getGroupChoicesForAccessRequest(Resource res,
			SecurityModel perms) {
		Collection<UserGroup> groups = new LinkedList<UserGroup>();
		for (UserGroup g : ugd.getGroups(perms)) {
			if (g.getPermissions(res) != null && g.getPermissions(res).hasAny())
				groups.add(g);
		}
		return groups;
	}

	private String getResourceDescription(Resource res) {
		switch (res.getType()) {
		case Resource.DATATABLE:
			return "On-Line Library: "
					+ dbutil.getProductFamily(res.getName())
							.getDescriptiveName();
		}
		return "";
	}

	public AjaxGroup getGroup(String groupName) {
		try {
			UserGroup group = ugd.getGroup(groupName);
			AjaxGroup agroup = createAjaxGroup(group);
			return agroup;
		} catch (Exception e) {
			getLog().warn("Failed to retrieve group", e);
			return null;
		}

	}

	public boolean updateRight(String groupName, String resName, int resType,
			String right, boolean hasRight) {
		try {
			getLog().debug(
					"Update " + right + " right for group " + groupName
							+ " on resource " + resName + "(" + resType
							+ ") to " + hasRight);
			ugd.updateGroupRights(groupName, resService.getResource(resName,
					resType), Right.getRight(right), hasRight, null,
					currentPerms());
			return true;
		} catch (Exception e) {
			getLog().warn("Failed to update rights", e);
			return false;
		}
	}

	public String[] getResourceNames(int resType) {
		Collection<Resource> resources = resService.getResources(resType);
		List<String> names = new ArrayList<String>();
		SecurityModel perms = currentPerms();
		for (Resource r : resources)
		{
			if(perms.getPermission(r.getName(),resType,Right.ADMIN))
					names.add(r.getName());
		}
		return names.toArray(new String[names.size()]);
	}

	public AjaxPermissions getPermissions() {
		return createPermissions((UserBean) getThreadLocalHandlerData()
				.getBean("user"));
	}

	private AjaxPermissions createPermissions(UserBean user) {
		AjaxPermissions perms = new AjaxPermissions();
		for (Resource r : user.getPermissions().getAvailableResourceList()) {
			AjaxResource ar = new AjaxResource(r.getName(), r.getType());
			Rights rights = user.getPermissions().getPermissions(r);
			AjaxRights arights = new AjaxRights(rights.getRight(Right.ADMIN),
					rights.getRight(Right.READ), rights.getRight(Right.ORDER),
					rights.getRight(Right.DOWNLOAD), rights
							.getRight(Right.DOWNLOAD_ORIG), rights
							.getRight(Right.UPLOAD));
			perms.addRight(ar, arights);
		}
		return perms;
	}

	public AjaxUser[] getUsers(String group) {
		try {
			List<CommerceUser> users = new LinkedList<CommerceUser>(ugd
					.listUsers(group, dbutil.getAdmin()));
			Collections.sort(users);
			AjaxUser[] ausers = new AjaxUser[users.size()];
			int count = 0;
			Collections.sort(users);
			for (CommerceUser u : users) {
				ausers[count++] = createAjaxUser(u);
			}
			return ausers;
		} catch (Exception e) {
			getLog().warn("Failed to get users", e);
			return new AjaxUser[0];
		}
	}

	public List getUsers(boolean canAdmin) {
		List list = new ArrayList();
		for (CommerceUser u : ugd.getUsers(currentPerms())) {
			list.add(createAjaxUser(u));
		}
		return list;
	}

	public boolean isAdmin() {
		return currentPerms().getPermission("all", Resource.DATABASE,
				Right.ADMIN);
	}

	public String getCallingUrl() {
		HandlerData req = this.getThreadLocalHandlerData();
		getLog().info("calling page was " + req.getBean("callingUrl"));
		if (req.getBean("callingUrl") != null)
			return (String) req.getBean("callingUrl");
		else
			return "";
	}

	public AjaxGroup[] getGroups(boolean canAdmin) {
		try {
			Collection<UserGroup> groups = null;
			if (!canAdmin) {
				groups = ugd.getGroups(dbutil.getAdmin());
			} else {
				groups = ugd.getGroups(currentPerms());
			}
			AjaxGroup[] groupNames = new AjaxGroup[groups.size()];
			int count = 0;
			for (UserGroup g : groups) {
				groupNames[count++] = createAjaxGroup(g);
			}
			return groupNames;
		} catch (Exception e) {
			getLog().warn("Exception getting groups", e);
			return new AjaxGroup[0];
		}
	}

	public boolean addUser(String firstName, String lastName, String email,
			String middleInitial, String username, String password,
			String phone, String company, String address1, String address2,
			String city, String zip, String state, String country,
			String expDate, String[] groups) {
		try {
			if (lastName == null || lastName.length() < 1 || firstName == null
					|| firstName.length() < 1 || email == null
					|| email.length() < 1 || username == null
					|| username.length() < 1) {
				return false;
			}
			if (password == null || password.length() < 1) {
				password = ugd.createRandomPassword();
			}
			if (ugd.getUser(username) != null) {
				return false;
			}
			CommerceUser user = new CommerceUser();
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setEmailAddress(email);
			user.setBillAddress1(address1);
			user.setBillAddress2(address2);
			user.setBillCity(new City(city));
			user.setBillState(new State(state));
			user.setBillZip(zip);
			user.setBillCountry(new Country(country));
			user.setUsername(username);
			user.setPassword(password);
			user.setPhone(phone);
			user.setCompany(new Company(company));
			user.setMiddleInitial(middleInitial);
			user.setExpDate(Converter.getCalendar(expDate, null));
			Collection<UserGroup> gps = new LinkedList<UserGroup>();
			for (String g : groups) {
				gps.add(ugd.getGroup(g));
			}
			ugd.addUser(user, gps, currentPerms());
			emailNewUser(user, getThreadLocalHandlerData());
			return true;
		} catch (Exception e) {
			getLog().warn("Couldn't create user", e);
			return false;
		}
	}

	public AjaxUser getUser(String username) {
		try {
			CommerceUser u = ugd.getUser(username);
			if (u == null)
				return null;
			AjaxUser user = createAjaxUser(u);
			return user;
		} catch (Exception e) {
			getLog().warn("Error retrieving user " + username, e);
			return null;
		}
	}

	public boolean updateGroupName(String groupName, String newGroupName) {
		try {
			ugd.updateGroup(groupName, newGroupName, currentPerms());
			return true;
		} catch (Exception e) {
			getLog().warn("Failed to update group name", e);
			return false;
		}
	}

	public boolean updateGroupDescription(String groupName,
			String newDescription) {
		try {
			ugd.updateGroupDescription(groupName, newDescription,
					currentPerms());
			return true;
		} catch (Exception e) {
			getLog().warn("Failed to update group name", e);
			return false;
		}
	}

	public AjaxUser[] searchUsers(String searchText, boolean canAdmin) {
		try {
			List<CommerceUser> users = new LinkedList<CommerceUser>(ugd
					.searchUsers(searchText));
			Collections.sort(users);
			AjaxUser[] ret = new AjaxUser[users.size()];
			int i = 0;
			for (CommerceUser u : users) {
				ret[i++] = createAjaxUser(u);
			}
			return ret;
		} catch (Exception e) {
			getLog().warn("Failed to search users");
			return new AjaxUser[0];
		}
	}

	public boolean addUserToGroup(String username, String groupName) {
		try {
			ugd.addUserToGroup(username, groupName, currentPerms());
			return true;
		} catch (Exception e) {
			getLog()
					.warn(
							"Failed to add user " + username + " to group "
									+ groupName, e);
			return false;
		}
	}

	public boolean toggleUserToGroup(String username, String groupName) {
		try {
			CommerceUser user = ugd.getUser(username);
			UserGroup group = ugd.getGroup(groupName);
			if (user.getGroups().contains(group))
				removeUserFromGroup(username, groupName);
			else
				addUserToGroup(username, groupName);
			return true;
		} catch (Exception e) {
			getLog().warn(
					"Failed to change user " + username
							+ " membership to group " + groupName);
			return false;
		}
	}

	public boolean clearSesseionVariable(String var) {
		getThreadLocalHandlerData().removeUserBean(var);
		return true;
	}

	public String getSessionVariable(String var) {
		return (String) getThreadLocalHandlerData().getUserBean(var);
	}

	public boolean removeUserFromGroup(String username, String groupName) {
		try {
			ugd.removeUserFromGroup(username, groupName, currentPerms());
			return true;
		} catch (Exception e) {
			getLog().warn(
					"Failed to remove user " + username + " from group "
							+ groupName, e);
			return false;
		}
	}

	public boolean updateUser(String firstName, String lastName, String email,
			String middleInitial, String username, String password,
			String phone, String company, String address1, String address2,
			String city, String zip, String state, String country,
			String expDate, String[] groups) {
		try {
			if (lastName == null || lastName.length() < 1 || firstName == null
					|| firstName.length() < 1 || email == null
					|| email.length() < 1 || username == null
					|| username.length() < 1) {
				return false;
			}
			if (groups != null && groups.length == 0) {
				return false;
			}
			CommerceUser user = ugd.getUser(username);
			if(!currentPerms().getPermission(new Resource(user.getUsername(),Resource.USER), Right.ADMIN)) return false;
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setEmailAddress(email);
			user.setBillAddress1(address1);
			user.setBillAddress2(address2);
			user.setBillCity(new City(city));
			user.setBillState(new State(state));
			user.setBillZip(zip);
			user.setBillCountry(new Country(country));
			if (password != null && password.length() > 0
					&& !password.equals("N/A"))
				user.setPassword(password);
			user.setPhone(phone);
			user.setCompany(new Company(company));
			user.setMiddleInitial(middleInitial);
			user.setExpDate(Converter.getCalendar(expDate, null));
			if (groups != null) {
				for (UserGroup g : user.getGroups()) {
					try {
						if (!contains(groups, g.getName()))
							ugd.removeUserFromGroup(user.getUsername(), g
									.getName(), currentPerms());
					} catch (Exception e) {
					}
				}
				for (String g : groups) {
					ugd.addUserToGroup(user.getUsername(), g, currentPerms());
				}
			}
			return ugd.updateUser(user, currentPerms());
		} catch (Exception e) {
			getLog().warn("Failed to update user", e);
			return false;
		}
	}

	public AjaxUser getCurrentUser() {
		UserBean ub = (UserBean) getThreadLocalHandlerData().getBean("user");
		getLog().info("Current user = " + ub);
		if (ub != null)
			return createAjaxUser(ub.getUser());
		else
			return null;
	}

	public AjaxUser login(String username, String password, boolean setCookie) {
		try {
			CommerceUser user = ugd.checkUser(username, password);
			loginActivities(getThreadLocalHandlerData(), user);
			HandlerData actionInfo = getThreadLocalHandlerData();
			if (setCookie) {
				String[] host = ((String) actionInfo.getBean("host"))
						.split(":");
				getLog()
						.info(
								"Adding password cookie "
										+ user.getEncryptedPassword());

				try {
					actionInfo.addPermCookie("username", URLEncoder.encode(user
							.getUsername(), "utf-8"), host[0],
							((ServletHandlerData) actionInfo).getRequest()
									.getContextPath(), false);
					actionInfo.addPermCookie("password", URLEncoder.encode(user
							.getEncryptedPassword(), "utf-8"), host[0],
							((ServletHandlerData) actionInfo).getRequest()
									.getContextPath(), false);
				} catch (UnsupportedEncodingException e) {
					getLog().warn("Unsupported encoding - utf-8");
				}
			}
			return createAjaxUser(user);
		} catch (Exception e) {
			getLog().info("Failed to login user: " + username);
			return null;
		}
	}

	protected void loginActivities(HandlerData actionInfo, CommerceUser user)
			throws ActionException {
		UserBean userBean = (UserBean) actionInfo.getBean("user");
		if (userBean == null) {
			userBean = new UserBean();
			actionInfo.setUserBean("user", userBean);
		}
		userBean.setUser(user);
		userBean.setPermissions(ugd.getSecurity(user));
		ShoppingCartBean shoppingCart = cartUtil.getCart(actionInfo);
		cartUtil.initCart(shoppingCart, user);
		GenericDataBean dataBean = new GenericDataBean();
		actionInfo.setUserBean("data", dataBean);
		logUtil.logAction(new String[] { "action", "login", "user",
				user.getUsername() });
		getLog().info("Login successful for: " + user.getUsername());
	}

	private boolean contains(String[] list, String item) {
		for (int i = 0; i < list.length; i++) {
			if (list[i].equals(item))
				return true;
		}
		return false;
	}

	@CoinjemaDependency(type = "userService")
	public void setUserService(UserService u) {
		ugd = u;
	}

	@CoinjemaDependency(type = "cacheService")
	public void setCache(CacheService c) {
		cache = c;
	}

	@CoinjemaDependency(type = "objectMappingService")
	public void setMapper(ObjectMappingService mapper) {
		this.mapper = mapper;
	}

	@CoinjemaDependency(type = "emailService")
	public void setEmailer(NotificationService emailer) {
		this.emailer = emailer;
	}

	@CoinjemaDependency(type = "resourceService")
	public void setResourceService(ResourceService rs) {
		resService = rs;
	}

	@CoinjemaDynamic(type = "velocityService")
	public TemplateService getTemplateService() {
		return null;
	}

	@CoinjemaDynamic(alias = "webmaster")
	public String getWebmaster() {
		return "webmaster@lazerinc.com";
	}

}