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

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/*******************************************************************************
 * Holds all the design values of a table. Column names, datatypes, primary key,
 * foreign keys, unique constraints, indexed columns. This class is used as
 * input to the DBConnect.createTable() method. Once designed, it never needs to
 * be messed with again.
 * 
 * @author Michael Stover
 * @version 1.0 <BR>
 ******************************************************************************/
public class TableDesign implements java.io.Serializable {
	private static final long serialVersionUID = 1;

	Hashtable columns;

	Hashtable references;

	Vector uniqueList, indexes;

	String primary;

	boolean primaryAuto = false;

	Hashtable nullConstraint, uniqueConstraint;

	String name;

	String test;

	/***************************************************************************
	 * Returns true if the column has a "NOT NULL" constraint. False Otherwise.
	 * 
	 * @param c
	 *            Name of column to check
	 * @return True if column has a NOT NULL constraint. False otherwise.
	 **************************************************************************/
	public boolean getNullConstraint(String c) {
		boolean returnValue = false;
		if (nullConstraint != null) {
			if (nullConstraint.containsKey(c)) {
				returnValue = ((Boolean) nullConstraint.get(c)).booleanValue();
			}
		}
		return returnValue;
	} // end of Method

	/***************************************************************************
	 * Returns true if the column has a "UNIQUE" constraint. False Otherwise.
	 * 
	 * @param c
	 *            Name of column to check
	 * @return True if column has a UNIQUE constraint. False otherwise.
	 **************************************************************************/
	public boolean getUniqueConstraint(String c) {
		boolean returnValue = false;
		if (uniqueConstraint != null) {
			if (uniqueConstraint.containsKey(c)) {
				returnValue = ((Boolean) uniqueConstraint.get(c))
						.booleanValue();
			}
		}
		return returnValue;
	} // end of Method

	/***************************************************************************
	 * Returns a list of columns that are indexed for the table.
	 * 
	 * @return Array of strings of names of all indexed columns
	 **************************************************************************/
	public String[] getIndexes() {
		if (indexes != null) {
			String[] list = new String[indexes.size()];
			indexes.copyInto(list);
			return list;
		} else
			return null;
	} // end of Method

	/***************************************************************************
	 * Returns a list of columns that make up a unique key for the table.
	 * 
	 * @return Array of strings of names of all columns that make up a unique
	 *         key for the table
	 **************************************************************************/
	public String[] getUniqueList() {
		if (uniqueList != null) {
			String[] list = new String[uniqueList.size()];
			uniqueList.copyInto(list);
			return list;
		} else
			return null;
	} // end of Method

	/***************************************************************************
	 * Returns the foreign reference of the desired column. The returned array
	 * has two values. The first is the name of the table that holds the
	 * referenced column. The second value holds the name of the referenced
	 * column. If the column name has no foreign key, "" and "" are returned.
	 * 
	 * @param c
	 *            Name of column to check for foreign references
	 * @return Array of 2 strings. The first is the name of the table
	 *         referenced. The second is the name of the column in that table
	 *         that is referenced. If there is no foreign reference, "" and ""
	 *         are returned in the array.
	 **************************************************************************/
	public String[] getReference(String c) {
		String[] returnValue = new String[2];
		returnValue[0] = new String("");
		returnValue[1] = new String("");
		if (references != null) {
			if (references.containsKey(c)) {
				ForeignKey ref = (ForeignKey) references.get(c);
				returnValue[0] = ref.getReferencedTable();
				returnValue[1] = ref.getReferencedTableColumn();
			}
		}
		return returnValue;
	} // end of Method

	/***************************************************************************
	 * Returns the boolean value indicating whether this table's primary key
	 * column has automatically generated numbers. If true, then the table
	 * should automatically generate a new value of the primary key of any
	 * inserted record. If false, then it should not do this.
	 * 
	 * @return True if the table has automatically generated primary key
	 *         numbers. False otherwise
	 **************************************************************************/
	public boolean getPrimaryAuto() {
		return primaryAuto;
	} // end of Method

	/***************************************************************************
	 * Returns the name of the primary Key column for this table.
	 * 
	 * @return String containing the name of the primary key column.
	 **************************************************************************/
	public String getPrimary() {
		return primary;
	} // end of Method

