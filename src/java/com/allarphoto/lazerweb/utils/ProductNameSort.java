package com.lazerinc.lazerweb.utils;

import java.io.Serializable;
import java.util.Comparator;

import com.lazerinc.application.Product;

public class ProductNameSort implements Comparator<Product>, Serializable {

	public ProductNameSort() {

	}

	public int compare(Product p1, Product p2) {
		int ret = 0;
		if (p1.getProductFamily() != null && p2.getProductFamily() != null)
			ret = p1.getProductFamily().getTableName().compareTo(
					p2.getProductFamily().getTableName());
		if (ret == 0)
			ret = p1.getPrimary().compareTo(p2.getPrimary());
		if (ret == 0 && p1.getPath() != null)
			ret = p1.getPath().compareTo(p2.getPath());
		return ret;
	}

}
