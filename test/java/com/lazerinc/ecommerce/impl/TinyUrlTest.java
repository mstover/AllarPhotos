package com.allarphoto.ecommerce.impl;

import junit.framework.TestCase;

import com.allarphoto.utils.TinyUrl;

public class TinyUrlTest extends TestCase
{

   public TinyUrlTest(String arg0)
   {
      super(arg0);
      // TODO Auto-generated constructor stub
   }

   /*
    * Test method for 'com.allarphoto.ecommerce.impl.LazerwebFulfillment.getTinyUrl(String)'
    */
   public void testGetTinyUrl()
   {
      String href = "http://lazerinc-image.com/lazerweb/howdy/doody/dff?fsfhdfkf8f=34&r=j fsd07 @#$% hdfs";
      TinyUrl tiny = new TinyUrl(href);
      assertTrue(tiny.getUrl(), tiny.getUrl().startsWith("http://tinyurl.com/"));
      assertTrue(tiny.getUrl(), tiny.getUrl().length() > 21);
   }

}
