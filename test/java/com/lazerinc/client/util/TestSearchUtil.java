package com.lazerinc.client.util;

import org.coinjema.context.CoinjemaContext;
import org.coinjema.context.ContextFactory;

import com.lazerinc.client.beans.ProductSetBean;
import com.lazerinc.server.UserService;
import com.lazerinc.testObjects.AbstractIntegratedTest;

public class TestSearchUtil extends AbstractIntegratedTest {

	public TestSearchUtil(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	
	public void testCreation() throws Exception
	{
		ContextFactory.pushContext(new CoinjemaContext("lazerweb"));
		SearchUtil su = new SearchUtil();
		UserService ugd = new UserService();
		ProductSetBean set = new ProductSetBean();
		su.getInitialSet("ia_bali", set, ugd.getSecurity(ugd.getUser("mstover")));
		ContextFactory.popContext();
		assertTrue(set.size() > 0);
		ContextFactory.pushContext(new CoinjemaContext("lazerweb"));
		su = new SearchUtil();
		su.getInitialSet("ia_bali", set, ugd.getSecurity(ugd.getUser("mstover")));
		ContextFactory.popContext();
		assertTrue(set.size() > 0);
	}

}
