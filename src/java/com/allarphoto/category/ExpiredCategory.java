package com.lazerinc.category;

import strategiclibrary.service.BatchUpdate;
import strategiclibrary.service.DataBase;
import strategiclibrary.service.sql.ObjectMappingService;

import com.lazerinc.ecommerce.CommerceProduct;
import com.lazerinc.ecommerce.Merchant;

public class ExpiredCategory extends ProductField {
	private static final long serialVersionUID = 1;

	public ExpiredCategory() {
		super();
	}

	public ExpiredCategory(String family, String n, int d, int s) {
		super(family, n, d, s, EXPIRED_TYPE);
	}

	public ExpiredCategory(String family, String n, int d) {
		super(family, n, d, EXPIRED_TYPE);
	}

	@Override
	public void addValue(CommerceProduct prod, Object keyword,
			BatchUpdate batch, ObjectMappingService mapper, DataBase db)
			throws Exception {
		throw new UnsupportedOperationException();

	}

}
