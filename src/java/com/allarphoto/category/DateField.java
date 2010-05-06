package com.lazerinc.category;

import strategiclibrary.service.BatchUpdate;
import strategiclibrary.service.DataBase;
import strategiclibrary.service.sql.ObjectMappingService;

import com.lazerinc.ecommerce.CommerceProduct;

public class DateField extends ProductField {
	private static final long serialVersionUID = 1;

	public DateField() {
		super();
	}

	public DateField(String family, String n, int d, int s) {
		super(family, n, d, s, DATE_FIELD);
	}

	public DateField(String family, String n, int d) {
		super(family, n, d, DATE_FIELD);
	}

	@Override
	public void addValue(CommerceProduct prod, Object keyword,
			BatchUpdate batch, ObjectMappingService mapper, DataBase db)
			throws Exception {

	}

}
