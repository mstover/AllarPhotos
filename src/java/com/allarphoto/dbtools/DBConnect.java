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

import java.lang.ref.SoftReference;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaContext;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaDynamic;
import org.coinjema.context.CoinjemaObject;

import strategiclibrary.service.DataBase;

import com.allarphoto.utils.Counter;
import com.allarphoto.utils.Data;
import com.allarphoto.utils.Functions;

/*******************************************************************************
 * This abstract class describes the methods necessary to interact with any
 * database. The specific implementations of the interface deal with the various
 * kinds of databases out their and their different drivers, and connection
 * protocols, as well as the subtle differences in SQL and abilities.
 * 
 * @author Michael Stover
 * @version 1.0 10/13/1998
 ******************************************************************************/
@CoinjemaObject(type = "lazerDb")
public abstract class DBConnect {

	private static Logger log = DBConnect.getLogger();

	@CoinjemaDynamic(alias = "log4j")
	protected static Logger getLogger() {
		return null;
	}

	String url, username, password;

	protected DataBase database;

	boolean caching = true;

	private static Map collectedQueries = Collections
			.synchronizedMap(new HashMap());

	private static Map countedQueries = Collections
			.synchronizedMap(new HashMap());

	private static Map collectedByTable = Collections
			.synchronizedMap(new HashMap());

	protected static String indexSuffix = "_index";

	protected static String uniqueIndexSuffix = "_u_index";

	protected static String uniqueColumnConstraintSuffix = "_u_col";

	protected static String referenceSuffix = "_ref";

	protected static String primaryKeySuffix = "_primkey";

	public static final int EQ = 0;

	public static final int NOTEQ = 1;

	public static final int GT = 2;

	public static final int GTEQ = 3;

	public static final int LT = 4;

	public static final int LTEQ = 5;

	public static final int IN = 6;

	public static final int NOTIN = 7;

	public static final int LIKE = 8;

	public static final int NOTLIKE = 9;

	public static final int NULL = 10;

	public static final int NOTNULL = 11;

	public static final int CONTAINS = 0;

	public static final int IS = 1;

	public static final int STARTSWITH = 2;

	public static final int ENDSWITH = 3;

	public static final int CONTAINSCASEINSENSITIVE = 4;

	public static final int ISCASEINSENSITIVE = 5;

	public static final int STARTSWITHCASEINSENSITIVE = 6;

	public static final int ENDSWITHCASEINSENSITIVE = 7;

	public static final int NUMBER = 8;

	public static final int COLUMN = 9;

	public static final int STRING = 10;

	public static final String DEFAULT = "N/A";

	public DBConnect() {
	}

