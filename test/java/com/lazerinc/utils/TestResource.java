package com.lazerinc.utils;

import com.lazerinc.server.ResourceService;
import com.lazerinc.testObjects.AbstractIntegratedTest;


public class TestResource extends AbstractIntegratedTest
{

   public TestResource(String arg0)
   {
      super(arg0);
   }
   
   public void testGetResource() throws Exception
   {
      Resource res = new Resource();
      assertNotNull(res);
      assertEquals("all",new ResourceService().getResource("all",Resource.DATABASE).getName());
   }
   
   public void testResourceUpdate() throws Exception
   {
      Resource res = new ResourceService().getResource("admin",Resource.GROUP);
      assertEquals(Resource.GROUP,res.getType());
      res.setName("Super Users");
      res.update();
      Resource changed = new ResourceService().getResource("Super Users",Resource.GROUP);
      assertEquals(changed.getId(),res.getId());
      changed.setName("admin");
      changed.update();
      assertEquals("admin",new ResourceService().getResource("admin",Resource.GROUP).getName());
   }

}
