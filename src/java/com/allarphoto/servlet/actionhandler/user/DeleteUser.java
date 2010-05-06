package com.lazerinc.servlet.actionhandler.user;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.lazerinc.ecommerce.CommerceUser;
import com.lazerinc.servlet.actionhandler.ActionHandlerBase;
import com.lazerinc.utils.WebBean;

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
