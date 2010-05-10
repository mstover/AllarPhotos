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

package com.allarphoto.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

/*******************************************************************************
 * Class of static functions for various purposes.
 * 
 * @author Michael Stover
 * @version 1.0 10/14/1998
 ******************************************************************************/
public class Functions {

	private static Logger log = Logger.getLogger(Functions.class);

	/***************************************************************************
	 * static variable for the assert method. If true, assert checking is on. If
	 * false, assert checking is off.
	 **************************************************************************/
	public final static boolean NDEBUG = true;

	/***************************************************************************
	 * Returns the String representation of the month given the months number
	 * (1-12)
	 * 
	 * @param month
	 *            int representing the month desired.
	 * @return String representation of the month.
	 **************************************************************************/
	public static String getMonth(int month) {
		String[] months = { "January", "February", "March", "April", "May",
				"June", "July", "August", "September", "October", "November",
				"December" };
		if (month > 0 && month < 13)
			return months[month - 1];
		else
			return "Bad month";
	}

	public static GregorianCalendar getDate(String date) {
		String[] vals = split(date, "/");
		if (vals.length != 3)
			return null;
		int month = Integer.parseInt(vals[0]), day = Integer.parseInt(vals[1]), year = Integer
				.parseInt(vals[2]);
		year = y2kConvert(year);
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.YEAR, year);
		return cal;
	}

	public static int y2kConvert(int year) {
		if (year < 100) {
			int currentYear = new GregorianCalendar().get(Calendar.YEAR);
			int cent = currentYear / 100;
			currentYear %= 100;
			int factor;
			if (currentYear >= 50)
				factor = 1;
			else
				factor = -1;
			if (Math.abs(year % 100 - currentYear) < 50)
				year = cent * 100 + year;
			else
				year = (cent + factor) * 100 + year;
		}
		return year;
	}

	/***************************************************************************
	 * Checks the given boolean argument. If true, does nothing. If false, it
	 * throws an AssertException.
	 * 
	 * @param expression
	 *            Boolean expression to be tested.
	 * @return If successfull, it always returns true.
	 * @throws AssertException.
	 **************************************************************************/
	public static boolean assertTrue(boolean expression) throws AssertException {
		if (NDEBUG && !expression)
			throw new AssertException();
		return true;
	}

	/***************************************************************************
	 * Returns a single string from the elements given. Purpose of this method
	 * is to be faster than regulare string concatenation.
	 * 
	 * @param s1
	 *            String to be concatted.
	 * @param s2
	 *            String to be concatted.
	 * @param s3
	 *            String to be concatted.
	 * @return concatenated string.
	 **************************************************************************/
	public static String concat(String s1, String s2, String s3) {
		StringBuffer s = new StringBuffer(s1);
		s.append(s2);
		s.append(s3);
		return s.toString();
	} // End Method

	/***************************************************************************
	 * Returns a single string from the elements given. Purpose of this method
	 * is to be faster than regulare string concatenation.
	 * 
	 * @param s1
	 *            String array to be concatted all into 1 string.
	 * @return concatenated string.
	 **************************************************************************/
	public static String concat(String[] s1) {
		StringBuffer s = new StringBuffer("");
		int count = -1;
		while (++count < s1.length)
			s.append(s1[count]);
		return s.toString();
	} // End Method

	/***************************************************************************
	 * Returns a single string from the elements given. Purpose of this method
	 * is to be faster than regulare string concatenation.
	 * 
	 * @param s1
	 *            String to be concatted.
	 * @param s2
	 *            String to be concatted.
	 * @param s3
	 *            String to be concatted.
	 * @param s4
	 *            String to be concatted.
	 * @return concatenated string.
	 **************************************************************************/
	public static String concat(String s1, String s2, String s3, String s4) {
		StringBuffer s = new StringBuffer(s1);
		s.append(s2);
		s.append(s3);
		s.append(s4);
		return s.toString();
	} // End Method

	/***************************************************************************
	 * Returns a single string from the elements given. Purpose of this method
	 * is to be faster than regulare string concatenation.
	 * 
	 * @param s1
	 *            String to be concatted.
	 * @param s2
	 *            String to be concatted.
	 * @param s3
	 *            String to be concatted.
	 * @param s4
	 *            String to be concatted.
	 * @param s5
	 *            String to be concatted.
	 * @return concatenated string.
	 **************************************************************************/
	public static String concat(String s1, String s2, String s3, String s4,
			String s5) {
		StringBuffer s = new StringBuffer(s1);
		s.append(s2);
		s.append(s3);
		s.append(s4);
		s.append(s5);
		return s.toString();
	} // End Method

	/***************************************************************************
	 * Returns a single string from the elements given. Purpose of this method
	 * is to be faster than regulare string concatenation.
	 * 
	 * @param s1
	 *            String to be concatted.
	 * @param s2
	 *            String to be concatted.
	 * @param s3
	 *            String to be concatted.
	 * @param s4
	 *            String to be concatted.
	 * @param s5
	 *            String to be concatted.
	 * @param s6
	 *            String to be concatted.
	 * @return concatenated string.
	 **************************************************************************/
	public static String concat(String s1, String s2, String s3, String s4,
			String s5, String s6) {
		StringBuffer s = new StringBuffer(s1);
		s.append(s2);
		s.append(s3);
		s.append(s4);
		s.append(s5);
		s.append(s6);
		return s.toString();
	} // End Method

	/***************************************************************************
	 * Returns a single string from the elements given. Purpose of this method
	 * is to be faster than regulare string concatenation.
	 * 
	 * @param s1
	 *            String to be concatted.
	 * @param s2
	 *            String to be concatted.
	 * @param s3
	 *            String to be concatted.
	 * @param s4
	 *            String to be concatted.
	 * @param s5
	 *            String to be concatted.
	 * @param s6
	 *            String to be concatted.
	 * @param s7
	 *            String to be concatted.
	 * @return concatenated string.
	 **************************************************************************/
	public static String concat(String s1, String s2, String s3, String s4,
			String s5, String s6, String s7) {
		StringBuffer s = new StringBuffer(s1);
		s.append(s2);
		s.append(s3);
		s.append(s4);
		s.append(s5);
		s.append(s6);
		s.append(s7);
		return s.toString();
	} // End Method

	/***************************************************************************
	 * Returns a single string from the elements given. Purpose of this method
	 * is to be faster than regulare string concatenation.
	 * 
	 * @param s1
	 *            String to be concatted.
	 * @param s2
	 *            String to be concatted.
	 * @param s3
	 *            String to be concatted.
	 * @param s4
	 *            String to be concatted.
	 * @param s5
	 *            String to be concatted.
	 * @param s6
	 *            String to be concatted.
	 * @param s7
	 *            String to be concatted.
	 * @param s8
	 *            String to be concatted.
	 * @return concatenated string.
	 **************************************************************************/
	public static String concat(String s1, String s2, String s3, String s4,
			String s5, String s6, String s7, String s8) {
		StringBuffer s = new StringBuffer(s1);
		s.append(s2);
		s.append(s3);
		s.append(s4);
		s.append(s5);
		s.append(s6);
		s.append(s7);
		s.append(s8);
		return s.toString();
	} // End Method

	/***************************************************************************
	 * Returns a single string from the elements given. Purpose of this method
	 * is to be faster than regulare string concatenation.
	 * 
	 * @param s1
	 *            String to be concatted.
	 * @param s2
	 *            String to be concatted.
	 * @param s3
	 *            String to be concatted.
	 * @param s4
	 *            String to be concatted.
	 * @param s5
	 *            String to be concatted.
	 * @param s6
	 *            String to be concatted.
	 * @param s7
	 *            String to be concatted.
	 * @param s8
	 *            String to be concatted.
	 * @param s9
	 *            String to be concatted.
	 * @return concatenated string.
	 **************************************************************************/
	public static String concat(String s1, String s2, String s3, String s4,
			String s5, String s6, String s7, String s8, String s9) {
		StringBuffer s = new StringBuffer(s1);
		s.append(s2);
		s.append(s3);
		s.append(s4);
		s.append(s5);
		s.append(s6);
		s.append(s7);
		s.append(s8);
		s.append(s9);
		return s.toString();
	} // End Method

	/***************************************************************************
	 * Returns a single string from the elements given. Purpose of this method
	 * is to be faster than regulare string concatenation.
	 * 
	 * @param s1
	 *            String to be concatted.
	 * @param s2
	 *            String to be concatted.
	 * @param s3
	 *            String to be concatted.
	 * @param s4
	 *            String to be concatted.
	 * @param s5
	 *            String to be concatted.
	 * @param s6
	 *            String to be concatted.
	 * @param s7
	 *            String to be concatted.
	 * @param s8
	 *            String to be concatted.
	 * @param s9
	 *            String to be concatted.
	 * @param s10
	 *            String to be concatted.
	 * @return concatenated string.
	 **************************************************************************/
	public static String concat(String s1, String s2, String s3, String s4,
			String s5, String s6, String s7, String s8, String s9, String s10) {
		StringBuffer s = new StringBuffer(s1);
		s.append(s2);
		s.append(s3);
		s.append(s4);
		s.append(s5);
		s.append(s6);
		s.append(s7);
		s.append(s8);
		s.append(s9);
		s.append(s10);
		return s.toString();
	} // End Method

	/***************************************************************************
	 * Returns a single string from the elements given. Purpose of this method
	 * is to be faster than regulare string concatenation.
	 * 
	 * @param s1
	 *            String to be concatted.
	 * @param s2
	 *            String to be concatted.
	 * @param s3
	 *            String to be concatted.
	 * @param s4
	 *            String to be concatted.
	 * @param s5
	 *            String to be concatted.
	 * @param s6
	 *            String to be concatted.
	 * @param s7
	 *            String to be concatted.
	 * @param s8
	 *            String to be concatted.
	 * @param s9
	 *            String to be concatted.
	 * @param s10
	 *            String to be concatted.
	 * @param s11
	 *            String to be concatted.
	 * @return concatenated string.
	 **************************************************************************/
	public static String concat(String s1, String s2, String s3, String s4,
			String s5, String s6, String s7, String s8, String s9, String s10,
			String s11) {
		StringBuffer s = new StringBuffer(s1);
		s.append(s2);
		s.append(s3);
		s.append(s4);
		s.append(s5);
		s.append(s6);
		s.append(s7);
		s.append(s8);
		s.append(s9);
		s.append(s10);
		s.append(s11);
		return s.toString();
	} // End Method

	/***************************************************************************
	 * Returns a single string from the elements given. Purpose of this method
	 * is to be faster than regulare string concatenation.
	 * 
	 * @param s1
	 *            String to be concatted.
	 * @param s2
	 *            String to be concatted.
	 * @param s3
	 *            String to be concatted.
	 * @param s4
	 *            String to be concatted.
	 * @param s5
	 *            String to be concatted.
	 * @param s6
	 *            String to be concatted.
	 * @param s7
	 *            String to be concatted.
	 * @param s8
	 *            String to be concatted.
	 * @param s9
	 *            String to be concatted.
	 * @param s10
	 *            String to be concatted.
	 * @param s11
	 *            String to be concatted.
	 * @param s12
	 *            String to be concatted.
	 * @return concatenated string.
	 **************************************************************************/
	public static String concat(String s1, String s2, String s3, String s4,
			String s5, String s6, String s7, String s8, String s9, String s10,
			String s11, String s12) {
		StringBuffer s = new StringBuffer(s1);
		s.append(s2);
		s.append(s3);
		s.append(s4);
		s.append(s5);
		s.append(s6);
		s.append(s7);
		s.append(s8);
		s.append(s9);
		s.append(s10);
		s.append(s11);
		s.append(s12);
		return s.toString();
	} // End Method

	/***************************************************************************
	 * Writes a message to the javalog.txt file in the current user's working
	 * directory.
	 * 
	 * @param mess
	 *            Message to write - suggest including application name in each
	 *            message.
	 **************************************************************************/
	public synchronized static void javaLog(String mess) {
		log.info(mess);
	}

	/***************************************************************************
	 * Method to send all output to the browser. It sets the content length
	 * 
	 * @param res
	 *            servlet response object
	 * @param output
	 *            String to write to browser
	 **************************************************************************/
	public static void outputToBrowser(
			javax.servlet.http.HttpServletResponse res, String output) {
		PrintWriter out;
		try {
			out = new PrintWriter(res.getOutputStream());
			int length = output.length();
			res.setContentType("text/html");
			res.setContentLength(length);
			out.print(output);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	} // End Method

	/***************************************************************************
	 * Finds a string in an array of strings and returns the index values of
	 * "array" that are contained in the array "value".
	 * 
	 * @param array
	 *            Array of strings.
	 * @param value
	 *            Array of Strings to compare to array values.
	 * @return Array of indexes of value in array, or -1 if none in array.
	 **************************************************************************/
	public static int[] findInArray(String[] array, String[] value) {
		int count = 0, count1 = 0;
		OrderedIntList list = new OrderedIntList();
		while (count < array.length) {
			count1 = 0;
			while (count1 < value.length) {
				if (array[count].equals(value[count1])) {
					list.add(count);
					break;
				}
				count1++;
			}
			count++;
		}
		return list.getArray();
	}

	/***************************************************************************
	 * Gets the strings in the first array that are not in the second array.
	 * 
	 * @param array
	 *            Array of strings.
	 * @param value
	 *            Array of Strings to compare to array values.
	 * @return Array of Strings from the first array that are not part of the
	 *         second array.
	 **************************************************************************/
	public static String[] getNotInArray(String[] array, String[] value) {
		int count = 0, count1 = 0;
		boolean notin;
		Vector list = new Vector();
		while (array != null && count < array.length) {
			count1 = 0;
			notin = true;
			while (value != null && count1 < value.length) {
				if (array[count].equals(value[count1])) {
					notin = false;
					break;
				}
				count1++;
			}
			if (notin)
				list.addElement(array[count]);
			count++;
		}
		String[] result = new String[list.size()];
		list.copyInto(result);
		return result;
	}

	/***************************************************************************
	 * Gets the strings in the first array that are in the second array.
	 * 
	 * @param array
	 *            Array of strings.
	 * @param value
	 *            Array of Strings to compare to array values.
	 * @return Array of Strings from the first array that are part of the second
	 *         array.
	 **************************************************************************/
	public static String[] getInArray(String[] array, String[] value) {
		int count = 0, count1 = 0;
		Vector list = new Vector();
		while (array != null && count < array.length) {
			count1 = 0;
			while (value != null && count1 < value.length) {
				if (array[count].equals(value[count1])) {
					list.addElement(array[count]);
					break;
				}
				count1++;
			}
			count++;
		}
		String[] result = new String[list.size()];
		list.copyInto(result);
		return result;
	}

	/***************************************************************************
	 * Finds a string in an array of strings and returns the
	 * 
	 * @param array
	 *            Array of strings.
	 * @param value
	 *            String to compare to array values.
	 * @return Index of value in array, or -1 if not in array.
	 **************************************************************************/
	public static int findInArray(String[] array, String value) {
		int count = -1;
		int index = -1;
		if (array != null && value != null) {
			while (++count < array.length) {
				if (array[count] != null && array[count].equals(value)) {
					index = count;
					break;
				}
			}
		}
		return index;
	}

	/***************************************************************************
	 * Adds the elements of int array b to int array a and returns the resulting
	 * array.
	 * 
	 * @param a
	 *            Int Array to be added to.
	 * @param b
	 *            Int array to add from.
	 * @return int array of all combined elements from a and b.
	 **************************************************************************/
	public static int[] combineIntArrays(int[] a, int[] b) {
		int[] c = new int[a.length + b.length];
		int count = 0, mark;
		while (count < a.length) {
			c[count] = a[count];
			count++;
		}
		mark = count;
		count = 0;
		while (count < b.length)
			c[mark++] = b[count++];
		return c;

	} // End Method

	/***************************************************************************
	 * Adds an element to an array.
	 * 
	 * @param array
	 *            Array to add an element to.
	 * @param obj
	 *            Object to be added to array.
	 * @return Resulting array.
	 **************************************************************************/
	public static String[] addToArray(String[] array, Object obj) {
		String[] result = new String[array.length + 1];
		Vector vec = new Vector(array.length + 1);
		int count = -1;
		while (++count < array.length)
			result[count] = array[count];
		result[count] = obj.toString();
		return result;
	}

	/***************************************************************************
	 * Adds an element to an array.
	 * 
	 * @param array
	 *            Array to add an element to.
	 * @param obj
	 *            Object array to be added to array.
	 * @return Resulting array.
	 **************************************************************************/
	public static String[] addToArray(String[] array, Object[] obj) {
		String[] result = new String[array.length + obj.length];
		int count = -1;
		while (++count < array.length)
			result[count] = array[count];
		int counter = -1;
		while (++counter < obj.length)
			result[count++] = obj[counter].toString();
		return result;
	}

	/***************************************************************************
	 * This method takes a list of files and jars them up, saving the jar file
	 * where instructed
	 * 
	 * @param files
	 *            array of File objects to be jarred up
	 * @param zipFileName
	 *            name of file to save jar file to
	 * @return boolean true if successful at jarring everything, false otherwise
	 **************************************************************************/
	public static boolean jar(File[] files, String jarFileName) {
		byte[] buffer = new byte[10000];
		int numBytes;
		boolean returnValue = true;
		JarOutputStream jarFile;
		FileInputStream getFile;
		try {
			jarFile = new JarOutputStream(new FileOutputStream(jarFileName));
			int countFiles = 0;

			while (countFiles < files.length) {

				try {
					getFile = new FileInputStream(files[countFiles]);
				} catch (IOException exc) {
					try {
						exc.printStackTrace();
						getFile = new FileInputStream(
								"d:\\netscape\\suitespot\\docs\\iwimages\\lazerlogo.gif");
					} catch (IOException ex) {
						return false;
					}
				}
				jarFile.putNextEntry(new JarEntry(files[countFiles].getName()));
				do {
					numBytes = getFile.read(buffer);
					if (numBytes > -1)
						jarFile.write(buffer, 0, numBytes);
				} while (numBytes > -1);
				getFile.close();
				countFiles++;
				jarFile.closeEntry();
			}
			jarFile.close();
		} catch (Exception e) {
			System.out.println("bad file parameter");
			e.printStackTrace();
			returnValue = false;
		}

		return returnValue;
	} // End Method

	/***************************************************************************
	 * This method takes a list of files and zips them up for Macs by using
	 * MediaBanks Childp process (yuck), saving the zip file where instructed.
	 * 
	 * @param files
	 *            array of File objects to be zipped up
	 * @param zipFileName
	 *            name of file to save zip file to
	 * @param childp
	 *            executable file to package files for Macs.
	 * @return boolean true if successful at zipping everything, false otherwise
	 **************************************************************************/
	public static boolean zipForMacWithPacker(File[] files, String zipFileName,
			String childp) {
		Properties sysProps = System.getProperties();
		StringBuffer instructions = new StringBuffer("999\tFLATTEN\r\n"
				+ zipFileName + "\r\nLIST\tFILES\r\n");
		int count = -1;
		while (++count < files.length)
			instructions.append(files[count].getPath() + "\r\n");
		instructions.append("ENDLIST\r\n");
		int rnd = (int) (Math.random() * 10000.0000);
		String inbox = sysProps.getProperty("user.dir")
				+ "\\in"
				+ rnd
				+ zipFileName.substring(zipFileName.lastIndexOf("\\") + 1,
						zipFileName.lastIndexOf(".")) + ".txt";
		String outbox = sysProps.getProperty("user.dir")
				+ "\\out"
				+ rnd
				+ zipFileName.substring(zipFileName.lastIndexOf("\\") + 1,
						zipFileName.lastIndexOf(".")) + ".txt";
		String command = childp + " " + outbox + " " + inbox;

		Filer.writeFile(outbox, instructions.toString());
		Runtime sending = Runtime.getRuntime();
		Process sendingProcess;
		int exit = -1;
		try {
			sendingProcess = sending.exec(command);
			exit = sendingProcess.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Filer.deleteFile(outbox);
		// Filer.deleteFile(inbox);
		if (exit == 0)
			return true;
		else
			return false;
	} // End Method

	/***************************************************************************
	 * This method takes a list of files and zips them up for Macs by using
	 * DropStuff, saving the zip file where instructed.
	 * 
	 * @param files
	 *            array of File objects to be zipped up
	 * @param zipFileName
	 *            name of file to save zip file to
	 * @param dropstuff
	 *            executable file to package files for Macs.
	 * @return boolean true if successful at zipping everything, false otherwise
	 **************************************************************************/
	public static boolean zipForMac(File[] files, String zipFileName,
			String dropstuff) {
		Properties sysProps = System.getProperties();
		String fileSeparator = sysProps.getProperty("file.separator");
		String temp = zipFileName.substring(zipFileName
				.lastIndexOf(fileSeparator) + 1);
		File file = new File(temp);
		file.delete();
		StringBuffer instructions = new StringBuffer("");
		for (int count = 0; count < files.length; instructions.append(" \""
				+ files[count++].getPath() + "\""))
			;
		String command = dropstuff + " " + temp + instructions;
		Functions.javaLog(command);
		Runtime sending = Runtime.getRuntime();
		Process sendingProcess;
		int exit = -1;
		try {
			Functions.javaLog("sending process");
			sendingProcess = sending.exec(command);
			exit = sendingProcess.waitFor();
			Functions.javaLog("exiting process");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (exit == 0) {
			Filer.copyFile(temp, zipFileName);
			file.delete();
			Functions.javaLog("dropstuff returned success");
			return true;
		} else
			Functions.javaLog("dropstuff returned failure");
		return false;
	} // End Method

	/***************************************************************************
	 * Returns the output of running "dir" on a windows NT machine.
	 * 
	 * @return the output of dir.
	 **************************************************************************/
	public static boolean dosDir(String path, String file) {
		StringBuffer output = new StringBuffer();
		String temp;
		int convert = -1;
		String command = "dir " + path + " > " + file;
		Runtime dir = Runtime.getRuntime();
		Process dirProcess;
		int exit = -1;
		try {
			dirProcess = dir.exec(command);
			exit = dirProcess.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (exit == 0)
			return true;
		else
			return false;
	} // End Method

	/***************************************************************************
	 * This method takes a list of files and zips them up, saving the zip file
	 * where instructed.
	 * 
	 * @param files
	 *            array of File objects to be zipped up
	 * @param zipFileName
	 *            name of file to save zip file to
	 * @return boolean true if successful at zipping everything, false otherwise
	 **************************************************************************/
	public static boolean zip(File[] files, String zipPath, String zipFileName) {
		byte[] buffer = new byte[100000];
		int numBytes;
		boolean returnValue = true;
		ZipOutputStream zipFile = null;
		InputStream getFile;
		OutputStream fileOut = null;
		try {
			File dir = new File(zipPath);
			dir = new File(dir, zipFileName);
			fileOut = new BufferedOutputStream(new FileOutputStream(dir));
			zipFile = new ZipOutputStream(fileOut);
			// zipFile.setLevel(0); // ts compression to nothing.
			for (int countFiles = 0; countFiles < files.length; countFiles++) {
				javaLog("Zipping file " + files[countFiles].getAbsolutePath());
				try {
					getFile = new BufferedInputStream(new FileInputStream(
							files[countFiles]));
					zipFile.putNextEntry(new ZipEntry(files[countFiles]
							.getName()));
					do {
						numBytes = getFile.read(buffer);
						if (numBytes > -1)
							zipFile.write(buffer, 0, numBytes);
					} while (numBytes > -1);
					getFile.close();
					zipFile.closeEntry();
				} catch (IOException exc) {
					log.error("Problem zipping "
							+ files[countFiles].getAbsolutePath(), exc);
				}
			}
		} catch (Exception e) {
			log.error("bad file parameter", e);
			returnValue = false;
		} finally {
			try {
				zipFile.close();
			} catch (Exception e) {
				try {
					fileOut.close();
				} catch (Exception err) {
					log.error("Problem closing zip file", err);
				}
			}
		}

		return returnValue;
	} // End Method

	/***************************************************************************
	 * Rounds a float to the given precision
	 * 
	 * @param f
	 *            float number to be rounded
	 * @param p
	 *            place to be rounded to (ie 2 means round to the second decimal
	 *            place)
	 * @return rounded float number
	 **************************************************************************/
	public static float round(float f, int p) {
		double mult = Math.pow((double) 10, (double) p);
		f *= mult;
		int result = Math.round(f);
		f = (float) (result / mult);
		return f;
	} // End Method

	/***************************************************************************
	 * Rounds a double to the given precision.
	 * 
	 * @param f
	 *            float number to be rounded
	 * @param p
	 *            place to be rounded to (ie 2 means round to the second decimal
	 *            place)
	 * @return rounded float number
	 **************************************************************************/
	public static float round(double f, int p) {
		float ret;
		double mult = Math.pow((double) 10, (double) p);
		f *= mult;
		long result = Math.round(f);
		ret = (float) (result / mult);
		return ret;
	} // End Method

	/***************************************************************************
	 * Takes a string of name value pair (separated by = signs) and creates a
	 * new array of name,value)
	 * 
	 * @param pair
	 *            String of name and value separted by an = sign.
	 * @return Array of name,value
	 **************************************************************************/
	public static String[] nameValues(String pair) {
		int count = 0, eq;
		String[] nameValues = new String[2];
		String temp;
		eq = pair.indexOf("=");
		if (eq == -1)
			eq = pair.length();
		temp = pair.substring(0, eq);
		if (temp.length() >= pair.length() - 1) {
			nameValues = new String[1];
			nameValues[0] = temp;
		} else {
			nameValues[0] = temp;
			nameValues[1] = pair.substring(eq + 1, pair.length());
		}
		return nameValues;
	}

	/***************************************************************************
	 * Takes an array of name value pairs (separated by = signs) and creates a
	 * new array of name,value,name,value,name,value....)
	 * 
	 * @param pairs
	 *            array of pairs, with equals
	 * @return Array of name,value,name,value pairs
	 **************************************************************************/
	public static String[] nameValues(String[] pairs) {
		int count = 0, eq;
		Vector nameValues = new Vector();
		String temp;
		while (count < pairs.length) {
			eq = pairs[count].indexOf("=");
			if (eq == -1)
				eq = pairs[count].length();
			temp = pairs[count].substring(0, eq);
			if (temp.length() >= pairs[count].length() - 1) {
				nameValues.addElement(temp);
				nameValues.addElement("");
			} else {
				nameValues.addElement(temp);
				nameValues.addElement(pairs[count].substring(eq + 1,
						pairs[count].length()));
			}
			count++;
		}
		String[] ret = new String[nameValues.size()];
		nameValues.copyInto(ret);
		return ret;
	}

	/***************************************************************************
	 * Takes an array of strings and a tokenizer character, and returns a new
	 * array of strings of all the strings split by the tokenizer character.
	 * 
	 * @param splittee
	 *            Array of strings, each of which is to be split
	 * @param splitChar
	 *            Character to split the strings on
	 * @return Array of all the tokens.
	 **************************************************************************/
	public static String[] split(String[] splittee, String splitChar) {
		if (splittee == null || splitChar == null)
			return new String[0];
		int count = 0;
		String temp;
		StringTokenizer tokens;
		Vector returns = new Vector();
		int spot;
		while (count < splittee.length) {
			while ((spot = splittee[count].indexOf(splitChar + splitChar)) != -1)
				splittee[count] = splittee[count].substring(0, spot
						+ splitChar.length())
						+ splittee[count].substring(spot + 2
								* splitChar.length(), splittee[count].length());

			tokens = new StringTokenizer(splittee[count], splitChar);
			while (tokens.hasMoreTokens()) {
				temp = (String) tokens.nextToken();
				returns.addElement(temp);
			}
			count++;
		}
		String[] values = new String[returns.size()];
		returns.copyInto(values);
		return values;
	} // End Method

	/***************************************************************************
	 * Takes an array of strings and a tokenizer character, and returns a new
	 * array of strings of all the strings split by the tokenizer character.
	 * 
	 * @param splittee
	 *            Array of strings, each of which is to be split
	 * @param splitChar
	 *            Character to split the strings on
	 * @param def
	 *            Default value to place between two split chars that have
	 *            nothing between them
	 * @return Array of all the tokens.
	 **************************************************************************/
	public static String[] split(String[] splittee, String splitChar, String def) {
		if (splittee == null || splitChar == null)
			return new String[0];
		int count = 0;
		String temp;
		StringTokenizer tokens;
		Vector returns = new Vector();
		int spot;
		while (count < splittee.length) {
			while ((spot = splittee[count].indexOf(splitChar + splitChar)) != -1)
				splittee[count] = splittee[count].substring(0, spot
						+ splitChar.length())
						+ def
						+ splittee[count].substring(spot + 1
								* splitChar.length(), splittee[count].length());
			tokens = new StringTokenizer(splittee[count], splitChar);
			while (tokens.hasMoreTokens()) {
				temp = (String) tokens.nextToken();
				returns.addElement(temp);
			}
			count++;
		}
		String[] values = new String[returns.size()];
		returns.copyInto(values);
		return values;
	} // End Method

	/***************************************************************************
	 * Takes an array of strings and a tokenizer character, and returns a string
	 * of all the strings concatenated with the tokenizer string in between each
	 * one.
	 * 
	 * @param splittee
	 *            Array of Objects to be concatenated.
	 * @param splitChar
	 *            Object to unsplit the strings with.
	 * @return Array of all the tokens.
	 **************************************************************************/
	public static String unsplit(Object[] splittee, Object splitChar) {
		StringBuffer retVal = new StringBuffer("");
		int count = -1;
		while (++count < splittee.length) {
			if (splittee[count] != null)
				retVal.append(splittee[count]);
			if (count + 1 < splittee.length && splittee[count + 1] != null)
				retVal.append(splitChar);
		}
		return retVal.toString();
	} // End Method

	/***************************************************************************
	 * Takes an array of strings and a tokenizer character, and returns a string
	 * of all the strings concatenated with the tokenizer string in between each
	 * one.
	 * 
	 * @param splittee
	 *            Array of Objects to be concatenated.
	 * @param splitChar
	 *            Object to unsplit the strings with.
	 * @param def
	 *            Default value to replace null values in array.
	 * @return Array of all the tokens.
	 **************************************************************************/
	public static String unsplit(Object[] splittee, Object splitChar, String def) {
		StringBuffer retVal = new StringBuffer("");
		int count = -1;
		while (++count < splittee.length) {
			if (splittee[count] != null)
				retVal.append(splittee[count]);
			else
				retVal.append(def);
			if (count + 1 < splittee.length)
				retVal.append(splitChar);
		}
		return retVal.toString();
	} // End Method

	/***************************************************************************
	 * Takes an array of strings and a tokenizer character, and returns a string
	 * of all the strings concatenated with the tokenizer string in between each
	 * one.
	 * 
	 * @param splittee
	 *            Array of int to be concatenated.
	 * @param splitChar
	 *            Object to unsplit the strings with.
	 * @return Array of all the tokens.
	 **************************************************************************/
	public static String unsplit(int[] splittee, Object splitChar) {
		StringBuffer retVal = new StringBuffer("");
		int count = -1;
		while (++count < splittee.length) {
			retVal.append(splittee[count]);
			if (count + 1 < splittee.length)
				retVal.append(splitChar);
		}
		return retVal.toString();
	} // End Method

	/***************************************************************************
	 * Takes a String and a tokenizer character, and returns a new array of
	 * strings of the string split by the tokenizer character.
	 * 
	 * @param splittee
	 *            String to be split
	 * @param splitChar
	 *            Character to split the string on
	 * @return Array of all the tokens.
	 **************************************************************************/
	public static String[] split(String splittee, String splitChar) {
		if (splittee == null || splitChar == null)
			return new String[0];
		StringTokenizer tokens;
		String temp;
		int spot;
		while ((spot = splittee.indexOf(splitChar + splitChar)) != -1)
			splittee = splittee.substring(0, spot + splitChar.length())
					+ splittee.substring(spot + 2 * splitChar.length(),
							splittee.length());
		Vector returns = new Vector();
		tokens = new StringTokenizer(splittee, splitChar);
		while (tokens.hasMoreTokens()) {
			temp = (String) tokens.nextToken();
			returns.addElement(temp);
		}
		String[] values = new String[returns.size()];
		returns.copyInto(values);
		return values;
	} // End Method

	/***************************************************************************
	 * Takes a String and a tokenizer character, and returns a new array of
	 * strings of the string split by the tokenizer character.
	 * 
	 * @param splittee
	 *            String to be split
	 * @param splitChar
	 *            Character to split the string on
	 * @param def
	 *            Default value to place between two split chars that have
	 *            nothing between them
	 * @return Array of all the tokens.
	 **************************************************************************/
	public static String[] split(String splittee, String splitChar, String def) {
		if (splittee == null || splitChar == null)
			return new String[0];
		StringTokenizer tokens;
		String temp;
		int spot;
		while ((spot = splittee.indexOf(splitChar + splitChar)) != -1)
			splittee = splittee.substring(0, spot + splitChar.length())
					+ def
					+ splittee.substring(spot + 1 * splitChar.length(),
							splittee.length());
		Vector returns = new Vector();
		tokens = new StringTokenizer(splittee, splitChar);
		while (tokens.hasMoreTokens()) {
			temp = (String) tokens.nextToken();
			returns.addElement(temp);
		}
		String[] values = new String[returns.size()];
		returns.copyInto(values);
		return values;
	} // End Method

	/***************************************************************************
	 * Given any object of type Object, universalToString will determine the
	 * objects runtime class type and will return it's true toString value. This
	 * is done for the core object types (those types most likely to be returned
	 * from a SQL query statement).
	 * 
	 * @param obj
	 *            any object.
	 * @return The toString method of the object is called, and the result
	 *         returned. Date: 5/29/98
	 **************************************************************************/
	public static String universalToString(Object obj) {
		String r; // String to be returned
		if (obj == null)
			r = "null";
		else {
			Class c = obj.getClass();
			String className = c.getName();

			if (className.equals("java.lang.String"))
				r = (String) obj;
			else if (className.equals("java.lang.Integer"))
				r = ((Integer) obj).toString();
			else if (className.equals("java.lang.StringBuffer"))
				r = ((StringBuffer) obj).toString();
			else if (className.equals("java.lang.Boolean"))
				r = ((Boolean) obj).toString();
			else if (className.equals("java.lang.Character"))
				r = ((Character) obj).toString();
			else if (className.equals("java.lang.Byte"))
				r = ((Byte) obj).toString();
			else if (className.equals("java.lang.Double"))
				r = ((Double) obj).toString();
			else if (className.equals("java.lang.Float"))
				r = ((Float) obj).toString();
			else if (className.equals("java.lang.Long"))
				r = ((Long) obj).toString();
			else if (className.equals("java.util.Date"))
				r = ((java.util.Date) obj).toString();
			else if (className.equals("java.lang.Short"))
				r = ((Short) obj).toString();
			else if (className.equals("java.lang.Boolean"))
				r = ((Boolean) obj).toString();
			else if (className.equals("java.util.Calendar"))
				r = ((java.util.Calendar) obj).toString();
			else if (className.equals("java.util.GregorianCalendar"))
				r = ((java.util.GregorianCalendar) obj).toString();
			else if (className.equals("java.sql.Date"))
				r = ((java.sql.Date) obj).toString();
			else
				r = obj.toString();

		}
		return r;
	}// end method

	/***************************************************************************
	 * Takes a string that represents a date, such as 6/14/97, or 970614, and
	 * converts it to a Y2000 compliant integer format of YYYYMMDD
	 **************************************************************************/
	public int extractDate(String dateString) {
		GregorianCalendar cal = new GregorianCalendar();
		int year = cal.get(Calendar.YEAR);
		int date, pos, pos2;
		year = (int) (year / 100);
		year *= 1000000;
		if ((pos = dateString.indexOf("/")) > -1) {
			date = year
					+ (Integer.parseInt(dateString.substring(dateString
							.length() - 2, dateString.length())) * 10000);
			date += (Integer.parseInt(dateString.substring(0, pos)) * 100);
			pos2 = pos;
			if ((pos = dateString.indexOf("/")) > -1)
				date += Integer.parseInt(dateString.substring(pos2 + 1, pos));
			else
				System.out.println("Bad Date");
		} else {
			date = year + (Integer.parseInt(dateString));
		}
		return date;
	}

	/***************************************************************************
	 * Sorts the first array based on the integer values of the second array.
	 * Ascending order. If the 5th element of the second array has the highest
	 * integer value, then the 5th element of the first array is moved to the
	 * first position. And so on. 2/23/98 5:03PM
	 **************************************************************************/
	public static void sortAsc(Object[] array, int[] value, int start, int end) {
		int x;
		int y;
		x = start;
		y = end - 1;
		if (x >= y)
			return;
		int middle = (int) ((x + y) / 2);

		int pivot = value[middle];
		int tempInt;
		Object tempObj;
		tempObj = array[middle];
		value[middle] = value[y];
		array[middle] = array[y];
		value[y] = pivot;
		array[y] = tempObj;
		y--;
		while (x < y) {
			while (x < end - 1 && value[x] < pivot)
				x++;
			while (y > start && value[y] > pivot)
				y--;
			if (x < y) {
				tempInt = value[x];
				tempObj = array[x];
				value[x] = value[y];
				array[x] = array[y];
				value[y] = tempInt;
				array[y] = tempObj;
				x++;
				y--;
			}
		}
		if (value[x] > value[end - 1]) {
			tempInt = value[x];
			tempObj = array[x];
			value[x] = value[end - 1];
			array[x] = array[end - 1];
			value[end - 1] = tempInt;
			array[end - 1] = tempObj;
		}
		if (x == y)
			x++;
		Functions.sortAsc(array, value, start, x);
		Functions.sortAsc(array, value, x, end);

	} // end of Method

	/***************************************************************************
	 * Sorts an array of strings. Ascending order.
	 * 
	 * @param array
	 *            Array of strings to be sorted.
	 * @param start
	 *            starting index value to be sorted from.
	 * @param ending
	 *            index value to be sorted to. 2/23/98 5:03PM
	 **************************************************************************/
	public static void sortAsc(String[] array, int start, int end) {
		int x;
		int y;
		x = start;
		y = end - 1;
		if (x >= y)
			return;
		int middle = (int) ((x + y) / 2);

		String pivot = array[middle];
		int tempInt;
		String tempObj;
		tempObj = array[middle];
		array[middle] = array[y];
		array[y] = tempObj;
		y--;
		while (x < y) {
			while (x < y && array[x].compareToIgnoreCase(pivot) < 0)
				x++;
			while (y > x && array[y].compareToIgnoreCase(pivot) > 0)
				y--;
			if (x < y) {
				tempObj = array[x];
				array[x] = array[y];
				array[y] = tempObj;
				x++;
				y--;
			}
		}
		if (array[x].compareToIgnoreCase(array[end - 1]) > 0) {
			tempObj = array[x];
			array[x] = array[end - 1];
			array[end - 1] = tempObj;
		}
		if (x == y)
			x++;
		Functions.sortAsc(array, start, x);
		Functions.sortAsc(array, x, end);

	} // end of Method

	/***************************************************************************
	 * Sorts an array of strings. Descending order.
	 * 
	 * @param array
	 *            Array of strings to be sorted.
	 * @param start
	 *            starting index value to be sorted from.
	 * @param ending
	 *            index value to be sorted to. 2/23/98 5:03PM
	 **************************************************************************/
	public static void sortDesc(String[] array, int start, int end) {
		int x;
		int y;
		x = start;
		y = end - 1;
		if (x >= y)
			return;
		int middle = (int) ((x + y) / 2);

		String pivot = array[middle];
		int tempInt;
		String tempObj;
		tempObj = array[middle];
		array[middle] = array[y];
		array[y] = tempObj;
		y--;
		while (x < y) {
			while (x < y && array[x].compareToIgnoreCase(pivot) > 0)
				x++;
			while (y > x && array[y].compareToIgnoreCase(pivot) < 0)
				y--;
			if (x < y) {
				tempObj = array[x];
				array[x] = array[y];
				array[y] = tempObj;
				x++;
				y--;
			}
		}
		if (array[x].compareToIgnoreCase(array[end - 1]) < 0) {
			tempObj = array[x];
			array[x] = array[end - 1];
			array[end - 1] = tempObj;
		}
		if (x == y)
			x++;
		Functions.sortDesc(array, start, x);
		Functions.sortDesc(array, x, end);

	} // end of Method

	/***************************************************************************
	 * Sorts the first array based on the integer values of the second array.
	 * Descending order. If the 5th element of the second array has the highest
	 * integer value, then the 5th element of the first array is moved to the
	 * first position. And so on. 2/23/98 5:03PM
	 **************************************************************************/
	public static void sortDesc(Object[] array, int[] value, int start, int end) {
		int x;
		int y;
		x = start;
		y = end - 1;
		if (x >= y)
			return;
		int middle = (int) ((x + y) / 2);

		int pivot = value[middle];
		int tempInt;
		Object tempObj;
		tempObj = array[middle];
		value[middle] = value[y];
		array[middle] = array[y];
		value[y] = pivot;
		array[y] = tempObj;
		y--;
		while (x < y) {
			while (x < end - 1 && value[x] > pivot)
				x++;
			while (y > start && value[y] < pivot)
				y--;
			if (x < y) {
				tempInt = value[x];
				tempObj = array[x];
				value[x] = value[y];
				array[x] = array[y];
				value[y] = tempInt;
				array[y] = tempObj;
				x++;
				y--;
			}
		}
		if (value[x] < value[end - 1]) {
			tempInt = value[x];
			tempObj = array[x];
			value[x] = value[end - 1];
			array[x] = array[end - 1];
			value[end - 1] = tempInt;
			array[end - 1] = tempObj;
		}
		if (x == y)
			x++;
		Functions.sortDesc(array, value, start, x);
		Functions.sortDesc(array, value, x, end);

	} // end of Method

	/***************************************************************************
	 * Sorts the first array based on the integer values of the second array.
	 * Descending order. If the 5th element of the second array has the highest
	 * integer value, then the 5th element of the first array is moved to the
	 * first position. And so on. 2/23/98 5:03PM
	 **************************************************************************/
	public static void sortAsc(int[] array, int[] value, int start, int end) {
		int x;
		int y;
		x = start;
		y = end - 1;
		if (x >= y)
			return;
		int middle = (int) ((x + y) / 2);

		int pivot = value[middle];
		int tempInt;
		int tempObj;
		tempObj = array[middle];
		value[middle] = value[y];
		array[middle] = array[y];
		value[y] = pivot;
		array[y] = tempObj;
		y--;
		while (x < y) {
			while (x < end - 1 && value[x] < pivot)
				x++;
			while (y > start && value[y] > pivot)
				y--;
			if (x < y) {
				tempInt = value[x];
				tempObj = array[x];
				value[x] = value[y];
				array[x] = array[y];
				value[y] = tempInt;
				array[y] = tempObj;
				x++;
				y--;
			}
		}
		if (value[x] > value[end - 1]) {
			tempInt = value[x];
			tempObj = array[x];
			value[x] = value[end - 1];
			array[x] = array[end - 1];
			value[end - 1] = tempInt;
			array[end - 1] = tempObj;
		}
		if (x == y)
			x++;
		Functions.sortAsc(array, value, start, x);
		Functions.sortAsc(array, value, x, end);

	} // end of Method

	/***************************************************************************
	 * Sorts the first array based on the integer values of the second array.
	 * Ascending order. If the 5th element of the second array has the highest
	 * integer value, then the 5th element of the first array is moved to the
	 * first position. And so on. 2/23/98 5:03PM
	 **************************************************************************/
	public static void sortDesc(int[] array, int[] value, int start, int end) {
		int x;
		int y;
		x = start;
		y = end - 1;
		if (x >= y)
			return;
		int middle = (int) ((x + y) / 2);

		int pivot = value[middle];
		int tempInt;
		int tempObj;
		tempObj = array[middle];
		value[middle] = value[y];
		array[middle] = array[y];
		value[y] = pivot;
		array[y] = tempObj;
		y--;
		while (x < y) {
			while (x < end - 1 && value[x] > pivot)
				x++;
			while (y > start && value[y] < pivot)
				y--;
			if (x < y) {
				tempInt = value[x];
				tempObj = array[x];
				value[x] = value[y];
				array[x] = array[y];
				value[y] = tempInt;
				array[y] = tempObj;
				x++;
				y--;
			}
		}
		if (value[x] < value[end - 1]) {
			tempInt = value[x];
			tempObj = array[x];
			value[x] = value[end - 1];
			array[x] = array[end - 1];
			value[end - 1] = tempInt;
			array[end - 1] = tempObj;
		}
		if (x == y)
			x++;
		Functions.sortDesc(array, value, start, x);
		Functions.sortDesc(array, value, x, end);

	} // end of Method

	/***************************************************************************
	 * Sorts the first array based on the integer values of the second array.
	 * Descending order. If the 5th element of the second array has the highest
	 * integer value, then the 5th element of the first array is moved to the
	 * first position. And so on. 2/23/98 5:03PM
	 **************************************************************************/
	public static void sortAsc(float[] array, int[] value, int start, int end) {
		int x;
		int y;
		x = start;
		y = end - 1;
		if (x >= y)
			return;
		int middle = (int) ((x + y) / 2);

		int pivot = value[middle];
		int tempInt;
		float tempObj;
		tempObj = array[middle];
		value[middle] = value[y];
		array[middle] = array[y];
		value[y] = pivot;
		array[y] = tempObj;
		y--;
		while (x < y) {
			while (x < end - 1 && value[x] < pivot)
				x++;
			while (y > start && value[y] > pivot)
				y--;
			if (x < y) {
				tempInt = value[x];
				tempObj = array[x];
				value[x] = value[y];
				array[x] = array[y];
				value[y] = tempInt;
				array[y] = tempObj;
				x++;
				y--;
			}
		}
		if (value[x] > value[end - 1]) {
			tempInt = value[x];
			tempObj = array[x];
			value[x] = value[end - 1];
			array[x] = array[end - 1];
			value[end - 1] = tempInt;
			array[end - 1] = tempObj;
		}
		if (x == y)
			x++;
		Functions.sortAsc(array, value, start, x);
		Functions.sortAsc(array, value, x, end);

	} // end of Method

	/***************************************************************************
	 * Sorts the first array based on the integer values of the second array.
	 * Ascending order. If the 5th element of the second array has the highest
	 * integer value, then the 5th element of the first array is moved to the
	 * first position. And so on. 2/23/98 5:03PM
	 **************************************************************************/
	public static void sortDesc(float[] array, int[] value, int start, int end) {
		int x;
		int y;
		x = start;
		y = end - 1;
		if (x >= y)
			return;
		int middle = (int) ((x + y) / 2);

		int pivot = value[middle];
		int tempInt;
		float tempObj;
		tempObj = array[middle];
		value[middle] = value[y];
		array[middle] = array[y];
		value[y] = pivot;
		array[y] = tempObj;
		y--;
		while (x < y) {
			while (x < end - 1 && value[x] > pivot)
				x++;
			while (y > start && value[y] < pivot)
				y--;
			if (x < y) {
				tempInt = value[x];
				tempObj = array[x];
				value[x] = value[y];
				array[x] = array[y];
				value[y] = tempInt;
				array[y] = tempObj;
				x++;
				y--;
			}
		}
		if (value[x] < value[end - 1]) {
			tempInt = value[x];
			tempObj = array[x];
			value[x] = value[end - 1];
			array[x] = array[end - 1];
			value[end - 1] = tempInt;
			array[end - 1] = tempObj;
		}
		if (x == y)
			x++;
		Functions.sortDesc(array, value, start, x);
		Functions.sortDesc(array, value, x, end);

	} // end of Method

	/***************************************************************************
	 * Pulls out one item from an array, where count is the index of the item to
	 * be pulled and rearranges the rest. Assumes the array is of type String
	 **************************************************************************/
	public static String[] pullOutOfArray(String[] array, int count) {
		int length = array.length;
		String[] new_array = new String[length - 1];
		int counter = 0;
		while (counter < count)
			new_array[counter] = array[counter++];

		while (count < (length - 1)) {
			new_array[count] = array[count + 1];
			// System.out.println(count);
			count++;
		}

		return new_array;
	}

	/***************************************************************************
	 * chops off the ending '\n' off the array of characters
	 **************************************************************************/
	public static void chop(char[] choppy) {
		int count = 0;
		while (choppy[count++] != '\n')
			;
		choppy[--count] = 0;
	}

	/***************************************************************************
	 * Takes a string and strips starting and ending spaces. Returns resulting
	 * String. 2/23/98 8:27AM
	 **************************************************************************/
	public static String stripSpace(String input) {
		while (input.startsWith(" ") || input.startsWith("\n")
				|| input.startsWith("\r") || input.startsWith("\t"))
			input = new String(input.substring(1, input.length()));
		while (input.endsWith(" ") || input.endsWith("\n")
				|| input.endsWith("\r") || input.endsWith("\t"))
			input = new String(input.substring(0, input.length() - 1));

		return input;
	} // end of Method

	/***************************************************************************
	 * Takes a string and strips starting and ending String, depending on what's
	 * given. Returns resulting String. 2/23/98 8:27AM
	 **************************************************************************/
	public static String stripString(String input, String strip) {
		while (input.startsWith(strip))
			input = new String(input.substring(strip.length(), input.length()));
		while (input.endsWith(strip))
			input = new String(input.substring(0, input.length()
					- strip.length()));

		return input;
	} // end of Method

	/***************************************************************************
	 * Strips the spaces from the end of a char array
	 **************************************************************************/
	public static String stripString(char[] choice) {
		int count = 0;
		while (choice[count++] != 0)
			;
		count--;
		String string = new String(choice, 0, count);
		return string;
	}

	/***************************************************************************
	 * This function takes an array of Strings and sorts them according to
	 * length, descending order.
	 **************************************************************************/
	public static String[] sort(String[] array, int start, int end) {
		int x = start, y = end - 1;
		int basis = array[x].length();
		String temp;
		if (x == y)
			return array;
		while (x <= y) {
			while (x < end && array[x].length() > basis)
				x++;
			while (y >= (start - 1) && array[y].length() < basis)
				y--;
			if (x <= y) {
				temp = array[y];
				array[y] = array[x];
				array[x] = temp;
				x++;
				y--;
			}
		}
		if (x == y)
			x++;
		y = end - x;
		if (x > 0)
			array = Functions.sort(array, start, x);
		if (y > 0)
			array = Functions.sort(array, x, end);
		return array;
	}

	/***************************************************************************
	 * Method to send an email message using the blat utility. For this to work,
	 * blat needs to be in the system path.
	 * 
	 * @param recipient
	 *            email address of recipient
	 * @param sender
	 *            email address of sender
	 * @param message
	 *            text message to be sent
	 * @param subject
	 *            subject of email message
	 * @return true if successful, false otherwise
	 **************************************************************************/
	public static boolean sendToBlat(String recipient, String sender,
			String message, String subject) {
		Runtime sending = Runtime.getRuntime();
		Properties sysProps = System.getProperties();
		int x = (int) (Math.random() * 1000);
		String messageFile = new String(sysProps.getProperty("user.dir")
				+ "\\order" + x + "txt");
		Filer.writeFile(messageFile, message);
		String command = new String("blat " + messageFile + " -t " + recipient
				+ " -f " + sender + " -s " + subject);
		Process sendingProcess;
		int exit = -1;
		try {
			sendingProcess = sending.exec(command);
			exit = sendingProcess.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Filer.deleteFile(messageFile);
		if (exit == 0)
			return true;
		else
			return false;
	} // End Method

	/***************************************************************************
	 * Method runs any command in an external process, and returns the string
	 * result Returns "Could not run process", if an error occurs.
	 * 
	 * @param command
	 *            Command to be run.
	 * @return String of the results of the command.
	 **************************************************************************/
	public static String runCommand(String command) {
		String result = "", temp = "";
		Runtime thisRun = Runtime.getRuntime();
		Properties sysProps = System.getProperties();
		Process newProcess;
		int exit = -1;
		try {
			newProcess = thisRun.exec(command);
			BufferedReader data = new BufferedReader(new InputStreamReader(
					newProcess.getInputStream()));
			exit = newProcess.waitFor();
			while ((temp = data.readLine()) != null)
				result.concat(temp);
		} catch (Exception e) {
			result = "Could not run process";
			e.printStackTrace();
		}
		return result;
	}

	/***************************************************************************
	 * @Converts a regular date string to java.sql.Date format
	 * @param d
	 *            a date string
	 * @return date string in java.sql.Date format
	 **************************************************************************/

	public static String properDateFormat(String d) {
		int currentYear = new GregorianCalendar().get(Calendar.YEAR);
		int cent = currentYear / 100;
		currentYear %= 100;
		int factor;
		if (currentYear >= 50)
			factor = 1;
		else
			factor = -1;
		String[] dt = Functions.split(d, "/");
		int count = 0;
		while (count < dt.length)
			dt[count] = Functions.stripSpace(dt[count++]);
		String date;
		if (dt.length == 3) {
			if (dt[2].length() == 2) {
				if (Math.abs(Integer.parseInt(dt[2]) % 100 - currentYear) < 50)
					dt[2] = "" + cent + dt[2];
				else
					dt[2] = "" + (cent + factor) + dt[2];
			}
			date = dt[2] + "-" + dt[0] + "-" + dt[1];
		} else
			date = d;
		return date;
	}

	/***************************************************************************
	 * Strips any quotes, single or double from the input string and returns the
	 * result.
	 * 
	 * @param s
	 *            Input string.
	 * @return Resulting string after all quotes have been stripped out.
	 **************************************************************************/
	public static String stripQuotes(String s) {
		int x;
		while ((x = s.indexOf("'")) > -1) {
			if (s.length() > 1) {
				if (x == 0)
					s = s.substring(x + 1, s.length());
				else if (x < s.length() - 1)
					s = s.substring(0, x) + s.substring(x + 1, s.length());
				else
					s = s.substring(0, x);
			} else
				s = "";
		}
		while ((x = s.indexOf("\"")) > -1) {
			if (s.length() > 1) {
				if (x == 0)
					s = s.substring(x + 1, s.length());
				else if (x < s.length() - 1)
					s = s.substring(0, x) + s.substring(x + 1, s.length());
				else
					s = s.substring(0, x);
			} else
				s = "";
		}
		return s;

	} // End Method

	/***************************************************************************
	 * Takes the size of an image and adjusts it if necessary to fit on the web
	 * page.
	 * 
	 * @param sizes
	 *            array holding the height and width of an image.
	 * @param width
	 *            int holding maximum width for this image
	 * @param height
	 *            int holding maximum height for this image
	 * @return array holding the new, adjusted height and width of image.
	 **************************************************************************/
	public static com.allarphoto.beans.Dimension resizeDimension(
			com.allarphoto.beans.Dimension current,
			com.allarphoto.beans.Dimension max) {
		com.allarphoto.beans.Dimension retVal = new com.allarphoto.beans.Dimension();
		float widthRatio = current.getWidth() / max.getWidth();
		float heightRatio = current.getHeight() / max.getHeight();
		if (heightRatio > 1 || widthRatio > 1) {
			if (heightRatio > widthRatio) {
				retVal.setHeight(current.getHeight() / heightRatio);
				retVal.setWidth(current.getWidth() / heightRatio);
			} else {
				retVal.setHeight(current.getHeight() / widthRatio);
				retVal.setWidth(current.getWidth() / widthRatio);
			}
		} else
			retVal = current;
		return retVal;
	}

	/***************************************************************************
	 * Method to get an image from a file
	 * 
	 * @param filename
	 *            Name of file that holds binary image data
	 * @return Image object
	 **************************************************************************/
	public static com.allarphoto.beans.Dimension getImageDimensions(
			String filename, String defaultImage) {
		com.allarphoto.beans.Dimension dim = new com.allarphoto.beans.Dimension();
		javax.media.jai.RenderedOp destImage;
		try {
			destImage = javax.media.jai.JAI.create("fileload", filename);
			dim.setHeight((float) destImage.getHeight());
			dim.setWidth((float) destImage.getWidth());
		} catch (Exception e) {
			log.warn("Couldn't get image dimensions", e);
			destImage = javax.media.jai.JAI.create("fileload", defaultImage);
			dim.setHeight((float) destImage.getHeight());
			dim.setWidth((float) destImage.getWidth());
		}
		return dim;
	} // End Method

	/***************************************************************************
	 * This method emails a message using a socket connection to an email
	 * server.
	 * 
	 * @param mailHost
	 *            Name of email server to use.
	 * @param from
	 *            From email address.
	 * @param to
	 *            List of recipients, separated by commas.
	 * @param subject
	 *            Subject of message.
	 * @param msg
	 *            Body of message.
	 * @return True if message successfully sent, false otherwise.
	 *         ******************************************************************************************************************
	 *         public static boolean email(String mailHost, String from, String
	 *         to, String subject, String msg) { String incoming; String
	 *         statusCode; String HELO = "HELO"; String DATA = "DATA "; String[]
	 *         recipients = split(to, ","); boolean retValue = true;
	 * 
	 * try { HELO += " " + InetAddress.getLocalHost().getHostAddress(); } catch
	 * (UnknownHostException e) { javaLog("Functions(email): Unknown localhost " +
	 * e.toString()); retValue = false; } msg = "Subject:" + subject +
	 * "\r\n\r\n" + parseSingleDot(msg); from = "MAIL FROM:<" + from + ">";
	 * SimpleSocket socket = null; try { socket = new SimpleSocket(mailHost,
	 * 25); } catch (IOException e) { javaLog("Functions(email): Error while
	 * making connections "); retValue = false; } catch (NullPointerException e) {
	 * javaLog("Functions(email): Error while making connections "); retValue =
	 * false; } // Check for hand-shake if (socket.getQuickResponse() == false) {
	 * javaLog("Functions(email): No initial hand-shake " +
	 * socket.getIncoming()); retValue = false; } if
	 * (!socket.getStatusCode().equals("2")) { javaLog("Functions(email): Bad
	 * connection " + socket.getStatusCode()); retValue = false; } // Build the
	 * command list String[][] commands = { // command // expected return value //
	 * ========== SMTP ========== { HELO, "2" }, { from, "2" }, { "", "2" }, //
	 * TO is not known until RCPTO is tokenized { DATA, "3" }, { msg, "2" } };
	 * boolean saidHello = false; // Send each person in toList the message.
	 * Multiple "RCPT TO" commands can be sent // before sending the data
	 * command. The following loop avoids doing this so // recipients will not
	 * get a long "Recipients" header. int count = -1; while (++count <
	 * recipients.length) { commands[2][0] = "RCPT TO:<" + recipients[count] +
	 * ">"; for (int i = 0; i < commands.length; i++) { if (i > 0 || !saidHello) {
	 * socket.sendCommand(commands[i][0]); if (socket.getQuickResponse() ==
	 * false) { javaLog("Functions(email): Bad command " + commands[i][0] +
	 * "\r\n\t" + socket.getIncoming()); retValue = false; } if
	 * (socket.getStatusCode().equals(commands[i][1]) == false) {
	 * javaLog("Functions(email): Bad response " + commands[i][0] + "\r\n\t" +
	 * socket.getStatusCode() + " " + socket.getIncoming()); retValue = false; }
	 * saidHello = true; } } }// end while try { socket.closeSocket(); } catch
	 * (IOException e) { javaLog("Functions(email): Couldn't close socket " +
	 * e.toString()); } return retValue; }
	 */

	/***************************************************************************
	 * This method removes lines from a string that start with a '.' and
	 * replaces the entire line with "..". This is used for email messages.
	 * 
	 * @param mesgBody
	 *            Email message body.
	 * @return The reworked message.
	 **************************************************************************/
	public static String parseSingleDot(String MesgBody) {

		StringBuffer sb = new StringBuffer();
		String temp = null;

		StringTokenizer st = new StringTokenizer(MesgBody, "\n", false);

		while (st.hasMoreTokens()) {
			temp = st.nextToken();
			temp.trim();

			if (temp != null && temp.length() > 0 && temp.charAt(0) == '.')
				sb.append(".." + "\r\n");
			else
				sb.append(temp + "\r\n");
		}

		return sb.toString() + "\r\n.";
	}

	/***************************************************************************
	 * Converts a given number from a given base to a different given base.
	 * 
	 * @param digits
	 *            Number to be converted.
	 * @param fromBase
	 *            Base to convert from.
	 * @param toBase
	 *            Base to convert to.
	 * @return Resulting int.
	 **************************************************************************/
	public static String baseConvert(String digits, int fromBase, int toBase) {
		int temp, num;
		StringBuffer retVal = new StringBuffer("");
		int place = 0;
		int count = digits.length();
		num = 0;
		if (fromBase != 10) {
			while (--count > -1) {
				temp = Integer.parseInt(digits.substring(count, count + 1));
				num += temp * Math.pow(fromBase, place);
				place++;
			}
		} else
			num = Integer.parseInt(digits);
		if (toBase != 10) {
			place = 1;
			while (num > 0) {
				temp = toBase;
				retVal.append("" + num % temp);
				num -= num % temp;
				num /= temp;
			}
			retVal.reverse();
		} else
			retVal = new StringBuffer("" + num);
		return retVal.toString();
	}

}
