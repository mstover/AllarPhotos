package com.lazerinc.lazerweb.utils;

import java.io.Serializable;
import java.util.Comparator;

import com.lazerinc.application.Product;

public class ProductDateSort implements Comparator<Product>, Serializable {

	public ProductDateSort() {
	}

	public int compare(Product o1, Product o2) {
		if (o1 == null && o2 == null)
			return 0;
		else if (o1 == null)
			return 1;
		else if (o2 == null)
			return -1;
		if (o1.getDateCataloged().after(o2.getDateCataloged()))
			return -1;
		else if (o2.getDateCataloged().after(o1.getDateCataloged()))
			return 1;
		else {
			int ret = 0;

			if (o1.getProductFamily() != null && o2.getProductFamily() != null)
				ret = o1.getProductFamily().getTableName().compareTo(
						o2.getProductFamily().getTableName());
			if (ret == 0)
				ret = o1.getPrimary().compareTo(o2.getPrimary());
			if (ret == 0 && o1.getPath() != null)
				ret = o1.getPath().compareTo(o2.getPath());
			return ret;
		}
	}

}
