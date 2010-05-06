package com.lazerinc.servlet.actionhandler.admin;

import org.coinjema.context.CoinjemaDependency;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.lazerinc.client.exceptions.LazerwebException;
import com.lazerinc.ecommerce.UserGroup;
import com.lazerinc.server.ResourceService;
import com.lazerinc.servlet.actionhandler.ActionHandlerBase;
import com.lazerinc.utils.Resource;
import com.lazerinc.utils.Right;

public class UpdateRight extends ActionHandlerBase {

	public UpdateRight() {
		super();
	}

	public void performAction(HandlerData actionInfo) throws ActionException {
		UserGroup group = ugd.getGroup(actionInfo.getParameterAsInt("group_id",
				0));
		if (group != null) {
			Resource res = resService.getResource(actionInfo.getParameterAsInt(
					"resource_id", -1));
			if (res != null) {
				Right r = Right.getRight(actionInfo.getParameter("right"));
				if (r != Right.NONE) {
					boolean newVal = actionInfo.getParameterAsBool("value",
							group.getPermission(res, r));
					getUgd().updateGroupRights(group, res, r, newVal, null,
							getCurrentUserPerms(actionInfo));
				} else
					throw new LazerwebException(
							LazerwebException.INCOMPLETE_INFO);
			} else
				throw new LazerwebException(LazerwebException.INCOMPLETE_INFO);
		} else
			throw new LazerwebException(LazerwebException.INCOMPLETE_INFO);

	}

	public String getName() {
		return "update_right";
	}

	protected ResourceService resService;

	@CoinjemaDependency(type = "resourceService")
	public void setResourceService(ResourceService rs) {
		resService = rs;
	}

}
