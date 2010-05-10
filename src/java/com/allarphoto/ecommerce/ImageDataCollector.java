// Title: Commerce
// Version:
// Copyright: Copyright (c) 1999
// Author: Michael Stover
// Company: Lazer inc.
// Description: Your description

package com.allarphoto.ecommerce;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

import com.allarphoto.application.Controller;
import com.allarphoto.dbtools.DBConnect;
import com.allarphoto.utils.Data;
import com.allarphoto.utils.Functions;

@CoinjemaObject
public class ImageDataCollector {

	Controller controller;

	DBConnect database;

	DatabaseUtilities dbUtil;

	Set needsDeleting;

	static final String PRICE = "PRICE";

	static final String CATEGORY = "CATEGORY";

	static final String PRIMARY = "PRIMARY";

	static final String DESCRIPTION = "DESCRIPTION";

	static final String DESCRIPTIVE_NAME = "DESCRIPTIVE_NAME";

	static final String PHYSICAL = "PHYSICAL";

	static final String ELECTRONIC = "ELECTRONIC";

	static final String TABLE_NAME = "TABLE_NAME";

	static final String PRIMARY_LABEL = "PRIMARY_LABEL";

	static final String SEARCH_ORDER = "SEARCH ORDER";

	static final String DISPLAY_ORDER = "DISPLAY ORDER";

	static final String INVENTORY = "INVENTORY";

	static final String PRICE_BREAK = "PRICE_BREAK";

	static final String ORDER_MODEL = "ORDER_MODEL";

	static final String PRODUCT_TYPE = "PRODUCT_TYPE";

	static final String NUMERICAL = "NUMERICAL";

	public ImageDataCollector() {
	}

	/***************************************************************************
	 * Method to set the application controller for the data-aware component.
	 * The application controller holds the handle for the DBConnect object this
	 * component needs to connect to.
	 * 
	 * @param c
	 *            DatabaseApplicationController object.
	 **************************************************************************/
	@CoinjemaDependency(type = "appController", method = "appController")
	public void setController(Controller c) {
		controller = c;
	}

	@CoinjemaDependency(type = "dbutil", method = "dbutil")
	public void setDbUtil(DatabaseUtilities du) {
		dbUtil = du;
	}

	@CoinjemaDependency(type = "dbconnect", method = "dbconnect")
	public void setDatabase(DBConnect db) {
		database = db;
	}

	private Set getStartList(String table) {
		String[] tables = { table };
		String[] cols = { dbUtil.primaryLabelColumn };
		Data data = database.select(tables, cols, null);
		Set set = new HashSet();
		data.reset();
		while (data.next())
			set.add(data.getColumnValue(cols[0]));
		return set;
	}

