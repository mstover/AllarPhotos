package com.allarphoto.cached.functions;

import java.util.HashMap;
import java.util.Map;

import com.allarphoto.ecommerce.ProductFamily;

public class FamilyFunctor extends AbstractCacheFunction<ProductFamily> {

	public FamilyFunctor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Class getRelevantType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object[] getSearchPath(ProductFamily obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean isValidObj(ProductFamily obj) {
		// TODO Auto-generated method stub
		return false;
	}

	public void add(ProductFamily obj) {
		// TODO Auto-generated method stub

	}

	public void delete(ProductFamily obj) {
		// TODO Auto-generated method stub

	}

	public void update(ProductFamily obj) {
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("family", obj);
		mapper.doUpdate("updateProductFamily.sql", values);

	}

}
