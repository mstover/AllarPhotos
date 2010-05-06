package com.lazerinc.server.cataloger;

import java.util.Collection;

import strategiclibrary.util.TextFile;

import com.lazerinc.application.Product;
import com.lazerinc.client.util.ProductFileParser;
import com.lazerinc.server.UserService;
import com.lazerinc.testObjects.AbstractIntegratedTest;

public class KeywordImportTester extends AbstractIntegratedTest {

	public KeywordImportTester(String arg0) {
		super(arg0);
	}
	
	public void testProductUpdate() throws Exception
	{
		UserService ugd = new UserService();
		ProductFileParser pfp = new ProductFileParser("ia_bali","\t",ugd.getSecurity(ugd.getUser("mstover")));
		TextFile tf = new TextFile("test/import_files/ia_bali.csv");
		Collection<Product> productsForUpdate = pfp.parseFileForProducts(tf.getLines());
		assertEquals(2,productsForUpdate.size());
		assertNotNull(productsForUpdate.iterator().next());
	}

}
