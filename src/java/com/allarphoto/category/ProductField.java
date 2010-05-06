/***********************************************************************************************************************
 * Copyright 1999 (c) Lazer Incorporated All code contained in this file is the property of Lazer Incorporated. All
 * rights reserved.
 **********************************************************************************************************************/

package com.lazerinc.category;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import strategiclibrary.service.BatchUpdate;
import strategiclibrary.service.DataBase;
import strategiclibrary.service.sql.ObjectMappingService;

import com.lazerinc.cached.DatabaseObject;
import com.lazerinc.ecommerce.CommerceProduct;

public abstract class ProductField implements Comparable, Serializable,
		DatabaseObject {
	private static final long serialVersionUID = 1;

	public static final String keywordTable = "_key";

	public static final String categoryTable = "category";

	public static final String descriptionTable = "description";

	public static final String statTable = "stats";

	public static final String descriptionKeyTable = "_descript";

	String family;

	protected ProductField(String family, String n, int d, int t) {
		this.family = family;
		name = n;
		displayOrder = d;
		type = t;
	}

	protected ProductField(String family, String n, int d, int s, int t) {
		this(family, n, d, t);
		searchOrder = s;
	}

	public ProductField() {

	}

	public static ProductField createField(String family, String name,
			int type, int d, int so) {
		switch (type) {
		case CATEGORY:
			return new CategoryField(family, name, d, so);
		case TAG:
			return new TagField(family, name, d, so);
		case DESCRIPTION:
			return new DescriptionField(family, name, d, so);
		case NUMERICAL:
			return new NumericalField(family, name, d, so);
		case PROTECTED:
			return new ProtectedField(family, name, d, so);
		case PRIMARY:
			return new PrimaryField(family, name, d, so);
		}
		return null;
	}

	void readObject(ObjectInputStream ois) throws ClassNotFoundException,
			IOException {
		ois.defaultReadObject();
	}

	void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
	}

	public void setName(String newName) {
		name = newName;
	}

	public String getName() {
		return name;
	}

	public void setSearchOrder(int newSearchOrder) {
		searchOrder = newSearchOrder;
	}

	public int getSearchOrder() {
		return searchOrder;
	}

	public void setType(int newType) {
		type = newType;
	}

	public int getType() {
		return type;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ProductField))
			return false;
		final ProductField other = (ProductField) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public void setDisplayOrder(int newDisplayOrder) {
		displayOrder = newDisplayOrder;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	/***************************************************************************
	 * Method to satisfy Comparator interface requirement. Used for sorting in
	 * sorted collections.
	 * 
	 * @param o1
	 *            First object to be compared.
	 * @param o2
	 *            Second object to be compared.
	 * @return -1 if o1<o2, 0 if o1=o2, 1 if o2>o1
	 **************************************************************************/
	public int compareTo(Object o2) throws ClassCastException {
		int f2 = ((ProductField) o2).getDisplayOrder() % 1000;
		int f1 = getDisplayOrder() % 1000;
		int ret;
		if (f1 < f2)
			ret = -1;
		else if (f1 == f2) {
			String s2 = ((ProductField) o2).getName();
			return name.compareTo(s2);
		} else
			ret = 1;
		return ret;
	}

	/***************************************************************************
	 * Gets the keyword given the keyword's ID number, and the name of the
	 * product keyword table it appears in.
	 * 
	 * @param pt
	 *            Product Table name.
	 * @param keyword
	 *            Keyword.
	 * @return keyword ID.
	 **************************************************************************/
	public abstract void addValue(CommerceProduct prod, Object keyword,
			BatchUpdate batch, ObjectMappingService mapper, DataBase db)
			throws Exception;

	protected void insertKeyValue(CommerceProduct prod, BatchUpdate batch,
			ObjectMappingService mapper, DataBase db, Map values)
			throws Exception {
		int keyId = -1;
		if (values.get("keyTable") != null) {
			Collection<Long> id = (Collection<Long>) mapper.getObjects(
					"getKeyword.sql", values);
			if (id != null && id.size() == 1)
				values.put("keyId", id.iterator().next().intValue());
			else {
				keyId = getId(prod.getProductFamilyName()
						+ values.get("keyTable"), mapper);
				values.put("keyId", keyId);
				db.executeTemplateUpdate("addKeyword.sql", db
						.getTemplateContext(values));
			}
		} else
			values.put("keyId", values.get("keyword"));
		if (batch != null)
			batch.addUpdate("insertFieldValue.sql", values);
		else
			db.executeTemplateUpdate("insertFieldValue.sql", db
					.getTemplateContext(values));
	}

	protected int getId(String tablename, ObjectMappingService mapper) {
		Map values = new HashMap();
		values.put("table", tablename);
		return ((Number) mapper.getObjects("getNewId.sql", values).iterator()
				.next()).intValue();
	}

	/***************************************************************************
	 * Implements the toString() method for ProductField objects.
	 * 
	 * @return String representation of the ProductField object.
	 **************************************************************************/
	public String toString() {
		String n = System.getProperty("line.separator");
		StringBuffer st = new StringBuffer(super.toString() + "{" + n);
		st.append("Name=" + name + n);
		st.append("search index=" + searchOrder + n);
		st.append("Order index=" + displayOrder + n);
		switch (type) {
		case CATEGORY:
			st.append("Type=category" + n);
			break;
		case DESCRIPTION:
			st.append("Type=description" + n);
			break;
		case PRICE:
			st.append("Type=price" + n);
			break;
		case PRIMARY:
			st.append("Type=primary" + n);
			break;
		case INVENTORY:
			st.append("Type=inventory" + n);
			break;
		case NUMERICAL:
			st.append("Type=numerical" + n);
			break;
		}
		st.append(n + "}");
		return st.toString();
	}

	public void setFieldID(int newFieldID) {
		fieldID = newFieldID;
	}

	public int getFieldID() {
		return fieldID;
	}

	public int getId() {
		return fieldID;
	}

	public void setId(int i) {
		fieldID = i;
	}

	private String name;

	private int searchOrder;

	private int type;

	private int displayOrder;

	public static final int CATEGORY = 1;

	public static final int DESCRIPTION = 2;

	public static final int PRICE = 4;

	public static final int PRIMARY = 6;

	public static final int DATE_FIELD = 18;

	public static final int MOD_DATE_FIELD = 19;

	public static final int INVENTORY = 7;

	public static final int NUMERICAL = 8;

	public static final int PATH = 10;

	public static final int PROTECTED = -1;

	public static final int TAG = 11;

	public static final String EXPIRED = "expired_category";

	public static final int EXPIRED_TYPE = 9;

	private int fieldID;

	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}
}
