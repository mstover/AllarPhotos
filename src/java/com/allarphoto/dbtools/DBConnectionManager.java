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
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.lazerinc.utils.Functions;

/*******************************************************************************
 * This class manages a pool of Connection objects (ConnectionObject). This pool
 * is constantly checked for old, over-used, or dead connections in a separated
 * thread. Connections are rented out and then given back by the DBConnect
 * object and its subclasses. This class is not directly accessed by the
 * end-user objects. It is accessed by the DBConnect object and its subclasses.
 * 
 * @author Michael Stover
 * @version 1.0 10/13/1998
 ******************************************************************************/
public class DBConnectionManager {

	static int absoluteMaxConnections = 100;

	static long accessInterval = 1800000;

	static Hashtable connections;

	static Hashtable rentedConnections;

	/***************************************************************************
	 * Constructor.
	 **************************************************************************/
	public DBConnectionManager() {
		if (connections == null)
			connections = new Hashtable();
		if (rentedConnections == null)
			rentedConnections = new Hashtable();
	}

	/***************************************************************************
	 * Starts the connection manager going for a given database connection, and
	 * returns the DBKey object required to get a Connection object for this
	 * database.
	 * 
	 * @param url
	 *            URL of database to be connected to.
	 * @param username
	 *            Username to use to connect to database.
	 * @param password
	 *            Password to use to connect to database.
	 * @param driver
	 *            Driver to use for the database.
	 * @param maxUsage
	 *            Sets the maxUsage parameter for connections to this database.
	 * @param maxConnections
	 *            Tells the DBConnectionManager how many connections to keep
	 *            active.
	 * @return DBKey object. Returns null if connection fails.
	 **************************************************************************/
	public DBKey startDatabaseConnection(String url, String username,
			String password, String driver, int maxUsage, int maxConnections) {
		DBKey key = new DBKey();
		if (registerDriver(driver)) {
			key.setDriver(driver);
			key.setMaxConnections(maxConnections);
			key.setMaxUsage(maxUsage);
			key.setPassword(password);
			key.setUrl(url);
			key.setUsername(username);
			if (!connections.containsKey(key))
				setup(key);
		} else
			key = null;
		return key;
	}

	/***************************************************************************
	 * Constructor.
	 * 
	 * @param key
	 *            DBKey that holds all information needed to set up a set of
	 *            connections.
	 **************************************************************************/
	public void setup(DBKey key) {
		/*
		 * Code used to create a JDBC log for debuggin purposes try{
		 * java.io.PrintWriter jdbcLog=new java.io.PrintWriter(new
		 * java.io.FileWriter(jdbcLogFile));
		 * DriverManager.setLogWriter(jdbcLog); }catch(Exception e){}
		 */
		String url = key.getUrl();
		if (testUrlForJndi(url)) {
			connections.put(key, getDataSource(url));
			return;
		}
		String username = key.getUsername();
		String password = key.getPassword();
		int maxConnections = key.getMaxConnections();
		int maxUsage = key.getMaxUsage();
		ConnectionObject[] connectionArray;
		int dbMax;
		try {
			DatabaseMetaData md = DriverManager.getConnection(url, username,
					password).getMetaData();
			dbMax = md.getMaxConnections();
			if (dbMax > 0 && maxConnections > dbMax) {
				maxConnections = dbMax;
				key.setMaxConnections(maxConnections);
			} else if (maxConnections > absoluteMaxConnections) {
				maxConnections = absoluteMaxConnections;
				key.setMaxConnections(maxConnections);
			}
		} catch (Exception e) {
			maxConnections = 1;
		}
		connectionArray = new ConnectionObject[maxConnections];
		int count = -1;
		while (++count < maxConnections)
			connectionArray[count] = new ConnectionObject(this, key);
		connections.put(key, connectionArray);
		System.gc();
	} // End Method

