/***********************************************************************************************************************
 * Copyright 1999 (c) Lazer Incorporated All code contained in this file is the property of Lazer Incorporated. All
 * rights reserved.
 **********************************************************************************************************************/

package com.allarphoto.ecommerce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.coinjema.context.CoinjemaContext;

import com.allarphoto.dbtools.FoxProDB;
import com.allarphoto.utils.Data;
import com.allarphoto.utils.Filer;
import com.allarphoto.utils.Functions;

public class MediaBankReader {

	private int foxProConnections = 1;

	private String voltodir_file;

	/***************************************************************************
	 * @param
	 * @return **************************************************** public
	 *         static void main(String[] args) { MediaBankImageImport f=new
	 *         MediaBankImageImport(); } // End Method
	 */

	/***************************************************************************
	 * @param
	 * @return
	 **************************************************************************/
	public MediaBankReader() {
		// database.close();
	} // End Method

	/***************************************************************************
	 * Reads the config file to get the list of MediaBank image tables, then
	 * reads each table and imports the relevant information to the Access
	 * database, creating new image tables as necessary.
	 * 
	 * @param source
	 *            Name of database source of mediabank images.
	 * @param table
	 *            Name of table/database name.
	 **************************************************************************/
	public List getImageData(String source, String table) {
		FoxProDB sourceBase = new FoxProDB(new CoinjemaContext("mediabank"));
		Data data;
		String filename, filetype, log_time, log_user, storedir, thumbdir, macC, macFiletype;
		Double resolution, height, width, filesize;
		java.sql.Date log_date;
		int logCount = 0;
		ArrayList lines = new ArrayList();
		String[] retVal;
		String[] filenames;
		String[] voltodir = new String[1];
		String[] columns = { MB_filename, MB_filetype, MB_resolution,
				MB_height, MB_width, MB_filesize, MB_logDate, MB_logTime,
				MB_folders, MB_storedir, MB_thumbdir, MB_macCreator,
				MB_macFiletype };
		int folder_id, mactype_id, macCreator_id, filetype_id;
		try {
			voltodir = Filer.readFile(voltodir_file);
		} catch (java.io.IOException e) {
			System.out.println("Invalid Config file");
			return null;
		}
		voltodir = Functions.split(voltodir, "|");
		Functions.javaLog("ImageDB: " + (new java.util.Date()).toString()
				+ ": " + table);
		String[] tables = { table };
		sourceBase.updateTable(table);
		data = sourceBase.select(tables, columns, null);

		// Closes database connection in case of SQL error
		if (null == data) {
			// sourceBase.close();
		} else {
			lines = new ArrayList(data.size());
			data.reset();
			while (data.next()) {
				Map temp = new HashMap();
				temp.put("filename",
						((String) data.getColumnValue(MB_filename)).trim());
				temp.put("filetype", data.getColumnValue(MB_filetype));
				try {
					temp.put("resolution", data.getColumnValue(MB_resolution));
				} catch (ClassCastException e) {
					temp.put("resolution", new Double(0));
				}
				try {
					temp.put("height", data.getColumnValue(MB_height));
				} catch (ClassCastException e) {
					temp.put("height", new Double(0));
				}
				try {
					temp.put("width", data.getColumnValue(MB_width));
				} catch (ClassCastException e) {
					temp.put("width", new Double(0));
				}
				try {
					temp.put("filesize", data.getColumnValue(MB_filesize));
				} catch (ClassCastException e) {
					temp.put("filesize", new Double(0));
				}
				temp.put("date", data.getColumnValue(MB_logDate));
				temp.put("time", data.getColumnValue(MB_logTime));
				temp.put("folders", data.getColumnValue(MB_folders));
				storedir = (String) data.getColumnValue(MB_storedir);
				thumbdir = (String) data.getColumnValue(MB_thumbdir);
				temp.put("mac creator", data.getColumnValue(MB_macCreator));
				temp.put("mac filetype", data.getColumnValue(MB_macFiletype));
				// Replace volume names with drive letters
				for (int x = 0; x < voltodir.length; x += 2) {
					// Get rid of quotes at beginning and end
					storedir = cleanVolume(storedir, x, voltodir);
					thumbdir = cleanVolume(thumbdir, x, voltodir);
				}
				temp.put("original directory", storedir);
				temp.put("thumbnail directory", thumbdir);
				lines.add(temp);
			}
		}
		return lines;
	} // End Method