	/***************************************************************************
	 * Returns the int representing the data type of the column.
	 * 
	 * @param column -
	 *            name of the column to get data type of.
	 * @return int value representing the data type of the column.
	 *         java.sql.Types.VARCHAR if column is not present in table.
	 **************************************************************************/
	public int getDataTypeOfColumn(String c) {
		if (!columns.containsKey(c))
			return java.sql.Types.VARCHAR;
		ColumnTypeSize cts = (ColumnTypeSize) columns.get(c);
		return cts.getDataType();

	} // end of Method

	/***************************************************************************
	 * Returns the String representing the size of the column.
	 * 
	 * @param columnName -
	 *            name of column to get size of.
	 * @return String representing the size of the column. "(20)", for example. ""
	 *         is returned if size is not appropriate for the column.
	 **************************************************************************/
	public int getSizeOfColumn(String c) {
		if (!columns.containsKey(c))
			return 0;
		ColumnTypeSize cts = (ColumnTypeSize) columns.get(c);
		int size = cts.getSize();
		return size;
	} // end of Method

	/***************************************************************************
	 * Returns the name of the table.
	 * 
	 * @return Name of table
	 **************************************************************************/
	public String getName() {
		return name;
	} // End of Method

	/***************************************************************************
	 * Changes the name of the table.
	 * 
	 * @param n
	 *            New name of table
	 **************************************************************************/
	public void setName(String n) {
		name = new String(n);
	} // End of Method

	/***************************************************************************
	 * Returns an array of Strings representing the names of all the columns in
	 * this table.
	 * 
	 * @return Array of strings representing the names of all the columns in
	 *         this table.
	 **************************************************************************/
	public String[] getColumns() {
		Enumeration enumer = columns.keys();
		String[] columnNames = new String[columns.size()];
		int count = 0;
		while (enumer.hasMoreElements()) {
			columnNames[count++] = (String) enumer.nextElement();
		}
		return columnNames;
	} // end of Method

	/***************************************************************************
	 * Sets the columns type and size.
	 * 
	 * @param c
	 *            name of column
	 * @param t
	 *            datatype
	 * @param s
	 *            size of column
	 **************************************************************************/
	public void setColumnTypeSize(String c, int t, int s) {
		if (columns.containsKey(c)) {
			ColumnTypeSize cts = (ColumnTypeSize) columns.get(c);
			cts.setSize(s);
			cts.setDataType(t);
		} else
			columns.put(c, new ColumnTypeSize(t, s));
	} // end of Method

	/***************************************************************************
	 * Sets the columns type.
	 * 
	 * @param c
	 *            name of column
	 * @param t
	 *            type of column
	 **************************************************************************/
	public void setColumnType(String c, int t) {
		if (columns.containsKey(c)) {
			ColumnTypeSize cts = (ColumnTypeSize) columns.get(c);
			cts.setDataType(t);
		} else
			columns.put(c, new ColumnTypeSize(t));
	} // end of Method

	/***************************************************************************
	 * Sets the columns size.
	 * 
	 * @param c
	 *            name of column
	 * @param s
	 *            size of column
	 **************************************************************************/
	public void setColumnSize(String c, int s) {
		if (columns.containsKey(c)) {
			ColumnTypeSize cts = (ColumnTypeSize) columns.get(c);
			cts.setSize(s);
		} else
			columns.put(c, new ColumnTypeSize(java.sql.Types.VARCHAR, s));
	} // end of Method

	/***************************************************************************
	 * Sets the null constraint to a column. A boolean value determines if the
	 * column can take null values. True means it has a "NOT NULL" constraint.
	 * False means it has no constraint.
	 * 
	 * @param c
	 *            Name of column to set.
	 * @param b
	 *            Boolean representing whether column has "NOT NULL" constraint
	 *            or not.
	 **************************************************************************/
	public void setNullConstraint(String c, boolean b) {
		if (nullConstraint == null)
			nullConstraint = new Hashtable();
		else if (nullConstraint.containsKey(c)) {
			nullConstraint.remove(c);
		}
		nullConstraint.put(c, Boolean.valueOf(b));

	} // end of Method

