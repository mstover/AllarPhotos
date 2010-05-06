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

package com.lazerinc.dbtools;

/*******************************************************************************
 * Class to store a description of a foreign key reference in a database. This
 * class is primarily used by the TableDesign class.
 * 
 * @author Michael Stover
 * @version 1.0 10/13/1998
 ******************************************************************************/
public class ForeignKey implements java.io.Serializable {
	private static final long serialVersionUID = 1;

	String columnName;

	String referencedTable;

	String referencedTableColumn;

	/***************************************************************************
	 * Constructor for a foreign key table design description.
	 * 
	 * @param c
	 *            name of column that holds the foreign key
	 * @param refT
	 *            table that holds the column referenced by the foreign key
	 * @param refTC
	 *            column referenced.
	 **************************************************************************/
	public ForeignKey(String c, String refT, String refTC) {
		columnName = new String(c);
		referencedTable = new String(refT);
		referencedTableColumn = new String(refTC);
	} // end of Method

	/***************************************************************************
	 * Returns the Name of the column holding the foreign key.
	 * 
	 * @return String representing the name of the column that references the
	 *         foreign key.
	 **************************************************************************/
	public String getColumnName() {
		return columnName;
	} // end of Method

	/***************************************************************************
	 * Returns the Name of the Table referenced by the foreign key.
	 * 
	 * @return String representing name of table being referenced.
	 **************************************************************************/
	public String getReferencedTable() {
		return referencedTable;
	} // end of Method

	/***************************************************************************
	 * Returns the Name of the column referenced by the foreign key.
	 * 
	 * @return String representing name of column being referenced.
	 **************************************************************************/
	public String getReferencedTableColumn() {
		return referencedTableColumn;
	} // end of Method

	/***************************************************************************
	 * Sets the value of the referenced column.
	 * 
	 * @param columnName
	 *            Name of referenced table.
	 **************************************************************************/
	public void setReferencedTableColumn(String columnName) {
		referencedTableColumn = new String(columnName);
	} // end of Method

	/***************************************************************************
	 * Sets the value of the referenced table.
	 * 
	 * @param tableName
	 *            Name of referenced table.
	 **************************************************************************/
	public void setReferencedTable(String tableName) {
		referencedTable = new String(tableName);
	} // end of Method

} // end of Method
