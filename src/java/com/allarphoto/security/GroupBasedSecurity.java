/**
 * 
 */
package com.allarphoto.security;

import java.io.ObjectInputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

import strategiclibrary.service.sql.ObjectMappingService;

import com.allarphoto.ecommerce.UserGroup;
import com.allarphoto.utils.Resource;
import com.allarphoto.utils.Right;
import com.allarphoto.utils.Rights;

@CoinjemaObject
public class GroupBasedSecurity extends AbstractSecurityModel {
	private static final long serialVersionUID = 1;

	transient Logger log;

	// protected static Resource superAdmin =
	// Resource.getResource("all",Resource.DATABASE);
	transient private UserGroup group;

	public GroupBasedSecurity(UserGroup group) {
		setGroup(group);
	}

	@CoinjemaDependency(alias = "log4j")
	public void setLog(Logger log) {
		this.log = log;
	}

	public String toString() {
		return "GroupBasedPermissions: " + permissions.toString();
	}

	private void setGroup(UserGroup g) {
		group = g;
		if (mapper != null)
			initRights(g);
	}

	/***************************************************************************
	 * Gets a Map object showing the permissions a given group has for a given
	 * product table.
	 * 
	 * @param pt
	 *            Product table name.
	 * @param group
	 *            Name of group in question.
	 * @return A Map object detailing it's permissions, in a form that can be
	 *         plugged directly into a Rights object.
	 **************************************************************************/
	private void initRights(UserGroup group) {
		Map values = new HashMap();
		values.put("group", group);
		for (Rights rght : (Collection<Rights>) mapper.getObjects(
				"getRights.sql", values)) {
			if (rght.getResource().getType() == Resource.DATABASE
					&& rght.getResource().getName().equals("all")
					&& rght.getRight(Right.ADMIN)) {
				permissions.put(rght.getResource(), rght);
				break;
			}
			permissions.put(rght.getResource(), rght);
		}
	}

	public void updateRight(Resource res, Right r, boolean newVal) {
		Rights rights = permissions.get(res);
		if (rights == null) {
			rights = new Rights(res);
			permissions.put(res, rights);
		}
		rights.setRight(r, newVal);
	}

	private void readObject(ObjectInputStream in) {
		try {
			in.defaultReadObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@CoinjemaDependency(type = "objectMappingService", order = CoinjemaDependency.Order.LAST)
	public void setMapper(ObjectMappingService m) {
		mapper = m;
		if (group != null)
			initRights(group);
	}

	ObjectMappingService mapper;
}