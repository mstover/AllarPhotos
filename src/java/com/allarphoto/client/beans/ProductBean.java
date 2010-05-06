package com.lazerinc.client.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import strategiclibrary.util.Converter;

import com.lazerinc.application.Product;
import com.lazerinc.application.SecurityModel;
import com.lazerinc.category.ProductField;
import com.lazerinc.ecommerce.ProductFamily;
import com.lazerinc.utils.Functions;
import com.lazerinc.utils.Right;

/**
 * Title: Lazerweb Description: Lazerweb - Lazer Inc. version 3.0 Copyright:
 * Copyright (c) 2001 Company: Lazer Inc.
 * 
 * @author Michael Stover
 * @version 1.0
 */

public class ProductBean implements Serializable {
	private static final long serialVersionUID = 1;

	Product product;

	boolean inCart;

	public void putProduct(Product p) {
		product = p;
	}

	public void setProduct(Product p) {
		product = p;
	}

	public Product getProduct() {
		return product;
	}

	public ProductFamily getProductFamily() {
		return getProduct().getProductFamily();
	}

	public String getProductFamilyName() {
		return getProduct().getProductFamilyName();
	}

	public String getPrimary() {
		return getProduct().getPrimary();
	}

	public String getDisplayName() {
		return getProduct().getName();
	}

	public int getId() {
		return getProduct().getId();
	}

	public Object getValue(String fieldName) {
		return getProduct().getValue(fieldName);
	}

	public void clear() {
		setProduct(null);
		setInCart(false);
	}

	public void setInCart(boolean statusInCart) {
		inCart = statusInCart;
	}

	public boolean isExpired() {
		return getProduct().getProductFamily().getProductExpirationTester()
				.isExpired(getProduct());
	}

	public boolean isFieldDisplayable(ProductField field) {
		return field.getDisplayOrder() > 0 || field.getSearchOrder() > 0;
	}

	public boolean isFieldEditable(ProductField field) {
		if (field.getDisplayOrder() >= 1000 && field.getDisplayOrder() < 2000)
			return true;
		return false;
	}

	public void setInCart(String statusInCart) {
		inCart = Boolean.parseBoolean(statusInCart);
	}

	public boolean getInCart() {
		return inCart;
	}

	public String getHeightInches() {
		String retVal = "error";
		try {
			float h = ((Number) getProduct().getValue("Height")).floatValue();
			retVal = "" + Functions.round(h, 2);
		} catch (Exception e) {

		}
		return retVal;
	}

	public String getWidthInches() {
		String retVal = "error";
		try {
			float w = ((Number) getProduct().getValue("Width")).floatValue();
			retVal = "" + Functions.round(w, 2);
		} catch (Exception e) {
		}
		return retVal;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((product == null) ? 0 : product.hashCode());
		return result;
	}

	public Collection<String[]> getDownloadableTypes(SecurityModel perms) {
		List<String[]> types = new ArrayList<String[]>();
		if (product.getProductFamily().getProductExpirationTester()
				.hasPermission(product, Right.DOWNLOAD, perms))
			types.add(new String[] { "jpg", "Download JPG" });
		if(perms.isSuperAdmin()) types.add(new String[]{"originals","Download Original"});
		return types;
	}

	public boolean isOrderable(SecurityModel perms) {
		if (product.getProductFamily().getProductExpirationTester()
				.hasPermission(product, Right.ORDER, perms))
			return true;
		else
			return false;
	}

	public List<String> getDisplayFields() {
		List<ProductField> fields = new ArrayList<ProductField>();
		for (ProductField f : getProductFamily().getFields()) {
			if (isFieldDisplayable(f))
				fields.add(f);
		}
		Collections.sort(fields);
		List<String> names = new ArrayList<String>();
		for (ProductField f : fields)
			names.add(f.getName());
		names.add("Creation Date");
		names.add("Modified Date");
		return names;

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ProductBean other = (ProductBean) obj;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		return true;
	}

	public Object getFieldValue(ProductField field) {
		return convertFieldValue(getProduct().getValue(field.getName()), field);
	}

	public List getFieldValues(ProductField field) {
		LinkedList<Comparable> values = new LinkedList<Comparable>();
		for (Object value : getProduct().getValues(field.getName())) {
			values.add((Comparable) convertFieldValue(value, field));
		}
		Collections.sort(values);
		return values;
	}

	public String getFieldValuesString(ProductField field, String delim) {
		LinkedList<Comparable> values = new LinkedList<Comparable>();
		if (getProduct().getValues(field.getName()) == null)
			return "";
		for (Object value : getProduct().getValues(field.getName())) {
			values.add((Comparable) convertFieldValue(value, field));
		}
		Collections.sort(values);
		if (values.size() > 0) {
			StringBuffer buf = new StringBuffer(values.removeFirst().toString());
			while (values.size() > 0) {
				buf.append(delim);
				buf.append(values.removeFirst());
			}
			return buf.toString();
		}
		return "";

	}

	protected Object convertFieldValue(Object value, ProductField field) {
		if (field.getName().equals("File Size"))
			return Converter.formatNumber(Converter.getDouble(value)
					/ (double) 1024, ".##")
					+ " Kb";
		else if (field.getName().equals("Hires File Size"))
			return Converter.formatNumber(Converter.getDouble(value)
					/ (double) 1024, ".##")
					+ " Kb";
		else if (field.getName().equals("Height")
				|| field.getName().equals("Width")) {
			if ("EPS".equals(value)
					|| "EPT".equals(getProduct().getValue("File Type")))
				return Converter.formatNumber(Converter.getDouble(value), "0")
						+ " pixels";
			else
				return Converter.formatNumber(Converter.getDouble(value)
						/ Converter.getFloat(getProduct()
								.getValue("Resolution")), ".##")
						+ " in";
		} else if (field.getName().equals("Resolution"))
			return Converter.formatNumber(Converter.getFloat(value), ".#")
					+ " dpi";
		else {
			if (value == null || value.toString().equalsIgnoreCase("null")
					|| value.equals("N/A"))
				return "";
			else
				return value;
		}
	}
}