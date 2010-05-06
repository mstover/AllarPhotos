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

package com.lazerinc.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import com.lazerinc.application.Controller;

/*******************************************************************************
 * Class for simplified File and keyboard input/output.
 * 
 * @author: Michael Stover
 ******************************************************************************/
public class Filer {

	boolean finish = false;

	private RandomAccessFile current_file;

	/***************************************************************************
	 * Writes a String to the current file as a stream of bytes, overwriting the
	 * file contents.
	 * 
	 * @param filename
	 *            Full path of file to be written to
	 * @param filestring
	 *            data to be written to file
	 * @return true if successful, false if not
	 **************************************************************************/
	public static boolean writeFile(String filename, byte[] contents) {
		boolean returnValue = true;
		File f = new File(filename);
		Filer.deleteFileContent(f);
		try {
			BufferedOutputStream pw = new BufferedOutputStream(
					new FileOutputStream(filename));
			pw.write(contents);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
			returnValue = false;
		}

		return returnValue;
	}

	public static String convertFileToUnix(String filename,
			Controller controller) {

		filename = filename.replaceAll("\\\\", "/");
		if (controller.getConfigValue("pathPattern").length() > 0) {
			filename = filename.replaceFirst(controller
					.getConfigValue("pathPattern"), controller
					.getConfigValue("pathSubstitute"));
		}
		return filename;
	}

	/***************************************************************************
	 * Creates a directory.
	 * 
	 * @param filename
	 *            Full pathname to the directory to be created.
	 * @return boolean indicating whether it was successful
	 **************************************************************************/
	public static boolean mkdir(String filename) {
		File new_dir = new File(filename);
		return new_dir.mkdir();
	}

	/***************************************************************************
	 * Creates a directory.
	 * 
	 * @param pathname
	 *            Path information to the directory where you wish to create the
	 *            new directory.
	 * @param filename
	 *            Name of directory to be created.
	 * @return boolean indicating whether it was successful
	 **************************************************************************/
	public static boolean mkdir(String pathname, String filename) {
		File new_dir = new File(pathname, filename);
		return new_dir.mkdir();
	}

	/***************************************************************************
	 * Checks if a file is a directory.
	 * 
	 * @param filename
	 *            full pathname of file or directory to be checked.
	 * @return true if it is a directory, false if not.
	 **************************************************************************/
	public static boolean isDir(String filename) {
		File check_file = new File(filename);
		return check_file.isDirectory();
	}

	/***************************************************************************
	 * Checks if a file is a directory.
	 * 
	 * @param pathname
	 *            path to file.
	 * @param filename
	 *            name of file to be checked.
	 * @return true if it is a directory, false if not.
	 **************************************************************************/
	public static boolean isDir(String pathname, String filename) {
		File check_file = new File(pathname, filename);
		return check_file.isDirectory();
	}

	/***************************************************************************
	 * checks if file exists.
	 * 
	 * @param pathname
	 *            full pathname of file or directory to check
	 * @return true if it exists, false if not.
	 **************************************************************************/
	public static boolean exist(String pathname) {
		File check_file = new File(pathname);
		return check_file.exists();
	}

	/***************************************************************************
	 * checks if file exists.
	 * 
	 * @param pathname
	 *            path to file.
	 * @param filename
	 *            name of file to check.
	 * @return true if it exists, false if not.
	 **************************************************************************/
	public static boolean exist(String pathname, String filename) {
		File check_file = new File(pathname, filename);
		return check_file.exists();
	}

	/***************************************************************************
	 * Returns the length of the file.
	 * 
	 * @param filename
	 *            full pathname of the file to check
	 * @return Length of the file in bytes.
	 **************************************************************************/
	public static long length(String filename) {
		File f = new File(filename);
		return f.length();
	}

	/***************************************************************************
	 * Returns the length of the file.
	 * 
	 * @param pathname
	 *            path to file.
	 * @param filename
	 *            Name of the file to check.
	 * @return Length of the file in bytes.
	 **************************************************************************/
	public static long length(String pathname, String filename) {
		File f = new File(pathname, filename);
		return f.length();
	}

	/***************************************************************************
	 * Returns the last modified date of file.
	 * 
	 * @param filename
	 *            Full pathname to the file to be checked.
	 * @return The last modified date of the file.
	 **************************************************************************/
	public static long filedate(String filename) {
		File it = new File(filename);
		return it.lastModified();
	}

