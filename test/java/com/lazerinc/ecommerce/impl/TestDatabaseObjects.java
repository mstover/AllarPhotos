package com.allarphoto.ecommerce.impl;

import java.util.Collection;

import strategiclibrary.service.cache.CacheService;
import strategiclibrary.service.cache.DefaultObjectCache;

import com.allarphoto.beans.Address;
import com.allarphoto.beans.City;
import com.allarphoto.beans.Company;
import com.allarphoto.beans.Country;
import com.allarphoto.beans.Industry;
import com.allarphoto.beans.State;
import com.allarphoto.cached.functions.AddressAdd;
import com.allarphoto.cached.functions.CompanyAdd;
import com.allarphoto.cached.functions.IndustryAdd;
import com.allarphoto.testObjects.AbstractIntegratedTest;


public class TestDatabaseObjects extends AbstractIntegratedTest
{

   public TestDatabaseObjects(String arg0)
   {
      super(arg0);
   }
   
   public void testStates() throws Exception
   {
      CacheService cs = new DefaultObjectCache();
      Collection<State> states = cs.getCache(State.class).getCachedList();
      System.out.println("Number of states = " + states.size());
      assertTrue(states.size() > 1);
   }
   
   public void testCities() throws Exception
   {
      CacheService cs = new DefaultObjectCache();
      Collection<City> cities = cs.getCache(City.class).getCachedList();
      System.out.println("Number of cities = " + cities.size());
      assertTrue(cities.size() > 1);
      City utica = cs.getCache(City.class).getCachedObject("name","Rome");
      System.out.println("utica = "+ utica);
      assertNull(utica);
   }
   
   public void testCountries() throws Exception
   {
      CacheService cs = new DefaultObjectCache();
      Collection<Country> countries = cs.getCache(Country.class).getCachedList();
      System.out.println("Number of countries = " + countries.size());
      assertTrue(countries.size() > 1);
   }
   
   public void testCacheAdding() throws Exception
   {
      Company c = new Company();
      c.setName("Lazer");
      c = new CompanyAdd().addOrGet(c);
      assertTrue(c.getId() > 0);
   }
   
   public void testAddresses() throws Exception
   {
	      CacheService cs = new DefaultObjectCache();
	   Collection<Address> addrs = cs.getCache(Address.class).getCachedObjects("address1","70 Bermar Park");
	   System.out.println(addrs);
	   assertTrue(addrs.size() > 0);
	   AddressAdd adder = new AddressAdd();
	   Address a = new Address();
	   a.setAddress1("70 Bermar Park");
	   a.setAddress2("N/A");
	   a.setAttn("Lazer Inc");
	   a.setCity(new City("Penfield"));
	   a.setCompany(new Company("Lazer Inc."));
	   a.setCountry(new Country("United States"));
	   a.setPhone("2479647");
	   a.setState(new State("ALABAMA"));
	   a.setZip("14624");
	   adder.addOrGet(a);	
	   Collection<Address> addrs2 = cs.getCache(Address.class).getCachedObjects("address1","70 Bermar Park");
	   assertEquals(addrs2.size(),addrs.size());
   }
   
   public void testIndustryAdd() throws Exception
   {
	      CacheService cs = new DefaultObjectCache();
	      Industry i = new Industry("");
	      System.out.println("id = " + i.getId());
	      i = new IndustryAdd().addOrGet(i);
	      System.out.println("id = " + i.getId());
	      assertTrue(i.getId() > 0);
   }

}
