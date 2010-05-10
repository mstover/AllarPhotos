package com.allarphoto.servlet.actionhandler.user;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaDynamic;

import strategiclibrary.service.notification.NotificationService;
import strategiclibrary.service.sql.ObjectMappingService;
import strategiclibrary.service.template.TemplateService;
import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;
import strategiclibrary.service.webaction.ServletHandlerData;

import com.allarphoto.beans.User;
import com.allarphoto.beans.UserRequest;
import com.allarphoto.client.exceptions.LazerwebException;
import com.allarphoto.ecommerce.UserGroup;
import com.allarphoto.server.ResourceService;
import com.allarphoto.servlet.actionhandler.ActionHandlerBase;
import com.allarphoto.utils.Resource;
import com.allarphoto.utils.Right;
import com.allarphoto.utils.WebBean;

public class UserAccessRequest extends ActionHandlerBase {

	protected ObjectMappingService mapper;

	NotificationService notifier;

	public UserAccessRequest() {
	}

	public String getName() {
		return "action_user_request";
	}

	public void performAction(HandlerData info) throws ActionException {
		Map<String, Set<String>> emailsToLibraries = new HashMap<String, Set<String>>();
		User requestingUser = null;
		if (info.getParameterValues("library").length == 0
				|| info.getParameter("library").length() == 0)
			throw new LazerwebException(LazerwebException.INCOMPLETE_INFO);
		for (String library : info.getParameterValues("library")) {
			requestingUser = createUserRequest(info, emailsToLibraries,
					requestingUser, library);
		}
		notifyAdmins(info, emailsToLibraries, requestingUser);
	}

	protected void notifyAdmins(HandlerData info,
			Map<String, Set<String>> emailsToLibraries, User requestingUser) {
		for (Entry<String, Set<String>> entry : emailsToLibraries.entrySet()) {
			UserRequest[] requests = new UserRequest[entry.getValue().size()];
			int count = 0;
			for (String library : entry.getValue()) {
				requests[count] = WebBean.setValues(new UserRequest(), info);
				requests[count].setRequestedResource(resService.getResource(
						library, Resource.DATATABLE));
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
			values.put("dbutil", dbUtil);
			values.put("url", "http://" + info.getBean("host")
					+ ((ServletHandlerData) info).getRequest().getContextPath()
					+ "/admin.jsp?section=AccessRequests");
			values.put("user", getUgd().getUser(requests[0].getUsername()));
			getTemplateService().mergeTemplate("access_request.vtl", values,
					msg);
			notifier.sendMessage(new String[] { entry.getKey() },
					getEmailReplyTo(), subject, "text/plain", msg.toString());
		}
	}

	protected User createUserRequest(HandlerData info,
			Map<String, Set<String>> emailsToLibraries, User requestingUser,
			String library) throws LazerwebException, ActionException {
		UserRequest request = WebBean.setValues(new UserRequest(), info);
		if (request.getUsername() != null
				&& request.getUsername().length() > 0
				&& (request.getEmail() == null || request.getEmail().length() == 0)) {
			requestingUser = getUgd().getUser(request.getUsername());
			if (requestingUser == null)
				throw new LazerwebException(LazerwebException.NO_SUCH_USER);
		} else if (request.getUsername() != null
				&& request.getUsername().length() > 0) {
			requestingUser = getUgd().getUser(request.getUsername());
			if (requestingUser == null)
				requestingUser = getUgd().getUserByEmail(request.getEmail());
			if (requestingUser == null)
				throw new LazerwebException(LazerwebException.NO_SUCH_USER);
			setRequestUser(requestingUser, request);
		}
		else if(request.getEmail() != null && request.getEmail().length() > 0)
		{
			if(isInfoIncomplete(request))
				throw new LazerwebException(LazerwebException.INCOMPLETE_INFO,true);
			requestingUser = getUgd().getUserByEmail(request.getEmail());
			setRequestUser(requestingUser, request);
		} else
			throw new LazerwebException(LazerwebException.INCOMPLETE_USER_INFO);
		request.setRequestedResource(resService.getResource(library,
				Resource.DATATABLE));
		gatherAdminEmails(emailsToLibraries, library, request);
		notifyRequestingUser(info, library, request);
		return requestingUser;
	}

	protected boolean isInfoIncomplete(UserRequest request) {
		return request.getCompany() == null || request.getReason() == null
				|| request.getPhone() == null
				|| request.getCompany().length() == 0
				|| request.getReason().length() == 0
				|| request.getPhone().length() == 0;
	}

	protected void gatherAdminEmails(
			Map<String, Set<String>> emailsToLibraries, String library,
			UserRequest request) {
		String[] emails = getRecipients(request);
		for (String email : emails) {
			Set<String> libraryList = emailsToLibraries.get(email);
			if (libraryList == null) {
				libraryList = new HashSet<String>();
				emailsToLibraries.put(email, libraryList);
			}
			libraryList.add(library);
		}
	}

	protected void notifyRequestingUser(HandlerData info, String library,
			UserRequest request) throws ActionException {
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("request", request);
		values.put("url", "http://" + info.getBean("host"));
		mapper.doUpdate("newUserRequest.sql", values);
		addMessage("Your request for access to "
				+ dbUtil.getProductFamily(library).getDescriptiveName()
				+ " has been received", request, info);
	}

	protected void setRequestUser(User requestingUser, UserRequest request) {
		if(requestingUser != null)
		{
			request.setUsername(requestingUser.getUsername());
			request.setEmail(null);
			request.setFirstName(null);
			request.setLastName(null);
		}
	}

	private String[] getRecipients(UserRequest request) {
		Resource res = request.getRequestedResource();
		List<String> recips = new LinkedList<String>();
		for(UserGroup g : getUgd().getGroupsWithPrivs(res,Right.READ))
		{
			getLog().info("Group with read right over " + res + " = " + g.getName());
			for (User u : getUgd().getAdmins(new Resource(g.getName(),Resource.GROUP))) {				
				recips.add(u.getEmailAddress());
			}
		}
		return (String[]) recips.toArray(new String[recips.size()]);
	}

	@CoinjemaDependency(type = "objectMappingService")
	public void setObjectMapper(ObjectMappingService m) {
		mapper = m;
	}

	@CoinjemaDependency(method = "emailService")
	public void setEmailService(NotificationService s) {
		notifier = s;
	}

	@CoinjemaDynamic(type = "velocityService", method = "emailTemplateService")
	public TemplateService getTemplateService() {
		return null;
	}

	protected ResourceService resService;

	@CoinjemaDependency(type = "resourceService")
	public void setResourceService(ResourceService rs) {
		resService = rs;
	}

}