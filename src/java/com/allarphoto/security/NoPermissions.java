package com.allarphoto.security;

import java.util.Collection;
import java.util.Collections;

import com.allarphoto.application.SecurityModel;
import com.allarphoto.utils.Resource;
import com.allarphoto.utils.Right;
import com.allarphoto.utils.Rights;

public class NoPermissions implements SecurityModel {

	public Collection<Resource> getAvailableResourceList() {
		return Collections.EMPTY_LIST;
	}

	public Collection<Resource> getAvailableResourceList(int type) {
		return Collections.EMPTY_LIST;
	}

	public Collection<Resource> getAvailableResourceList(int type, String right) {
		return Collections.EMPTY_LIST;
	}

	public Collection<Resource> getAvailableResourceList(int type, Right right) {
		return Collections.EMPTY_LIST;
	}

	public boolean getPermission(String resource, int type, String right) {
		return false;
	}

	public boolean getPermission(String resource, int type, Right right) {
		return false;
	}

	public boolean getPermission(Resource resource, String right) {
		return false;
	}

	public boolean getPermission(Resource resource, Right right) {
		return false;
	}

	public boolean isSuperAdmin() {
		return false;
	}

	public Rights getPermissions(String resource, int type) {
		return new Rights(new Resource(resource, type));
	}

	public Rights getPermissions(Resource resource) {
		return new Rights(resource);
	}

}
