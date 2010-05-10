package com.allarphoto.testObjects;

import java.util.Properties;

import org.coinjema.context.CoinjemaContext;
import org.coinjema.context.CoinjemaDynamic;

import com.allarphoto.application.Controller;


public class DoNothingController extends Controller
{

   public DoNothingController()
   {
      super();
      // TODO Auto-generated constructor stub
   }

   public DoNothingController(CoinjemaContext ctx)
   {
      super(ctx);
      // TODO Auto-generated constructor stub
   }

   @CoinjemaDynamic(alias="config")
   public Properties getConfig()
   {
      return null;
   }

}
