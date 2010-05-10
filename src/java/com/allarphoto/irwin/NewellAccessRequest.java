package com.allarphoto.irwin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.allarphoto.beans.User;
import com.allarphoto.beans.UserRequest;
import com.allarphoto.client.exceptions.InformationalException;
import com.allarphoto.ecommerce.UserGroup;
import com.allarphoto.servlet.actionhandler.user.UserAccessRequest;

public class NewellAccessRequest extends UserAccessRequest {

	Map<String, String> emailsToGroup = new HashMap<String, String>();

	public NewellAccessRequest() {
		emailsToGroup.put("irwin.com", "newell-rubbermaid");
		emailsToGroup.put("bernzomatic.com", "newell-rubbermaid");
		emailsToGroup.put("shurline.com", "newell-rubbermaid");
		emailsToGroup.put("newellco.com", "newell-rubbermaid");
		emailsToGroup.put("americantool.com", "newell-rubbermaid");
		emailsToGroup.put("lenoxsaw.com", "newell-rubbermaid");
		emailsToGroup.put("irwintools.au", "newell-rubbermaid");
		emailsToGroup.put("irwin.com.br", "newell-rubbermaid");
		
	}

	@Override
	public String getName() {
		return "newell_user_request";
	}

	@Override
	protected void notifyAdmins(HandlerData info,
			Map<String, Set<String>> emailsToLibraries, User requestingUser) {
		// no notification necessary
	}

	@Override
	public void performAction(HandlerData info) throws ActionException {
		Map<String, Set<String>> emailsToLibraries = new HashMap<String, Set<String>>();
		User requestingUser = null;
		try {
		requestingUser = createUserRequest(info, emailsToLibraries,
				requestingUser, "irwin");
		} catch(InformationalException e)
		{
			if(e.getMessage().equals("NotAuthorizedError"))
			{
				if(info.getParameter("Company") != null)
				{
					this.getContainer().executeAction("action_user_request", info);
				}
				else throw e;
			}
		}
		notifyAdmins(info, emailsToLibraries, requestingUser);
	}

	@Override
	protected void notifyRequestingUser(HandlerData info, String library, UserRequest request) throws ActionException {
		if(request.getEmail() == null)
			{
			addMessage("A user already exists with that email address",request,info);
			return;
			}
		UserGroup gp = ugd.getGroup(emailsToGroup.get(getEmailHostname(request.getEmail()).toLowerCase()));
		if(gp != null)
		{
			if(request.grant(this.mapper, ugd, gp.getName(), ugd.getSecurity(ugd.getUser("mstover"))))
				ugd.emailNewUser(ugd.getUser(request.getUsername()), info);
			addMessage(
					"Your request for access has been received, and you should receive an email with your username and password shortly",
					request, info);
		}
		else {
			info.setUserBean("request",request);
			throw new InformationalException("NotAuthorizedError");
		}
	}

	@Override
	protected boolean isInfoIncomplete(UserRequest request) {
		return false;
	}
	
	private String getEmailHostname(String email) throws ActionException
	{
		return email.substring(email.indexOf("@")+1);
	}

}
