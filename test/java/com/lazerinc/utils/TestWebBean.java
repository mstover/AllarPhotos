package com.lazerinc.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.collections.MultiMap;

import strategiclibrary.service.webaction.MapHandlerData;

import com.lazerinc.ecommerce.CommerceUser;


public class TestWebBean extends TestCase
{

   public TestWebBean(String arg0)
   {
      super(arg0);
   }
   
   public void testLoadUser() throws Exception
   {
      MultiMap params = new MultiHashMap();
      params.put("lastName","Stover");
      params.put("shipcity.name","Penfield");
      params.put("CommerceUser.company.name","Lazer Inc.");
      params.put("shipcountry.name","United States");
      params.put("ShipCountry.code","USA");
      params.put("company.industry.name","prepress");
      Map<String,Serializable> session = new HashMap<String,Serializable>();
      Map<String,Object> app = new HashMap<String,Object>();
      MapHandlerData info = new MapHandlerData(params,session,app);
      CommerceUser user = new CommerceUser();
      WebBean.setValues(user,info);
      assertEquals("Stover",user.getLastName());
      assertEquals("Penfield",user.getShipCity().getName());
      assertEquals("Lazer Inc.",user.getCompany().getName());
      assertEquals("United States",user.getShipCountry().getName());
      assertEquals("USA",user.getShipCountry().getCode());
      assertEquals("prepress",user.getCompany().getIndustry().getName());
      
   }

}
