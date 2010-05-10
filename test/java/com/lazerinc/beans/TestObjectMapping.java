package com.allarphoto.beans;

import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import strategiclibrary.service.sql.DefaultMappingService;
import strategiclibrary.service.sql.ObjectMappingService;
import strategiclibrary.util.Converter;

import com.allarphoto.ecommerce.DatabaseUtilities;
import com.allarphoto.ecommerce.Order;
import com.allarphoto.testObjects.AbstractIntegratedTest;

public class TestObjectMapping extends AbstractIntegratedTest {
	DatabaseUtilities dbutil;

	public TestObjectMapping(String arg0) {
		super(arg0);
	}
	
	
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		dbutil = new DatabaseUtilities();
	}



	public void testLoadOrderItems() throws Exception
	{
		ObjectMappingService mapper = new DefaultMappingService();
		Map values = new HashMap();
		values.put("category","status");
		values.put("value","fulfilled");
		Calendar day = new GregorianCalendar();
		day.add(Calendar.MONTH,-6);
		values.put("date",day);
		Collection<OrderItem> items = (Collection<OrderItem>)mapper.getObjects("getOrderItems.sql",values);
		System.out.println("Number of items = "+ items.size());
		Iterator<OrderItem> iter = items.iterator();
		OrderItem one = iter.next();
		System.out.println("order No = " + one.getOrderNo() + " id = " + one.getId() + " user = " + one.getValue("user"));
		System.out.println(one.getItem());
		one = iter.next();
		System.out.println("order No = " + one.getOrderNo() + " id = " + one.getId() + " user = " + one.getValue("user"));
		System.out.println(one.getItem());
		assertTrue(items.size() > 50);
	}
	
	public void testLoadMultipleOrders() throws Exception
	{
		ObjectMappingService mapper = new DefaultMappingService();
		Map values = new HashMap();
		values.put("category","status");
		values.put("value","fulfilled");
		Calendar day = new GregorianCalendar();
		day.add(Calendar.MONTH,-6);
		values.put("date",day);
		Collection<OrderItem> items = (Collection<OrderItem>)mapper.getObjects("getOrderItems.sql",values);
		Collection<Order> orders = Order.loadOrders(items);
		assertEquals(230,orders.size());
	}
	
	public void testLoadOrder() throws Exception
	{
		ObjectMappingService mapper = new DefaultMappingService();
		Map values = new HashMap();
		values.put("category","orderNo");
		values.put("value","1356_1149276220542.11468");
		Calendar day = new GregorianCalendar();
		day.add(Calendar.MONTH,-6);
		values.put("date",day);
		Collection<OrderItem> items = (Collection<OrderItem>)mapper.getObjects("getOrderItems.sql",values);
		Order o = new Order(items);
		System.out.println("Number of items = "+ items.size());
		assertEquals(9,o.getItems().size());
		assertEquals("kkooken",o.getUser().getUsername());
		items = (Collection<OrderItem>)mapper.getObjects("getOrderItems.sql",values);
		o = new Order(items);
		System.out.println("Number of items = "+ items.size());
		assertEquals(9,o.getItems().size());
		assertEquals("kkooken",o.getUser().getUsername());
		System.out.println("file map = " + o.getItems().iterator().next().getItem());
		assertEquals(1167,o.getItems().iterator().next().getProduct(dbutil.getAdmin()).getId());
	}
	
	public void testLoadDownloads() throws Exception
	{
		ObjectMappingService mapper = new DefaultMappingService();
		Map values = new HashMap();
		values.put("category","family");
		values.put("value","IA Just My Size On-Line Library");
		Calendar day = new GregorianCalendar();
		day.add(Calendar.MONTH,-6);
		values.put("date",day);
		Collection<DownloadItem> items = (Collection<DownloadItem>)mapper.getObjects("getDownloadItems.sql",values);
		System.out.println("Number of items = "+ items.size());
		long totalSize = 0;
		for(DownloadItem item : items)
		{
			System.out.println("file size = " + item.getSize() + " " + item.getProduct(dbutil.getAdmin()));
			totalSize += item.getSize();
		}
		System.out.println("Total Bytes Downloaded: " + Converter.formatNumber((double)totalSize/1024D/1024D,"0.##") + "MB");
		assertTrue(items.size() > 50);
	}
	
	public void testLoadDownloadsDateRange() throws Exception
	{
		ObjectMappingService mapper = new DefaultMappingService();
		Map values = new HashMap();
		values.put("category","family");
		values.put("value","IA Just My Size On-Line Library");
		Calendar day = new GregorianCalendar();
		day.add(Calendar.MONTH,-6);
		values.put("fromDate",day);
		values.put("toDate", new GregorianCalendar());
		Collection<DownloadItem> items = (Collection<DownloadItem>)mapper.getObjects("getDownloadItems.sql",values);
		System.out.println("Number of items = "+ items.size());
		long totalSize = 0;
		for(DownloadItem item : items)
		{
			System.out.println("file size = " + item.getSize() + " " + item.getProduct(dbutil.getAdmin()));
			totalSize += item.getSize();
		}
		System.out.println("Total Bytes Downloaded: " + Converter.formatNumber((double)totalSize/1024D/1024D,"0.##") + "MB");
		assertTrue(items.size() > 50);
	}

}