	private DataSource getDataSource(String url) {
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			return (DataSource) envCtx.lookup(getJndiNameFromUrl(url));
		} catch (Exception e) {
			throw new RuntimeException("Problem retrieving JNDI datasource"
					+ url);
		}
	}

	private boolean testUrlForJndi(String url) {
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			String jndiName = getJndiNameFromUrl(url);
			DataSource source = (DataSource) envCtx.lookup(jndiName);
			if (source != null)
				return true;
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	private String getJndiNameFromUrl(String url) {
		String jndiName = url.substring(url.lastIndexOf(":") + 1);
		while (jndiName.startsWith("/"))
			jndiName = jndiName.substring(1);
		return jndiName;
	}

	/***************************************************************************
	 * Rents out a database connection object.
	 * 
	 * @return Connection object.
	 **************************************************************************/
	public Connection getConnection(DBKey key) // deleted synchronized
	{
		Object connectionObject = connections.get(key);
		if (connectionObject instanceof DataSource) {
			try {
				return ((DataSource) connectionObject).getConnection();
			} catch (SQLException e) {
				throw new RuntimeException("Couldn't retrieve jdbc connection "
						+ key);
			}
		}
		ConnectionObject[] connectionArray = (ConnectionObject[]) connectionObject;
		int maxConnections = key.getMaxConnections();
		Connection c = null;
		int index = (int) (100 * Math.random());
		int count = -1;
		while (++count < maxConnections && c == null) {
			index++;
			c = connectionArray[index % maxConnections].grab();
		}
		if (c != null)
			rentedConnections.put(c, connectionArray[index % maxConnections]);
		return c;
	} // End Method

	/***************************************************************************
	 * Releases a connection back to the pool
	 * 
	 * @param c
	 *            Connection object being returned
	 **************************************************************************/
	public void releaseConnection(Connection c) // deleted synchronized
	{
		ConnectionObject connOb = (ConnectionObject) rentedConnections.get(c);
		if (connOb != null) {
			connOb.release();
			rentedConnections.remove(c);
		} else {
			try {
				c.close();
			} catch (SQLException e) {
				throw new RuntimeException("Failed to close connection");
			}
		}
	} // End Method

	/***************************************************************************
	 * Returns a new java.sql.Connection object.
	 * 
	 * @throws java.sql.SQLException
	 **************************************************************************/
	public Connection newConnection(DBKey key) throws SQLException // deleted
	// synchronized
	{
		Connection c;

		System.out.println("newConnection() getting new connection: "
				+ key.getUrl() + ":" + key.getUsername() + ":"
				+ key.getPassword());
		try {
			c = DriverManager.getConnection(key.getUrl(), key.getUsername(),
					key.getPassword());
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		System.out.println("newConnection() new connection = " + c);
		return c;
	}

	/***************************************************************************
	 * Closes out this object and returns resources to the system.
	 **************************************************************************/
	public void close(DBKey key) {
		Object connectionObject = connections.get(key);
		if (connectionObject instanceof DataSource) {
			connections.remove(key);
			return;
		}
		ConnectionObject[] connectionArray = (ConnectionObject[]) connectionObject;
		int count = -1;
		while (++count < connectionArray.length) {
			connectionArray[count].close();
			connectionArray[count] = null;
		}
		connections.remove(key);
	}

	/***************************************************************************
	 * Registers a driver for a database.
	 * 
	 * @param driver
	 *            full classname for the driver.
	 * @return True if successful, false otherwise.
	 **************************************************************************/
	public boolean registerDriver(String driver) {
		try {
			Functions.javaLog("trying to register driver: " + driver);
			DriverManager.registerDriver((Driver) Class.forName(driver)
					.newInstance());
		} catch (Exception e) {
			Functions.javaLog("failed to register driver: " + driver);
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/***************************************************************************
	 * Private method to check if database exists.
	 * 
	 * @return True if database exists, false otherwise
	 *         *****************************************************************
	 *         private synchronized boolean checkForDatabase() { boolean
	 *         connected=true; try { DatabaseMetaData
	 *         dmd=connection[counter].getCon().getMetaData(); int
	 *         cons=dmd.getMaxConnections(); }catch(SQLException
	 *         e){connected=false;e.printStackTrace();} return connected; }
	 *         //end of method
	 */
}
