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

// Title: Rights
// Author: Michael Stover
// Company: Lazer Inc.
package com.lazerinc.utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Set;
import java.util.Map.Entry;

import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

import com.lazerinc.server.ResourceService;

@CoinjemaObject
public class Rights implements Serializable {
	private static final long serialVersionUID = 1;

	static String[] rightNameList = new String[] { Right.READ.value(),
			Right.ORDER.value(), Right.DOWNLOAD.value(),
			Right.DOWNLOAD_ORIG.value(), Right.ADMIN.value(),
			Right.UPLOAD.value() };

	boolean read = false, order = false, download = false, admin = false,
			none = false, upload = false, download_orig = false;;

	public Rights(Resource res) {
		resource = res;
	}

	public Rights(Resource res, Rights r) {
		resource = res;
		read = r.read;
		order = r.order;
		download = r.download;
		admin = r.admin;
		upload = r.upload;
		download_orig = r.download_orig;
	}

	public Rights() {

	}

	void readObject(ObjectInputStream ois) throws ClassNotFoundException,
			IOException {
		ois.defaultReadObject();
	}

	void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource res) {
		if (res.getId() > 0 && res.getName() == null)
			resource = resService.getResource(res.getId());
		else
			resource = res;
	}

	protected ResourceService resService;

	@CoinjemaDependency(type = "resourceService")
	public void setResourceService(ResourceService rs) {
		resService = rs;
	}

	public String getResourceName() {
		return resource.getName();
	}

	public int getResourceType() {
		return resource.getType();
	}

	public boolean hasAll() {
		return read && order && download && admin && upload && download_orig;
	}

	public boolean hasAny() {
		return read || download_orig || order || download || admin || upload;
	}

	public void setRights(java.util.Map newRights) {
		for (Entry e : (Set<Entry>) newRights.entrySet()) {
			setRight((String) e.getKey(), (Boolean) e.getValue());
		}
	}

	public void setAllRights(boolean r) {
		read = order = download = admin = upload = download_orig = r;
	}

	public Rights union(Rights r) {
		if (r == null)
			return this;
		if (resource.equals(r.getResource())) {
			Rights combined = new Rights(resource);
			combined.admin = admin || r.admin;
			combined.order = order || r.order;
			combined.download = download || r.download;
			combined.read = read || r.read;
			combined.upload = upload || r.upload;
			combined.download_orig = download_orig || r.download_orig;
			combined.none = none && r.none;
			return combined;
		}
		return this;
	}

	public Rights intersect(Rights r) {
		if (r == null)
			return new Rights(getResource());
		if (resource.equals(r.getResource())) {
			Rights combined = new Rights(resource);
			combined.admin = admin && r.admin;
			combined.order = order && r.order;
			combined.download = download && r.download;
			combined.read = read && r.read;
			combined.upload = upload && r.upload;
			combined.download_orig = download_orig && r.download_orig;
			combined.none = none || r.none;
			return combined;
		}
		return this;
	}

	/***************************************************************************
	 * Gets a particular right for the resource.
	 * 
	 * @param right
	 *            Name of right to get.
	 * @return true if has right, false otherwise.
	 **************************************************************************/
	public boolean getRight(String right) {
		return getRight(Right.getRight(right));
	}

	public boolean getRight(Right right) {
		switch (right) {
		case READ:
			return read;
		case ORDER:
			return order;
		case DOWNLOAD:
			return download;
		case DOWNLOAD_ORIG:
			return download_orig;
		case ADMIN:
			return admin;
		case UPLOAD:
			return upload;
		default:
			return none;
		}
	}

	/***************************************************************************
	 * Gets a particular right for the resource.
	 * 
	 * @param right
	 *            Name of right to get.
	 * @return true if has right, false otherwise.
	 **************************************************************************/
	public void setRight(Right right, boolean value) {
		switch (right) {
		case READ:
			read = value;
			break;
		case ORDER:
			order = value;
			break;
		case DOWNLOAD:
			download = value;
			break;
		case DOWNLOAD_ORIG:
			download_orig = value;
			break;
		case ADMIN:
			admin = value;
			break;
		case UPLOAD:
			upload = value;
			break;
		}
	}

	/***************************************************************************
	 * Gets a particular right for the resource.
	 * 
	 * @param right
	 *            Name of right to get.
	 * @return true if has right, false otherwise.
	 **************************************************************************/
	public void setRight(String right, boolean value) {
		setRight(Right.getRight(right), value);
	}

	/***************************************************************************
	 * Returns a list of the rights dealt with in this Rights object.
	 * 
	 * @return Array of Strings of right names.
	 **************************************************************************/
	public String[] getRightNames() {
		return rightNameList;
	}

	public String toString() {
		String n = System.getProperty("line.separator");
		StringBuffer s = new StringBuffer();
		if (resource != null) {
			s.append("resource = " + resource.getName());
			s.append("{" + n);
			s.append("Type = ");
			switch (resource.getType()) {
			case GROUP:
				s.append("Group");
				break;
			case USER:
				s.append("User");
				break;
			case DATATABLE:
				s.append("DataTable");
				break;
			case MERCHANT:
				s.append("Merchant");
				break;
			case DATABASE:
				s.append("Database");
				break;
			}
		}
		s.append(n);
		s.append("List of Rights" + n);
		s.append("read = " + read);
		s.append("order = " + order);
		s.append("download = " + download);
		s.append("download originals = " + download_orig);
		s.append("admin = " + admin);
		s.append("upload = " + upload);
		s.append(n);
		s.append("}");
		return s.toString();
	}

	private Resource resource;

	public static final int NA = 0;

	public static final int GROUP = 1;

	public static final int USER = 2;

	public static final int DATATABLE = 3;

	public static final int MERCHANT = 4;

	public static final int PRICEKEY = 5;

	public static final int DATABASE = 6;

	public static final String LIGHTBOX = "lightbox";
}
