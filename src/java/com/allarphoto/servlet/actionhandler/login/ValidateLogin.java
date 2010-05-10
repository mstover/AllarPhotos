package com.allarphoto.servlet.actionhandler.login;

import static com.allarphoto.servlet.ActionConstants.ACTION_VALIDATE_LOGIN;
import strategiclibrary.service.webaction.HandlerData;

import com.allarphoto.client.beans.UserBean;
import com.allarphoto.client.exceptions.FatalException;
import com.allarphoto.client.exceptions.InformationalException;
import com.allarphoto.client.exceptions.LazerwebException;
import com.allarphoto.servlet.actionhandler.ActionHandlerBase;

/**
 * Checks to make sure the current user is correctly logged in. Otherwise, an
 * error is generated.
 * 
 * @author Administrator
 * @action action_validate_login
 */
public class ValidateLogin extends ActionHandlerBase {

	public ValidateLogin() {
	}

	public boolean requiresLogin() {
		return false;
	}

	public void performAction(HandlerData actionInfo) throws FatalException,
			InformationalException {
		UserBean user = getUserBean(actionInfo);
		if (user == null || user.getPermissions() == null) {
			getLog().debug("User has no permissions.");
			throw new InformationalException(
					LazerwebException.USER_NOT_LOGGED_IN);
		}
	}

	public String getName() {
		return ACTION_VALIDATE_LOGIN;
	}
}