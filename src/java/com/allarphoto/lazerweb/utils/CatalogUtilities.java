package com.lazerinc.lazerweb.utils;

import com.lazerinc.dbtools.DBConnect;
import com.lazerinc.dbtools.InterbaseDB;
import com.lazerinc.utils.Data;
import com.lazerinc.utils.Functions;

public class CatalogUtilities {

	private InterbaseDB database;

	private String datasource;

	private String username;

	private String password;

	private int maxConnections;

	private static final String categoryTable = "_categories";

	private static final String keycodeTable = "_keycodes";

	private static final String filenameTable = "_filenames";

	private static final String categoryIDColumn = "category_id";

	private static final String categoryColumn = "category_name";

	private static final String positionColumn = "name_position";

	private static final String keycodeColumn = "keycode";

	private static final String keywordColumn = "keyword";

	private static final String filenameIDColumn = "filename_id";

	private static final String filenameColumn = "filename";

	public static final String DEFAULT = "N/A";

	// Parameter-less constructor for testing purposes only
	public CatalogUtilities() {
		datasource = "//localhost/d:/DB Files/KEYWORDPARSE.GDB";
		username = "damuser";
		password = "r!VAld0";
		maxConnections = 5;
		database = new InterbaseDB();
	}

	public CatalogUtilities(String datasource, String username,
			String password, int maxConnections) {
		this.datasource = datasource;
		this.username = username;
		this.password = password;
		this.maxConnections = maxConnections;
		database = new InterbaseDB();
	}

	public int addCategory(String lib, String category, String position) {
		int retVal = -1;
		Data data;
		String[] tables = { lib + categoryTable };
		String[] cols = { categoryColumn, positionColumn };
		String[] vals = { database.cleanString(category), position };
		retVal = getCategoryID(lib, categoryColumn);
		if (retVal < 1) {
			database.insert(tables[0], cols, vals);
			retVal = getCategoryID(lib, category);
		}
		return retVal;
	}

	public String getCategory(String lib, String catID) {
		String retVal = DEFAULT;
		Data data;
		String[] tables = { lib + categoryTable };
		String[] cols = { categoryColumn };
		String where = database.where(categoryIDColumn, catID, DBConnect.EQ,
				DBConnect.NUMBER);
		data = database.select(tables, cols, where);
		data.reset();
		if (data.next()) {
			retVal = (String) data.getColumnValue(categoryColumn);
		}
		return retVal;
	}

	public String[] getCategories(String lib) {
		Data data;
		String[] tables = { lib + categoryTable };
		String[] cols = { categoryColumn };
		data = database.select(tables, cols, null);

		return data.getColumn(categoryColumn);
	}

	public int getCategoryID(String lib, String category) {
		int retVal = -1;
		Data data;
		String[] tables = { lib + categoryTable };
		String[] cols = { categoryIDColumn };
		String where = database.where(categoryColumn, category, DBConnect.EQ,
				DBConnect.ISCASEINSENSITIVE);
		data = database.select(tables, cols, where);
		data.reset();
		if (data.next()) {
			retVal = ((Integer) data.getColumnValue(categoryIDColumn))
					.intValue();
		}
		return retVal;
	}

	public int getPosition(String lib, String category) {
		int retVal = -1;
		Data data;
		String[] tables = { lib + categoryTable };
		String[] cols = { positionColumn };
		String where = database.where(categoryColumn, category, DBConnect.EQ,
				DBConnect.ISCASEINSENSITIVE);

		Functions.javaLog(where);

		data = database.select(tables, cols, where);

		Functions.javaLog("" + data.size());
		data.reset();
		if (data.next()) {
			retVal = ((Integer) data.getColumnValue(positionColumn)).intValue();
		}
		return retVal;
	}

	public String getKeycode(String lib, int categoryID, String keyword) {
		String retVal = DEFAULT;
		Data data;
		String[] tables = { lib + keycodeTable };
		String[] cols = { keycodeColumn };
		String where = database.where(categoryIDColumn, categoryID,
				DBConnect.EQ, DBConnect.NUMBER)
				+ " AND "
				+ database.where(keywordColumn, keyword, DBConnect.EQ,
						DBConnect.ISCASEINSENSITIVE);
		data = database.select(tables, cols, where);
		data.reset();
		if (data.next()) {
			retVal = (String) data.getColumnValue(keycodeColumn);
		}
		return retVal;
	}

	public String getKeycode(String lib, String category, String keyword) {
		int categoryID = getCategoryID(lib, category);
		return getKeycode(lib, categoryID, keyword);
	}

	public String[] getKeywords(String lib, String category) {

		Data data;
		int catID = getCategoryID(lib, category);
		String[] tables = { lib + keycodeTable };
		String[] cols = { keywordColumn };
		String where = database.where(categoryIDColumn, catID, DBConnect.EQ,
				DBConnect.NUMBER);
		data = database.select(tables, cols, where);

		return data.getColumn(keywordColumn);
	}

	public boolean addKeyword(String lib, int categoryID, String keycode,
			String keyword) {
		boolean retVal = false;
		Data data;
		String table = lib + keycodeTable;
		String[] cols = { categoryIDColumn, keycodeColumn, keywordColumn };
		String[] updateCols = { keywordColumn };
		String[] vals = { new Integer(categoryID).toString(),
				database.cleanString(keycode), database.cleanString(keyword) };
		String[] updateVals = { keyword };
		String where = database.where(categoryIDColumn, categoryID,
				DBConnect.EQ, DBConnect.NUMBER)
				+ " AND "
				+ database.where(keycodeColumn, keycode, DBConnect.EQ,
						DBConnect.IS);
		if (getKeycode(lib, categoryID, keyword).equals(DEFAULT)) {
			database.insert(table, cols, vals);
		} else {
			database.update(table, updateCols, updateVals, where);
		}
		if (keycode.equals(getKeycode(lib, categoryID, keyword))) {
			retVal = true;
		}
		return retVal;
	}

	public static void main(String[] args) {
		CatalogUtilities cu = new CatalogUtilities();
		cu.invokedStandalone = true;

		boolean x = cu.addKeyword(args[0], new Integer(args[1]).intValue(),
				args[2], args[3]);
		System.out.println(x);
	}

	private boolean invokedStandalone = false;
}