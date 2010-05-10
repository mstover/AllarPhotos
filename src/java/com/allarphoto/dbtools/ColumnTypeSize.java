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

/*******************************************************************************
 * This class holds the data type and size of a column in a database table. This
 * class is used primarily by the TableDesign class.
 * 
 * @author Michael Stover
 * @version 1.0 10/13/1998
 ******************************************************************************/
public class ColumnTypeSize implements java.io.Serializable {
	private static final long serialVersionUID = 1;

	int dataType;

	int size;

	/***************************************************************************
	 * Sets the size of the column. This is mostly relevant only for columns of
	 * type VARCHAR, or other text based fields.
	 * 
	 * @param s
	 *            Size of column
	 **************************************************************************/
	public void setSize(int s) {
		size = s;
	} // end of Method

	/***************************************************************************
	 * Sets the dataType of the column. Using the integer values from
	 * java.sql.Types.
	 * 
	 * @param t
	 *            Int value indicating the datatype of the table column
	 **************************************************************************/
	public void setDataType(int t) {
		dataType = t;
	} // end of Method

	/***************************************************************************
	 * Returns the integer representing the data type of the column using
	 * java.sql.Types.
	 * 
	 * @return int representing the datatype of the column.
	 **************************************************************************/
	public int getDataType() {
		return dataType;
	} // end of Method

	/***************************************************************************
	 * Returns the size, in bytes, of the column. Returns 0 if not applicable.
	 * 
	 * @return int representing the size of the column. Returns 0 if not
	 *         applicable.
	 **************************************************************************/
	public int getSize() {
		return size;
	} // end of Method

	/***************************************************************************
	 * Constructor for ColumnTypeSize.
	 * 
	 * @param dt
	 *            int indicating datatype of column (from java.sql.Types class)
	 * @param s
	 *            int indicating size of column.
	 **************************************************************************/
	public ColumnTypeSize(int dt, int s) {
		dataType = dt;
		size = s;
	} // end of Method

	/***************************************************************************
	 * Constructor for ColumnTypeSize.
	 * 
	 * @param dt
	 *            int indicating datatype of column (from java.sql.Types class)
	 **************************************************************************/
	public ColumnTypeSize(int dt) {
		dataType = dt;
		size = 255;
	} // end of Method
} // end of Method
