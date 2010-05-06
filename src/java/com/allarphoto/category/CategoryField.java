package com.lazerinc.category;

import java.util.HashMap;
import java.util.Map;

import strategiclibrary.service.BatchUpdate;
import strategiclibrary.service.DataBase;
import strategiclibrary.service.sql.ObjectMappingService;

import com.lazerinc.ecommerce.CommerceProduct;
import com.lazerinc.ecommerce.Merchant;

public class CategoryField extends ProductField {
	private static final long serialVersionUID = 1;

	public CategoryField(String family, String n, int d, int s) {
		super(family, n, d, s, CATEGORY);
	}

	public CategoryField(String family, String n, int d) {
		super(family, n, d, CATEGORY);
	}

	public CategoryField(String family, String n, int d, int s, int t) {
		super(family, n, d, s, t);
	}

	public CategoryField() {
		super();
	}

	@Override
	public void addValue(CommerceProduct prod, Object keyword,
			BatchUpdate batch, ObjectMappingService mapper, DataBase db)
			throws Exception {
		Map values = new HashMap();
		values.put("product", prod);
		values.put("field", this);
		values.put("keyword", keyword);
		values.put("family", prod.getProductFamilyName());
		values.put("table", categoryTable);
		values.put("keyTable", keywordTable);
		values.put("column", "keyword_id");
		insertKeyValue(prod, batch, mapper, db, values);

	}

}
