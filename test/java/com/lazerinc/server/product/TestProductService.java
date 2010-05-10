package com.allarphoto.server.product;

import com.allarphoto.server.ProductService;
import com.allarphoto.testObjects.AbstractIntegratedTest;


public class TestProductService extends AbstractIntegratedTest
{
   
   public TestProductService(String arg0)
   {
      super(arg0);
   }

   public void testSetup() throws Exception
   {
      new ProductService();
   }

}
