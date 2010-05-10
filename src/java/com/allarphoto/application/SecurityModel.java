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

// Title: SecurityModel
// Author: Michael Stover
// Company: Lazer Inc.
package com.allarphoto.application;

import java.util.Collection;

import com.allarphoto.utils.Resource;
import com.allarphoto.utils.Right;
import com.allarphoto.utils.Rights;

public interface SecurityModel {

	public boolean getPermission(String resource, int type, String right);

	public boolean getPermission(String resource, int type, Right right);

	public Rights getPermissions(String resource, int type);

	public Collection<Resource> getAvailableResourceList();

	public Collection<Resource> getAvailableResourceList(int type);

	public Collection<Resource> getAvailableResourceList(int type, String right);

	public Collection<Resource> getAvailableResourceList(int type, Right right);

	public boolean getPermission(Resource resource, String right);

	public boolean getPermission(Resource resource, Right right);

	public Rights getPermissions(Resource resource);
	
	public boolean isSuperAdmin();

}
