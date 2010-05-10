package com.allarphoto.actions;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.collections.MultiMap;
import org.coinjema.context.CoinjemaContext;
import org.coinjema.context.ContextFactory;

import strategiclibrary.service.webaction.MapHandlerData;

import com.allarphoto.application.Product;
import com.allarphoto.client.beans.ShoppingCartBean;
import com.allarphoto.client.exceptions.LazerwebException;
import com.allarphoto.ecommerce.DatabaseUtilities;
import com.allarphoto.kodak.actions.KodakExecuteOrder;
import com.allarphoto.server.ProductService;
import com.allarphoto.server.UserService;
import com.allarphoto.testObjects.AbstractIntegratedTest;

public class OrderActions extends AbstractIntegratedTest {
	DatabaseUtilities dbutil;
	UserService ugd;
	MapHandlerData info;
	ShoppingCartBean cart;
	MultiMap params;

	public OrderActions(String arg0) {
		super(arg0);
	}
	
	
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		dbutil = new DatabaseUtilities();
		ugd = new UserService();
	      params = new MultiHashMap();
	      Map<String,Serializable> session = new HashMap<String,Serializable>();
	      Map<String,Object> app = new HashMap<String,Object>();
	      info = new MapHandlerData(params,session,app);
		cart = info.getUserBean("cart",ShoppingCartBean.class);
		ContextFactory.pushContext(new CoinjemaContext("lazerweb"));
	}



	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
		ContextFactory.popContext();
	}



	public void testKodakDownload_NoUsageAgree() throws Exception
	{
		Product p = dbutil.getProductFamily("kdk_dai").getProduct(6246, ugd.getSecurity(ugd.getUser("mstover")));
		assertEquals("",p.getPathName());
		cart.add(p);
		cart.addInstruction(p, "download", "product");
		params.put("download_usage","Collateral");
		KodakExecuteOrder action = new KodakExecuteOrder();
		try {
			action.performAction(info);
		} catch(LazerwebException e)
		{
			assertEquals("UsageAgreeException",e.getMessage());
		}
	}

	public void testKodakDownload_Collateral() throws Exception
	{
		Product p = dbutil.getProductFamily("kdk_dai").getProduct(6246, ugd.getSecurity(ugd.getUser("mstover")));
		cart.add(p);
		cart.addInstruction(p, "download", "product");
		params.put("download_usage","Collateral");
		params.put("usage_agree", "yes");
		KodakExecuteOrder action = new KodakExecuteOrder();
		try {
			action.performAction(info);
		} catch(LazerwebException e)
		{
			assertEquals("BadDownloadUsageWarning",e.getMessage());
		}
	}



	public void testKodakOrder_NoUsageAgree() throws Exception
	{
		Product p = dbutil.getProductFamily("kdk_dai").getProduct(6246, ugd.getSecurity(ugd.getUser("mstover")));
		cart.add(p);
		cart.addInstruction(p, "order", "cd");
		params.put("order_usage","Collateral");
		params.put("charge_no", "test");
		KodakExecuteOrder action = new KodakExecuteOrder();
		try {
			action.performAction(info);
		} catch(LazerwebException e)
		{
			assertEquals("UsageAgreeException",e.getMessage());
		}
	}
	
	public void testTwoBadOrders() throws Exception
	{
		testKodakOrder_NoUsageAgree();
		params.clear();
		testKodakOrder_NoChargeNo();
	}
	
	public void testKodakOrder_NoChargeNo() throws Exception
	{
		Product p = dbutil.getProductFamily("kdk_dai").getProduct(6246, ugd.getSecurity(ugd.getUser("mstover")));
		cart.add(p);
		cart.addInstruction(p, "order", "cd");
		params.put("order_usage","Collateral");
		params.put("usage_agree", "yes");
		KodakExecuteOrder action = new KodakExecuteOrder();
		try {
			action.performAction(info);
		} catch(LazerwebException e)
		{
			assertEquals("NoChargeNoException",e.getMessage());
		}
	}
	

}
