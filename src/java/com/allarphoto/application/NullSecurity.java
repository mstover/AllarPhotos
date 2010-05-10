package com.allarphoto.application;

import java.util.Collection;
import java.util.LinkedList;

import com.allarphoto.utils.Resource;
import com.allarphoto.utils.Right;
import com.allarphoto.utils.Rights;

public class NullSecurity implements SecurityModel {

	public boolean getPermission(String resource, int type, String right) {
		return false;
	}

	public boolean getPermission(String resource, int type, Right right) {
		return false;
	}

	public Rights getPermissions(String resource, int type) {
		return null;
	}

	public Collection<Resource> getAvailableResourceList() {
		return new LinkedList<Resource>();
	}

	public Collection<Resource> getAvailableResourceList(int type) {
		return new LinkedList<Resource>();
	}

	public Collection<Resource> getAvailableResourceList(int type, String right) {
		return new LinkedList<Resource>();
	}

	public Collection<Resource> getAvailableResourceList(int type, Right right) {
		return new LinkedList<Resource>();
	}

	public boolean getPermission(Resource resource, String right) {
		return false;
	}

	public boolean isSuperAdmin() {
		return false;
	}

	public boolean getPermission(Resource resource, Right right) {
		return false;
	}

	public Rights getPermissions(Resource resource) {
		return null;
	}

}