	/***************************************************************************
	 * Returns the last modified date of file.
	 * 
	 * @param pathname
	 *            path to file.
	 * @param filename
	 *            name of file to check.
	 * @return The last modified date of the file.
	 **************************************************************************/
	public static long filedate(String pathname, String filename) {
		File it = new File(pathname, filename);
		return it.lastModified();
	}

	/***************************************************************************
	 * Renames file as lowercase.
	 * 
	 * @param pathname
	 *            path to file
	 * @param filename
	 *            File to be lowercased.
	 * @return nothing
	 **************************************************************************/
	public static void renametolower(String pathname, String filename) {
		String filenamelow = new String(filename.toLowerCase());
		File fromfile = new File(pathname, filename);
		File tofile = new File(pathname, filenamelow);
		fromfile.renameTo(tofile);
	}

	/***************************************************************************
	 * Renames file as uppercase.
	 * 
	 * @param pathname
	 *            path to file
	 * @param filename
	 *            file to be uppercased
	 * @return nothing
	 **************************************************************************/
	public static void renametoupper(String pathname, String filename) {
		String filenamelow = new String(filename.toUpperCase());
		File fromfile = new File(pathname, filename);
		File tofile = new File(pathname, filenamelow);
		fromfile.renameTo(tofile);
	}

	/***************************************************************************
	 * Returns a list of filenames in the given directory.
	 * 
	 * @param pathname
	 *            Full pathname to the directory
	 * @return Array of Strings listing contents of directory
	 **************************************************************************/
	public static String[] dir(String pathname) {
		String[] filenames;
		File file_object = new File(pathname);

		filenames = file_object.list();
		return filenames;
	}