	/***************************************************************************
	 * Sets the unique constraint to a column. A boolean value determines if the
	 * column can take null values. True means it has a "UNIQUE" constraint.
	 * False means it has no constraint.
	 * 
	 * @param c
	 *            Name of column to set.
	 * @param b
	 *            Boolean representing whether column has "UNIQUE" constraint or
	 *            not.
	 **************************************************************************/
	public void setUniqueConstraint(String c, boolean b) {
		if (uniqueConstraint == null)
			uniqueConstraint = new Hashtable();
		else if (uniqueConstraint.containsKey(c)) {
			uniqueConstraint.remove(c);
		}
		uniqueConstraint.put(c, Boolean.valueOf(b));

	} // end of Method

	/***************************************************************************
	 * Adds a foreign key flag to a column. Tells the database that the column
	 * refences another column from another table.
	 * 
	 * @param c
	 *            name of column that holds a foreign key
	 * @param t
	 *            Name of table being referenced by column
	 * @param tc
	 *            name of column being referenced. Must be a primary key in its
	 *            table (but this is not checked by this method).
	 **************************************************************************/
	public void setReferences(String c, String t, String tc) {
		if (references == null) {
			references = new Hashtable();
		}
		if (references.containsKey(c)) {
			ForeignKey fk = (ForeignKey) references.get(c);
			fk.setReferencedTable(t);
			fk.setReferencedTableColumn(tc);
		} else {
			references.put(c, new ForeignKey(c, t, tc));
		}
	} // end of Method

	/***************************************************************************
	 * Adds a column to the list of unique columns.
	 * 
	 * @param u
	 *            Name of column to be added into UNIQUE list.
	 **************************************************************************/
	public void addUniqueColumn(String u) {
		if (uniqueList == null)
			uniqueList = new Vector();
		uniqueList.addElement(u);
	} // end of Method

	/***************************************************************************
	 * Adds a column to the list of indexed columns.
	 * 
	 * @param u
	 *            Name of column to be added into INDEXED list.
	 **************************************************************************/
	public void addIndexedColumn(String u) {
		if (indexes == null)
			indexes = new Vector();
		if (!indexes.contains(u))
			indexes.addElement(u);
	} // end of Method

	/***************************************************************************
	 * Sets the primary column.
	 * 
	 * @param c
	 *            Name of primary key column
	 **************************************************************************/
	public void setPrimary(String c) {
		primary = new String(c);
	} // end of Method

	/***************************************************************************
	 * Sets whether the primary key is autoupdated on an insert. Defaults to
	 * true.
	 * 
	 * @param b
	 *            Boolean value indicating whether primary key column is
	 *            auto-updated
	 **************************************************************************/
	public void setPrimaryAuto(boolean b) {
		primaryAuto = b;
	} // end of Method

	/***************************************************************************
	 * Constructor for TableDesign class. Takes a string as the name of the
	 * table.
	 * 
	 * @param n
	 *            Name of table to be created
	 **************************************************************************/
	public TableDesign(String n) {
		primary = new String("");
		name = new String(n);
		columns = new Hashtable();
	} // End of Method

	/***************************************************************************
	 * Constructor for TableDesign class. Takes a list of column names and the
	 * name of the table.
	 * 
	 * @param n
	 *            Name of table
	 * @param cn
	 *            Array of column names to be included in table
	 **************************************************************************/
	public TableDesign(String n, String[] cn) {
		primary = new String("");
		name = new String(n);
		if (columns == null)
			columns = new Hashtable(cn.length, (float) .5);
		int count = 0;

		while (count < cn.length) {
			columns
					.put(cn[count++],
							new ColumnTypeSize(java.sql.Types.VARCHAR));
		}
	} // end of Method

	/***************************************************************************
	 * Constructor for TableDesign class. Takes a list of column names and a
	 * list of data types. Also takes the name of the table.
	 * 
	 * @param n
	 *            Name of table
	 * @param cn
	 *            Array of column names to be included in table
	 * @param t
	 *            Array of datatypes for each column, in a one-to-one
	 *            correspondence, using the java.sql.Types class's static
	 *            integer values
	 **************************************************************************/
	public TableDesign(String n, String[] cn, int[] t) {
		primary = new String("");
		name = new String(n);
		if (columns == null)
			columns = new Hashtable(cn.length, (float) .5);
		int count = 0;
		while (count < cn.length) {
			columns.put(cn[count], new ColumnTypeSize(t[count]));
			count++;
		}
	} // end of Method
} // end of Method
