package com.allarphoto.security;

import java.util.Arrays;
import java.util.Collection;

import com.allarphoto.application.SecurityModel;
import com.allarphoto.beans.User;
import com.allarphoto.utils.Resource;
import com.allarphoto.utils.Rights;

public class VirtualGroupBasedSecurity extends AbstractSecurityModel {
	private static final long serialVersionUID = 1;

	User user;

	public VirtualGroupBasedSecurity(Collection<? extends SecurityModel> groups) {
		initRights(groups);
	}

	public VirtualGroupBasedSecurity(User user,
			Collection<? extends SecurityModel> groups) {
		this.user = user;
		initRights(groups);
	}

	public VirtualGroupBasedSecurity(GroupBasedSecurity[] groups) {
		initRights(Arrays.asList(groups));
	}

	private void initRights(Collection<? extends SecurityModel> groups) {
		for (SecurityModel group : groups) {
			for (Resource r : group.getAvailableResourceList()) {
				if (permissions.get(r) != null) {
					permissions.put(r, permissions.get(r).union(
							group.getPermissions(r)));
				} else
					permissions.put(r, group.getPermissions(r));
			}
		}
	}

	@Override
	protected Rights getRightsOverUser(Resource user) {
		if (this.user != null && user.getName().equals(this.user.getUsername())) {
			Rights r = new Rights();
			r.setAllRights(true);
			return r;
		}
		return super.getRightsOverUser(user);
	}

}