	/***************************************************************************
	 * Strips out directories and file types not listed in the filemask String
	 * array.
	 * 
	 * @param filenames
	 *            array of strings giving filenames
	 * @param pathname
	 *            path to the directory containing these filenames
	 * @param filemask
	 *            array of filemasks, including the period.
	 * @return Array of strings after directories and files without the correct
	 *         extensions have been stripped out.
	 **************************************************************************/
	public static String[] stripDirectory(String[] filenames, String pathname,
			String[] filemask) {
		int count = -1;
		int maskcount = 0;
		String ext = new String();
		int length = filemask.length;
		boolean condition;
		Hashtable h = new Hashtable();
		maskcount = 0;
		while (maskcount < filemask.length) {
			h.put(filemask[maskcount], filemask[maskcount]);
			maskcount++;
		}

		try {
			while (filenames[++count] != null) {
				File file_object = new File(pathname, filenames[count]);
				condition = false;
				ext = filenames[count].substring(filenames[count]
						.lastIndexOf("."), filenames[count].length());
				condition = (condition || (!file_object.isFile()) || (!h
						.containsKey(ext)));
				if (condition) {
					String[] new_filenames;
					new_filenames = (String[]) Functions.pullOutOfArray(
							filenames, count);
					filenames = new_filenames;
					count--;
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
		} catch (NullPointerException e) {
		}
		return filenames;
	}

	/***************************************************************************
	 * Strips out directories from an array of filenames.
	 * 
	 * @param filenames
	 *            array of filenames
	 * @param pathname
	 *            path to the directory holding the filenames
	 * @return Array of files after directories have been stripped out.
	 **************************************************************************/
	public static String[] stripDirectory(String[] filenames, String pathname) {
		int count = -1;
		char wait;

		boolean condition;
		try {
			while (filenames[++count] != null) {
				File file_object = new File(pathname, filenames[count]);
				condition = false;
				condition = (!file_object.isFile());
				if (condition) {
					String[] new_filenames;
					new_filenames = (String[]) Functions.pullOutOfArray(
							filenames, count);
					filenames = new_filenames;
					count--;
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
		} catch (NullPointerException e) {
		}
		return filenames;
	}

	/***************************************************************************
	 * Gathers the directory names from a list of files.
	 * 
	 * @param filenames
	 *            list of filenames
	 * @param pathname
	 *            path to the directory holding the filenames
	 * @return Array holding list of directories
	 **************************************************************************/
	public static String[] getDir(String[] filenames, String pathname) {
		int count = 0;
		Vector directories = new Vector(filenames.length);
		while (count < filenames.length) {
			File file_object = new File(pathname, filenames[count]);
			if (file_object.isDirectory()) {
				directories.addElement(filenames[count]);
			}
			count++;
		}
		String[] r = new String[directories.size()];
		directories.copyInto(r);
		return r;
	}

	/***************************************************************************
	 * Returns true if the file (represented by the full pathname to the file)
	 * can be edited by this java process. False if the file is currently locked
	 * by another process.
	 * 
	 * @param fullPathName
	 *            full pathname to file to check
	 * @return true if this process can edit the file, false if not. 2/10/98
	 *         9:34AM
	 **************************************************************************/
	public static boolean canEdit(String fullPathName) {
		File f = new File(fullPathName);
		return f.canWrite();
	}

	/***************************************************************************
	 * Gets a String from the keyboard.
	 * 
	 * @return String of the keyboard input
	 * @throws java.io.IOException
	 **************************************************************************/
	public static String getInput() throws IOException {
		char a;
		StringBuffer rawInput = new StringBuffer();
		a = (char) System.in.read();
		while (a != '\n') {
			rawInput.append(a);
			a = (char) System.in.read();
		}
		return rawInput.toString();

	}

	/***************************************************************************
	 * Reads the contents of file into a String.
	 * 
	 * @param pathname
	 *            path to file
	 * @param filename
	 *            name of file to read in.
	 * @return String containing contents of the file.
	 * @throws java.io.IOException
	 **************************************************************************/
	public static String readFileToString(String pathname, String filename)
			throws java.io.IOException {
		StringBuffer filehold = new StringBuffer();
		String[] file = Filer.readFile(pathname, filename);
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		int count = 0;
		while (count < file.length)
			pw.println(file[count++]);
		pw.close();
		return sw.toString();
	}

	/***************************************************************************
	 * Reads the contents of file into a String.
	 * 
	 * @param filename
	 *            Full path to file to read in.
	 * @return String containing contents of the file.
	 * @throws java.io.IOException
	 **************************************************************************/
	public static String readFileToString(String filename)
			throws java.io.IOException {
		StringBuffer filehold = new StringBuffer();
		String[] file = Filer.readFile(filename);
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		int count = 0;
		while (count < file.length)
			pw.println(file[count++]);
		pw.close();
		return sw.toString();
	}

	/***************************************************************************
	 * Deletes the contents of a file
	 * 
	 * @param f
	 *            File object representing file to be deleted.
	 * @return true if successful, false if not Date: 6/2/98
	 **************************************************************************/
	public static boolean deleteFileContent(File f) {
		File temp = f;
		f.delete();
		f = new File(temp.getPath());
		return true;
	}// end method

	public static void deleteFile(String filename) {
		File temp = new File(filename);
		temp.delete();
	}

	/***************************************************************************
	 * Writes a String to the current file as a stream of bytes, overwriting the
	 * file contents.
	 * 
	 * @param pathname
	 *            path to file to be written to.
	 * @param filename
	 *            file to be written to
	 * @param filestring
	 *            data to be written to file
	 * @return true if successful, false if not
	 **************************************************************************/
	public static boolean writeFile(String pathname, String filename,
			String filestring) {
		boolean returnValue = true;
		File f = new File(pathname, filename);
		Filer.deleteFileContent(f);
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(f));
			pw.print(filestring);
			pw.close();
		} catch (IOException e) {
			returnValue = false;
		}

		return returnValue;
	}

	/***************************************************************************
	 * Writes a String to the current file as a stream of bytes, overwriting the
	 * file contents.
	 * 
	 * @param filename
	 *            Full path of file to be written to
	 * @param filestring
	 *            data to be written to file
	 * @return true if successful, false if not
	 **************************************************************************/
	public static boolean writeFile(String filename, String filestring) {
		boolean returnValue = true;
		File f = new File(filename);
		Filer.deleteFileContent(f);
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(f));
			pw.print(filestring);
			pw.close();
		} catch (IOException e) {
			returnValue = false;
		}

		return returnValue;
	}

