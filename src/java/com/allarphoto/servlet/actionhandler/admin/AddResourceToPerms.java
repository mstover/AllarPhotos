package com.lazerinc.servlet.actionhandler.admin;

import org.coinjema.context.CoinjemaDependency;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.lazerinc.ecommerce.UserGroup;
import com.lazerinc.server.ResourceService;
import com.lazerinc.servlet.actionhandler.ActionHandlerBase;
import com.lazerinc.utils.Resource;
import com.lazerinc.utils.Right;

public class AddResourceToPerms extends ActionHandlerBase {

	public AddResourceToPerms() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void performAction(HandlerData actionInfo) throws ActionException {
		UserGroup group = getUgd().getGroup(
				actionInfo.getParameterAsInt("group_id", -1));
		if (group != null) {
			Resource res = resService.getResource(actionInfo
					.getParameter("resource_name"), actionInfo
					.getParameterAsInt("resource_type", -1));
			if (res != null) {
				if (res.getType() != Resource.DATATABLE
						&& res.getType() != Resource.PROTECTED_FIELD) {
					getUgd().updateGroupRights(group, res, Right.ADMIN, true,
							null, getCurrentUserPerms(actionInfo));
				} else {
					getUgd().updateGroupRights(group, res, Right.READ, true,
							null, getCurrentUserPerms(actionInfo));
					getUgd().updateGroupRights(group, res, Right.ORDER, true,
							null, getCurrentUserPerms(actionInfo));
					getUgd().updateGroupRights(group, res, Right.DOWNLOAD,
							true, null, getCurrentUserPerms(actionInfo));
				}
			}
		}

	}

	public String getName() {
		return "add_resource_to_perms";
	}

	protected ResourceService resService;

	@CoinjemaDependency(type = "resourceService")
	public void setResourceService(ResourceService rs) {
		resService = rs;
	}

}
