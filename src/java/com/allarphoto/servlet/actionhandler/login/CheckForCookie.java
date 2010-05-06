package com.lazerinc.servlet.actionhandler.login;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.lazerinc.client.exceptions.InformationalException;
import com.lazerinc.servlet.actionhandler.ActionHandlerBase;

public class CheckForCookie extends ActionHandlerBase {

	public CheckForCookie() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return "check_for_login_cookie";
	}

	public void performAction(HandlerData info) throws ActionException {
		if (info.getCookie("username") != null
				&& info.getCookie("password") != null
				&& info.getBean("ignoreCookie") == null)
			throw new InformationalException("LoginCookieExists");

	}

}
