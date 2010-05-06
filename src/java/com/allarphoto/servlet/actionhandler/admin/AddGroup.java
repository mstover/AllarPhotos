package com.lazerinc.servlet.actionhandler.admin;

import org.coinjema.context.CoinjemaDependency;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.lazerinc.client.exceptions.LazerwebException;
import com.lazerinc.server.ResourceService;
import com.lazerinc.servlet.actionhandler.ActionHandlerBase;
import com.lazerinc.utils.Resource;
import com.lazerinc.utils.Right;

public class AddGroup extends ActionHandlerBase {

	public AddGroup() {
		super();
	}

	public void performAction(HandlerData actionInfo) throws ActionException {
		int[] adminGroups = actionInfo.getParameterValuesAsInt("admingroup_id",
				0);
		if (adminGroups.length > 0 && adminGroups[0] > -1) {
			String groupName = actionInfo.getParameter("name");
			getUgd().addGroup(groupName,
					getUgd().getGroup(adminGroups[0]).getName(),
					getCurrentUserPerms(actionInfo));
			for (int i = 1; i < adminGroups.length; i++) {
				if (adminGroups[i] > 0) {
					getUgd().updateGroupRights(
							getUgd().getGroup(adminGroups[i]),
							resService.getResource(groupName, Resource.GROUP),
							Right.ADMIN, true, null,
							getCurrentUserPerms(actionInfo));
				}
			}
			addMessage("Group " + groupName + " successfully added", groupName,
					actionInfo);
		} else {
			throw new LazerwebException(LazerwebException.INCOMPLETE_INFO);
		}
	}

	public String getName() {
		return "add_group";
	}

	protected ResourceService resService;

	@CoinjemaDependency(type = "resourceService")
	public void setResourceService(ResourceService rs) {
		resService = rs;
	}

}
