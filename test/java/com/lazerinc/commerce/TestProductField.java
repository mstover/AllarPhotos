package com.lazerinc.commerce;

import strategiclibrary.service.cache.CacheService;
import strategiclibrary.service.cache.DefaultObjectCache;

import com.lazerinc.category.ProductField;
import com.lazerinc.category.ProtectedField;
import com.lazerinc.ecommerce.DatabaseUtilities;
import com.lazerinc.testObjects.AbstractIntegratedTest;

public class TestProductField extends AbstractIntegratedTest {

	public TestProductField(String arg0) {
		super(arg0);
	}
	
	public void testLoadField() throws Exception
	{
		CacheService cs = new DefaultObjectCache();
		ProductField archive = cs.getCache("ia_balifields",ProductField.class).getCachedObject("name","Archive");
		System.out.println("field = "+ archive);
		assertEquals(ProductField.PROTECTED,archive.getType());
		assertEquals(29,cs.getCache("ia_balifields",ProductField.class).getCachedList().size());
	}
	
	public void testLoadProtectedField() throws Exception
	{
		CacheService cs = new DefaultObjectCache();
		ProductField archive = cs.getCache("ia_balifields",ProductField.class).getCachedObject("name","Archive");
		assertEquals(ProtectedField.class,archive.getClass());
	}

}
