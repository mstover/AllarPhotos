package com.lazerinc.utils;

import java.util.Collection;
import java.util.LinkedList;

import junit.framework.TestCase;

public class TestPagedList extends TestCase {

	public TestPagedList(String arg0) {
		super(arg0);
	}
	
	Collection list1;
	Collection list2;
	
	public void setUp() throws Exception
	{
		list1 = new LinkedList();
		list1.add("one");
		list1.add("two");
		list1.add("three");
		list1.add("four");
		list1.add("five");
		list1.add("six");
		list1.add("seven");
		list1.add("eight");
		list1.add("nine");
		list1.add("ten");
		list1.add("eleven");
		list1.add("twelve");
		list2 = new LinkedList();
		list2.add("2_one");
		list2.add("2_two");
		list2.add("2_three");
		list2.add("2_four");
		list2.add("2_five");
		list2.add("2_six");
		list2.add("2_seven");
		list2.add("2_eight");
		list2.add("2_nine");
		list2.add("2_ten");
		list2.add("2_eleven");
		list2.add("2_twelve");
	}
	
	public void testMultiList() throws Exception
	{
		PagedList pl = new PagedList();
		pl.addAll(list1);
		pl.addAll(list2);
		pl.setPagingSize(8);
		pl.setPageNo(1);
		assertEquals("nine",pl.getPage().iterator().next());
	}

}
