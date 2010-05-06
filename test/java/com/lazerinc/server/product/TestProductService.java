package com.lazerinc.server.product;

import com.lazerinc.server.ProductService;
import com.lazerinc.testObjects.AbstractIntegratedTest;


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