	public String[] getImageData(String table, Properties props) {
		needsDeleting = new HashSet();
		needsDeleting = getStartList(table);
		String voltodir_file = (String) controller
				.getConfigValue("voltodir_file");
		String mediabankSource = (String) controller
				.getConfigValue("mediabank_source");
		String folder;
		int numFolders = 1;
		String[] folders, eachLine;
		MediaBankReader dataReader = new MediaBankReader();
		dataReader.setVoltodir_file(voltodir_file);
		List imageItems;
		ArrayList lines;
		ArrayList headers;
		Map eachItem;
		Iterator it, itter;
		headers = new ArrayList();
		lines = new ArrayList();
		lines
				.add("TABLE_NAME\tDESCRIPTIVE_NAME\tDESCRIPTION\tPRODUCT_TYPE\tPRIMARY_LABEL\tORDER_MODEL");
		lines.add(table + "\t" + props.get("DESCRIPTIVE_NAME") + "\t"
				+ props.get("DESCRIPTION") + "\t" + props.get("PRODUCT_TYPE")
				+ "\t" + props.get("PRIMARY_LABEL") + "\t"
				+ props.get("ORDER_MODEL"));
		lines.add(headers);
		StringBuffer displayOrder = new StringBuffer("DISPLAY ORDER\t");
		StringBuffer searchOrder = new StringBuffer("SEARCH ORDER\t");
		displayOrder.append("1\t2\t7\t8\t0\t0\t3\t6\t4\t5");
		searchOrder.append("1\t0\t0\t0\t0\t0\t0\t0\t0\t0");
		lines.add(displayOrder.toString());
		lines.add(searchOrder.toString());
		StringBuffer temp;
		imageItems = dataReader.getImageData(mediabankSource, table);
		setHeaders(headers, props);
		it = imageItems.iterator();
		while (it.hasNext()) {
			eachItem = (Map) it.next();
			if (needsUpdating((Date) eachItem.get("date"), (String) eachItem
					.get("time"), (String) eachItem.get("filename"), table)) {
				folders = Functions.split((String) eachItem.get("folders"),
						"\\");
				eachLine = new String[eachItem.size() + folders.length];
				while (headers.size() < eachLine.length) {
					String tempFolderName = props.get("folders") + "_"
							+ (numFolders++);
					if (props.get(tempFolderName) != null)
						tempFolderName = (String) props.get(tempFolderName);
					else
						tempFolderName = tempFolderName.replace('_', ' ');
					headers.add(CATEGORY + "(" + tempFolderName + ")");
				}
				eachLine[0] = (String) eachItem.get("filename");
				eachLine[1] = (String) eachItem.get("filetype");
				eachLine[2] = (String) eachItem.get("mac creator");
				eachLine[3] = (String) eachItem.get("mac filetype");
				eachLine[4] = (String) eachItem.get("original directory");
				eachLine[5] = (String) eachItem.get("thumbnail directory");
				try {
					eachLine[6] = eachItem.get("filesize").toString();
				} catch (NullPointerException e) {
					eachLine[6] = "0";
				}
				try {
					eachLine[7] = eachItem.get("resolution").toString();
				} catch (NullPointerException e) {
					eachLine[7] = "0";
				}
				try {
					eachLine[8] = eachItem.get("height").toString();
				} catch (NullPointerException e) {
					eachLine[8] = "0";
				}
				try {
					eachLine[9] = eachItem.get("width").toString();
				} catch (NullPointerException e) {
					eachLine[9] = "0";
				}
				// date info
				try {
					eachLine[10] = (eachItem.get("date").toString());
				} catch (Exception e) {
					e.printStackTrace();
					eachLine[10] = "0";
				}
				for (int y = 0; y < folders.length; y++)
					eachLine[11 + y] = folders[y];
				lines.add("\t" + Functions.unsplit(eachLine, "\t", "N/A"));
			}
		}
		it = headers.iterator();
		temp = new StringBuffer();
		while (it.hasNext())
			temp.append("\t" + it.next());
		lines.set(2, temp.toString());
		for (int y = 10; y < headers.size(); y++) {
			displayOrder.append("\t" + ((y - 10) + 1000));
			searchOrder.append("\t100");
		}
		lines.set(3, displayOrder.toString());
		lines.set(4, searchOrder.toString());
		if (null != imageItems && imageItems.size() > 0) {
			deleteAll(table); // Only delete if MediaBankReader returns some
								// comparison
		}
		return cleanMBImport((String[]) lines.toArray(new String[0]));
	}

	public String[] cleanMBImport(String[] inputData) {

		String table = inputData[1].substring(0, inputData[1].indexOf("\t"));
		String column = "primary_label";
		StringBuffer badData = new StringBuffer("***Input data to check for "
				+ table + " import***\n");
		StringBuffer newData = new StringBuffer();

		String primary;
		String[] temp;
		for (int line = 0; line < inputData.length; line++) {
			if (line < 5) {
				newData.append(inputData[line] + "\n");
			} else {

				temp = Functions.split(inputData[line], "\t");
				for (int i = 0; i < temp.length; i++) {
					temp[i] = Functions.stripSpace(temp[i]);
					if (null == temp[i] || temp[i].equals("")) {
						temp[i] = DBConnect.DEFAULT;
					}
				}
				primary = temp[0];
				String[] tables = { table };
				String[] cols = { dbUtil.primaryLabelColumn };
				String where = database.where(dbUtil.primaryLabelColumn,
						primary, DBConnect.EQ, DBConnect.IS);
				Data data = database.select(tables, cols, where);
				data.reset();
				if (data.next()) {
					badData.append("\t" + Functions.unsplit(temp, "\t") + "\n");
				} else {
					newData.append("\t" + Functions.unsplit(temp, "\t") + "\n");
				}
			}
		}

		Functions.javaLog(badData.toString());
		return Functions.split(newData.toString(), "\n");
	}

