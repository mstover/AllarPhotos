package com.lazerinc.servlet.actionhandler.login;

import static com.lazerinc.servlet.ActionConstants.ACTION_LOGOUT;

import java.util.Map;

import strategiclibrary.service.webaction.HandlerData;

import com.lazerinc.servlet.actionhandler.ActionHandlerBase;

/**
 * The logout action clears all session information, which effectively means the
 * user will have to login again to gain access to the system.
 * 
 * @author Michael Stover
 * @version 1.0
 * @action action_logout
 */

public class Logout extends ActionHandlerBase {

	public Logout() {
	}

	public String getErrorPage(Map parm1) {
		return null;
	}

	public void performAction(HandlerData actionInfo)
			throws com.lazerinc.client.exceptions.FatalException,
			com.lazerinc.client.exceptions.InformationalException {
		for (String o : actionInfo.getBeanMap().keySet()) {
			getLog().debug("Removing bean " + o + " from user's session");
			if ("user".equals(o) || "productsFound".equals(o)
					|| "cart".equals(o) || "coinjemaContext".equals(o))
				actionInfo.removeUserBean(o);
		}
	}

	public String getName() {
		return ACTION_LOGOUT;
	}
}