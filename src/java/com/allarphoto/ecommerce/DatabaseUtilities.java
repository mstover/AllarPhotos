/***********************************************************************************************************************
 * Copyright 1999 (c) Lazer Incorporated All code contained in this file is the property of Lazer Incorporated. All
 * rights reserved.
 **********************************************************************************************************************/

package com.lazerinc.ecommerce;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaDynamic;
import org.coinjema.context.CoinjemaObject;

import strategiclibrary.service.cache.CacheService;
import strategiclibrary.service.sql.ObjectMappingService;

import com.lazerinc.application.LazerwebDataSource;
import com.lazerinc.application.Product;
import com.lazerinc.application.SecurityModel;
import com.lazerinc.beans.Company;
import com.lazerinc.beans.User;
import com.lazerinc.category.DateField;
import com.lazerinc.category.ExpiredCategory;
import com.lazerinc.category.ProductField;
import com.lazerinc.dbtools.DBConnect;
import com.lazerinc.server.ResourceService;
import com.lazerinc.server.UserService;
import com.lazerinc.utils.Data;
import com.lazerinc.utils.Functions;
import com.lazerinc.utils.Resource;

@CoinjemaObject
public class DatabaseUtilities implements LazerwebDataSource {
	private ObjectMappingService objectMapper;

	private CacheService objectCache;

	private ResourceService resService;

	public static ProductField expiredImage = new ExpiredCategory("",
			ProductField.EXPIRED, 0);

	public static ProductField dateField = new DateField("", "Date Posted", 0);

	public static ProductField modDateField = new DateField("",
			"Date Modified", 0);

	private static Logger log = DatabaseUtilities.getLogger();

	UserService ugd;

	@CoinjemaDynamic(alias = "log4j")
	private static Logger getLogger() {
		return null;
	}

	private DBConnect database;

	public DatabaseUtilities() {
	}

	@CoinjemaDependency(type = "dbconnect", method = "dbconnect", order = CoinjemaDependency.Order.LAST)
	public void setDatabase(DBConnect db) {
		database = db;
	}

	@CoinjemaDependency(type = "objectMappingService", method = "objectMappingService")
	public void setObjectMapper(ObjectMappingService svc) {
		objectMapper = svc;
	}

	@CoinjemaDependency(type = "resourceService")
	public void setResourceService(ResourceService rs) {
		resService = rs;
	}

	@CoinjemaDependency(type = "cacheService", method = "cacheService")
	public void setCacheService(CacheService cs) {
		objectCache = cs;
	}

	@CoinjemaDependency(type = "userService", method = "userService")
	public void setUserService(UserService ugd) {
		this.ugd = ugd;
	}

	public Set getKeywords(Collection<String> families) {
		Set keys = new HashSet();
		String[] tables = new String[1];
		String[] cols = { keywordColumn };
		for (String fam : families) {
			tables[0] = fam + keywordTable;
			Data data = database.select(tables, cols, null);
			data.reset();
			while (data.next())
				keys.add(data.getColumnValue(cols[0]));
		}
		return keys;
	}

	public void updateSecurityForFields(String family, String field,
			int fieldType) {
		if (Math.abs(fieldType) == ProductField.CATEGORY
				|| fieldType == ProductField.TAG) {
			if (fieldType > 0)
				removeSecurityForFields(family, field);
			else
				updateSecurityForFields(family, field);
		}
	}

	public SecurityModel getAdmin() {
		return ugd.getGroup("admin").getPermissions();
	}

	private void updateSecurityForFields(String family, String field) {
		String[] tables = { family + categoryTable, family + keywordTable,
				family + fieldTable };
		String[] cols = { keywordColumn };
		String where = database.where(family + keywordTable + "."
				+ keywordIDColumn, family + categoryTable + "."
				+ keywordIDColumn, DBConnect.EQ, DBConnect.COLUMN)
				+ " AND "
				+ database.where(family + fieldTable + "." + fieldIDColumn,
						family + categoryTable + "." + fieldIDColumn,
						DBConnect.EQ, DBConnect.COLUMN)
				+ " AND "
				+ database.where(family + fieldTable + "." + nameColumn, field,
						DBConnect.EQ, DBConnect.IS);
		Data data = database.select(tables, cols, where);
		data.reset();
		while (data.next()) {
			if (resService.getResource(family + "." + field + "."
					+ (String) data.getColumnValue(cols[0]),
					Resource.PROTECTED_FIELD) == null)
				objectCache.getCache(Resource.class).addItem(
						new Resource((int) getNewId("resources"), family + "."
								+ field + "."
								+ (String) data.getColumnValue(cols[0]),
								Resource.PROTECTED_FIELD));
		}

	}

	private void removeSecurityForFields(String family, String field) {
		String[] tables = { family + categoryTable, family + keywordTable,
				family + fieldTable };
		String[] cols = { keywordColumn };
		String where = database.where(family + keywordTable + "."
				+ keywordIDColumn, family + categoryTable + "."
				+ keywordIDColumn, DBConnect.EQ, DBConnect.COLUMN)
				+ " AND "
				+ database.where(family + fieldTable + "." + fieldIDColumn,
						family + categoryTable + "." + fieldIDColumn,
						DBConnect.EQ, DBConnect.COLUMN)
				+ " AND "
				+ database.where(family + fieldTable + "." + nameColumn, field,
						DBConnect.EQ, DBConnect.IS);
		Data data = database.select(tables, cols, where);
		data.reset();
		while (data.next())
			removeResource(family + "." + field + "."
					+ (String) data.getColumnValue(cols[0]),
					Resource.PROTECTED_FIELD);
	}

	public void removeResource(String resourceName, int resourceType) {
		Resource resource = resService.getResource(resourceName, resourceType);
		objectCache.getCache(Resource.class).deleteObject(resource);
		for (UserGroup g : objectCache.getCache(UserGroup.class)
				.getCachedList()) {
			g.removeResource(resource);
		}
	}

	private void addUserToGroup(int userID, String group) {
		String[] cols = { userIDColumn, groupIDColumn };
		String[] vals = { "" + userID, "" + getGroupID(group) };
		database.insert(userGroupTable, cols, vals);
	}

	private void addUserToGroup(int userID, int groupId) {
		String[] cols = { userIDColumn, groupIDColumn };
		String[] vals = { "" + userID, "" + groupId };
		database.insert(userGroupTable, cols, vals);
	}

	/***************************************************************************
	 * Gets the right id # associated with the given right name.
	 * 
	 * @param right
	 *            Right's name.
	 * @return Right's ID #.
	 **************************************************************************/
	public int getRightID(String right) {
		int retVal;
		String[] tables = { rightsTable };
		String[] cols = { rightIDColumn };
		String where = database.where(nameColumn, right, DBConnect.EQ,
				DBConnect.IS);
		Data data = database.select(tables, cols, where);
		data.reset();
		if (data.next())
			retVal = ((Integer) data.getColumnValue(cols[0])).intValue();
		else
			retVal = -1;
		return retVal;
	}

	/***************************************************************************
	 * Gets the group id # associated with the given group name.
	 * 
	 * @param group
	 *            Group's name.
	 * @return Group's ID #.
	 **************************************************************************/
	public int getGroupID(String group) {
		int retVal;
		String[] tables = { groupTable };
		String[] cols = { groupIDColumn };
		String where = database.where(nameColumn, group, DBConnect.EQ,
				DBConnect.IS);
		Data data = database.select(tables, cols, where);
		data.reset();
		if (data.next())
			retVal = ((Integer) data.getColumnValue(groupIDColumn)).intValue();
		else
			retVal = -1;
		return retVal;
	}

	public long getNewId(String tableName) {
		Map values = new HashMap();
		values.put("table", tableName);
		Long l = (Long) objectMapper.getObjects("getNewId.sql", values)
				.iterator().next();
		return l.longValue();
	}

