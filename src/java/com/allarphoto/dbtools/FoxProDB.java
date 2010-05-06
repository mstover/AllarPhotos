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

import java.sql.Connection;
import java.sql.Statement;
import java.sql.Types;

import org.coinjema.context.CoinjemaContext;

import com.lazerinc.utils.Data;

/*******************************************************************************
 * This class is used to connect and drive an ODBC database. No SQL is needed to
 * use this class - it can query, insert, update, and create tables and
 * autonumber primary keys. Use the TableDesign class to create a table.
 * 
 * @author Michael Stover
 * @version 1.0 10/13/1998 --------------------------------------------------
 */
public class FoxProDB extends DBConnect {

	String driverString = new String("sun.jdbc.odbc.JdbcOdbcDriver");

	String schema;

	public FoxProDB() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FoxProDB(CoinjemaContext cc) {
		super(cc);
		// TODO Auto-generated constructor stub
	}

	/***************************************************************************
	 * Takes a string to be sent to database and surrounds it with single quotes
	 * or double quotes, depending on what is needed. (depends on what
	 * characters are in the string. If the string has both single and double
	 * quotes in it, the double quotes are stripped out...
	 * 
	 * @param value
	 *            String to be cleaned.
	 * @return Cleaned up string ready for database use.
	 **************************************************************************/
	public String cleanString(String value) {
		int index;
		if (value != null) {
			value = value.trim();
			while ((index = value.indexOf("\'")) > -1) {
				if ((index + 1) < value.length() && index > 0)
					value = value.substring(0, index)
							+ value.substring(index + 1, value.length());
				else if (index == 0)
					value = value.substring(1, value.length());
				else
					value = value.substring(0, index);
			}
			value = "'" + value + "'";
		} else
			value = "'N/A'";
		return value;
	}

	/***************************************************************************
	 * Inserts data into a database table using the INSERT INTO SQL command.
	 * Returns number of rows inserted.
	 * 
	 * @param table
	 *            Name of table inserting into
	 * @param columns
	 *            Array of column names to insert into.
	 * @param values
	 *            Array of values inserting. Integers can be part of the string.
	 *            Strings need to be surrounded by single quotes.
	 * @return int representing number of rows inserted (should be 1, 0 means
	 *         failure)
	 **************************************************************************/
	public int insert(String tableName, String[] columns, String[] values) {
		StringBuffer queryString = new StringBuffer("INSERT INTO " + tableName
				+ "(");
		int returnValue = 0;
		try {

			int count = 0;
			while (count < columns.length) {
				queryString.append(columns[count++]);
				if (count < columns.length)
					queryString.append(",");

			}
			queryString.append(") VALUES(");
			count = 0;
			while (count < values.length) {
				queryString.append(values[count++]);
				if (count < values.length)
					queryString.append(",");
			}
			queryString.append(")");

			returnValue = executeUpdate(queryString.toString());
		} catch (Exception e) {
			System.out.println(queryString.toString());
			throw new RuntimeException(e);
		}
		return returnValue;
	} // end of Method

