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
import java.sql.SQLException;

import com.lazerinc.utils.Functions;

/*******************************************************************************
 * A wrapper class for a database Connection object. This class holds
 * information about the state of the connection object in a pool managed by a
 * DBConnectionManager object. It keeps track of how many times the connection
 * object has been used, the time of its last usage, and whether it is currently
 * in use.
 * 
 * @author Michael Stover
 * @version 1.0 10/13/1998
 ******************************************************************************/
public class ConnectionObject implements Runnable {

	Connection con;

	DBKey key;

	int useCount, maxUsage;

	long lastAccessed;

	boolean inUse, inMaintenance;

	DBConnectionManager manager;

	Thread reset;

	static long accessInterval = 180000;

	/***************************************************************************
	 * Constructor - takes a connection object.
	 * 
	 * @param man
	 *            DBConnectionManager object.
	 * @param k
	 *            DBKey object.
	 **************************************************************************/
	public ConnectionObject(DBConnectionManager man, DBKey k) {
		key = k;
		manager = man;
		reset = new Thread(this);
		useCount = 0;
		lastAccessed = System.currentTimeMillis();
		inMaintenance = true;
		inUse = false;
		con = null;
		maxUsage = key.getMaxUsage();
		reset.start();
		/*
		 * try{ reset(); }catch(SQLException e){e.printStackTrace();}
		 */
	} // End Method

	/***************************************************************************
	 * Gets whether the Connection Object is being maintained.
	 * 
	 * @return true if the ConnectionObject is being maintained, false
	 *         otherwise.
	 **************************************************************************/
	public boolean getInMaintenance() {
		return inMaintenance;
	}

	/***************************************************************************
	 * Sets whether the Connection Object is being maintained.
	 * 
	 * @param b
	 *            true if the ConnectionObject is being maintained, false
	 *            otherwise.
	 **************************************************************************/
	public void setInMaintenance(boolean b) {
		inMaintenance = b;
	}

	/***************************************************************************
	 * Gets the last time this object was accessed.
	 * 
	 * @return Time (in milliseconds) the connection object was last used
	 **************************************************************************/
	public long getLastAccessed() {
		return lastAccessed;
	} // End Method

	/***************************************************************************
	 * Closes out this object and returns resources to the system.
	 **************************************************************************/
	public void close() {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
			}
		}
		con = null;
	}

	/***************************************************************************
	 * Updates the last accessed time for the connection object.
	 **************************************************************************/
	public void update() {
		lastAccessed = System.currentTimeMillis();
	} // End Method

	/***************************************************************************
	 * Gets whether the connection object is currently in use.
	 * 
	 * @return True if it is in use, false otherwise.
	 **************************************************************************/
	public boolean getInUse() {
		return inUse;
	} // End Method

	/***************************************************************************
	 * Grabs the connection and sets the inUse value to true.
	 * 
	 * @return connection object
	 **************************************************************************/
	public synchronized Connection grab() {
		Connection c = null;
		if (!inUse && !inMaintenance) {
			if (con != null) {
				try {
					if (con.isClosed()) {
						inMaintenance = true;
						// reset=new Thread(this);
						reset.start();
					} else if (System.currentTimeMillis() - lastAccessed > accessInterval) {
						inMaintenance = true;
						// reset=new Thread(this);
						reset.start();
					} else {
						inUse = true;
						c = con;
					}
				} catch (SQLException e) {
				}
			} else {
				inMaintenance = true;
				// reset=new Thread(this);
				reset.start();
			}
		}
		return c;
	}

	/***************************************************************************
	 * Gets the number of times this connection object has been used.
	 * 
	 * @return Number of times the connection object has been used.
	 **************************************************************************/
	public int getUseCount() {
		return useCount;
	} // End Method

	/***************************************************************************
	 * Method to run in separate thread that resets the connection object
	 **************************************************************************/
	public void run() {
		// Functions.javaLog("ConnectionObject: Got to here - 1");
		boolean set = true;
		while (set) {
			try {
				reset();
				set = false;
				// Functions.javaLog("ConnectionObject: Got to here - 2");
			} catch (SQLException e) {
				Functions.javaLog("ConnectionObject: e=" + e.toString());
			}
		}
		// Functions.javaLog("ConnectionObject: Got to here - 3");
		reset = new Thread(this);
	}

	/***************************************************************************
	 * Resets the use count, the last accessed time, and the in Use values And
	 * replaces the old connection object with the new one.
	 * 
	 * @param c
	 *            new connection object to set to
	 **************************************************************************/
	public void reset() throws SQLException {
		if (con != null) {
			// try{
			// con.commit();
			// }catch(SQLException e){}
			try {
				con.close();
			} catch (Exception e) {
			}
			con = null;
		}
		con = manager.newConnection(key);
		useCount = 0;
		lastAccessed = System.currentTimeMillis();
		inUse = false;
		inMaintenance = false;
	} // End Method

	/***************************************************************************
	 * Releases the connection object. Increments its usage count, updates the
	 * last accessed time, and returns it for use in the pool.
	 **************************************************************************/
	public void release() {
		useCount++;
		try {
			if (con != null) {
				// con.commit();
				if (System.currentTimeMillis() - lastAccessed > accessInterval) {
					inMaintenance = true;
					// reset=new Thread(this);
					reset.start();
				} else if (useCount >= maxUsage) {
					inMaintenance = true;
					// reset=new Thread(this);
					reset.start();
				} else if (con.isClosed()) {
					inMaintenance = true;
					// reset=new Thread(this);
					reset.start();
				}
			} else {
				inMaintenance = true;
				// reset=new Thread(this);
				reset.start();
			}
		} catch (SQLException e) {
			inMaintenance = true;
			reset.start();
		}
		inUse = false;
		lastAccessed = System.currentTimeMillis();
	} // End Method

	/***************************************************************************
	 * Returns the connection held by this connection object.
	 * 
	 * @return Connection object
	 **************************************************************************/
	public Connection getCon() {
		return con;
	} // End Method
}
