package com.allarphoto.client.util;

import org.coinjema.context.CoinjemaContext;
import org.coinjema.context.ContextFactory;

import com.allarphoto.client.beans.ProductSetBean;
import com.allarphoto.server.UserService;
import com.allarphoto.testObjects.AbstractIntegratedTest;

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