	private void deleteAll(String table) {
		Iterator it = needsDeleting.iterator();
		while (it.hasNext())
			database.delete(table, database.where(dbUtil.primaryLabelColumn, it
					.next(), DBConnect.EQ, DBConnect.IS));
	}

	private boolean needsUpdating(Date d, String time, String filename,
			String table) {
		boolean retVal = false;
		GregorianCalendar date = new GregorianCalendar(), oldDate = new GregorianCalendar();
		date.setTime(d);
		String[] timeNumbers = Functions.split(time, ":");
		if (timeNumbers.length == 3) {
			date.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeNumbers[0]));
			date.set(Calendar.MINUTE, Integer.parseInt(timeNumbers[1]));
			date.set(Calendar.SECOND, Integer.parseInt(timeNumbers[2]));
		}
		String[] tables = { table };
		String[] cols = { dbUtil.dateCatalogedColumn };
		String where = database.where(dbUtil.primaryLabelColumn, filename,
				DBConnect.EQ, DBConnect.IS);
		Data data = database.select(tables, cols, where);
		data.reset();
		if (data.next()) {
			needsDeleting.remove(filename);
			oldDate.setTime((Date) data.getColumnValue(cols[0]));
			if (!oldDate.after(date)) {
				retVal = true;
				Functions.javaLog("Needs Updating: " + filename + " newDate="
						+ d.toString() + " oldDate="
						+ data.getColumnValue(cols[0]));
				Functions.javaLog("Needs Updating: " + filename
						+ " newCalendarDate=" + date.get(date.MONTH) + "."
						+ date.get(date.DAY_OF_MONTH) + "."
						+ date.get(date.YEAR) + " oldDate="
						+ oldDate.get(oldDate.MONTH) + "."
						+ oldDate.get(oldDate.DAY_OF_MONTH) + "."
						+ oldDate.get(oldDate.YEAR));
			} else {
				retVal = false;
			}
		} else {
			retVal = true;
			Functions.javaLog("Needs Updating: " + filename + " newDate="
					+ d.toString());
			Functions.javaLog("Needs Updating: " + filename
					+ " newCalendarDate=" + date.get(date.MONTH) + "."
					+ date.get(date.DAY_OF_MONTH) + "." + date.get(date.YEAR));
		}
		return retVal;
	}

	/***************************************************************************
	 * Sets up the headers for the data input file based on the configuration
	 * file for the table being imported.
	 * 
	 * @param headers
	 *            ArrayList object to set up.
	 * @param props
	 *            Properties file that holds the configuration information.
	 **************************************************************************/
	private void setHeaders(ArrayList headers, Properties props) {
		headers.add(PRIMARY + "(" + props.get("filename") + ")");
		headers.add(CATEGORY + "(" + props.get("filetype") + ")");
		headers.add(CATEGORY + "(" + props.get("mac_creator") + ")");
		headers.add(CATEGORY + "(" + props.get("mac_filetype") + ")");
		headers.add(DESCRIPTION + "(" + props.get("original_directory") + ")");
		headers.add(DESCRIPTION + "(" + props.get("thumbnail_directory") + ")");
		headers.add(NUMERICAL + "(" + props.get("filesize") + ")");
		headers.add(NUMERICAL + "(" + props.get("resolution") + ")");
		headers.add(NUMERICAL + "(" + props.get("height") + ")");
		headers.add(NUMERICAL + "(" + props.get("width") + ")");
		headers.add(CATEGORY + "(" + props.get("date") + ")");
	}
}
