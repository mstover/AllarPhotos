package com.lazerinc.application.impl;

import java.util.Comparator;

import com.lazerinc.application.Product;

public class DefaultProductComparator implements Comparator {

	public int compare(Object o1, Object o2) {
		Product p2 = (Product) o2;
		Product p1 = (Product) o1;
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
