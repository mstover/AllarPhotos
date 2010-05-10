package com.allarphoto.server.cataloger;

import java.util.Collection;

import strategiclibrary.util.TextFile;

import com.allarphoto.application.Product;
import com.allarphoto.client.util.ProductFileParser;
import com.allarphoto.server.UserService;
import com.allarphoto.testObjects.AbstractIntegratedTest;

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