	/***************************************************************************
	 * Updates data in a database table using the UPDATE SQL command. Returns
	 * number of rows updated.
	 * 
	 * @param table
	 *            name of table inserting into
	 * @param columns
	 *            array of column names to update.
	 * @param values
	 *            array of values updating. Integers can be part of the string.
	 *            Strings need to be surrounded by single quotes.
	 * @param whereClause
	 *            the value of the WHERE clause (eg "leader_id=3")
	 * @return int representing number of rows updated.
	 **************************************************************************/
	public int update(String tableName, String[] columns, String[] values,
			String whereClause) {
		int returnValue = 0;

		StringBuffer queryString = new StringBuffer("UPDATE " + tableName
				+ " SET ");
		int count = 0;
		if (columns.length == values.length) {
			while (count < columns.length) {
				queryString.append(columns[count] + "=" + values[count]);
				count++;
				if (count < columns.length)
					queryString.append(", ");
			}
			queryString.append(" WHERE " + whereClause);
			try {
				returnValue = executeUpdate(queryString.toString());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return returnValue;
	} // end of Method

	/***************************************************************************
	 * Takes a TableDesign object and returns the string that sets a column type
	 * and size.
	 * 
	 * @param table
	 *            table design class object
	 * @param c
	 *            name of column in the table design object
	 * @return String giving the column type in SQL.
	 **************************************************************************/
	public String setType(TableDesign table, String c) {
		String returnValue;
		int type = table.getDataTypeOfColumn(c);
		int size = table.getSizeOfColumn(c);
		switch (type) {
		case Types.BIGINT:
			returnValue = new String("N(20,0)");
			break;
		case Types.BIT:
			returnValue = new String("L");
			break;
		case Types.CHAR:
			returnValue = new String("C(1)");
			break;
		case Types.DATE:
			returnValue = new String("D");
			break;
		case Types.DECIMAL:
			returnValue = new String("F(16,6)");
			break;
		case Types.DOUBLE:
			returnValue = new String("B(12)");
			break;
		case Types.FLOAT:
			returnValue = new String("F(16,6)");
			break;
		case Types.INTEGER:
			returnValue = new String("N(10,0)");
			break;
		case Types.LONGVARCHAR:
			returnValue = new String("C(1000)");
			break;
		case Types.REAL:
			returnValue = new String("F(16,6");
			break;
		case Types.SMALLINT:
			returnValue = new String("N(5)");
			break;
		case Types.TINYINT:
			returnValue = new String("N(5)");
			break;
		case Types.VARCHAR:
			returnValue = new String("C(" + size + ")");
			break;
		default:
			returnValue = new String("C(20)");
			break;
		}
		return returnValue.toString();
	}

	/***************************************************************************
	 * Creates a table in the database.
	 * 
	 * @param table
	 *            A TableDesign object that fully describes the table to be
	 *            created.
	 * @return True if successful, False if not
	 **************************************************************************/
	public boolean createTable(TableDesign table) {
		boolean returnValue = true;
		StringBuffer queryString = new StringBuffer("CREATE TABLE "
				+ table.getName() + " (");
		String[] references;
		String[] unique;
		Statement query;
		int count = 0;
		String[] columns = table.getColumns();
		while (count < columns.length) {
			queryString.append(columns[count] + " ");
			queryString.append(setType(table, columns[count]));
			if (columns[count].equals(table.getPrimary()))
				queryString.append(" PRIMARY KEY");
			else if (table.getNullConstraint(columns[count]))
				queryString.append(" NOT NULL");
			else {
				references = table.getReference(columns[count]);
				if (!references[0].equals(""))
					queryString.append(" REFERENCES " + references[0] + "TAG"
							+ references[1]);
			}
			count++;
			if (count < columns.length)
				queryString.append(",");
		}
		if ((unique = table.getUniqueList()) != null) {
			queryString.append(",");
			queryString.append("UNIQUE ");
			count = 0;
			while (count < unique.length) {
				queryString.append(unique[count++]);
				if (count < unique.length)
					queryString.append(",");
			}
			queryString
					.append(" TAG " + table.getName().substring(0, 7) + "_u");
		}
		queryString.append(")");
		try {
			executeUpdate(queryString.toString());
			createPrimaryAutoNumber(table);
			returnValue = true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return returnValue;
	} // end of Method

	/***************************************************************************
	 * For ODBC databases, does nothing since the autonumber trigger should
	 * already have been created.
	 * 
	 * @param table
	 *            table to create the trigger for.
	 * @return Always returns true.
	 **************************************************************************/
	public boolean createPrimaryAutoNumber(TableDesign table) {
		boolean returnValue = true;
		StringBuffer queryString = new StringBuffer("");
		queryString.append("CREATE TRIGGER ON " + table.getName());
		queryString.append(" FOR INSERT AS FUNCTION insert_trigger \n");
		queryString.append("COUNT \n\tALL\n");
		queryString.append("\tTO ct \n");
		queryString.append("UPDATE " + table.getName() + " SET "
				+ table.getPrimary() + "=ct" + " WHERE " + table.getPrimary()
				+ " EMPTY()");
		queryString.append("RETURN true");
		try {
			executeUpdate(queryString.toString());
			returnValue = true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return returnValue;
	} // End of Method

	/***************************************************************************
	 * Creates the index for a table using a TableDesign object.
	 * 
	 * @param table
	 *            TableDesign object giving specifications for the creation of
	 *            the table
	 * @throws java.sql.SQLException
	 **************************************************************************/
	public void createIndex(TableDesign table) {
		String[] indexedColumns = table.getIndexes();
		if (indexedColumns != null) {
			StringBuffer queryString = new StringBuffer("CREATE INDEX "
					+ table.getName() + indexSuffix);
			queryString.append(" ON " + table.getName() + " (");
			int count = 0;

			while (count < indexedColumns.length) {
				queryString.append(indexedColumns[count++]);
				if (count < indexedColumns.length)
					queryString.append(",");
			}
			queryString.append(")");
			try {
				executeUpdate(queryString.toString());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	} // End Method

	/***************************************************************************
	 * Drops the index for a table using a TableDesign object.
	 * 
	 * @param table
	 *            TableDesign object giving specifications for the creation of
	 *            the table
	 * @throws java.sql.SQLException
	 **************************************************************************/
	public void dropIndex(TableDesign table) {
		String[] indexedColumns = table.getIndexes();
		if (indexedColumns != null) {
			Connection connection;
			StringBuffer queryString = new StringBuffer("DROP INDEX "
					+ table.getName() + indexSuffix + " ON " + table.getName());
			try {
				executeUpdate(queryString.toString());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	} // End Method

	/***************************************************************************
	 * Creates the UNIQUE index for a table using a TableDesign object.
	 * 
	 * @param table
	 *            TableDesign object giving specifications for the creation of
	 *            the table
	 * @throws java.sql.SQLException
	 **************************************************************************/
	public void createUniqueIndex(TableDesign table) {
		String[] indexedColumns = table.getUniqueList();
		if (indexedColumns != null) {
			StringBuffer queryString = new StringBuffer("CREATE UNIQUE INDEX "
					+ table.getName() + uniqueIndexSuffix);
			queryString.append(" ON " + table.getName() + " (");
			int count = 0;

			while (count < indexedColumns.length) {
				queryString.append(indexedColumns[count++]);
				if (count < indexedColumns.length)
					queryString.append(",");
			}
			queryString.append(")");
			try {
				executeUpdate(queryString.toString());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	} // End Method

	/***************************************************************************
	 * Drops the UNIQUE index for a table using a TableDesign object.
	 * 
	 * @param table
	 *            TableDesign object giving specifications for the creation of
	 *            the table
	 * @throws java.sql.SQLException
	 **************************************************************************/
	public void dropUniqueIndex(TableDesign table) {
		String[] indexedColumns = table.getUniqueList();
		int counting = 0;
		if (indexedColumns != null) {
			StringBuffer queryString = new StringBuffer("DROP INDEX "
					+ table.getName() + uniqueIndexSuffix + " ON "
					+ table.getName());
			try {
				executeUpdate(queryString.toString());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	} // End Method

	/***************************************************************************
	 * Creates the UNIQUE index for a single column in the table using a
	 * TableDesign object.
	 * 
	 * @param table
	 *            TableDesign object giving specifications for the creation of
	 *            the table
	 * @throws java.sql.SQLException
	 **************************************************************************/
	public void createColumnUniqueConstraints(TableDesign table) {
		String[] columns = table.getColumns();
		int count = -1;
		while (++count < columns.length) {
			if (table.getUniqueConstraint(columns[count])) {
				StringBuffer queryString = new StringBuffer(
						"CREATE UNIQUE INDEX " + table.getName() + "_"
								+ columns[count] + uniqueColumnConstraintSuffix);
				queryString.append(" ON " + table.getName() + " ("
						+ columns[count] + ")");
				try {
					executeUpdate(queryString.toString());
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	} // End Method

	/***************************************************************************
	 * Drops the UNIQUE index for a single column in the table using a
	 * TableDesign object.
	 * 
	 * @param table
	 *            TableDesign object giving specifications for the creation of
	 *            the table
	 * @throws java.sql.SQLException
	 **************************************************************************/
	public void dropColumnUniqueConstraints(TableDesign table) {
		String[] columns = table.getColumns();
		int count = -1, counting = 0;
		while (++count < columns.length) {
			if (table.getUniqueConstraint(columns[count])) {
				StringBuffer queryString = new StringBuffer("DROP INDEX "
						+ table.getName() + "_" + columns[count]
						+ uniqueColumnConstraintSuffix + " ON "
						+ table.getName());
				try {
					executeUpdate(queryString.toString());
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	} // End Method

	/***************************************************************************
	 * Returns the date in a format that can be used to compare to a date in the
	 * database in the WHERE clause.
	 * 
	 * @param year
	 *            Number indicating year(4 digits or 2 digits).
	 * @param month
	 *            Number indicating month(1-12).
	 * @param day
	 *            Number indicating day (1 based).
	 * @return String giving the date in SQL format for comparison purposes.
	 **************************************************************************/
	public String getDateForComparison(int year, int month, int day) {
		String date;
		String monthString, dayString, yearString;
		yearString = "" + year;
		if (yearString.length() > 2)
			yearString = yearString.substring(yearString.length() - 2,
					yearString.length());
		monthString = "" + month;
		dayString = "" + day;
		date = monthString + "/" + dayString + "/" + yearString;
		return "#" + date + "#";
	}

	/***************************************************************************
	 * Drops all indexes, Unique indexes, and unique column constraints for the
	 * database.
	 * 
	 * @param table
	 *            TableDesign object giving specifications for the creation of
	 *            the table.
	 **************************************************************************/
	public void dropIndexes(TableDesign table) {
		dropIndex(table);
		/*
		 * shouldn't drop unique constraints dropUniqueIndex(table);
		 * dropColumnUniqueConstraints(table);
		 */
	}

	/***************************************************************************
	 * Creates all indexes, Unique indexes, and unique column constraints for
	 * the database.
	 * 
	 * @param table
	 *            TableDesign object giving specifications for the creation of
	 *            the table.
	 **************************************************************************/
	public void createIndexes(TableDesign table) {
		createIndex(table);
		createUniqueIndex(table);
		createColumnUniqueConstraints(table);
	}

	/***************************************************************************
	 * Returns the resultset (in com.lazerinc.utils.Data format) from the stored
	 * select procedure specified.
	 * 
	 * @param procedureName
	 *            name of the select procedure to use
	 * @param columns
	 *            Names of the columns selecting from
	 * @param args
	 *            List of arguments to pass to the stored procedure
	 * @return A Data object holding the records gathered from the procedure
	 **************************************************************************/
	public Data useStoredProcedure(String procedureName, String[] columns,
			String[] args) {
		return null;
		// TODO: implement this com.lazerinc.dbtools.DBConnect abstract method
	}
}