	/***************************************************************************
	 * Method to replace volume names with drive letters. Because we are dealing
	 * with the database from MediaBank, all the image pathnames are given with
	 * Mac volume names which we need to convert to regular drive letters.
	 * 
	 * @param dir
	 *            Pathway to image as it comes from MediaBank.
	 * @param x
	 *            Index of voltodir array of the element to check against.
	 * @param voltodir
	 *            array of strings. They come in pairs - the first is the Mac
	 *            name that we are searching for in the dir argument. The second
	 *            is the replacement string that contains the drive letter.
	 * @return A string of the pathway after the volume name has been replaced
	 *         by the drive letter.
	 **************************************************************************/
	private String cleanVolume(String dir, int x, String[] voltodir) {
		// String i=PerformanceLog.start("MediaBankImageImport(cleanVolume)");
		while (dir.startsWith("\""))
			dir = dir.substring(1, dir.length());
		while (dir.endsWith(" ") || dir.endsWith("\""))
			dir = dir.substring(0, dir.length() - 1);
		if (dir.startsWith(voltodir[x])) {
			dir = voltodir[x + 1]
					+ dir.substring(voltodir[x].length(), dir.length());
		}
		// PerformanceLog.end("MediaBankImageImport(cleanVolume)",i);
		return dir;
	} // End Method

	public void setVoltodir_file(String newVoltodir_file) {
		voltodir_file = newVoltodir_file;
	}

	public String getVoltodir_file() {
		return voltodir_file;
	}

	public static final String MB_filename = "filename";

	public static final String MB_filetype = "filetype";

	public static final String MB_resolution = "resoln";

	public static final String MB_height = "height";

	public static final String MB_width = "width";

	public static final String MB_filesize = "filesize";

	public static final String MB_logDate = "log_date";

	public static final String MB_logTime = "log_time";

	public static final String MB_folders = "log_user";

	public static final String MB_storedir = "storedir";

	public static final String MB_thumbdir = "thumbdir";

	public static final String MB_macCreator = "fcreator";

	public static final String MB_macFiletype = "falttype";

	public static final String MB_commentColumn = "Comment";

	public static final String MB_category1 = "class1";

	public static final String MB_category2 = "class2";

	public static final String MB_category3 = "class3";

	public static final String MB_category4 = "class4";

	public static final String MB_category5 = "class5";

	public static final String MB_keyword1 = "key1";

	public static final String MB_keyword2 = "key2";

	public static final String MB_keyword3 = "key3";

	public static final String MB_keyword4 = "key4";

	public static final String MB_keyword5 = "key5";

	public static final String MB_ufld1 = "ufld1";

	public static final String MB_ufld2 = "ufld2";

	public static final String MB_ufld3 = "ufld3";

	public static final String MB_ufld4 = "ufld4";

	public static final String MB_ufld5 = "ufld5";

	public static final String MB_ufld6 = "ufld6";

	public static final String MB_ufld7 = "ufld7";

	public static final String MB_ufld8 = "ufld8";

	public static final String MB_ufld9 = "ufld9";

	public static final String MB_ufld10 = "ufld10";

	public static final String MB_ufld11 = "ufld11";

	public static final String MB_ufld12 = "ufld12";

	public static final String MB_ufld13 = "ufld13";

	public static final String MB_ufld14 = "ufld14";

	public static final String MB_ufld15 = "ufld15";

	public static final String MB_ufld16 = "ufld16";

	public static final String MB_ufld17 = "ufld17";

	public static final String MB_ufld18 = "ufld18";

	public static final String MB_ufld19 = "ufld19";

	public static final String MB_ufld20 = "ufld20";

}
