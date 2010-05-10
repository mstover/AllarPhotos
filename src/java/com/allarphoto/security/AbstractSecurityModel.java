package com.allarphoto.security;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

import com.allarphoto.application.SecurityModel;
import com.allarphoto.ecommerce.UserGroup;
import com.allarphoto.server.ResourceService;
import com.allarphoto.server.UserService;
import com.allarphoto.utils.Resource;
import com.allarphoto.utils.Right;
import com.allarphoto.utils.Rights;

@CoinjemaObject
public abstract class AbstractSecurityModel implements SecurityModel,
		Serializable {
	transient Logger log;

	protected Map<Resource, Rights> permissions = new HashMap<Resource, Rights>();

	protected ResourceService resService;

	transient protected UserService ugd;

	public AbstractSecurityModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	/***************************************************************************
	 * Gets the list of resource names this SecurityModel has access to, given
	 * the type of resource and the right in question. This can be used to get a
	 * list of users it has Admin privileges for, or a list of groups, or
	 * datatables, or merchants, etc.....
	 * 
	 * @return A Set of resource names (Strings).
	 **************************************************************************/
	public Collection<Resource> getAvailableResourceList() {
		Resource superRes = superRes();
		if (permissions.get(superRes) != null
				&& permissions.get(superRes).getRight(Right.ADMIN))
			return resService.getResources();
		else
			return permissions.keySet();
	}

	protected Resource superRes() {
		return resService.getResource("all", Resource.DATABASE);
	}

	public void removeResource(Resource res) {
		permissions.remove(res);
	}

	/***************************************************************************
	 * Gets the list of resource names this SecurityModel has access to, given
	 * the type of resource in question. This can be used to get a list of users
	 * it has Admin privileges for, or a list of groups, or datatables, or
	 * merchants, etc.....
	 * 
	 * @param type
	 *            Type of resource of interest.
	 * @return A Set of resource names (Strings).
	 **************************************************************************/
	public Collection<Resource> getAvailableResourceList(int type) {
		Resource superRes = superRes();
		if (permissions.get(superRes) != null
				&& permissions.get(superRes).getRight(Right.ADMIN))
			return resService.getResources(type);
		else {
			Set<Resource> retVal = new HashSet<Resource>();
			for (Resource r : permissions.keySet()) {
				if (r.getType() == type)
					retVal.add(r);
			}
			return retVal;
		}
	}
	
	public boolean isSuperAdmin()
	{
		return getPermission("all",Resource.DATABASE,Right.ADMIN);
	}

	/***************************************************************************
	 * Gets the list of resource names this SecurityModel has access to, given
	 * the type of resource and the right in question. This can be used to get a
	 * list of users it has Admin privileges for, or a list of groups, or
	 * datatables, or merchants, etc.....
	 * 
	 * @param type
	 *            Type of resource of interest.
	 * @param right
	 *            Right of interest.
	 * @return A Set of resource names (Strings).
	 **************************************************************************/
	public Collection<Resource> getAvailableResourceList(int type, Right right) {
		Resource superRes = superRes();
		if (permissions.get(superRes) != null
				&& permissions.get(superRes).getRight(Right.ADMIN))
			return resService.getResources(type);
		else {
			Set<Resource> retVal = new HashSet<Resource>();
			for (Resource r : permissions.keySet()) {
				if (r.getType() == type && permissions.get(r).getRight(right))
					retVal.add(r);
			}
			return retVal;
		}
	}

	public Collection<Resource> getAvailableResourceList(int type, String right) {
		return getAvailableResourceList(type, Right.getRight(right));
	}

	/***************************************************************************
	 * Returns true if the user has the given right to the given resource, false
	 * otherwise.
	 * 
	 * @param resource
	 *            Name of resource requesting permission for.
	 * @param type
	 *            The type of resource it is.
	 * @param right
	 *            Name of the right of interest.
	 * @return True if this SecurityModel has permissions for this right and
	 *         resource, false otherwise.
	 **************************************************************************/
	public boolean getPermission(String resource, int type, Right right) {
		if (type == Resource.USER)
			return getPermission(new Resource(resource, type), right);
		else
			return getPermission(resService.getResource(resource, type), right);
	}

	public boolean getPermission(String resource, int type, String right) {
		try {
			return getPermission(resource, type, Right.getRight(right));
		} catch (Exception e) {
			log.error("error getting permission", e);
			return false;
		}
	}

	/***************************************************************************
	 * Returns true if the user has the given right to the given resource, false
	 * otherwise.
	 * 
	 * @param resource
	 *            Name of resource requesting permission for.
	 * @param type
	 *            The type of resource it is.
	 * @param right
	 *            Name of the right of interest.
	 * @return True if this SecurityModel has permissions for this right and
	 *         resource, false otherwise.
	 **************************************************************************/
	public boolean getPermission(Resource resource, Right right) {
		Rights all = permissions.get(superRes());
		if (resource == null) {
			if (all != null)
				return all.getRight(Right.ADMIN);
			else
				return false;
		}
		Rights perm = resource.getType() != Resource.USER ? permissions
				.get(resource) : getRightsOverUser(resource);
		return ((all != null && all.getRight(Right.ADMIN)) || (perm != null && perm
				.getRight(right)));
	}

	protected Rights getRightsOverUser(Resource user) {
		Collection<UserGroup> groups = ugd.getUser(user.getName()).getGroups();
		Rights r = new Rights(user);
		r.setAllRights((groups != null && groups.size() > 0) ? true : false);
		for (UserGroup g : groups) {
			if (getPermissions(g.getName(), Resource.GROUP) == null) {
				r.setAllRights(false);
				break;
			}
			Rights gr = new Rights(user, getPermissions(g.getName(),
					Resource.GROUP));
			r = r.intersect(gr);
		}
		return r;
	}

	public boolean getPermission(Resource resource, String right) {
		return getPermission(resource, Right.getRight(right));
	}

	/***************************************************************************
	 * Gets the Rights object associated with this resource, for this
	 * SecurityModel object.
	 * 
	 * @param resource
	 *            Name of resource.
	 * @param type
	 *            Type of resource.
	 * @return The Rights object associated with this resource for this
	 *         SecurityModel object.
	 **************************************************************************/
	public Rights getPermissions(String resource, int type) {
		if (type == Resource.USER)
			return getPermissions(new Resource(resource, type));
		else
			return getPermissions(resService.getResource(resource, type));
	}

	/***************************************************************************
	 * Gets the Rights object associated with this resource, for this
	 * SecurityModel object.
	 * 
	 * @param resource
	 *            Name of resource.
	 * @param type
	 *            Type of resource.
	 * @return The Rights object associated with this resource for this
	 *         SecurityModel object.
	 **************************************************************************/
	public Rights getPermissions(Resource resource) {
		Rights all = permissions.get(superRes());
		if (resource == null)
			return all;
		Rights perm = resource.getType() != Resource.USER ? permissions
				.get(resource) : getRightsOverUser(resource);
		if (all != null)
			return new Rights(resource, all);
		else
			return perm;
	}

	@CoinjemaDependency(type = "userService", method = "userService")
	public void setUserService(UserService ugd) {
		this.ugd = ugd;
	}

	@CoinjemaDependency(type = "resourceService")
	public void setResourceService(ResourceService rs) {
		resService = rs;
	}

	@CoinjemaDependency(alias = "log4j")
	public void setLog(Logger l) {
		log = l;
	}

}
