package com.lazerinc.servlet.actionhandler.user;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.lazerinc.servlet.actionhandler.ActionHandlerBase;

public class SearchUsers extends ActionHandlerBase {

	public SearchUsers() {
		super();
	}

	public void performAction(HandlerData actionInfo) throws ActionException {
		actionInfo.setRequestBean("users", getUgd().searchUsers(
				actionInfo.getParameter("user_search_term", "NO SEARCH",
						this.emptyStrings)));
	}

	public String getName() {
		return "search_users";
	}

}
