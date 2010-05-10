/***********************************************************************************************************************
 * Copyright (C) 1999 Michael Stover This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA. Michael Stover can be reached via email at mstover1@rochester.rr.com or via snail mail at 130 Corwin
 * Rd. Rochester, NY 14610 The following exception to this license exists for Lazer Incorporated: Lazer Inc is excepted
 * from all limitations and requirements stipulated in the GPL. Lazer Inc. is the only entity allowed this limitation.
 * Lazer does have the right to sell this exception, if they choose, but they cannot grant additional exceptions to any
 * other entities.
 **********************************************************************************************************************/

package com.allarphoto.dbtools;

import org.coinjema.context.CoinjemaContext;

/*******************************************************************************
 * This class is used to connect and drive an Firebird database. No SQL is
 * needed to use this class - it can query, insert, update, and create tables
 * and autonumber primary keys. Use the TableDesign class to create a table.
 * 
 * @author Michael Stover
 * @version 1.0 10/13/1998 --------------------------------------------------
 */
public class FirebirdDB extends InterbaseDB {

	public FirebirdDB() {
		super();
	}

	public FirebirdDB(CoinjemaContext cc) {
		super(cc);
	}

} // End Class