	/***************************************************************************
	 * Gets the product table id # associated with the given product table name.
	 * 
	 * @param table
	 *            Table name.
	 * @return table's ID #.
	 **************************************************************************/
	public int getProductTableID(String table) {
		int retVal;
		String[] tables = { productTable };
		String[] cols = { productTableIDColumn };
		String where = database.where(tableNameColumn, table, DBConnect.EQ,
				DBConnect.IS);
		Data data = database.select(tables, cols, where);
		data.reset();
		if (data.next())
			retVal = ((Integer) data.getColumnValue(cols[0])).intValue();
		else
			retVal = -1;
		return retVal;
	}

	/***************************************************************************
	 * Method to get a value for a product. Either a CATEGORY value or a
	 * DESCRIPTION value.
	 * 
	 * @param pt
	 *            Product Table
	 * @param productID
	 *            ID # of product.
	 * @param fieldName
	 *            Name of field.
	 * @return String of values for the field (multiple values joined with a
	 *         ",".
	 **************************************************************************/
	Object getValue(String pt, int productID, String fieldName) {
		ProductField f = getProductFamily(pt).getField(fieldName);
		return getValue(pt, productID, f);
	}

	/***************************************************************************
	 * Method to get a value for a product given a ProductField.
	 * 
	 * @param pt
	 *            Product Table.
	 * @param productID
	 *            ID # of product.
	 * @param field
	 *            ProductField object.
	 * @return String of values for the field (multiple values joined with a
	 *         ",".
	 **************************************************************************/
	Object getValue(String pt, int productID, ProductField fld) {
		Object retVal = new String("N/A");
		switch (fld.getType()) {
		case ProductField.TAG:
			;
		case ProductField.CATEGORY: {
			retVal = Functions.unsplit(getCategoryValues(pt, productID, fld
					.getName()), ",");
			break;
		}
		case ProductField.DESCRIPTION: {
			retVal = getDescriptionValue(pt, productID, fld.getName());
			break;
		}
		case ProductField.NUMERICAL: {
			retVal = getNumericalValue(pt, productID, fld.getName());
			break;
		}
		case ProductField.PRIMARY: {
			retVal = getPrimaryValue(pt, productID);
			break;
		}
		}
		return retVal;
	}

	/***************************************************************************
	 * Gets the ID of the given price key.
	 * 
	 * @param pt
	 *            Product Table.
	 * @param priceKey
	 *            Price Key to look up.
	 * @return ID # of price key.
	 **************************************************************************/
	public int getPriceKeyID(String pt, String priceKey) {
		int retVal;
		String[] tables = { pt + priceKeyTable };
		String[] cols = { priceKeyIDColumn };
		String where = database.where(priceKeyColumn, priceKey, DBConnect.EQ,
				DBConnect.IS);
		Data data = database.select(tables, cols, where);
		data.reset();
		if (data.next())
			retVal = ((Integer) data.getColumnValue(priceKeyIDColumn))
					.intValue();
		else
			retVal = 0;
		return retVal;
	}

	/***************************************************************************
	 * Gets the value of a category keyword field for a certain product. Returns
	 * the result as an array of strings.
	 * 
	 * @param pt
	 *            Name of product table involved.
	 * @param productID
	 *            ID of product to get values for.
	 * @param fieldName
	 *            Name of field to get for product.
	 * @return Array of values for the field.
	 **************************************************************************/
	String[] getCategoryValues(String pt, int productID, String fieldName) {
		String linkTable = pt + categoryTable;
		String keyTable = pt + keywordTable;
		String fldTable = pt + fieldTable;
		String[] tables = { keyTable, linkTable, fldTable };
		String[] cols = { keywordColumn };
		String where = database.where(keyTable + "." + keywordIDColumn,
				linkTable + "." + keywordIDColumn, DBConnect.EQ,
				DBConnect.COLUMN)
				+ " AND "
				+ database.where(fldTable + "." + fieldIDColumn, linkTable
						+ "." + fieldIDColumn, DBConnect.EQ, DBConnect.COLUMN)
				+ " AND "
				+ database.where(fieldNameColumn, fieldName, DBConnect.EQ,
						DBConnect.IS)
				+ " AND "
				+ productIDColumn
				+ " = "
				+ productID;
		Data data = database.select(tables, cols, where);
		String[] values = data.getColumn(keywordColumn);
		return values;
	}

	/***************************************************************************
	 * Adds a state to the database.
	 * 
	 * @param state
	 *            Name of state to be added.
	 * @param stateAbbr
	 *            Abbreviation for the state
	 **************************************************************************/
	void addState(String state, String stateAbbr) {
		String[] cols = { nameColumn, stateAbbrColumn };
		String[] vals = { database.cleanString(state),
				database.cleanString(stateAbbr) };
		database.insert(stateTable, cols, vals);
	}

	/***************************************************************************
	 * Adds a city to the database.
	 * 
	 * @param city
	 *            Name of city to be added.
	 **************************************************************************/
	void addCity(String city) {
		String[] cols = { nameColumn };
		String[] vals = { database.cleanString(city) };
		database.insert(cityTable, cols, vals);
	}

	/***************************************************************************
	 * Adds a country to the database.
	 * 
	 * @param country
	 *            Name of country to be added.
	 * @param countryCode
	 *            Code for the country
	 **************************************************************************/
	void addCountry(String country, String countryCode) {
		String[] cols = { nameColumn, countryCodeColumn };
		String[] vals = { database.cleanString(country),
				database.cleanString(countryCode) };
		database.insert(countryTable, cols, vals);
	}

	/***************************************************************************
	 * Gets the value of a category keyword field for a certain product. Returns
	 * the result as an array of strings.
	 * 
	 * @param pt
	 *            Name of product table involved.
	 * @param primary
	 *            Primary label value of product to get values for.
	 * @param fieldName
	 *            Name of field to get for product.
	 * @return Array of values for the field.
	 **************************************************************************/
	String[] getCategoryValues(String pt, String primary, String fieldName) {
		int pID = getProductID(pt, primary);
		return getCategoryValues(pt, pID, fieldName);
	}

	/***************************************************************************
	 * Gets the value of a category keyword field for a certain product. Returns
	 * the result as an array of strings.
	 * 
	 * @param ptID
	 *            ID of product table involved.
	 * @param productID
	 *            ID of product to get values for.
	 * @param fieldName
	 *            Name of field to get for product.
	 * @return Array of values for the field.
	 **************************************************************************/
	String[] getCategoryValues(int ptID, int productID, String fieldName) {
		return getCategoryValues(getProductTable(ptID), productID, fieldName);
	}

	/***************************************************************************
	 * Gets the value of a category keyword field for a certain product. Returns
	 * the result as an array of strings.
	 * 
	 * @param ptID
	 *            ID of product table involved.
	 * @param primary
	 *            Primary Label of product to get values for.
	 * @param fieldName
	 *            Name of field to get for product.
	 * @return Array of values for the field.
	 **************************************************************************/
	String[] getCategoryValues(int ptID, String primary, String fieldName) {
		String pt = getProductTable(ptID);
		int pID = getProductID(pt, primary);
		return getCategoryValues(pt, pID, fieldName);
	}

	/***************************************************************************
	 * Gets the state ID # given the name of the state.
	 * 
	 * @param state
	 *            Name of the state.
	 * @return ID # of state.
	 **************************************************************************/
	public int getStateID(String state) {
		if (state == null)
			state = DEFAULT;
		int retVal;
		String[] tables = { stateTable };
		String[] cols = { stateIDColumn };
		String where = database.where(nameColumn, state, DBConnect.EQ,
				DBConnect.IS);
		Data data = database.select(tables, cols, where);
		data.reset();
		if (data.next())
			retVal = ((Integer) data.getColumnValue(stateIDColumn)).intValue();
		else {
			addState(state, state);
			retVal = getStateID("N/A");
		}
		return retVal;
	}

	/***************************************************************************
	 * Gets the city ID # given the name of the city.
	 * 
	 * @param city
	 *            Name of the city.
	 * @return ID # of city.
	 **************************************************************************/
	public int getCityID(String city) {
		if (city == null)
			city = DEFAULT;
		int retVal;
		String[] tables = { cityTable };
		String[] cols = { cityIDColumn };
		String where = database.where(nameColumn, city, DBConnect.EQ,
				DBConnect.IS);
		Data data = database.select(tables, cols, where);
		data.reset();
		if (data.next())
			retVal = ((Integer) data.getColumnValue(cityIDColumn)).intValue();
		else {
			addCity(city);
			retVal = getCityID(city);
		}
		return retVal;
	}

