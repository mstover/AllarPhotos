package com.allarphoto.category;

import strategiclibrary.service.BatchUpdate;
import strategiclibrary.service.DataBase;
import strategiclibrary.service.sql.ObjectMappingService;

import com.allarphoto.ecommerce.CommerceProduct;
import com.allarphoto.ecommerce.Merchant;

public class PathField extends ProductField {
	private static final long serialVersionUID = 1;

	public PathField() {
		super();
	}

	@Override
	public void addValue(CommerceProduct prod, Object keyword,
			BatchUpdate batch, ObjectMappingService mapper, DataBase db)
			throws Exception {
		throw new UnsupportedOperationException();

	}

}
