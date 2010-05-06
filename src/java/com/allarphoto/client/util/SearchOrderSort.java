package com.lazerinc.client.util;

import java.util.Comparator;

import com.lazerinc.category.ProductField;

public class SearchOrderSort implements Comparator<ProductField> {

	public SearchOrderSort() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int compare(ProductField o1, ProductField o2) {
		return o1.getSearchOrder() - o2.getSearchOrder();
	}

}