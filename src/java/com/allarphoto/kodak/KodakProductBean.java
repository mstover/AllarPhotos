package com.allarphoto.kodak;

import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

import com.allarphoto.category.ProductField;
import com.allarphoto.client.beans.ProductBean;
import com.allarphoto.ecommerce.DatabaseUtilities;

@CoinjemaObject
public class KodakProductBean extends ProductBean {

	protected DatabaseUtilities dbutil;

	public boolean isFieldDisplayable(ProductField field) {
		return field.getDisplayOrder() > 0 && getValue(field.getName()) != null
				&& !getValue(field.getName()).equals("N/A")
				&& !getValue(field.getName()).equals("Not Applicable")
				&& getValue(field.getName()).toString().length() > 0;
	}

	public boolean isFieldEditable(ProductField field) {
		if (field.getDisplayOrder() >= 10)
			return true;
		else
			return false;
	}

	public boolean isFieldEasyEditable(ProductField field) {
		if (field.getDisplayOrder() >= 10
				&& (field.getSearchOrder() > 100 || field.getDisplayOrder() > 100))
			return true;
		else
			return false;
	}

	@Override
	protected Object convertFieldValue(Object value, ProductField field) {
		Object v = super.convertFieldValue(value, field);
		if (v == null || "N/A".equals(v) || "Not Applicable".equals(v))
			return "Not Applicable";
		return v;
	}

	@Override
	public Object getValue(String fieldName) {
		return getFieldValue(getProductField(fieldName));
	}

	protected ProductField getProductField(String field) {
		return getProductFamily().getField(field);
	}

	@CoinjemaDependency(type = "dbutil", method = "dbutil")
	public void setDatabaseUtilities(DatabaseUtilities dbutil) {
		this.dbutil = dbutil;
	}

}
