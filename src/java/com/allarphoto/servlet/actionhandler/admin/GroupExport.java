package com.allarphoto.servlet.actionhandler.admin;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;
import strategiclibrary.service.webaction.ServletHandlerData;

import com.allarphoto.application.SecurityModel;
import com.allarphoto.client.beans.UserBean;
import com.allarphoto.ecommerce.UserGroup;
import com.allarphoto.servlet.actionhandler.ActionHandlerBase;

public class GroupExport extends ActionHandlerBase {

	public GroupExport() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return "group_export";
	}

	public void performAction(HandlerData info) throws ActionException {
		UserBean user = getUserBean(info);
		SecurityModel perms = user.getPermissions();
		UserGroup g = ugd.getGroup(info.getParameter("group_name", ""));
		info.setRequestBean("users", g.getUsers());
		((ServletHandlerData) info).getResponse().setContentType("text/csv");
	}

}
