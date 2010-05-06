package com.lazerinc.worldkitchen;

import strategiclibrary.util.Converter;

import com.lazerinc.category.ProductField;
import com.lazerinc.client.beans.ProductBean;

public class WorldKitProductBean extends ProductBean {

	public String getThumbnailDir() {
		return (String) getValue("Thumbnail Directory");
	}

	public int getHeight() {
		return Converter.getInt(getValue("Height"));
	}

	public int getWidth() {
		return Converter.getInt(getValue("Width"));
	}

	public boolean isFieldDisplayable(ProductField field) {
		return field.getDisplayOrder() > 0 && field.getDisplayOrder() < 5000
				&& getValue(field.getName()) != null
				&& !getValue(field.getName()).equals("N/A")
				&& getValue(field.getName()).toString().length() > 0;
	}

	public boolean isFieldEditable(ProductField field) {
		if (field.getDisplayOrder() > 1008)
			return true;
		else
			return false;
	}

}
