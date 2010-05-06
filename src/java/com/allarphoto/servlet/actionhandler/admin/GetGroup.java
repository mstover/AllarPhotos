package com.lazerinc.servlet.actionhandler.admin;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.lazerinc.server.UserService;
import com.lazerinc.servlet.actionhandler.ActionHandlerBase;

public class GetGroup extends ActionHandlerBase {

	public GetGroup() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void performAction(HandlerData actionInfo) throws ActionException {
		UserService ugd = getUgd();
		actionInfo.setRequestBean("group", ugd.getGroup(actionInfo
				.getParameterAsInt("group_id", -1)));
	}

	public String getName() {
		return "get_group";
	}

}