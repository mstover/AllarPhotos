package com.allarphoto.servlet.actionhandler.admin;

import org.coinjema.context.CoinjemaDependency;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.allarphoto.client.exceptions.LazerwebException;
import com.allarphoto.ecommerce.UserGroup;
import com.allarphoto.server.ResourceService;
import com.allarphoto.servlet.actionhandler.ActionHandlerBase;
import com.allarphoto.utils.Resource;
import com.allarphoto.utils.Right;

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
