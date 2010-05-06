package com.lazerinc.lazerweb.utils;

import java.io.Serializable;
import java.util.Comparator;

import com.lazerinc.application.Product;

public class ProductFieldSort implements Comparator<Product>, Serializable {

	String fieldName;

	public ProductFieldSort() {
		// TODO Auto-generated constructor stub
	}

	public ProductFieldSort(String fieldName) {
		this.fieldName = fieldName;
	}

	public int compare(Product o1, Product o2) {
		try {
			if (o1 == null && o2 == null)
				return 0;
			else if (o1 == null)
				return 1;
			else if (o2 == null)
				return -1;
			String value1 = o1.getValue(fieldName) != null ? o1.getValue(
					fieldName).toString() : null;
			String value2 = o2.getValue(fieldName) != null ? o2.getValue(
					fieldName).toString() : null;
			if (value1 == null)
				return 1;
			else if (value2 == null)
				return -1;
			else {
				int ret = value1.compareTo(value2);
				if (ret == 0)
					ret = o1.getPrimary().compareTo(o2.getPrimary());
				if (ret == 0 && o1.getPath() != null)
					ret = o1.getPath().compareTo(o2.getPath());
				return ret;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

}