	/***************************************************************************
	 * Gets the country ID # given the name of the country.
	 * 
	 * @param country
	 *            Name of the country.
	 * @return ID # of country.
	 **************************************************************************/
	public int getCountryID(String country) {
		if (country == null)
			country = DEFAULT;
		int retVal;
		String[] tables = { countryTable };
		String[] cols = { countryIDColumn };
		String where = database.where(nameColumn, country, DBConnect.EQ,
				DBConnect.IS);
		Data data = database.select(tables, cols, where);
		data.reset();
		if (data.next())
			retVal = ((Integer) data.getColumnValue(countryIDColumn))
					.intValue();
		else {
			addCountry(country, country);
			retVal = getCountryID("N/A");
		}
		return retVal;
	}

	/***************************************************************************
	 * Gets the referrer ID # given the name of the referrer.
	 * 
	 * @param referrer
	 *            Name of the referrer.
	 * @return ID # of referrer.
	 **************************************************************************/
	int getReferrerID(String referrer) {
		int retVal;
		String[] tables = { referrerTable };
		String[] cols = { referrerIDColumn };
		String where = database.where(nameColumn, referrer, DBConnect.EQ,
				DBConnect.IS);
		Data data = database.select(tables, cols, where);
		data.reset();
		if (data.next())
			retVal = ((Integer) data.getColumnValue(cols[0])).intValue();
		else {
			retVal = addReferrer(referrer);
		}
		return retVal;
	}

	/***************************************************************************
	 * Gets the Company ID # for a company.
	 * 
	 * @param companyName
	 *            Name of company.
	 * @return Company ID #.
	 **************************************************************************/
	public int addCompany(Company company) {
		if (null == company || company.equals("")) {
			company = this.objectCache.getCache(Company.class).getCachedObject(
					"name", DEFAULT);
		}
		if (company.getName() == null)
			company.setName(DEFAULT);
		int retVal;
		int ind = company.getIndustry().getId();
		String[] tables = { companyTable };
		String[] cols = { companyIDColumn };
		String where = database.where(nameColumn, company.getName(),
				DBConnect.EQ, DBConnect.IS);
		Data data = database.select(tables, cols, where);
		data.reset();
		if (data.next())
			retVal = ((Integer) data.getColumnValue(cols[0])).intValue();
		else {
			String[] columns = { nameColumn, industryIDColumn };
			String[] vals = { database.cleanString(company.getName()), "" + ind };
			database.insert(companyTable, columns, vals);
			data = database.select(tables, cols, where);
			data.reset();
			if (data.next())
				retVal = ((Integer) data.getColumnValue(cols[0])).intValue();
			else
				retVal = -1;
		}
		return retVal;
	}

	/***************************************************************************
	 * Gets the city given the ID # for the city.
	 * 
	 * @param cityID
	 *            ID # for the city.
	 * @return Name of city.
	 **************************************************************************/
	String getCity(int cityID) {
		String retVal;
		String[] tables = { cityTable };
		String[] cols = { nameColumn };
		String where = database.where(cityIDColumn, cityID, DBConnect.EQ,
				DBConnect.NUMBER);
		Data data = database.select(tables, cols, where);
		data.reset();
		if (data.next())
			retVal = (String) data.getColumnValue(nameColumn);
		else
			retVal = null;
		return retVal;
	}

	/***************************************************************************
	 * Gets the country given the ID # for the country.
	 * 
	 * @param countryID
	 *            ID # for the country.
	 * @return Name of country.
	 **************************************************************************/
	String getCountry(int countryID) {
		String retVal;
		String[] tables = { countryTable };
		String[] cols = { nameColumn };
		String where = database.where(countryIDColumn, countryID, DBConnect.EQ,
				DBConnect.NUMBER);
		Data data = database.select(tables, cols, where);
		data.reset();
		if (data.next())
			retVal = (String) data.getColumnValue(nameColumn);
		else
			retVal = null;
		return retVal;
	}

	/***************************************************************************
	 * Gets the user id # associated with the given username.
	 * 
	 * @param username
	 *            User's username.
	 * @return User's ID #.
	 **************************************************************************/
	public int getUserID(String username) {
		int retVal;
		String[] tables = { userTable };
		String[] cols = { userIDColumn };
		String where = database.where(usernameColumn, username, DBConnect.EQ,
				DBConnect.IS);
		Data data = database.select(tables, cols, where);
		data.reset();
		if (data.next())
			retVal = ((Integer) data.getColumnValue(userIDColumn)).intValue();
		else
			retVal = -1;
		return retVal;
	}

	/***************************************************************************
	 * Gets the state given the ID # for the state.
	 * 
	 * @param stateID
	 *            ID # for the state.
	 * @return Name of state.
	 **************************************************************************/
	String getState(int stateID) {
		String retVal;
		String[] tables = { stateTable };
		String[] cols = { nameColumn };
		String where = database.where(stateIDColumn, stateID, DBConnect.EQ,
				DBConnect.NUMBER);
		Data data = database.select(tables, cols, where);
		data.reset();
		if (data.next())
			retVal = (String) data.getColumnValue(nameColumn);
		else
			retVal = null;
		return retVal;
	}

	/***************************************************************************
	 * Gets the value of the primary field for a certain product. Returns the
	 * result as a string.
	 * 
	 * @param pt
	 *            Name of product table involved.
	 * @param productID
	 *            ID of product to get values for.
	 * @return Values for the primary field.
	 **************************************************************************/
	String getPrimaryValue(String pt, int productID) {
		String value;
		String[] tables = { pt };
		String[] cols = { primaryLabelColumn };
		String where = productIDColumn + " = " + productID;
		Data data = database.select(tables, cols, where);
		data.reset();
		if (data.next())
			value = (String) data.getColumnValue(cols[0]);
		else
			value = null;
		return value;
	}

	/***************************************************************************
	 * Gets the value of a description field for a certain product. Returns the
	 * result as an array of strings.
	 * 
	 * @param pt
	 *            Name of product table involved.
	 * @param productID
	 *            ID of product to get values for.
	 * @param fieldName
	 *            Name of field to get for product.
	 * @return Array of values for the field.
	 **************************************************************************/
	String getDescriptionValue(String pt, int productID, String fieldName) {
		String value;
		String keyTable = pt + descriptionKeyTable;
		String linkTable = pt + descriptionTable;
		String fldTable = pt + fieldTable;
		String[] tables = { keyTable, linkTable, fldTable };
		String[] cols = { descriptionColumn };
		String where = database.where(keyTable + "." + descriptionIDColumn,
				linkTable + "." + descriptionIDColumn, DBConnect.EQ,
				DBConnect.COLUMN)
				+ " AND "
				+ database.where(fldTable + "." + fieldIDColumn, linkTable
						+ "." + fieldIDColumn, DBConnect.EQ, DBConnect.COLUMN)
				+ " AND "
				+ database.where(fieldNameColumn, fieldName, DBConnect.EQ,
						DBConnect.IS)
				+ " AND "
				+ productIDColumn
				+ " = "
				+ productID;
		Data data = database.select(tables, cols, where);
		data.reset();
		if (data.next())
			value = (String) data.getColumnValue(descriptionColumn);
		else
			value = null;
		return value;
	}

	/***************************************************************************
	 * Gets the value of a description field for a certain product. Returns the
	 * result as an array of strings.
	 * 
	 * @param pt
	 *            Name of product table involved.
	 * @param primary
	 *            Primary label value of product to get values for.
	 * @param fieldName
	 *            Name of field to get for product.
	 * @return Array of values for the field.
	 **************************************************************************/
	String getDescriptionValue(String pt, String primary, String fieldName) {
		int pID = getProductID(pt, primary);
		return getDescriptionValue(pt, pID, fieldName);
	}

