/*******************************************************************************
 * Copyright (C) 1999 Michael Stover This program is free software; you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version. This program is distributed
 * in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details. You should have received
 * a copy of the GNU General Public License along with this program; if not,
 * write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA. Michael Stover can be reached via email at
 * mstover1@rochester.rr.com or via snail mail at 130 Corwin Rd. Rochester, NY
 * 14610 The following exception to this license exists for Lazer Incorporated:
 * Lazer Inc is excepted from all limitations and requirements stipulated in the
 * GPL. Lazer Inc. is the only entity allowed this limitation. Lazer does have
 * the right to sell this exception, if they choose, but they cannot grant
 * additional exceptions to any other entities.
 ******************************************************************************/

// Title: PerformanceLog
// Author: Michael Stover
// Company: Lazer Inc.
package com.allarphoto.utils;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

import com.allarphoto.beans.MethodPerformance;

public class PerformanceLog implements Serializable {
	private static final long serialVersionUID = 1;

	private static Hashtable methodID = new Hashtable();

	private long startTime;

	public PerformanceLog() {
	}

	/***************************************************************************
	 * Writes a message to the javaPerformance.txt file in the current user's
	 * working directory.
	 * 
	 * @param method
	 *            Name of calling method.
	 * @return A String object used to link start and end calls.
	 **************************************************************************/
	public void start(String method) {
		startTime = System.currentTimeMillis();
		MethodPerformance perf;
		if (!methodID.containsKey(method)) {
			perf = new MethodPerformance();
			perf.setMethodName(method);
			synchronized (methodID) {
				methodID.put(method, perf);
			}
		}
	}

	/***************************************************************************
	 * Writes a message to the javaPerformance.txt file in the current user's
	 * working directory.
	 * 
	 * @param method
	 *            Name of calling method.
	 * @param id
	 *            String to be used to retrieve start info for this method.
	 **************************************************************************/
	public void end(String method) {
		long time = System.currentTimeMillis();
		MethodPerformance perf = (MethodPerformance) methodID.get(method);
		perf.hit();
		perf.addTime(time - startTime);
	}

	/***************************************************************************
	 * Prints out performance log results to performancelog.txt in the current
	 * working directory.
	 **************************************************************************/
	public static void printResults() {
		String sys = System.getProperty("user.dir");
		String cr = System.getProperty("line.separator");
		String fs = System.getProperty("file.separator");
		sys = sys + fs + "javaperformance.txt";
		Enumeration enumer = methodID.elements();
		while (enumer.hasMoreElements()) {
			MethodPerformance perf = (MethodPerformance) enumer.nextElement();
			Filer.appendToFile(sys,
					perf.getMethodName()
							+ ":"
							+ cr
							+ "\tNumber Hits: "
							+ perf.getNumberHits()
							+ cr
							+ "\tTotal Time: "
							+ Functions.round((double) ((double) perf
									.getTotalTime() / (double) 1000), 2) + cr
							+ "\tAverage Time: " + perf.getAverageTime() + cr);
		}
	}

}
