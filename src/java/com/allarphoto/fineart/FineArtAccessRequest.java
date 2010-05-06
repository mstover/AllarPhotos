package com.lazerinc.fineart;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.lazerinc.beans.User;
import com.lazerinc.beans.UserRequest;
import com.lazerinc.client.exceptions.InformationalException;
import com.lazerinc.ecommerce.UserGroup;
import com.lazerinc.servlet.actionhandler.user.UserAccessRequest;

public class FineArtAccessRequest extends UserAccessRequest {

	Map<String, String> emailsToGroup = new HashMap<String, String>();

	public FineArtAccessRequest() {
		emailsToGroup.put("lazerinc.com", "fineart");
	}

	@Override
	public String getName() {
		return "fineart_user_request";
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
				requestingUser, "fineart");
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