	/***************************************************************************
	 * Gets the value of a description field for a certain product. Returns the
	 * result as an array of strings.
	 * 
	 * @param ptID
	 *            ID of product table involved.
	 * @param productID
	 *            ID of product to get values for.
	 * @param fieldName
	 *            Name of field to get for product.
	 * @return Array of values for the field.
	 **************************************************************************/
	String getDescriptionValue(int ptID, int productID, String fieldName) {
		return getDescriptionValue(getProductTable(ptID), productID, fieldName);
	}

	/***************************************************************************
	 * Gets the value of a description field for a certain product. Returns the
	 * result as an array of strings.
	 * 
	 * @param ptID
	 *            ID of product table involved.
	 * @param primary
	 *            Primary Label of product to get values for.
	 * @param fieldName
	 *            Name of field to get for product.
	 * @return Array of values for the field.
	 **************************************************************************/
	String getDescriptionValue(int ptID, String primary, String fieldName) {
		String pt = getProductTable(ptID);
		int pID = getProductID(pt, primary);
		return getDescriptionValue(pt, pID, fieldName);
	}

	/***************************************************************************
	 * Gets the ProductFamily object for the given product table.
	 * 
	 * @param pt
	 *            Product Family.
	 * @return ProductFamily object.
	 **************************************************************************/
	public ProductFamily getProductFamily(String pt) {
		return objectCache.getCache(ProductFamily.class).getCachedObject(
				"tableName", pt);
	}

	/***************************************************************************
	 * Gets the ProductFamily object for the given descriptive name.
	 * 
	 * @param pt
	 *            Product Family.
	 * @return ProductFamily object.
	 **************************************************************************/
	public ProductFamily getProductFamilyFromDescription(String descriptiveName) {
		return objectCache.getCache(ProductFamily.class).getCachedObject(
				"descriptiveName", descriptiveName);
	}

	/***************************************************************************
	 * Returns a list of Product Tables in this this.database.
	 * 
	 * @return Array of pt's (Product Tables).
	 **************************************************************************/
	public Collection<ProductFamily> getProductFamilies() {
		return objectCache.getCache(ProductFamily.class).getCachedList();
	}

	/***************************************************************************
	 * Method to return the product table's name given it's ID number.
	 * 
	 * @param ptID
	 *            Product table's ID number.
	 * @return Name of product table.
	 **************************************************************************/
	public String getProductTable(int ptID) {
		String pt = null;
		String[] tables = { productTable };
		String[] cols = { tableNameColumn };
		String where = productTableIDColumn + " = " + ptID;
		Data data = database.select(tables, cols, where);
		data.reset();
		if (data.next())
			pt = (String) data.getColumnValue(cols[0]);
		return pt;
	}

	/***************************************************************************
	 * Method to return the product table's ID number given it's string name.
	 * 
	 * @param pt
	 *            Product table's name.
	 * @return ID # of product table.
	 **************************************************************************/
	public int getProductTable(String pt) {
		int ptID = -1;
		String[] tables = { productTable };
		String[] cols = { productTableIDColumn };
		String where = tableNameColumn + " = " + this.database.cleanString(pt);
		Data data = database.select(tables, cols, where);
		data.reset();
		if (data.next())
			ptID = ((Integer) data.getColumnValue(cols[0])).intValue();
		return ptID;
	}

	/***************************************************************************
	 * Gets the product ID number given the product table and sku.
	 * 
	 * @param pt
	 *            Product table name.
	 * @param primary
	 *            Primary label value of product.
	 * @return Product ID #.
	 **************************************************************************/
	int getProductID(String pt, String primary) {
		int productID;
		String[] tables = { pt };
		String[] cols = { productIDColumn };
		String where = database.where(primaryLabelColumn, primary,
				DBConnect.EQ, DBConnect.ISCASEINSENSITIVE);
		Data data = database.select(tables, cols, where);
		data.reset();
		if (data.next())
			productID = ((Integer) data.getColumnValue(productIDColumn))
					.intValue();
		else
			productID = -1;
		return productID;
	}

	/***************************************************************************
	 * Gets the product ID number given the product table and sku.
	 * 
	 * @param ptID
	 *            Product table Id #.
	 * @param primary
	 *            Primary label value of product.
	 * @return Product ID #.
	 **************************************************************************/
	int getProductID(int ptID, String primary) {
		return getProductID(getProductTable(ptID), primary);
	}

	/***************************************************************************
	 * Gets a price for a product given a price key.
	 * 
	 * @param pt
	 *            Product Table.
	 * @param productID
	 *            Product ID #.
	 * @param priceKey.
	 * @return Price for the product.
	 **************************************************************************/
	public float getPrice(String pt, int productID, String priceKey) {
		Float retVal;
		int priceKeyID = getPriceKeyID(pt, priceKey);
		String prTable = pt + priceTable;
		String keyTable = pt + priceKeyTable;
		String[] tables = { prTable };
		String[] cols = { priceColumn };
		String where = database.where(prTable + "." + priceKeyIDColumn,
				priceKeyID, DBConnect.EQ, DBConnect.NUMBER)
				+ " AND "
				+ database.where(productIDColumn, productID, DBConnect.EQ,
						DBConnect.NUMBER);
		Data data = database.select(tables, cols, where);
		data.reset();
		if (data.next())
			retVal = (Float) data.getColumnValue(cols[0]);
		else
			retVal = new Float(0.00);
		return Functions.round(retVal.floatValue(), 2);
	}

	/***************************************************************************
	 * Gets a price for a product given a price key.
	 * 
	 * @param pt
	 *            Product Table.
	 * @param primary
	 *            Product primary label value.
	 * @param priceKey.
	 * @return Price for the product.
	 **************************************************************************/
	public float getPrice(String pt, String primary, String priceKey) {
		int pID = getProductID(pt, primary);
		return getPrice(pt, pID, priceKey);
	}

	/***************************************************************************
	 * Returns all the Price Keys for a particular Product Table. The price key
	 * is the string that is keyed to a certain price column in the product
	 * table. When requesting the price of a product, give the price key, and
	 * the appropriate price will be returned.
	 * 
	 * @param ptID
	 *            ID # of database to get fields for.
	 * @return Hashtable of price descriptive name-column name pairs.
	 **************************************************************************/
	String[] getPriceKeys(int ptID) {
		String pt = getProductTable(ptID);
		return getPriceKeys(pt);
	}

	/***************************************************************************
	 * Returns all the Price Keys for a particular Product Table. The price key
	 * is the string that is keyed to a certain price column in the product
	 * table. When requesting the price of a product, give the price key, and
	 * the appropriate price will be returned.
	 * 
	 * @param pt
	 *            Name of database to get fields for.
	 * @return Hashtable of price descriptive name-column name pairs.
	 **************************************************************************/
	public String[] getPriceKeys(String pt) {
		String keyTable = pt + priceKeyTable;
		String[] tables = { keyTable };
		String[] cols = { priceKeyColumn };
		Data data = database.select(tables, cols, null);
		return data.getColumn(priceKeyColumn);
	}

	/***************************************************************************
	 * Gets a Map of all the price breaks for a given object.
	 * 
	 * @param pt
	 *            Product Table.
	 * @param productID
	 *            ID # of product.
	 * @return Map object containing all price breaks for the given product.
	 **************************************************************************/
	public Map getPriceBreaks(String pt, int productID) {
		Map retVal = new HashMap();
		Map temp;
		String pKey;
		Integer bp;
		Float price;
		String keyTable = pt + priceKeyTable;
		String breakTable = pt + priceBreakTable;
		String[] tables = { keyTable, breakTable };
		String[] cols = { priceKeyColumn, breakPointColumn, priceColumn };
		String where = database.where(keyTable + "." + priceKeyIDColumn,
				breakTable + "." + priceKeyIDColumn, DBConnect.EQ,
				DBConnect.COLUMN)
				+ " AND "
				+ database.where(productIDColumn, productID, DBConnect.EQ,
						DBConnect.NUMBER);
		Data data = database.select(tables, cols, where);
		data.reset();
		while (data.next()) {
			pKey = (String) data.getColumnValue(priceKeyColumn);
			bp = (Integer) data.getColumnValue(breakPointColumn);
			price = (Float) data.getColumnValue(priceColumn);
			if (retVal.containsKey(pKey)) {
				temp = (Map) retVal.get(pKey);
				temp.put(bp, price);
			} else {
				temp = new HashMap();
				temp.put(bp, price);
				retVal.put(pKey, temp);
			}
		}
		return retVal;
	}

