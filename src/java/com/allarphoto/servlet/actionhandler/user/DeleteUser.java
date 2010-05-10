package com.allarphoto.servlet.actionhandler.user;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.allarphoto.ecommerce.CommerceUser;
import com.allarphoto.servlet.actionhandler.ActionHandlerBase;
import com.allarphoto.utils.WebBean;

public class DeleteUser extends ActionHandlerBase {

	public void performAction(HandlerData info) throws ActionException {
		CommerceUser user = getUgd().getUser(
				WebBean.setValues(new CommerceUser(), info).getId());
		getUgd().deleteUser(user, getCurrentUserPerms(info));
	}

	public String getName() {
		return "delete_user";
	}

}
