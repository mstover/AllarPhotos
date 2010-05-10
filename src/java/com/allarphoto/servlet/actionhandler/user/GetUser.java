package com.allarphoto.servlet.actionhandler.user;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.allarphoto.servlet.actionhandler.ActionHandlerBase;

public class GetUser extends ActionHandlerBase {

	public GetUser() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void performAction(HandlerData actionInfo) throws ActionException {
		actionInfo.setRequestBean("editUser", getUgd().getUser(
				actionInfo.getParameterAsInt("user_id", 0)));
	}

	public String getName() {
		return "get_user";
	}

}