	public DBConnect(CoinjemaContext cc) {
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

	public void clearCache() {
		collectedByTable.clear();
		collectedQueries.clear();
		countedQueries.clear();
	}

	/***************************************************************************
	 * Gets the appropriate comparator string for this database.
	 **************************************************************************/
	private String getComparator(int c) {
		switch (c) {
		case EQ:
			return "=";
		case NOTEQ:
			return "!=";
		case GT:
			return ">";
		case GTEQ:
			return ">=";
		case LT:
			return "<";
		case LTEQ:
			return "<=";
		case IN:
			return " IN(";
		case NOTIN:
			return " NOT IN(";
		case LIKE:
			return " LIKE ";
		case NOTLIKE:
			return " NOT LIKE ";
		case NULL:
			return " IS NULL ";
		case NOTNULL:
			return " IS NOT NULL ";
		}
		return "=";
	}

	/***************************************************************************
	 * Creates a segment of a where clause given arguments describing the two
	 * comparable values and the type of comparison to be used.
	 * 
	 * @param columnName
	 *            Name of column - First comparable value.
	 * @param value
	 *            String to be cleaned - Second comparable value.
	 * @param comparator
	 *            int indicating which comparator to use (eg. DBConnect.LIKE or
	 *            DBConnect.EQ).
	 * @param searchType
	 *            Type of search - mostly for searching strings.
	 *            DBConnect.CONTAINS DBConnect.IS DBConnect.STARTSWITH
	 *            DBConnect.ENDSWITH DBConnect.CONTAINSCASEINSENSITIVE
	 *            DBConnect.ISCASEINSENSITIVE
	 *            DBConnect.STARTSWITHCASEINSENSITIVE
	 *            DBConnect.ENDSWITHCASEINSENSITIVE DBConnect.COLUMN
	 *            DBConnect.NUMBER
	 * @return Cleaned up string ready for database use.
	 **************************************************************************/
	public String where(String columnName, Object value, int comparator,
			int searchType) {
		boolean upper = false;
		String c = this.getComparator(comparator);
		StringBuffer where = new StringBuffer("");
		if (searchType == CONTAINSCASEINSENSITIVE
				|| searchType == ISCASEINSENSITIVE
				|| searchType == STARTSWITHCASEINSENSITIVE
				|| searchType == ENDSWITHCASEINSENSITIVE) {
			where.append("UPPER(" + columnName + ")");
			upper = true;
		} else
			where.append(columnName);
		where.append(c);
		if (comparator != NULL && comparator != NOTNULL)
			where.append(getWhereValue(value, searchType, upper));
		if (comparator == IN || comparator == NOTIN)
			where.append(")");
		return where.toString();
	}

	/***************************************************************************
	 * Creates a segment of a where clause given arguments describing the two
	 * comparable values and the type of comparison to be used.
	 * 
	 * @param columnName
	 *            Name of column - First comparable value.
	 * @param value
	 *            String to be cleaned - Second comparable value.
	 * @param comparator
	 *            int indicating which comparator to use (eg. DBConnect.LIKE or
	 *            DBConnect.EQ).
	 * @param searchType
	 *            Type of search DBConnect.CONTAINS DBConnect.IS
	 *            DBConnect.STARTSWITH DBConnect.ENDSWITH
	 *            DBConnect.CONTAINSCASEINSENSITIVE DBConnect.ISCASEINSENSITIVE
	 *            DBConnect.STARTSWITHCASEINSENSITIVE
	 *            DBConnect.ENDSWITHCASEINSENSITIVE DBConnect.COLUMN
	 *            DBConnect.NUMBER
	 * @return Cleaned up string ready for database use.
	 **************************************************************************/
	public String where(String columnName, int v, int comparator, int searchType) {
		boolean upper = false;
		String c = this.getComparator(comparator);
		StringBuffer where = new StringBuffer("");
		if (searchType == CONTAINSCASEINSENSITIVE
				|| searchType == ISCASEINSENSITIVE
				|| searchType == STARTSWITHCASEINSENSITIVE
				|| searchType == ENDSWITHCASEINSENSITIVE) {
			where.append("UPPER(" + columnName + ")");
			upper = true;
		} else
			where.append(columnName);
		where.append(c);
		if (comparator != NULL && comparator != NOTNULL)
			where.append(getWhereValue(v, searchType, upper));
		if (comparator == IN || comparator == NOTIN)
			where.append(")");
		return where.toString();
	}

	/***************************************************************************
	 * Creates a segment of a where clause given arguments describing the two
	 * comparable values and the type of comparison to be used.
	 * 
	 * @param columnName
	 *            Name of column - First comparable value.
	 * @param value
	 *            String to be cleaned - Second comparable value.
	 * @param comparator
	 *            int indicating which comparator to use (eg. DBConnect.LIKE or
	 *            DBConnect.EQ).
	 * @param searchType
	 *            Type of search DBConnect.CONTAINS DBConnect.IS
	 *            DBConnect.STARTSWITH DBConnect.ENDSWITH
	 *            DBConnect.CONTAINSCASEINSENSITIVE DBConnect.ISCASEINSENSITIVE
	 *            DBConnect.STARTSWITHCASEINSENSITIVE
	 *            DBConnect.ENDSWITHCASEINSENSITIVE DBConnect.COLUMN
	 *            DBConnect.NUMBER
	 * @return Cleaned up string ready for database use.
	 **************************************************************************/
	public String where(String columnName, float v, int comparator,
			int searchType) {
		boolean upper = false;
		String c = this.getComparator(comparator);
		StringBuffer where = new StringBuffer("");
		if (searchType == CONTAINSCASEINSENSITIVE
				|| searchType == ISCASEINSENSITIVE
				|| searchType == STARTSWITHCASEINSENSITIVE
				|| searchType == ENDSWITHCASEINSENSITIVE) {
			where.append("UPPER(" + columnName + ")");
			upper = true;
		} else
			where.append(columnName);
		where.append(c);
		if (comparator != NULL && comparator != NOTNULL)
			where.append(getWhereValue(v, searchType, upper));
		if (comparator == IN || comparator == NOTIN)
			where.append(")");
		return where.toString();
	}

	/***************************************************************************
	 * Creates a segment of a where clause given arguments describing the two
	 * comparable values and the type of comparison to be used.
	 * 
	 * @param columnName
	 *            Name of column - First comparable value.
	 * @param value
	 *            String to be cleaned - Second comparable value.
	 * @param comparator
	 *            int indicating which comparator to use (eg. DBConnect.LIKE or
	 *            DBConnect.EQ).
	 * @param searchType
	 *            Type of search DBConnect.CONTAINS DBConnect.IS
	 *            DBConnect.STARTSWITH DBConnect.ENDSWITH
	 *            DBConnect.CONTAINSCASEINSENSITIVE DBConnect.ISCASEINSENSITIVE
	 *            DBConnect.STARTSWITHCASEINSENSITIVE
	 *            DBConnect.ENDSWITHCASEINSENSITIVE DBConnect.COLUMN
	 *            DBConnect.NUMBER
	 * @return Cleaned up string ready for database use.
	 **************************************************************************/
	public String where(String columnName, long v, int comparator,
			int searchType) {
		boolean upper = false;
		String c = this.getComparator(comparator);
		StringBuffer where = new StringBuffer("");
		if (searchType == CONTAINSCASEINSENSITIVE
				|| searchType == ISCASEINSENSITIVE
				|| searchType == STARTSWITHCASEINSENSITIVE
				|| searchType == ENDSWITHCASEINSENSITIVE) {
			where.append("UPPER(" + columnName + ")");
			upper = true;
		} else
			where.append(columnName);
		where.append(c);
		if (comparator != NULL && comparator != NOTNULL)
			where.append(getWhereValue(v, searchType, upper));
		if (comparator == IN || comparator == NOTIN)
			where.append(")");
		return where.toString();
	}

	/***************************************************************************
	 * Creates a segment of a where clause given arguments describing the two
	 * comparable values and the type of comparison to be used.
	 * 
	 * @param columnName
	 *            Name of column - First comparable value.
	 * @param value
	 *            String to be cleaned - Second comparable value.
	 * @param comparator
	 *            int indicating which comparator to use (eg. DBConnect.LIKE or
	 *            DBConnect.EQ).
	 * @param searchType
	 *            Type of search DBConnect.CONTAINS DBConnect.IS
	 *            DBConnect.STARTSWITH DBConnect.ENDSWITH
	 *            DBConnect.CONTAINSCASEINSENSITIVE DBConnect.ISCASEINSENSITIVE
	 *            DBConnect.STARTSWITHCASEINSENSITIVE
	 *            DBConnect.ENDSWITHCASEINSENSITIVE DBConnect.COLUMN
	 *            DBConnect.NUMBER
	 * @return Cleaned up string ready for database use.
	 **************************************************************************/
	public String where(String columnName, int[] v, int comparator,
			int searchType) {
		String[] values = new String[v.length];
		boolean upper = false;
		String c = this.getComparator(comparator);
		StringBuffer where = new StringBuffer("");
		if (searchType == CONTAINSCASEINSENSITIVE
				|| searchType == ISCASEINSENSITIVE
				|| searchType == STARTSWITHCASEINSENSITIVE
				|| searchType == ENDSWITHCASEINSENSITIVE) {
			where.append("UPPER(" + columnName + ")");
			upper = true;
		} else
			where.append(columnName);
		where.append(this.getComparator(comparator));
		int count = -1;
		while (++count < v.length)
			values[count] = getWhereValue(v[count], searchType, upper);
		if (comparator == IN || comparator == NOTIN)
			where.append(Functions.unsplit(values, ",") + ")");
		return where.toString();
	}

	/***************************************************************************
	 * Creates a segment of a where clause given arguments describing the two
	 * comparable values and the type of comparison to be used.
	 * 
	 * @param columnName
	 *            Name of column - First comparable value.
	 * @param value
	 *            String to be cleaned - Second comparable value.
	 * @param comparator
	 *            int indicating which comparator to use (eg. DBConnect.LIKE or
	 *            DBConnect.EQ).
	 * @param searchType
	 *            Type of search DBConnect.CONTAINS DBConnect.IS
	 *            DBConnect.STARTSWITH DBConnect.ENDSWITH
	 *            DBConnect.CONTAINSCASEINSENSITIVE DBConnect.ISCASEINSENSITIVE
	 *            DBConnect.STARTSWITHCASEINSENSITIVE
	 *            DBConnect.ENDSWITHCASEINSENSITIVE DBConnect.COLUMN
	 *            DBConnect.NUMBER
	 * @return Cleaned up string ready for database use.
	 **************************************************************************/
	public String where(String columnName, Object[] v, int comparator,
			int searchType) {
		String[] values = new String[v.length];
		boolean upper = false;
		String c = this.getComparator(comparator);
		StringBuffer where = new StringBuffer("");
		if (searchType == CONTAINSCASEINSENSITIVE
				|| searchType == ISCASEINSENSITIVE
				|| searchType == STARTSWITHCASEINSENSITIVE
				|| searchType == ENDSWITHCASEINSENSITIVE) {
			where.append("UPPER(" + columnName + ")");
			upper = true;
		} else
			where.append(columnName);
		where.append(this.getComparator(comparator));
		int count = -1;
		while (++count < v.length)
			values[count] = getWhereValue(v[count], searchType, upper);
		if (comparator == IN || comparator == NOTIN)
			where.append(Functions.unsplit(values, ",") + ")");
		return where.toString();
	}

	/***************************************************************************
	 * Creates a segment of a where clause given arguments describing the two
	 * comparable values and the type of comparison to be used.
	 * 
	 * @param columnName
	 *            Name of column - First comparable value.
	 * @param value
	 *            Array of values to be cleaned - Second comparable value.
	 * @param comparator
	 *            int indicating which comparator to use (eg. DBConnect.LIKE or
	 *            DBConnect.EQ).
	 * @param searchType
	 *            Type of search DBConnect.CONTAINS DBConnect.IS
	 *            DBConnect.STARTSWITH DBConnect.ENDSWITH
	 *            DBConnect.CONTAINSCASEINSENSITIVE DBConnect.ISCASEINSENSITIVE
	 *            DBConnect.STARTSWITHCASEINSENSITIVE
	 *            DBConnect.ENDSWITHCASEINSENSITIVE DBConnect.COLUMN
	 *            DBConnect.NUMBER
	 * @return Cleaned up string ready for database use.
	 **************************************************************************/
	public String where(String columnName, String[] values, int comparator,
			int searchType) {
		boolean upper = false;
		String c = this.getComparator(comparator);
		StringBuffer where = new StringBuffer("");
		if (searchType == CONTAINSCASEINSENSITIVE
				|| searchType == ISCASEINSENSITIVE
				|| searchType == STARTSWITHCASEINSENSITIVE
				|| searchType == ENDSWITHCASEINSENSITIVE) {
			where.append("UPPER(" + columnName + ")");
			upper = true;
		} else
			where.append(columnName);
		where.append(this.getComparator(comparator));
		int count = -1;
		while (++count < values.length)
			values[count] = getWhereValue(values[count], searchType, upper);
		if (comparator == IN || comparator == NOTIN)
			where.append(Functions.unsplit(values, ",") + ")");
		return where.toString();
	}

	/***************************************************************************
	 * Gets an individual comparison term in the Where clause.
	 * 
	 * @param value
	 *            String value to be handled.
	 * @param searchType
	 *            type of search.
	 * @upper boolean indicating whether value should be uppercased by the
	 *        database.
	 * @return String value of comparison term.
	 **************************************************************************/
	private String getWhereValue(String value, int searchType, boolean upper) {
		if (searchType != NUMBER && searchType != COLUMN) {
			if (searchType == CONTAINS || searchType == CONTAINSCASEINSENSITIVE)
				value = "%" + value + "%";
			else if (searchType == STARTSWITH
					|| searchType == STARTSWITHCASEINSENSITIVE)
				value = value + "%";
			else if (searchType == ENDSWITH
					|| searchType == ENDSWITHCASEINSENSITIVE)
				value = "%" + value;
			value = this.cleanString(value);
			if (upper)
				value = "UPPER(" + value + ")";
		}
		return value;
	}

	/***************************************************************************
	 * Gets an individual comparison term in the Where clause.
	 * 
	 * @param v
	 *            int value to be handled.
	 * @param searchType
	 *            type of search.
	 * @return String value of comparison term.
	 **************************************************************************/
	private String getWhereValue(long v, int searchType, boolean upper) {
		String value = "" + v;
		if (searchType != NUMBER && searchType != COLUMN) {
			if (searchType == CONTAINS || searchType == CONTAINSCASEINSENSITIVE)
				value = "%" + value + "%";
			else if (searchType == STARTSWITH
					|| searchType == STARTSWITHCASEINSENSITIVE)
				value = value + "%";
			else if (searchType == ENDSWITH
					|| searchType == ENDSWITHCASEINSENSITIVE)
				value = "%" + value;
			value = this.cleanString(value);
			if (upper)
				value = "UPPER(" + value + ")";
		}
		return value;
	}

	/***************************************************************************
	 * Gets an individual comparison term in the Where clause.
	 * 
	 * @param v
	 *            int value to be handled.
	 * @param searchType
	 *            type of search.
	 * @return String value of comparison term.
	 **************************************************************************/
	private String getWhereValue(int v, int searchType, boolean upper) {
		String value = "" + v;
		if (searchType != NUMBER && searchType != COLUMN) {
			if (searchType == CONTAINS || searchType == CONTAINSCASEINSENSITIVE)
				value = "%" + value + "%";
			else if (searchType == STARTSWITH
					|| searchType == STARTSWITHCASEINSENSITIVE)
				value = value + "%";
			else if (searchType == ENDSWITH
					|| searchType == ENDSWITHCASEINSENSITIVE)
				value = "%" + value;
			value = this.cleanString(value);
			if (upper)
				value = "UPPER(" + value + ")";
		}
		return value;
	}

	/***************************************************************************
	 * Gets an individual comparison term in the Where clause.
	 * 
	 * @param v
	 *            float value to be handled.
	 * @param searchType
	 *            type of search.
	 * @return String value of comparison term.
	 **************************************************************************/
	private String getWhereValue(float v, int searchType, boolean upper) {
		String value = "" + v;
		if (searchType != NUMBER && searchType != COLUMN) {
			if (searchType == CONTAINS || searchType == CONTAINSCASEINSENSITIVE)
				value = "%" + value + "%";
			else if (searchType == STARTSWITH
					|| searchType == STARTSWITHCASEINSENSITIVE)
				value = value + "%";
			else if (searchType == ENDSWITH
					|| searchType == ENDSWITHCASEINSENSITIVE)
				value = "%" + value;
			value = this.cleanString(value);
			if (upper)
				value = "UPPER(" + value + ")";
		}
		return value;
	}

	/***************************************************************************
	 * Gets an individual comparison term in the Where clause.
	 * 
	 * @param v
	 *            Object value to be handled.
	 * @param searchType
	 *            type of search.
	 * @return String value of comparison term.
	 **************************************************************************/
	private String getWhereValue(Object v, int searchType, boolean upper) {
		String value = Functions.universalToString(v);
		if (searchType != NUMBER && searchType != COLUMN) {
			if (searchType == CONTAINS || searchType == CONTAINSCASEINSENSITIVE)
				value = "%" + value + "%";
			else if (searchType == STARTSWITH
					|| searchType == STARTSWITHCASEINSENSITIVE)
				value = value + "%";
			else if (searchType == ENDSWITH
					|| searchType == ENDSWITHCASEINSENSITIVE)
				value = "%" + value;
			value = this.cleanString(value);
			if (upper)
				value = "UPPER(" + value + ")";
		}
		return value;
	}

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
	 *            String indicating which comparator to use (eg.
	 *            DBConnect.LIKE).
	 * @return Cleaned up string ready for database use.
	 * @deprecated.
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
	 * Protected method to execute updates to a database that should only occur
	 * one at a time.
	 * 
	 * @param query
	 *            Statement object with which to execute query.
	 * @param queryString
	 *            Query String to use.
	 * @return int number of rows effected.
	 * @throws SQLException.
	 **************************************************************************/
	protected synchronized int executeUpdate(String queryString)
			throws Exception {
		// Functions.javaLog("DBConnect: executeUpdate(): Start");
		// Functions.javaLog(queryString);
		int i = database.executeUpdate(queryString);
		// Functions.javaLog("DBConnect: executeUpdate(): End");
		return i;
	}

	protected synchronized ResultSet executeQuery(String queryString)
			throws Exception {
		return database.executeQuery(queryString);
	}

	/***************************************************************************
	 * Returns the current date in proper SQL database format.
	 * 
	 * @return Date as a String in proper database format.
	 **************************************************************************/
	public String getDate() {
		GregorianCalendar d = new GregorianCalendar();
		String date = d.get(Calendar.YEAR) + "-" + (d.get(Calendar.MONTH) + 1)
				+ "-" + d.get(Calendar.DAY_OF_MONTH);
		return date;
	} // End Method

	public String getDateForComparison(Calendar d) {
		String date = d.get(Calendar.YEAR) + "-" + (d.get(Calendar.MONTH) + 1)
				+ "-" + d.get(Calendar.DAY_OF_MONTH) + ":"
				+ d.get(Calendar.HOUR_OF_DAY) + "-" + d.get(Calendar.MINUTE)
				+ "-" + d.get(Calendar.SECOND);
		return "'" + date + "'";
	}

	/***************************************************************************
	 * Private method to get the three letter version of the month given an
	 * integer.
	 **************************************************************************/
	protected String getMonthAsString(int month) {
		String[] months = { "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL",
				"AUG", "SEP", "OCT", "NOV", "DEC" };
		return months[month];
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
			date = "1999-02-01";
		return "'" + date + "'";
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
		String date = d.get(Calendar.YEAR) + "-" + (d.get(Calendar.MONTH) + 1)
				+ "-" + d.get(Calendar.DAY_OF_MONTH);
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
		String date = d.get(Calendar.YEAR) + "-" + (d.get(Calendar.MONTH) + 1)
				+ "-" + d.get(Calendar.DAY_OF_MONTH);
		return date;
	} // End Method

	/***************************************************************************
	 * Returns the current date in proper SQL database format.
	 * 
	 * @return d as a String in proper database format.
	 **************************************************************************/
	public String getDate(Calendar d) {
		String date = d.get(Calendar.YEAR) + "-" + (d.get(Calendar.MONTH) + 1)
				+ "-" + d.get(Calendar.DAY_OF_MONTH);
		return date;
	} // End Method

	/***************************************************************************
	 * Returns the current time in String format.
	 * 
	 * @return Time as a String.
	 **************************************************************************/
	public String getTime() {
		GregorianCalendar d = new GregorianCalendar();
		String time = d.get(Calendar.HOUR_OF_DAY) + ":"
				+ d.get(Calendar.MINUTE) + ":" + d.get(Calendar.SECOND);
		return time;
	} // End Method

	/***************************************************************************
	 * Determines whether a given table exists in the database. Note: Many
	 * databases do not support this function.
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
			rs = database.executeQuery(queryString.toString());
		} catch (Exception e) {
			value = false;
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return value;
	}

	/***************************************************************************
	 * Inserts data into a database table using the INSERT INTO SQL command.
	 * Returns number of rows inserted.
	 * 
	 * @param table
	 *            name of table inserting into
	 * @param columns
	 *            array of column names to insert into.
	 * @param values
	 *            array of values inserting. Integers can be part of the string.
	 *            Strings need to be surrounded by single quotes.
	 * @return int representing number of rows inserted. 0 indicates failure.
	 **************************************************************************/
	public abstract int insert(String tableName, String[] columns,
			String[] values);

	/***************************************************************************
	 * Updates data in a database table using the UPDATE SQL command. Returns
	 * number of rows inserted.
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
	 * @return int representing number of rows updated. 0 indicates failure or
	 *         no rows updated.
	 **************************************************************************/
	public abstract int update(String tableName, String[] columns,
			String[] values, String whereClause);

	/***************************************************************************
	 * Takes a TableDesign object and returns the string that sets a column type
	 * and size. Used primarily internally the object by the createTable()
	 * method.
	 * 
	 * @param table
	 *            table design class object
	 * @param c
	 *            name of column in the table design object
	 * @return String giving the data type in the SQL language.
	 **************************************************************************/
	public abstract String setType(TableDesign table, String c);

	/***************************************************************************
	 * Creates a table in the database.
	 * 
	 * @param table
	 *            A TableDesign object that fully describes the table to be
	 *            created.
	 * @return True if table is successfully created, False otherwise
	 **************************************************************************/
	public abstract boolean createTable(TableDesign table);

	/***************************************************************************
	 * Creates an autonumber generator for the primary key of the table argument
	 * sent to the function. This method is for internal use for this class. It
	 * is automatically called by the createTable method.
	 * 
	 * @param table
	 *            TableDesign object to create the autonumbering for.
	 * @return True if successful, false otherwise
	 **************************************************************************/
	public abstract boolean createPrimaryAutoNumber(TableDesign table);

	/***************************************************************************
	 * Creates all indexes, Unique indexes, and unique column constraints for
	 * the database.
	 * 
	 * @param table
	 *            TableDesign object giving specifications for the creation of
	 *            the table.
	 **************************************************************************/
	public abstract void createIndexes(TableDesign table);

	/***************************************************************************
	 * Drops all indexes, Unique indexes, and unique column constraints for the
	 * database.
	 * 
	 * @param table
	 *            TableDesign object giving specifications for the creation of
	 *            the table.
	 **************************************************************************/
	public abstract void dropIndexes(TableDesign table);

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
	public abstract Data useStoredProcedure(String procedureName,
			String[] columns, String[] args);

	/***************************************************************************
	 * Method to get a column of data from a result set.
	 * 
	 * @param rs
	 *            The result set to get the data from.
	 * @param col
	 *            The number of the column to get.
	 * @return An ArrayList object with the objects.
	 **************************************************************************/
	public static ArrayList getResultSetColumn(ResultSet rs, int col) {
		ArrayList list = new ArrayList();
		try {
			while (rs != null && rs.next())
				list.add(rs.getObject(col));
		} catch (SQLException e) {
		}
		return list;
	}

	/***************************************************************************
	 * Returns the resultset from a query in the form of a ResultSet object. The
	 * ResultSet object must be returned by calling the release method. This
	 * method is not recommended.
	 * 
	 * @param tableName
	 *            Names of tables selecting from
	 * @param columns
	 *            Names of the columns selecting from
	 * @param whereClause
	 *            The where clause
	 * @return A Data object holding the records gathered from the select
	 *         statement
	 **************************************************************************/
	public ResultSet qSelect(String[] tableName, String[] columns,
			String whereClause) {
		// Functions.javaLog("DBConnect: select(): Start");
		ResultSet rs = null;
		StringBuffer queryString = new StringBuffer("SELECT ");
		try {
			int count = 0;
			while (count < columns.length) {
				queryString.append(columns[count++]);
				if (count < columns.length)
					queryString.append(",");
			}
			queryString.append(" FROM ");
			count = 0;
			while (count < tableName.length) {
				if (tableName[count] != null)
					queryString.append(tableName[count]);
				while (++count < tableName.length && tableName[count] == null)
					;
				if (count < tableName.length)
					queryString.append(",");
			}
			if (whereClause != null && !whereClause.equals("")) {
				queryString.append(" WHERE ");
				queryString.append(whereClause);
			}
			String q = queryString.toString();
			rs = database.executeQuery(q);
		} catch (Exception e) {
			Functions.javaLog("DBConnect: " + queryString.toString()
					+ "\r\nerror=" + e.toString());
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception err) {
				}
			}
			throw new RuntimeException("bad query: " + queryString.toString(),
					e);
		}
		// Functions.javaLog("DBConnect: select(): End");
		return rs;
	} // end of Method

	/***************************************************************************
	 * Internal class to hold a query and make it possible to hash it and
	 * quickly know if it uses a given table
	 **************************************************************************/
	private class QueryThing {

		String[] tables;

		String[] columns;

		String where;

		String qString;

		int code;

		public QueryThing(String[] t, String[] c, String w) {
			tables = t;
			columns = c;
			where = w;
			StringBuffer q = new StringBuffer("SELECT DISTINCT ");
			q.append(Functions.unsplit(c, ","));
			q.append(" FROM ");
			q.append(Functions.unsplit(t, ","));
			if (where != null && where.length() > 0) {
				q.append(" WHERE ");
				q.append(where);
			}
			qString = q.toString();
			code = qString.hashCode();
		}

		public QueryThing(String[] t, String[] c, String w, String o) {
			tables = t;
			columns = c;
			where = w;
			StringBuffer q = new StringBuffer("SELECT DISCTINCT ");
			q.append(Functions.unsplit(c, ","));
			q.append(" FROM ");
			q.append(Functions.unsplit(t, ","));
			if (where != null && where.length() > 0) {
				q.append(" WHERE ");
				q.append(where);
			}
			q.append(" ORDER BY ");
			q.append(o);
			qString = q.toString();
			code = qString.hashCode();
		}

		public int hashCode() {
			return code;
		}

		public boolean equals(Object o) {
			boolean eq;
			try {
				eq = (qString.equals(((QueryThing) o).getQString()));
			} catch (Exception e) {
				eq = false;
			}
			return eq;
		}

		public String getQString() {
			return qString;
		}

		public String[] getTables() {
			return tables;
		}
	}

	public void setCaching(boolean c) {
		caching = c;
	}

	/***************************************************************************
	 * Returns the resultset from a query in the form of a
	 * com.allarphoto.utils.Data object.
	 * 
	 * @param tableName
	 *            Names of tables selecting from
	 * @param columns
	 *            Names of the columns selecting from
	 * @param whereClause
	 *            The where clause
	 * @return A Data object holding the records gathered from the select
	 *         statement
	 **************************************************************************/
	public Data select(String[] tableName, String[] columns, String whereClause) {
		log.debug("==");
		log.debug("DBConnect: select(): Start");
		QueryThing queryObject = new QueryThing(tableName, columns, whereClause);
		ResultSet rs = null;
		Data returnValue = null;
		if (caching && ((returnValue = getStoredQuery(queryObject)) != null)) {
			log.debug("Getting storedQuery,  queryString=: "
					+ queryObject.getQString());
			log.debug("DBConnect: select(): End");
			return returnValue;
		}
		String q = null;
		try {
			q = queryObject.getQString();
			rs = database.executeQuery(q);
			returnValue = getDataFromResultSet(rs, columns);
			if (caching)
				storeQuery(queryObject, returnValue);
		} catch (Exception e) {
			log.error("DBConnect: " + q, e);
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
				throw new RuntimeException("Couldn't close resource", e);
			}
		}
		log.debug("queryString=: " + q);
		log.debug("DBConnect: select(): End");
		return returnValue;
	} // end of Method

	/***************************************************************************
	 * Returns the resultset from a query in the form of a
	 * com.allarphoto.utils.Data object.
	 * 
	 * @param tableName
	 *            Names of tables selecting from
	 * @param columns
	 *            Names of the columns selecting from
	 * @param whereClause
	 *            The where clause
	 * @return A Data object holding the records gathered from the select
	 *         statement
	 **************************************************************************/
	public Data select(String[] tableName, String[] columns,
			String whereClause, String orderByClause) {
		log.debug("==");
		log.debug("DBConnect: select(): Start");
		QueryThing queryObject = new QueryThing(tableName, columns,
				whereClause, orderByClause);
		ResultSet rs = null;
		Data returnValue = null;
		if (caching && ((returnValue = getStoredQuery(queryObject)) != null)) {
			log.debug("Getting storedQuery,  queryString=: "
					+ queryObject.getQString());
			log.debug("DBConnect: select(): End");
			return returnValue;
		}
		String q = null;
		try {
			q = queryObject.getQString();
			rs = database.executeQuery(q);
			returnValue = getDataFromResultSet(rs, columns);
			if (caching)
				storeQuery(queryObject, returnValue);
		} catch (Exception e) {
			log.error("Error executing query: " + q, e);
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
				throw new RuntimeException("Couldn't close resource", e);
			}
		}
		log.debug("queryString=: " + q);
		log.debug("DBConnect: select(): End");
		return returnValue;
	} // end of Method

	private void storeQuery(QueryThing q, Data d) {
		if (d.size() > 1 && d.getHeaders().length > 2)
			return;
		Counter count = (Counter) countedQueries.get(q);
		if (count.x >= 2) {
			if (!collectedQueries.containsKey(q))
				sweep(q);
			SoftReference sr = new SoftReference(d);
			collectedQueries.put(q, sr.get());
		}
		count.x++;
	}

	/***************************************************************************
	 * If a new query is added to collectedQueries, then sweep it up also into
	 * collectedByTable.
	 * 
	 * @param pt
	 *            QueryThing object.
	 **************************************************************************/
	protected void sweep(QueryThing qt) {
		Set set;
		String[] tables = qt.getTables();
		for (int x = 0; x < tables.length; x++) {
			if ((set = (Set) collectedByTable.get(tables[x])) == null) {
				set = Collections.synchronizedSet(new HashSet());
				collectedByTable.put(tables[x], set);
			}
			set.add(qt);
		}
	}

	/***************************************************************************
	 * Let the system know if a table has been updated.
	 * 
	 * @param table
	 *            Table that was updated.
	 **************************************************************************/
	public void updateTable(String table) {
		Set set = (Set) collectedByTable.get(table);
		if (set != null) {
			Iterator it = set.iterator();
			synchronized (set) {
				while (it.hasNext())
					collectedQueries.put(it.next(), null);
			}
		}
	}

	private Data getStoredQuery(QueryThing q) {
		if (countedQueries.containsKey(q)) {
			Counter count = (Counter) countedQueries.get(q);
			count.x++;
		} else
			countedQueries.put(q, new Counter(1));
		Data data = ((Data) collectedQueries.get(q));
		return data;
	}

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
		try {
			database.executeUpdate(queryString.toString());
			updateTable(tableName);
		} catch (Exception e) {
			throw new RuntimeException("bad query: " + queryString.toString(),
					e);
		}
	} // end of Method

	/***************************************************************************
	 * Gets a Data object from a ResultSet.
	 * 
	 * @param rs
	 *            ResultSet passed in from a database query
	 * @return A Data object (com.allarphoto.utils)
	 * @throws java.sql.SQLException
	 **************************************************************************/
	public static Data getDataFromResultSet(ResultSet rs, String[] columns)
			throws SQLException {
		ResultSetMetaData meta;
		int numColumns = columns.length;
		Data data = new Data();
		meta = rs.getMetaData();
		data.setHeaders(columns);
		String[] dbCols = new String[numColumns];
		for (int count = 1; count <= numColumns; count++)
			dbCols[count - 1] = meta.getColumnName(count);
		while (rs.next()) {
			data.next();
			for (int count = 0; count < columns.length; count++) {
				Object o = rs.getObject(count + 1);
				data.addColumnValue(columns[count], o);
			}
		}
		return data;
	}// end method

	/***************************************************************************
	 * Encrypts a string.
	 * 
	 * @param input
	 *            String to be enrypted.
	 * @return The encrypted string.
	 **************************************************************************/
	public String encryptString(String input) {
		if (input == null || input.length() == 0)
			return "";
		String[] chars = new String[input.length()];
		StringBuffer number = new StringBuffer("");
		StringBuffer retVal = new StringBuffer("");
		char start;
		int count = -1;
		while (++count < input.length())
			chars[count] = Integer.toString((int) input.charAt(count));
		count = -1;
		while (++count < chars.length) {
			chars[count] = Functions.baseConvert(chars[count], 10, 8);
			if (chars[count].length() == 2)
				chars[count] = "00" + chars[count];
			else if (chars[count].length() == 3)
				chars[count] = "0" + chars[count];
		}
		count = -1;
		while (++count < chars.length)
			number.append(chars[count]);
		start = number.charAt(0);
		for (count = 3; count < number.length(); count += 3)
			number.setCharAt(count - 3, number.charAt(count));
		number.setCharAt(count - 3, start);
		chars = new String[number.length() / 2];
		for (count = 0; (count * 2 + 3) < number.length(); count++)
			chars[count] = number.toString().substring(count * 2 + 1,
					count * 2 + 3);
		chars[count] = number.toString()
				.substring(count * 2 + 1, count * 2 + 2)
				+ number.toString().substring(0, 1);
		count = -1;
		while (++count < chars.length)
			retVal.append((char) (32 + Integer.parseInt(chars[count])));
		return retVal.toString();
	}

	/***************************************************************************
	 * Decrypts a string.
	 * 
	 * @param input
	 *            String to be decrypted.
	 * @return The decrypted string.
	 **************************************************************************/
	public String decryptString(String input) {
		if (input == null || input.length() == 0)
			return "";
		String[] chars = new String[input.length()];
		StringBuffer retVal = new StringBuffer("");
		StringBuffer number = new StringBuffer("");
		char end, start;
		for (int count = 0; count < input.length(); count++) {
			chars[count] = Integer.toString(((int) input.charAt(count)) - 32);
			if (chars[count].length() == 1)
				chars[count] = "0" + chars[count];
		}
		number.append(chars[chars.length - 1].substring(1, 2));
		for (int count = 0; count < chars.length - 1; count++)
			number.append(chars[count]);
		number.append(chars[chars.length - 1].substring(0, 1));
		end = number.charAt(0);
		for (int count = 0; count + 3 < number.length(); count += 3) {
			start = number.charAt(count + 3);
			number.setCharAt(count + 3, end);
			end = start;
		}
		number.setCharAt(0, end);
		chars = new String[number.length() / 4];
		for (int count = 0; (count * 4 + 4) <= number.length(); count++)
			chars[count] = number.toString()
					.substring(count * 4, count * 4 + 4);
		for (int count = 0; count < chars.length; count++)
			chars[count] = Functions.baseConvert(chars[count], 8, 10);
		for (int count = 0; count < chars.length; count++)
			retVal.append((char) Integer.parseInt(chars[count]));
		return retVal.toString();
	}

	/***************************************************************************
	 * Gets the current increment value for a given table @ param table Table to
	 * check @ param keyColumn Column holding the increment value
	 * @return The current increment value
	 **************************************************************************/
	public Object getIncrementValue(String table, String keyColumn) {
		log.debug("DBConnect: getting increment");
		// Add generic implementation here
		return null;
	}

	@CoinjemaDependency(type = "database", method = "database")
	public void setDatabase(DataBase db) {
		database = db;
	}

} // End of Class