	/***************************************************************************
	 * Gets the group names that a given user belongs to.
	 * 
	 * @param user_id
	 *            ID number of user to check.
	 * @return Array of group names.
	 **************************************************************************/
	String[] getUserGroup(int user_id) {
		String[] groups;
		String[] tables = { userGroupTable, groupTable };
		String[] cols = { nameColumn };
		String where = database.where(userGroupTable + "." + groupIDColumn,
				groupTable + "." + groupIDColumn, DBConnect.EQ,
				DBConnect.COLUMN)
				+ " AND "
				+ database.where(userIDColumn, user_id, DBConnect.EQ,
						DBConnect.NUMBER);
		Data data = database.select(tables, cols, where);
		groups = data.getColumn(nameColumn);
		if (groups.length == 0) {
			groups = new String[1];
			groups[0] = PUBLIC;
		}
		return groups;
	} // End Method

	/***************************************************************************
	 * Gets the merchant object associated with a given product family.
	 * 
	 * @param name
	 *            Descriptive name of merchant.
	 * @return Merchant object.
	 **************************************************************************/
	public Merchant getMerchant(String name) {
		return objectCache.getCache(Merchant.class).getCachedObject("name",
				name);
	}

	/***************************************************************************
	 * Gets the merchant object associated with a given product family.
	 * 
	 * @param pf
	 *            Product to find merchant for.
	 * @return Merchant object.
	 **************************************************************************/
	public Merchant getMerchant(ProductFamily pf) {
		return objectCache.getCache(Merchant.class).getCachedObject(
				"productFamily", pf);
	}

	/***************************************************************************
	 * Gets the states the merchant must charge tax on if the user is ordering
	 * from that state.
	 * 
	 * @param merchant
	 *            Merchant object.
	 **************************************************************************/
	public void getTaxedStates(Merchant merchant) {
		String[] tables = { merchantTaxedStatesTable, stateTable };
		String[] cols = { nameColumn };
		String where = database.where(merchantIDColumn, merchant
				.getMerchantID(), DBConnect.EQ, DBConnect.IS);
		Data data = database.select(tables, cols, where);
		data.reset();
		while (data.next())
			merchant.addTaxedState((String) data.getColumnValue(cols[0]));
	}

	private int getCreditCardID(String number) {
		String[] tables = { creditCardTable };
		String[] cols = { ccIDColumn };
		String where = database.where(ccNumberColumn, database
				.encryptString(number), DBConnect.EQ, DBConnect.STRING);
		Data data = database.select(tables, cols, where);
		data.reset();
		if (data.next())
			return ((Integer) data.getColumnValue(cols[0])).intValue();
		else
			return -1;
	}

	int addCreditCard(CreditCard cc) {
		int ccID = getCreditCardID(cc.getNumber());
		if (ccID != -1)
			return ccID;
		else {
			String[] cols = { ccTypeColumn, ccNumberColumn, expDateColumn,
					firstNameColumn, lastNameColumn, middleInitialColumn };
			String[] vals = {
					"" + cc.getType(),
					database
							.cleanString(database.encryptString(cc.getNumber())),
					database.cleanString(database.getDate(cc.getExpDate())),
					database.cleanString(cc.getFirstName()),
					database.cleanString(cc.getLastName()),
					database.cleanString(cc.getMiddleInitial()) };
			database.insert(creditCardTable, cols, vals);
		}
		return getCreditCardID(cc.getNumber());
	}

	public void getProductData(String pt, Product product) {
		if (product == null)
			return;
		String fieldT = pt + fieldTable;
		String keyT = pt + keywordTable;
		String descKeyT = pt + descriptionKeyTable;
		String catLinkT = pt + categoryTable;
		String statT = pt + statTable;
		String descriptionT = pt + descriptionTable;
		String[] catTables = { catLinkT, fieldT };
		String[] descTables = { descriptionT, fieldT, descKeyT };
		String[] statTables = { statT, fieldT };
		String[] cols = { nameColumn, keywordColumn };
		String[] catCols = { nameColumn };
		// Columns for stored procedure
		String[] prodValueCols = { nameColumn, productValueColumn };

		// Trying to use stored procedure
		String where1 = database.where(fieldT + "." + fieldIDColumn, catLinkT
				+ "." + fieldIDColumn, DBConnect.EQ, DBConnect.COLUMN)
				+ " AND ";

		// Getting category data
		String where2 = database.where(productIDColumn, product.getId(),
				DBConnect.EQ, DBConnect.NUMBER);

		Data data = database.select(catTables, catCols, where1 + where2);
		data.reset();
		String cTemp = "";
		while (data.next()) {
			cTemp = (String) data.getColumnValue(catCols[0]);
			log.debug("getting product values for category: " + cTemp);
			product.setValue(cTemp, Functions.unsplit(getCategoryValues(pt,
					product.getId(), cTemp), ", "));
		}

		// getting description data
		cols[1] = descriptionColumn;
		where1 = database.where(fieldT + "." + fieldIDColumn, descriptionT
				+ "." + fieldIDColumn, DBConnect.EQ, DBConnect.COLUMN)
				+ " AND "
				+ database.where(descKeyT + "." + descriptionIDColumn,
						descriptionT + "." + descriptionIDColumn, DBConnect.EQ,
						DBConnect.COLUMN) + " AND ";
		data = database.select(descTables, cols, where1 + where2);
		data.reset();
		while (data.next()) {
			product.setValue((String) data.getColumnValue(cols[0]), data
					.getColumnValue(cols[1]));
		}
		// getting statistical (numerical) data
		cols[1] = valueColumn;
		where1 = database.where(fieldT + "." + fieldIDColumn, statT + "."
				+ fieldIDColumn, DBConnect.EQ, DBConnect.COLUMN)
				+ " AND ";
		data = database.select(statTables, cols, where1 + where2);
		data.reset();
		while (data.next())
			product.setValue((String) data.getColumnValue(cols[0]), data
					.getColumnValue(cols[1]));
	}

	public void saveUserProperties(User user) {
		int userID = user.getUserID();
		int attID, valueID;
		String[] cols, vals;
		String table1 = userLongDataTable;
		String where = database.where(userIDColumn, "" + userID, DBConnect.EQ,
				DBConnect.NUMBER);
		database.delete(table1, where);
		String table2 = userDataTable;
		database.delete(table2, where);
		String[] props = UserProperties.longProps;
		for (int x = 0; x < props.length; x++) {
			attID = getUserAttributeID(props[x], UserProperties.LONG);
			valueID = getUserLongValueID(user.getProperty(props[x]));
			cols = new String[] { attributeIDColumn, userIDColumn,
					longValueIDColumn };
			vals = new String[] { "" + attID, "" + userID, "" + valueID };
			database.insert(table1, cols, vals);
		}
		props = UserProperties.shortProps;
		for (int x = 0; x < props.length; x++) {
			attID = getUserAttributeID(props[x], UserProperties.SHORT);
			valueID = getUserValueID(user.getProperty(props[x]));
			cols = new String[] { attributeIDColumn, userIDColumn,
					valueIDColumn };
			vals = new String[] { "" + attID, "" + userID, "" + valueID };
			database.insert(table1, cols, vals);
		}
	}

	void clearUserProperties(User user) {
		int userID = user.getUserID();
		String[] cols, vals;
		String table1 = userLongDataTable;
		String where = database.where(userIDColumn, "" + userID, DBConnect.EQ,
				DBConnect.NUMBER);
		database.delete(table1, where);
		String table2 = userDataTable;
		database.delete(table2, where);
	}

