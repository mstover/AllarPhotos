/*******************************************************************************
 * Copyright (C) 1999 Michael Stover This program is free software; you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version. This program is distributed
 * in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details. You should have received
 * a copy of the GNU General Public License along with this program; if not,
 * write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA. Michael Stover can be reached via email at
 * mstover1@rochester.rr.com or via snail mail at 130 Corwin Rd. Rochester, NY
 * 14610 The following exception to this license exists for Lazer Incorporated:
 * Lazer Inc is excepted from all limitations and requirements stipulated in the
 * GPL. Lazer Inc. is the only entity allowed this limitation. Lazer does have
 * the right to sell this exception, if they choose, but they cannot grant
 * additional exceptions to any other entities.
 ******************************************************************************/

// Title: UserGroup
// Author: Michael Stover
// Company: Lazer Inc.
package com.lazerinc.ecommerce;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;

import org.coinjema.context.CoinjemaDynamic;

import strategiclibrary.service.cache.CacheService;

import com.lazerinc.application.SecurityModel;
import com.lazerinc.security.GroupBasedSecurity;
import com.lazerinc.utils.Resource;
import com.lazerinc.utils.Right;
import com.lazerinc.utils.Rights;

public class UserGroup implements Serializable, Comparable<UserGroup>,
		SecurityModel {
	private static final long serialVersionUID = 1;

	GroupBasedSecurity permissions;

	public UserGroup() {
	}

	private synchronized void initRights() {
		if (permissions == null)
			permissions = new GroupBasedSecurity(this);
	}

	void readObject(ObjectInputStream ois) throws ClassNotFoundException,
			IOException {
		ois.defaultReadObject();
	}

	void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
	}

	public void setId(int newId) {
		id = newId;
	}

	public int getId() {
		return id;
	}

	public void setName(String newName) {
		name = newName;
	}

	public String getName() {
		return name;
	}

	int id;

	String name;

	String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int compareTo(UserGroup o) {
		return getName().toUpperCase().compareTo(o.getName().toUpperCase());
	}

	public boolean isSuperAdmin() {
		return permissions.isSuperAdmin();
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final UserGroup other = (UserGroup) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public void updateRight(Resource res, Right r, boolean newVal) {
		initRights();
		permissions.updateRight(res, r, newVal);
	}

	public Collection<CommerceUser> getUsers() {
		return getUserCache().getCache(CommerceUser.class).getCachedObjects(
				"group", this);
	}

	public void removeResource(Resource r) {
		initRights();
		permissions.removeResource(r);
	}

	@CoinjemaDynamic(type = "cacheService")
	CacheService getUserCache() {
		return null;
	}

	public Collection<Resource> getAvailableResourceList() {
		initRights();
		return permissions.getAvailableResourceList();
	}

	public Collection<Resource> getAvailableResourceList(int type, Right right) {
		initRights();
		return permissions.getAvailableResourceList(type, right);
	}

	public Collection<Resource> getAvailableResourceList(int type, String right) {
		initRights();
		return permissions.getAvailableResourceList(type, right);
	}

	public Collection<Resource> getAvailableResourceList(int type) {
		initRights();
		return permissions.getAvailableResourceList(type);
	}

	public boolean getPermission(Resource resource, Right right) {
		initRights();
		return permissions.getPermission(resource, right);
	}

	public boolean getPermission(Resource resource, String right) {
		initRights();
		return permissions.getPermission(resource, right);
	}

	public boolean getPermission(String resource, int type, Right right) {
		initRights();
		return permissions.getPermission(resource, type, right);
	}

	public boolean getPermission(String resource, int type, String right) {
		initRights();
		return permissions.getPermission(resource, type, right);
	}

	public Rights getPermissions(Resource resource) {
		initRights();
		return permissions.getPermissions(resource);
	}

	public Rights getPermissions(String resource, int type) {
		initRights();
		return permissions.getPermissions(resource, type);
	}

	@Override
	public String toString() {
		return getName();
	}

	public GroupBasedSecurity getPermissions() {
		initRights();
		return permissions;
	}
}
