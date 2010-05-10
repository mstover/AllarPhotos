package com.allarphoto.commerce;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import strategiclibrary.service.cache.CacheService;
import strategiclibrary.service.cache.DefaultObjectCache;

import com.allarphoto.application.SecurityModel;
import com.allarphoto.ecommerce.CommerceUser;
import com.allarphoto.ecommerce.UserGroup;
import com.allarphoto.server.UserService;
import com.allarphoto.testObjects.AbstractIntegratedTest;


public class TestUserMapping extends AbstractIntegratedTest
{

   public TestUserMapping(String arg0)
   {
      super(arg0);
      // TODO Auto-generated constructor stub
   }
   
   public void testUserRetrieval() throws Exception
   {
      CacheService c = new DefaultObjectCache();
      CommerceUser u = (CommerceUser)c.getCache(CommerceUser.class).getCachedObject("username","mstover");
      assertEquals("mstover",u.getUsername());
      assertEquals("Lazer Inc.",u.getCompany().getName());
      Collection<UserGroup> groups = u.getGroups();
      UserGroup firstGroup = groups.iterator().next();
      System.out.println("Groups = '" + groups + "'");
      assertEquals("admin",firstGroup.getName());
      List<CommerceUser> users = (List<CommerceUser>)firstGroup.getUsers();
      Collections.sort(users);
      CommerceUser firstUser = users.iterator().next();
      System.out.println("users in admin: " + groups.iterator().next().getUsers());
      assertEquals("Cousins",firstUser.getLastName());
   }
   
   public void testGroupAdd() throws Exception
   {
      UserService impl = new UserService();
      CommerceUser me = impl.getUser("mstover");
      SecurityModel perms = impl.getSecurity(me);
      impl.addGroup("myNewestGroup","admin",perms);
      UserGroup group = impl.getGroup("myNewestGroup");
      assertEquals("myNewestGroup",group.getName());
      impl.deleteGroup(group.getName(),perms);
   }
   
   @Override
   public void tearDown() throws Exception
   {
      UserService impl = new UserService();
      CommerceUser me = impl.getUser("mstover");
      SecurityModel perms = impl.getSecurity(me);
      UserGroup group = impl.getGroup("myNewestGroup");
      if(group != null)
         impl.deleteGroup(group.getName(),perms);
      super.tearDown();
      
   }

}