	int getUserValueID(String value) {
		int retVal = -1;
		String[] tables = { userValueTable };
		String[] cols = { valueIDColumn };
		String where = database.where(valueColumn, value, DBConnect.EQ,
				DBConnect.IS);
		Data data = database.select(tables, cols, where);
		if (data.size() > 0) {
			data.reset();
			data.next();
			retVal = ((Integer) data.getColumnValue(cols[0])).intValue();
		} else {
			String[] columns = new String[] { valueColumn };
			String[] vals = { database.cleanString(value) };
			database.insert(userValueTable, columns, vals);
			data = database.select(tables, cols, where);
			if (data.size() > 0) {
				data.reset();
				data.next();
				retVal = ((Integer) data.getColumnValue(cols[0])).intValue();
			}
		}
		return retVal;
	}

	int getUserLongValueID(String value) {
		int retVal = -1;
		String[] tables = { userLongValueTable };
		String[] cols = { longValueIDColumn };
		String where = database.where(longValueColumn, value, DBConnect.EQ,
				DBConnect.IS);
		Data data = database.select(tables, cols, where);
		if (data.size() > 0) {
			data.reset();
			data.next();
			retVal = ((Integer) data.getColumnValue(cols[0])).intValue();
		} else {
			String[] columns = new String[] { longValueColumn };
			String[] vals = { database.cleanString(value) };
			database.insert(userLongValueTable, columns, vals);
			data = database.select(tables, cols, where);
			if (data.size() > 0) {
				data.reset();
				data.next();
				retVal = ((Integer) data.getColumnValue(cols[0])).intValue();
			}
		}
		return retVal;
	}

	int getUserAttributeID(String att, int type) {
		int retVal = -1;
		String[] tables = { userAttributeTable };
		String[] cols = { attributeIDColumn };
		String where = database.where(nameColumn, att, DBConnect.EQ,
				DBConnect.IS)
				+ " AND "
				+ database.where(attributeTypeColumn, type, DBConnect.EQ,
						DBConnect.NUMBER);
		Data data = database.select(tables, cols, where);
		if (data.size() > 0) {
			data.reset();
			data.next();
			retVal = ((Integer) data.getColumnValue(cols[0])).intValue();
		} else {
			String[] columns = new String[] { nameColumn, attributeTypeColumn };
			String[] vals = { database.cleanString(att), "" + type };
			database.insert(userAttributeTable, columns, vals);
			data = database.select(tables, cols, where);
			if (data.size() > 0) {
				data.reset();
				data.next();
				retVal = ((Integer) data.getColumnValue(cols[0])).intValue();
			}
		}
		return retVal;
	}

	public Collection getCities() {
		String[] tables = { cityTable };
		String[] cols = { nameColumn };
		return database.select(tables, cols, null).getColumnAsList(nameColumn);
	}

	public Collection<Resource> getResources() {
		return resService.getResources();
	}

	/***************************************************************************
	 * Adds the information for a new product table to the this.database.
	 * 
	 * @param pt
	 *            Name of product table.
	 * @param desName
	 *            Descriptive name for product table.
	 * @param productType
	 *            Type of product.
	 * @param description
	 *            Description of Product table.
	 * @param primaryLabel
	 *            The Label for the primary column for the product table.
	 **************************************************************************/
	public void addProductTable(String pt, String desName, int productType,
			String description, String primaryLabel, String orderClass) {
		String table = productTable;
		String[] cols = { tableNameColumn, descriptiveNameColumn,
				productTypeColumn, descriptionColumn, primaryLabelColumn,
				modelClassColumn };
		String[] vals = { database.cleanString(pt),
				database.cleanString(desName), "" + productType,
				database.cleanString(description),
				database.cleanString(primaryLabel),
				database.cleanString(orderClass) };
		String where = database.where(tableNameColumn, pt, DBConnect.EQ,
				DBConnect.IS);
		String[] tables = { productTable };
		String[] columns = { productTableIDColumn };
		Data data = database.select(tables, columns, where);
		if (data.size() > 0) {
			if (desName != null && description != null && primaryLabel != null
					&& orderClass != null)
				database.update(table, cols, vals, where);
		} else {
			database.insert(table, cols, vals);
			objectCache.getCache(Resource.class).addItem(
					new Resource((int) getNewId("resources"), pt,
							Resource.DATATABLE));
		}
	}

	public void addResource(String name, int type) {
		objectCache.getCache(Resource.class).addItem(
				new Resource((int) getNewId("resources"), name, type));
	}

	/***************************************************************************
	 * Sets the basic information for a Product object.
	 * 
	 * @param pt
	 *            Product Table.
	 * @param product
	 *            Product object to set values for.
	 * @return ID # of product.
	 **************************************************************************/
	private int setProductBasics(String pt, Product product) {
		int retVal = 0;
		String primary = product.getPrimary();
		String[] table = { pt };
		String[] cols = { primaryLabelColumn, dateCatalogedColumn,
				inventoryColumn };
		String[] columns = { productIDColumn };
		String[] vals = { database.cleanString(primary),
				database.cleanString(database.getDate()),
				"" + product.getInventory() };
		String where = database.where(pt + "." + primaryLabelColumn, primary,
				DBConnect.EQ, DBConnect.IS);
		Data data = database.select(table, columns, where);
		if (data.size() == 0) {
			database.insert(table[0], cols, vals);
			data = database.select(table, columns, where);
			data.reset();
			if (data.next())
				retVal = ((Integer) data.getColumnValue(productIDColumn))
						.intValue();
			else
				retVal = 0;
		} else {
			database.update(table[0], cols, vals, where);
			data.reset();
			if (data.next())
				retVal = ((Integer) data.getColumnValue(productIDColumn))
						.intValue();
			else
				retVal = 0;
		}
		return retVal;
	}

	public String[] getRights() {
		String[] tables = { rightsTable };
		String[] cols = { nameColumn };
		return database.select(tables, cols, null).getColumn(nameColumn);
	}

	public Collection getReferrers() {
		String[] tables = { referrerTable };
		String[] cols = { nameColumn };
		return database.select(tables, cols, null).getColumnAsList(nameColumn);
	}

	public Collection getStates() {
		String[] tables = { stateTable };
		String[] cols = { nameColumn };
		return database.select(tables, cols, null).getColumnAsList(nameColumn);
	}

	public Collection getIndustries() {
		String[] tables = { industryTable };
		String[] cols = { nameColumn };
		return database.select(tables, cols, null).getColumnAsList(nameColumn);
	}

	public Collection getCountries() {
		String[] tables = { countryTable };
		String[] cols = { nameColumn };
		return database.select(tables, cols, null).getColumnAsList(nameColumn);
	}

	/***************************************************************************
	 * Sets the price keys for a product family.
	 * 
	 * @param pt
	 *            Product table.
	 * @param pKeys
	 *            Array of price keys.
	 **************************************************************************/
	public void setPriceKeys(String pt, String[] pKeys) {
		int count = -1;
		String key;
		while (++count < pKeys.length) {
			setPriceKey(pt, pKeys[count], pKeys[count]);
		}
	}

	public void removeDescriptionValue(String pt, int productID,
			String fieldName, String value) {
		int descriptionID = getDescription(pt, value);
		int fieldID = getFieldID(pt, fieldName);
		String linkTable = pt + descriptionTable;
		String table = linkTable;
		String where = database.where(productIDColumn, productID, DBConnect.EQ,
				DBConnect.NUMBER)
				+ " AND "
				+ database.where(fieldIDColumn, fieldID, DBConnect.EQ,
						DBConnect.NUMBER)
				+ " AND "
				+ database.where(descriptionIDColumn, descriptionID,
						DBConnect.EQ, DBConnect.NUMBER);
		database.delete(table, where);
	}

	public void removeNumericalValue(String pt, int productID,
			String fieldName, String value) {

		int fieldID = getFieldID(pt, fieldName);
		String linkTable = pt + statTable;
		String table = linkTable;
		String where = database.where(productIDColumn, productID, DBConnect.EQ,
				DBConnect.NUMBER)
				+ " AND "
				+ database.where(fieldIDColumn, fieldID, DBConnect.EQ,
						DBConnect.NUMBER)
				+ " AND "
				+ database.where(valueColumn, value, DBConnect.EQ,
						DBConnect.NUMBER);
		database.delete(table, where);
	}