	/***************************************************************************
	 * Writes a String to the current file as a stream of bytes, overwriting the
	 * file contents.
	 * 
	 * @param filename
	 *            Full path of file to be written to
	 * @param filestring
	 *            data to be written to file
	 * @return true if successful, false if not
	 **************************************************************************/
	public static boolean appendToFile(String filename, String filestring) {
		boolean returnValue = true;
		File f = new File(filename);
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(f.getPath(), true));
			pw.print(filestring);
			pw.close();
		} catch (IOException e) {
			returnValue = false;
		}

		return returnValue;
	}

	/***************************************************************************
	 * This class takes a filename for input and returns an array of Strings
	 * representing the lines read from that file.
	 * 
	 * @param filename
	 *            Full path to file to be read.
	 * @return Array of strings representing the lines of the file.
	 * @throws java.io.IOException
	 **************************************************************************/
	public static String[] readStream(InputStream stream)
			throws java.io.IOException {
		List lines = new LinkedList();
		BufferedReader in = new BufferedReader(new InputStreamReader(stream));
		String temp;
		while ((temp = in.readLine()) != null)
			lines.add(temp);
		in.close();
		return (String[]) lines.toArray(new String[lines.size()]);
	}

	/***************************************************************************
	 * This class takes a filename for input and returns an array of Strings
	 * representing the lines read from that file.
	 * 
	 * @param filename
	 *            Full path to file to be read.
	 * @return Array of strings representing the lines of the file.
	 * @throws java.io.IOException
	 **************************************************************************/
	public static String[] readFile(String filename) throws java.io.IOException {
		File file = new File(filename);
		List lines = new LinkedList();
		BufferedReader in = new BufferedReader(new FileReader(file));
		String temp;
		while ((temp = in.readLine()) != null)
			lines.add(temp);
		in.close();
		return (String[]) lines.toArray(new String[lines.size()]);
	}

	/***************************************************************************
	 * Use this class to read in an entire file as an array of bytes.
	 * 
	 * @param filename
	 *            Full pathname of file.
	 * @return Byte array holding data.
	 **************************************************************************/
	public static byte[] readFileData(String filename)
			throws java.io.IOException {
		File file = new File(filename);
		byte[] b = new byte[(int) file.length()];
		BufferedInputStream stream = new BufferedInputStream(
				new FileInputStream(filename));
		stream.read(b);
		return b;
	}

	/***************************************************************************
	 * This class takes a filename for input and returns an array of Strings
	 * representing the lines read from that file.
	 * 
	 * @param pathname
	 *            path to file.
	 * @param filename
	 *            name of file to be read.
	 * @return Array of strings representing the lines of the file.
	 * @throws java.io.IOException
	 **************************************************************************/
	public static String[] readFile(String pathname, String filename)
			throws java.io.IOException {
		File file = new File(pathname, filename);
		Vector lines = new Vector();
		BufferedReader in = new BufferedReader(new FileReader(file));
		String temp;
		while ((temp = in.readLine()) != null)
			lines.addElement(temp);
		in.close();
		String[] line = new String[lines.size()];
		lines.copyInto(line);
		return line;
	}

	/***************************************************************************
	 * This class takes a full pathname for output and writes the contents of
	 * the String array to the file.
	 * 
	 * @param filename
	 *            Full path to file
	 * @param contents
	 *            array of strings representing the lines to be written to file
	 * @return true if successful, false if not
	 **************************************************************************/

	public static boolean writeFile(String filename, String[] contents) {
		boolean returnValue = true;
		File file = new File(filename);
		file.delete();
		try {
			PrintWriter out = new PrintWriter(new FileWriter(file));

			int x = 0;
			while (x < contents.length) {
				out.println(contents[x++]);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			returnValue = false;
		}
		return returnValue;
	}

	/***************************************************************************
	 * Copy one file to another and touches the copied file.
	 * 
	 * @param filename_from
	 *            full pathname of from file
	 * @param filename_to
	 *            full pathname of to file
	 * @return true if successful, false if not
	 **************************************************************************/
	public static boolean copyFile(String filename_from, String filename_to) {
		boolean returnValue = true;
		int buffer_size;
		File to_file = new File(filename_to);
		File from_file = new File(filename_from);
		try {
			FileOutputStream out = new FileOutputStream(to_file);
			FileInputStream in = new FileInputStream(from_file);
			int x = 0;
			if (from_file.length() > 20000)
				buffer_size = 100000;
			else
				buffer_size = 10000;
			byte[] b = new byte[buffer_size];

			x = in.read(b);
			while (x == buffer_size) {
				out.write(b);
				x = in.read(b);
			}
			if (x > 0)
				out.write(b, 0, x);
			in.close();
			out.close();
			if (from_file.length() > 0) {
				Filer.touch(from_file);
			}
		} catch (IOException e) {
			returnValue = false;
		}
		return returnValue;

	}

	/***************************************************************************
	 * Updates the modified date of a file without changing anything.
	 * 
	 * @param filename
	 *            File object representing file
	 * @return nothing
	 * @throws java.io.IOException
	 **************************************************************************/
	public static void touch(File filename) throws java.io.IOException {
		RandomAccessFile file = new RandomAccessFile(filename, "rw");
		int b, c;
		file.seek(0);
		b = file.readInt();
		file.seek(0);
		if (b < 255)
			c = b + 1;
		else
			c = b - 1;
		file.writeInt(c);
		file.seek(0);
		file.writeInt(b);
		file.close();
	}
}
