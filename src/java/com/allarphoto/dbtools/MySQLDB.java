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

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.allarphoto.utils.Data;
import com.allarphoto.utils.Functions;

public class MySQLDB extends DBConnect {

	String driverString = new String("org.gjt.mm.mysql.Driver");

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

	public boolean createTable(TableDesign parm1) {
		// TODO: implement this com.allarphoto.dbtools.DBConnect abstract method
		return false;
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
		} catch (Exception e) {
			Functions.javaLog("Interbase: e = " + e.toString());
			Functions.javaLog("Interbase: " + queryString.toString());

			throw new RuntimeException(e);
		}
		return returnValue;
	} // end of Method

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
		return "'" + date + " 00:00:00'";
	}

	/***************************************************************************
	 * Returns the current date in proper SQL database format.
	 * 
	 * @return Date as a String in proper database format.
	 **************************************************************************/
	public String getDate(java.util.Date dt) {
		GregorianCalendar d = new GregorianCalendar();
		d.setTime(dt);
		String date = d.get(Calendar.YEAR) + "-" + (d.get(Calendar.MONTH) + 1)
				+ "-" + d.get(Calendar.DAY_OF_MONTH) + " "
				+ d.get(Calendar.HOUR_OF_DAY) + ":" + d.get(Calendar.MINUTE)
				+ ":" + d.get(Calendar.SECOND);
		return date;
	} // End Method

	/***************************************************************************
	 * Returns the current date in proper SQL database format.
	 * 
	 * @return d as a String in proper database format.
	 **************************************************************************/
	public String getDate(Calendar d) {
		String date = d.get(Calendar.YEAR) + "-" + (d.get(Calendar.MONTH) + 1)
				+ "-" + d.get(Calendar.DAY_OF_MONTH) + " "
				+ d.get(Calendar.HOUR_OF_DAY) + ":" + d.get(Calendar.MINUTE)
				+ ":" + d.get(Calendar.SECOND);
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
	 * Returns the current date in proper SQL database format.
	 * 
	 * @return Date as a String in proper database format.
	 **************************************************************************/
	public String getDate() {
		GregorianCalendar d = new GregorianCalendar();
		String date = d.get(Calendar.YEAR) + "-" + (d.get(Calendar.MONTH) + 1)
				+ "-" + d.get(Calendar.DAY_OF_MONTH) + " "
				+ d.get(Calendar.HOUR_OF_DAY) + ":" + d.get(Calendar.MINUTE)
				+ ":" + d.get(Calendar.SECOND);
		return date;
	} // End Method

	public String getDateForComparison(GregorianCalendar d) {
		String date = d.get(Calendar.YEAR) + "-" + (d.get(Calendar.MONTH) + 1)
				+ "-" + d.get(Calendar.DAY_OF_MONTH) + ":"
				+ d.get(Calendar.HOUR_OF_DAY) + "-" + d.get(Calendar.MINUTE)
				+ "-" + d.get(Calendar.SECOND);
		return "'" + date + "'";
	}

	public boolean createPrimaryAutoNumber(TableDesign parm1) {
		// TODO: implement this com.allarphoto.dbtools.DBConnect abstract method
		return false;
	}

	public void dropIndexes(TableDesign parm1) {
		// TODO: implement this com.allarphoto.dbtools.DBConnect abstract method
	}

	public void createIndexes(TableDesign parm1) {
		// TODO: implement this com.allarphoto.dbtools.DBConnect abstract method
	}

	public String setType(TableDesign parm1, String parm2) {
		return "";
		// TODO: implement this com.allarphoto.dbtools.DBConnect abstract method
	}

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
		return null;
		// TODO: implement this com.allarphoto.dbtools.DBConnect abstract method
	}

}