	/***************************************************************************
	 * Sets a price for a product given a price key.
	 * 
	 * @param pt
	 *            Product Table.
	 * @param productID
	 *            Product ID #.
	 * @param priceKey.
	 * @param price
	 *            Price for the product.
	 **************************************************************************/
	public void setPrice(String pt, int productID, String priceKey, float price) {
		int priceKeyID = getPriceKeyID(pt, priceKey);
		String prTable = pt + priceTable;
		String[] tables = { prTable };
		String[] cols = { productIDColumn, priceKeyIDColumn, priceColumn };
		String[] vals = { "" + productID, "" + priceKeyID,
				"" + Functions.round(price, 2) };
		String where = database.where(prTable + "." + priceKeyIDColumn,
				priceKeyID, DBConnect.EQ, DBConnect.NUMBER)
				+ " AND "
				+ database.where(productIDColumn, productID, DBConnect.EQ,
						DBConnect.NUMBER);
		Data data = database.select(tables, cols, where);
		if (data.size() == 0)
			database.insert(tables[0], cols, vals);
		else
			database.update(tables[0], cols, vals, where);
	}

	/***************************************************************************
	 * Sets a price for a product given a price key.
	 * 
	 * @param pt
	 *            Product Table.
	 * @param primary
	 *            Product primary label value.
	 * @param priceKey.
	 * @param price
	 *            Price for the product.
	 **************************************************************************/
	public void setPrice(String pt, String primary, String priceKey, float price) {
		int pID = getProductID(pt, primary);
		setPrice(pt, pID, priceKey, price);
	}

	/***************************************************************************
	 * Sets a price Key for a given product table.
	 * 
	 * @param pt
	 *            Product Table.
	 * @param oldkey
	 *            Old Price Key.
	 * @param newKey
	 *            New Price Key.
	 **************************************************************************/
	public void setPriceKey(String pt, String oldKey, String newKey) {
		String[] tables = { pt + priceKeyTable };
		String[] cols = { priceKeyColumn };
		String[] vals = { database.cleanString(newKey) };
		String where = database.where(priceKeyColumn, oldKey, DBConnect.EQ,
				DBConnect.IS);
		Data data = database.select(tables, cols, where);
		if (data.size() == 0)
			database.insert(tables[0], cols, vals);
		else
			database.update(tables[0], cols, vals, where);
	}

	/***************************************************************************
	 * Sets a price Key for a given product table.
	 * 
	 * @param ptID
	 *            Product Table ID #.
	 * @param oldkey
	 *            Old Price Key.
	 * @param newKey
	 *            New Price Key.
	 **************************************************************************/
	public void setPriceKey(int ptID, String oldKey, String newKey) {
		setPriceKey(getProductTable(ptID), oldKey, newKey);
	}

	/***************************************************************************
	 * Gets the value of a numerical field for a certain product. Returns the
	 * result as a Double.
	 * 
	 * @param pt
	 *            Name of product table involved.
	 * @param productID
	 *            ID of product to get values for.
	 * @param fieldName
	 *            Name of field to get for product.
	 * @return Array of values for the field.
	 **************************************************************************/
	Float getNumericalValue(String pt, int productID, String fieldName) {
		Float value;
		String linkTable = pt + statTable;
		String fldTable = pt + fieldTable;
		String[] tables = { linkTable, fldTable };
		String[] cols = { valueColumn };
		String where = database.where(fldTable + "." + fieldIDColumn, linkTable
				+ "." + fieldIDColumn, DBConnect.EQ, DBConnect.COLUMN)
				+ " AND "
				+ database.where(fieldNameColumn, fieldName, DBConnect.EQ,
						DBConnect.IS)
				+ " AND "
				+ productIDColumn
				+ " = "
				+ productID;
		Data data = database.select(tables, cols, where);
		data.reset();
		if (data.next())
			value = (Float) data.getColumnValue(valueColumn);
		else
			value = null;
		return value;
	}

	/***************************************************************************
	 * Gets the string name of a field from the field table for a given product
	 * line.
	 * 
	 * @param fieldID
	 *            the ID # of the field to retrieve.
	 * @return Name of field.
	 **************************************************************************/
	public int getFieldID(String pt, String fieldName) {
		int retVal;
		String[] tables = { pt + fieldTable };
		String[] cols = { fieldIDColumn };
		String where = database.where(fieldNameColumn, fieldName, DBConnect.EQ,
				DBConnect.IS);
		Data data = database.select(tables, cols, where);
		data.reset();
		if (data.next())
			retVal = ((Integer) data.getColumnValue(fieldIDColumn)).intValue();
		else
			retVal = -1;
		return retVal;
	}

	/***************************************************************************
	 * Adds a description to the database.
	 * 
	 * @param pt
	 *            Product Table name.
	 * @param description
	 *            Description.
	 * @return Description ID.
	 **************************************************************************/
	public int addDescription(String pt, String desc) {
		int descID;
		String[] tables = { pt + descriptionKeyTable };
		String[] cols = { descriptionIDColumn };
		String where = database.where(descriptionColumn, desc, DBConnect.EQ,
				DBConnect.IS);
		Data data = database.select(tables, cols, where);
		data.reset();
		if (data.next())
			descID = ((Integer) data.getColumnValue(descriptionIDColumn))
					.intValue();
		else {
			String table = tables[0];
			String[] columns = { descriptionColumn };
			String[] vals = { database.cleanString(desc) };
			database.insert(table, columns, vals);
			data = database.select(tables, cols, where);
			data.reset();
			if (data.next())
				descID = ((Integer) data.getColumnValue(descriptionIDColumn))
						.intValue();
			else
				descID = -1;
		}
		return descID;
	}

	/***************************************************************************
	 * Adds an industry to the database.
	 * 
	 * @param industry
	 *            Keyword.
	 * @return industry ID.
	 **************************************************************************/
	public int addIndustry(String industry) {
		if (null == industry || industry.equals("")) {
			industry = DEFAULT;
		}
		int industryID;
		String[] tables = { industryTable };
		String[] cols = { industryIDColumn };
		String where = database.where(nameColumn, industry, DBConnect.EQ,
				DBConnect.IS);
		Data data = database.select(tables, cols, where);
		data.reset();
		if (data.next())
			industryID = ((Integer) data.getColumnValue(industryIDColumn))
					.intValue();
		else {
			String table = tables[0];
			String[] columns = { nameColumn };
			String[] vals = { database.cleanString(industry) };
			database.insert(table, columns, vals);
			data = database.select(tables, cols, where);
			data.reset();
			if (data.next())
				industryID = ((Integer) data.getColumnValue(industryIDColumn))
						.intValue();
			else
				industryID = -1;
		}
		return industryID;
	}

	/***************************************************************************
	 * Adds a referrer to the database.
	 * 
	 * @param referrer
	 *            Referrer.
	 * @return referrer ID.
	 **************************************************************************/
	public int addReferrer(String referrer) {
		if (null == referrer || referrer.equals("")) {
			referrer = DEFAULT;
		}
		int referrerID;
		String[] tables = { referrerTable };
		String[] cols = { referrerIDColumn };
		String where = database.where(nameColumn, referrer, DBConnect.EQ,
				DBConnect.IS);
		Data data = database.select(tables, cols, where);
		data.reset();
		if (data.next())
			referrerID = ((Integer) data.getColumnValue(referrerIDColumn))
					.intValue();
		else {
			String table = tables[0];
			String[] columns = { nameColumn };
			String[] vals = { database.cleanString(referrer) };
			database.insert(table, columns, vals);
			data = database.select(tables, cols, where);
			data.reset();
			if (data.next())
				referrerID = ((Integer) data.getColumnValue(referrerIDColumn))
						.intValue();
			else
				referrerID = -1;
		}
		return referrerID;
	}

	/***************************************************************************
	 * Gets the keyword given the keyword's ID number, and the name of the
	 * product keyword table it appears in.
	 * 
	 * @param pt
	 *            Product Table name.
	 * @param keywordID
	 *            ID # of keyword.
	 * @return Keyword.
	 **************************************************************************/
	public String getKeyword(String pt, int keywordID) {
		String keyword;
		String[] tables = { pt + keywordTable };
		String[] cols = { keywordColumn };
		String where = keywordIDColumn + " = " + keywordID;
		Data data = this.database.select(tables, cols, where);
		data.reset();
		if (data.next())
			keyword = (String) data.getColumnValue(keywordColumn);
		else
			keyword = "N/A";
		return keyword;
	}

