package com.allarphoto.testObjects;

import java.io.File;

import junit.framework.TestCase;

import org.apache.log4j.PropertyConfigurator;
import org.coinjema.context.ContextFactory;
import org.coinjema.context.source.FileContextSource;


public abstract class AbstractIntegratedTest extends TestCase
{

   public AbstractIntegratedTest(String arg0)
   {
      super(arg0);
   }
   
   @Override
   public void setUp() throws Exception
   {
	   System.out.println("Looking for file " + new File("build/WEB-INF/contexts").getAbsolutePath());
      ContextFactory.createRootContext(new FileContextSource("build/WEB-INF/contexts"));
      PropertyConfigurator.configure("build/WEB-INF/contexts/logging4j.properties");
   }

   @Override
   protected void tearDown() throws Exception
   {
      ContextFactory.destroyContext(null);
   }
   

}
