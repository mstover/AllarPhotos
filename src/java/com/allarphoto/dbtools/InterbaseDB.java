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

import java.sql.ResultSet;
import java.sql.Types;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaContext;

import com.allarphoto.utils.Data;
import com.allarphoto.utils.Functions;

/*******************************************************************************
 * This class is used to connect and drive an Interbase database. No SQL is
 * needed to use this class - it can query, insert, update, and create tables
 * and autonumber primary keys. Use the TableDesign class to create a table.
 * 
 * @author Michael Stover
 * @version 1.0 10/13/1998 --------------------------------------------------
 */
public class InterbaseDB extends DBConnect {

	private static Logger log = InterbaseDB.getLogger();

	String schema;

	public InterbaseDB() {
		super();
		// TODO Auto-generated constructor stub
	}

	public InterbaseDB(CoinjemaContext cc) {
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
		int index = 0;
		if (value != null) {
			value = value.trim();
			while ((index < value.length())
					&& (index = value.indexOf("'", index)) > -1) {
				if ((index + 1) < value.length() && index > 0)
					value = value.substring(0, index) + "'"
							+ value.substring(index, value.length());
				else if (index == 0)
					value = "'" + value;
				else
					value = value + "'";
				index += 2;
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
			queryString.append(");");
			returnValue = executeUpdate(queryString.toString());
			updateTable(tableName);
			Functions.javaLog("Interbase: " + queryString.toString());
		} catch (Exception e) {
			Functions.javaLog("Interbase: e = " + e.toString());
			Functions.javaLog("Interbase: " + queryString.toString());
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
		StringBuffer queryString = new StringBuffer("UPDATE " + tableName
				+ " SET ");
		int returnValue = 0;
		try {

			int count = 0;
			if (columns.length == values.length) {
				while (count < columns.length) {
					queryString.append(columns[count] + "=" + values[count]);
					count++;
					if (count < columns.length)
						queryString.append(", ");
				}
				queryString.append(" WHERE " + whereClause);
				queryString.append(";");
				returnValue = executeUpdate(queryString.toString());
				updateTable(tableName);
			}
		} catch (Exception e) {
			System.out.println(queryString.toString());
			throw new RuntimeException(e);
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
			returnValue = new String("NUMERIC(20,0)");
			break;
		case Types.BINARY:
			returnValue = new String("BLOB");
			break;
		case Types.BIT:
			returnValue = new String("SMALLINT");
			break;
		case Types.CHAR:
			returnValue = new String("CHAR(1)");
			break;
		case Types.DATE:
			returnValue = new String("DATE");
			break;
		case Types.DECIMAL:
			returnValue = new String("FLOAT");
			break;
		case Types.DOUBLE:
			returnValue = new String("DOUBLE");
			break;
		case Types.FLOAT:
			returnValue = new String("FLOAT");
			break;
		case Types.INTEGER:
			returnValue = new String("INTEGER");
			break;
		case Types.LONGVARBINARY:
			returnValue = new String("BLOB");
			break;
		case Types.LONGVARCHAR:
			returnValue = new String("VARCHAR(32765)");
			break;
		case Types.REAL:
			returnValue = new String("FLOAT");
			break;
		case Types.SMALLINT:
			returnValue = new String("SMALLINT");
			break;
		case Types.TINYINT:
			returnValue = new String("SMALLINT");
			break;
		case Types.VARCHAR:
			returnValue = new String("VARCHAR(" + size + ")");
			break;
		default:
			returnValue = new String("VARCHAR(255)");
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
		int count = 0;
		String[] columns = table.getColumns();
		while (count < columns.length) {
			queryString.append(columns[count] + " ");
			queryString.append(setType(table, columns[count]));
			if (columns[count].equals(table.getPrimary())) {
				queryString.append(" NOT NULL PRIMARY KEY");
			} else if (table.getNullConstraint(columns[count]))
				queryString.append(" NOT NULL");
			references = table.getReference(columns[count]);
			if (!references[0].equals("")) {
				// queryString.append(" REFERENCES
				// "+references[0]+"("+references[1]+
				// ") ON UPDATE CASCADE ON DELETE CASCADE");
				table.addIndexedColumn(columns[count]);
			}
			count++;
			if (count < columns.length)
				queryString.append(",");
		}

		queryString.append(")");
		try {
			executeUpdate(queryString.toString());
			returnValue = true;
			if (table.getPrimaryAuto())
				createPrimaryAutoNumber(table);
			createIndexes(table);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return returnValue;
	} // end of Method

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
	 * Creates the UNIQUE index for a table using a TableDesign object.
	 * 
	 * @param table
	 *            TableDesign object giving specifications for the creation of
	 *            the table
	 * @throws java.sql.SQLException
	 **************************************************************************/
	public void createUniqueIndex(TableDesign table) {
		String[] uniqueColumns = table.getUniqueList();
		if (uniqueColumns != null) {
			StringBuffer queryString = new StringBuffer("CREATE UNIQUE INDEX "
					+ table.getName() + uniqueIndexSuffix);
			queryString.append(" ON " + table.getName() + " (");
			int count = 0;

			while (count < uniqueColumns.length) {
				queryString.append(uniqueColumns[count++]);
				if (count < uniqueColumns.length)
					queryString.append(",");
			}
			queryString.append(")");
			try {
				executeUpdate(queryString.toString());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

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
	 * Creates the indexes for a table using a TableDesign object.
	 * 
	 * @param table
	 *            TableDesign object giving specifications for the creation of
	 *            the table
	 * @throws java.sql.SQLException
	 **************************************************************************/
	public void createIndex(TableDesign table) {
		String[] indexedColumns = table.getIndexes();
		if (indexedColumns != null) {
			String cName = "", tName = table.getName();
			if (tName.length() > 8)
				tName = tName.substring(0, 8);
			StringBuffer queryString = new StringBuffer("CREATE INDEX " + tName
					+ "_");

			int count = 0;

			while (count < indexedColumns.length) {
				if (!cName.equals(indexedColumns[count])) {
					cName = indexedColumns[count];
					if (cName.length() > 16)
						cName = cName.substring(0, 16);
					try {
						Functions.javaLog("InterbaseDB: "
								+ queryString.toString() + cName + indexSuffix
								+ " ON " + table.getName() + " ("
								+ indexedColumns[count] + ")");
						executeUpdate(queryString.toString() + cName
								+ indexSuffix + " ON " + table.getName() + " ("
								+ indexedColumns[count] + ")");
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
				count++;
			}
		}
	} // End Method

	/***************************************************************************
	 * Drops the indexes for a table using a TableDesign object.
	 * 
	 * @param table
	 *            TableDesign object giving specifications for the creation of
	 *            the table
	 * @throws java.sql.SQLException
	 **************************************************************************/
	public void dropIndex(TableDesign table) {
		String[] indexedColumns = table.getIndexes();
		if (indexedColumns != null) {
			String cName = "", tName = table.getName();
			if (tName.length() > 8)
				tName = tName.substring(0, 8);
			StringBuffer queryString = new StringBuffer("DROP INDEX " + tName
					+ "_");

			int count = 0;

			while (count < indexedColumns.length) {
				if (!cName.equals(indexedColumns[count])) {
					cName = indexedColumns[count];
					if (cName.length() > 16)
						cName = cName.substring(0, 16);
					try {
						Functions.javaLog("InterbaseDB: "
								+ queryString.toString() + cName + indexSuffix);
						executeUpdate(queryString.toString() + cName
								+ indexSuffix);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
				count++;
			}
		}
	} // End Method

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
		String prim = table.getPrimary();
		String tName = table.getName();
		String genName = tName + "_auto_incr";
		String trigName = tName + "_trig_insert";
		StringBuffer gen = new StringBuffer("CREATE GENERATOR " + genName);
		try {
			executeUpdate(gen.toString());
			gen = new StringBuffer("SET GENERATOR " + genName + " TO 1");
			executeUpdate(gen.toString());
			try {
				Thread.sleep(5000);
			} catch (Exception e) {
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		StringBuffer trig = new StringBuffer("");
		trig.append("CREATE TRIGGER " + trigName + " FOR " + tName + " ");
		trig.append("BEFORE INSERT AS ");
		trig.append("BEGIN ");
		trig.append("new." + prim + "=GEN_ID(" + genName + ",1); ");
		trig.append("END");
		try {
			executeUpdate(trig.toString());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return returnValue;
	} // End of Method

	/***************************************************************************
	 * Determines whether a given table exists in the database.
	 * 
	 * @param tablename
	 *            Name of table to check for
	 * @return true or false for whether table exists
	 **************************************************************************/
	public boolean checkTableExists(String tablename) {
		ResultSet rs = null;
		boolean value = true;
		try {
			StringBuffer queryString = new StringBuffer("SELECT * FROM "
					+ tablename);
			queryString.append(";");
			rs = executeQuery(queryString.toString());
		} catch (Exception e) {
			value = false;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
		}
		return value;
	} // End Method

	/***************************************************************************
	 * Deletes records from a table.
	 * 
	 * @param tableName
	 *            Names of table deleting from
	 * @param whereClause
	 *            The where clause to determine which records to delete.
	 **************************************************************************/
	public void delete(String tableName, String whereClause) {
		StringBuffer queryString = new StringBuffer("DELETE FROM " + tableName);
		if (whereClause != null && !whereClause.equals("")) {
			queryString.append(" WHERE ");
			queryString.append(whereClause);
		}
		queryString.append(";");
		try {
			executeUpdate(queryString.toString());
			updateTable(tableName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	} // end of Method

	/***************************************************************************
	 * Creates a segment of a where clause that makes for a case-insensitive
	 * search in the database's own language, comparing the column's value to
	 * the value given. The result is returned as a SQL where clause -
	 * "columnName='<value>'"
	 * 
	 * @param columnName
	 *            Name of column.
	 * @param value
	 *            String to be cleaned.
	 * @param comparator
	 *            String used to compare the two values (eg. "="," LIKE "," NOT
	 *            LIKE",etc.
	 * @return Cleaned up string ready for database use.
	 **************************************************************************/
	public String whereCaseInsensitive(String columnName, String value,
			String comparator) {
		StringBuffer where = new StringBuffer("UPPER(");
		where.append(columnName);
		where.append(")");
		where.append(comparator);
		value = this.cleanString(value);
		where.append("UPPER(");
		where.append(value);
		where.append(")");
		return where.toString();
	}

	/***************************************************************************
	 * Returns the current date in proper SQL database format.
	 * 
	 * @return Date as a String in proper database format.
	 **************************************************************************/
	public String getDate() {
		GregorianCalendar d = new GregorianCalendar();
		String date = d.get(Calendar.DAY_OF_MONTH) + "-"
				+ getMonthAsString((d.get(Calendar.MONTH))) + "-"
				+ d.get(Calendar.YEAR) + ":" + d.get(Calendar.HOUR_OF_DAY)
				+ "-" + d.get(Calendar.MINUTE) + "-" + d.get(Calendar.SECOND);
		return date;
	} // End Method

	/***************************************************************************
	 * Returns the resultset (in com.allarphoto.utils.Data format) from the stored
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
		log.debug("InterbaseDB: useStoredProcedure(): Start");
		ResultSet rs = null;
		Data retVal = new Data();
		StringBuffer queryString = new StringBuffer("SELECT ");
		try {
			int count = 0;

			while (count < columns.length) {
				queryString.append(columns[count++]);
				if (count < columns.length)
					queryString.append(",");
			}
			queryString.append(" FROM ");
			if (procedureName != null && !procedureName.equals("")) {
				queryString.append(procedureName);
			}
			count = 0;
			while (count < args.length) {
				if (count == 0)
					queryString.append("(");
				queryString.append(cleanString(args[count++]));
				if (count < args.length)
					queryString.append(",");
				if (count == args.length)
					queryString.append(")");
			}
			rs = executeQuery(queryString.toString());
			retVal = getDataFromResultSet(rs, columns);
		} catch (Exception e) {
			System.out.println("InterbaseDB: " + queryString.toString());
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		log.debug("InterbaseDB: query=" + queryString.toString());
		log.debug("InterbaseDB: useStoredProcedure(): End");
		return retVal;
	}

	/***************************************************************************
	 * Returns the current date in proper SQL database format.
	 * 
	 * @return Date as a String in proper database format.
	 **************************************************************************/
	public String getDate(java.util.Date dt) {
		String[] temps = Functions.split(dt.toString(), "-");
		GregorianCalendar d = new GregorianCalendar(Integer.parseInt(temps[0]),
				Integer.parseInt(temps[1]) - 1, Integer.parseInt(temps[2]));
		String date = d.get(Calendar.DAY_OF_MONTH) + "-"
				+ getMonthAsString((d.get(Calendar.MONTH))) + "-"
				+ d.get(Calendar.YEAR) + ":" + d.get(Calendar.HOUR_OF_DAY)
				+ "-" + d.get(Calendar.MINUTE) + "-" + d.get(Calendar.SECOND);
		return date;
	} // End Method

	/***************************************************************************
	 * Returns the current date in proper SQL database format.
	 * 
	 * @return Date as a String in proper database format.
	 **************************************************************************/
	public String getDate(java.sql.Date dt) {
		String[] temps = Functions.split(dt.toString(), "-");
		GregorianCalendar d = new GregorianCalendar(Integer.parseInt(temps[0]),
				Integer.parseInt(temps[1]) - 1, Integer.parseInt(temps[2]));
		String date = d.get(Calendar.DAY_OF_MONTH) + "-"
				+ getMonthAsString((d.get(Calendar.MONTH))) + "-"
				+ d.get(Calendar.YEAR) + ":" + d.get(Calendar.HOUR_OF_DAY)
				+ "-" + d.get(Calendar.MINUTE) + "-" + d.get(Calendar.SECOND);
		return date;
	} // End Method

	/***************************************************************************
	 * Returns the current date in proper SQL database format.
	 * 
	 * @return d as a String in proper database format.
	 **************************************************************************/
	public String getDate(Calendar d) {
		String date = d.get(Calendar.DAY_OF_MONTH) + "-"
				+ getMonthAsString((d.get(Calendar.MONTH))) + "-"
				+ d.get(Calendar.YEAR) + ":" + d.get(Calendar.HOUR_OF_DAY)
				+ "-" + d.get(Calendar.MINUTE) + "-" + d.get(Calendar.SECOND);
		return date;
	} // End Method

	public String getDateForComparison(GregorianCalendar d) {
		String date = d.get(Calendar.DAY_OF_MONTH) + "-"
				+ getMonthAsString((d.get(Calendar.MONTH))) + "-"
				+ d.get(Calendar.YEAR) + ":" + d.get(Calendar.HOUR_OF_DAY)
				+ "-" + d.get(Calendar.MINUTE) + "-" + d.get(Calendar.SECOND);
		return "'" + date + "'";
	}

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
		int currentYear = new GregorianCalendar().get(Calendar.YEAR);
		int cent = currentYear / 100;
		currentYear %= 100;
		int factor;
		if (currentYear < 50)
			factor = -1;
		else
			factor = 1;
		if (month < 10)
			monthString = "0" + month;
		else
			monthString = "" + month;
		if (day < 10)
			dayString = "0" + day;
		else
			dayString = "" + day;
		if (year < 10)
			yearString = "0" + year;
		else
			yearString = "" + year;
		if (year > 999)
			date = monthString + "-" + dayString + "-" + yearString;
		else if (Math.abs((year % 100) - currentYear) < 50)
			date = monthString + "-" + dayString + "-" + (cent) + yearString;
		else if (year < 100)
			date = monthString + "-" + dayString + "-" + (cent + factor)
					+ yearString;
		else
			date = "01-02-1999";
		return "'" + date + "'";
	}

	/***************************************************************************
	 * Gets the current increment value for a given table @ param table Table to
	 * check @ param keyColumn Column holding the increment value
	 * @return The current increment value
	 **************************************************************************/
	public Object getIncrementValue(String table, String keyColumn) {
		log.debug("InterbaseDB: getting increment");
		ResultSet rs = null;
		String[] cols = { "gen_id" };
		Object retVal = null;
		Data data;
		StringBuffer queryString = new StringBuffer("SELECT GEN_ID(");
		queryString.append(keyColumn + ", 0) FROM " + table);
		try {
			rs = executeQuery(queryString.toString());
			data = getDataFromResultSet(rs, cols);
			log.debug("quearySring=" + queryString.toString());
			String[] dataString = data.getDataAsText();
			retVal = dataString[dataString.length - 1];
		} catch (Exception e) {
			log.error("InterbaseDB: " + queryString.toString());
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		// Functions.javaLog("DBConnect: select(): End");
		return retVal;

	}

} // End Class