	/***************************************************************************
	 * Gets the description ID given the description's value, and the name of
	 * the product description table it appears in.
	 * 
	 * @param pt
	 *            Product Table name.
	 * @param description
	 *            Description.
	 * @return description ID.
	 **************************************************************************/
	public int getDescription(String pt, String description) {
		int descriptionID;
		String[] tables = { pt + descriptionTable };
		String[] cols = { descriptionIDColumn };
		String where = database.where(descriptionColumn, description,
				DBConnect.EQ, DBConnect.ISCASEINSENSITIVE);
		Data data = database.select(tables, cols, where);
		data.reset();
		if (data.next())
			descriptionID = ((Integer) data.getColumnValue(descriptionIDColumn))
					.intValue();
		else
			descriptionID = addDescription(pt, "N/A");
		return descriptionID;
	}

	// Table Names
	public static final String priceBreakTable = "price_break";

	public static final String productTable = "product_tables";

	public static final String priceKeyTable = "price_keys";

	public static final String descriptionTable = "description";

	public static final String categoryTable = "category";

	public static final String statTable = "stats";

	public static final String keywordTable = "_key";

	public static final String priceTable = "price";

	public static final String fieldTable = "fields";

	public static final String referrerTable = "referrers";

	public static final String companyTable = "companies";

	public static final String industryTable = "industries";

	public static final String groupTable = "groups";

	public static final String userTable = "users";

	public static final String stateTable = "states";

	public static final String cityTable = "cities";

	public static final String countryTable = "countries";

	public static final String merchantTable = "merchants";

	public static final String merchantProductTable = "merchant_product_tables";

	public static final String userGroupTable = "user_group";

	public static final String transactionTable = "transactions";

	public static final String securityTable = "security";

	public static final String resourcesTable = "resources";

	public static final String rightsTable = "rights";

	public static final String merchantTaxedStatesTable = "merchant_tax_states";

	public static final String creditCardTable = "credit_cards";

	public static final String descriptionKeyTable = "_descript";

	public static final String orderTable = "orders";

	public static final String userAttributeTable = "user_att";

	public static final String userValueTable = "user_values";

	public static final String userLongValueTable = "user_long_values";

	public static final String userDataTable = "user_data";

	public static final String userLongDataTable = "user_long_data";

	// Column Names
	public static final String attributeIDColumn = "att_id";

	public static final String valueIDColumn = "value_id";

	public static final String longValueIDColumn = "long_value_id";

	public static final String longValueColumn = "long_value";

	public static final String attributeTypeColumn = "att_type";

	public static final String expirationDateColumn = "exp_date";

	public static final String quantityColumn = "quantity";

	public static final String descriptionIDColumn = "description_id";

	public static final String valueColumn = "val_col";

	public static final String ccIDColumn = "credit_id";

	public static final String ccTypeColumn = "cc_type";

	public static final String ccNumberColumn = "cc_number";

	public static final String expDateColumn = "exp_date";

	public static final String taxRateColumn = "sales_tax";

	public static final String fieldIDColumn = "field_id";

	public static final String fieldNameColumn = "name";

	public static final String fieldTypeColumn = "field_type";

	public static final String searchOrderColumn = "search_order";

	public static final String displayOrderColumn = "display_order";

	public static final String productIDColumn = "product_id";

	public static final String primaryLabelColumn = "primary_label";

	public static final String nameColumn = "name";

	public static final String priceColumn = "price";

	public static final String keywordIDColumn = "keyword_id";

	public static final String keywordColumn = "keyword";

	public static final String productTableIDColumn = "product_table_id";

	public static final String breakPointColumn = "break_point";

	public static final String tableNameColumn = "table_name";

	public static final String descriptiveNameColumn = "descriptive_name";

	public static final String priceKeyIDColumn = "price_key_id";

	public static final String priceKeyColumn = "price_key";

	public static final String descriptionColumn = "description";

	public static final String inventoryColumn = "inventory";

	public static final String dateCatalogedColumn = "date_cataloged";

	public static final String productTypeColumn = "product_type";

	// public static final String typeColumn = "type";
	public static final String merchantAdministrationTable = "merchant_admin";

	public static final String groupAdministrationTable = "group_admin";

	public static final String groupAdministerIDColumn = "administers_id";

	public static final String browseRightsColumn = "browse_rights";

	public static final String adminRightsColumn = "admin_rights";

	public static final String orderRightsColumn = "order_rights";

	public static final String downloadRightsColumn = "download_rights";

	public static final String companyIDColumn = "company_id";

	public static final String industryIDColumn = "industry_id";

	public static final String referrerIDColumn = "referrer_id";

	public static final String groupIDColumn = "group_id";

	public static final String firstNameColumn = "first_name";

	public static final String lastNameColumn = "last_name";

	public static final String middleInitialColumn = "middle_initial";

	public static final String usernameColumn = "username";

	public static final String passwordColumn = "passwd";

	public static final String emailColumn = "email";

	public static final String billPhoneColumn = "bill_phone";

	public static final String billAddress1Column = "bill_address1";

	public static final String billAddress2Column = "bill_address2";

	public static final String billCityIDColumn = "bill_city_id";

	public static final String billStateIDColumn = "bill_state_id";

	public static final String billZipColumn = "bill_zip";

	public static final String billCountryIDColumn = "bill_country_id";

	public static final String faxColumn = "fax";

	public static final String shipPhoneColumn = "ship_phone";

	public static final String shipAddress1Column = "ship_address1";

	public static final String shipAddress2Column = "ship_address2";

	public static final String shipCityIDColumn = "ship_city_id";

	public static final String shipStateIDColumn = "ship_state_id";

	public static final String shipZipColumn = "ship_zip";

	public static final String shipCountryIDColumn = "ship_country_id";

	public static final String userIDColumn = "user_id";

	public static final String stateIDColumn = "state_id";

	public static final String stateAbbrColumn = "abbr";

	public static final String cityIDColumn = "city_id";

	public static final String countryIDColumn = "country_id";

	public static final String countryCodeColumn = "country_code";

	public static final String orderingEmailColumn = "ordering_email";

	public static final String phoneColumn = "phone";

	public static final String address1Column = "address1";

	public static final String address2Column = "address2";

	public static final String creditCardsAcceptedColumn = "credit_cards";

	public static final String fulfillmentEmailColumn = "fulfillment_email";

	public static final String orderProcessingColumn = "order_processing";

	public static final String zipColumn = "zip";

	public static final String merchantModelColumn = "order_model";

	public static final String merchantIDColumn = "merchant_id";

	public static final String modelClassColumn = "order_model";

	public static final String resourceIDColumn = "resource_id";

	public static final String resourceTypeColumn = "resource_type";

	public static final String rightIDColumn = "right_id";

	public static final String transactionIDColumn = "transaction_id";

	public static final String dateEnteredColumn = "date_entered";

	public static final String dateFulfilledColumn = "date_fulfilled";

	public static final String authCodeColumn = "auth_code";

	public static final String billCodeColumn = "bill_code";

	public static final String referenceNumColumn = "reference_num";

	public static final String requestNumColumn = "request_num";

	public static final String authMsgColumn = "auth_msg";

	public static final String billMsgColumn = "bill_msg";

	public static final String authAmountColumn = "auth_amount";

	public static final String billAmountColumn = "bill_amount";

	public static final String timeBilledColumn = "time_billed";

	public static final String currencyColumn = "currency";

	// Stored Procedures
	final static String catValuesSP = "_cat_values";

	final static String productValuesSP = "_prod_values";

	final static String productValueColumn = "prod_value";

	// Other database values
	public static final String DATABASE = "commerce_database";

	public static final String ADD_USER_RIGHT = "add_user";

	public static final String DEFAULT = "N/A";

	public static final String PUBLIC = "public";
}
