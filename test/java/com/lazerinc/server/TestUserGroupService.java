package com.allarphoto.server;

import com.allarphoto.beans.City;
import com.allarphoto.beans.Country;
import com.allarphoto.beans.State;
import com.allarphoto.beans.User;
import com.allarphoto.ecommerce.CommerceUser;
import com.allarphoto.server.UserService;
import com.allarphoto.testObjects.AbstractIntegratedTest;


public class TestUserGroupService extends AbstractIntegratedTest
{
	UserService ugd;

   public TestUserGroupService(String arg0)
   {
      super(arg0);
      // TODO Auto-generated constructor stub
   }
   
   public void setUp() throws Exception
   {
	   super.setUp();
	   ugd = new UserService();
   }
   

   
   public void testServiceCreation() throws Exception
   {
      assertNotNull(ugd.getLog());
   }
   
   public void testUserGet() throws Exception
   {
	   ugd.searchUsers("lazer");
      CommerceUser user = ugd.getUser(4);
      System.out.println("city = " + user.getShipCity());
      assertEquals("Stover",user.getLastName());
      CommerceUser user2 = ugd.getUser(4);
      assertTrue(user == user2);
   }
   
   public void testUserGet2() throws Exception
   {
      CommerceUser user = ugd.getUser(4);
      assertEquals("Stover",user.getLastName());
      CommerceUser user2 = ugd.getUser(4);
      assertTrue(user == user2);
   }
   
   public void testUserAdd() throws Exception
   {
	   CommerceUser baseUser = ugd.getUser(4);
	   CommerceUser newUser = new CommerceUser();
	   newUser.setLastName(baseUser.getLastName());
	   newUser.setFirstName("Matt");
	   newUser.setUsername("mmmsssttt");
	   newUser.setBillCity(new City("Penfield"));
	   newUser.setBillState(new State("New York"));
	   newUser.setBillCountry(new Country("United States"));
	   ugd.addUser(newUser, baseUser.getGroups(), ugd.getSecurity(baseUser));
	   assertEquals("Penfield",ugd.getUser("mmmsssttt").getBillCity().getName());
	   ugd.deleteUser(newUser, ugd.getSecurity(baseUser));
	   assertNull(ugd.getUser("mmmsssttt"));
   }
   
   public void testModifyUser() throws Exception
   {
	   ugd.searchUsers("lazer");
      CommerceUser user = ugd.getUser(4);
      user.setLastName("Duper");
      ugd.updateUser(user,ugd.getSecurity(user));
      ugd.searchUsers("Dupe");
      assertTrue(ugd.getUser(4) == user);
      user.setLastName("Stover");
      ugd.updateUser(user,ugd.getSecurity(user));
   }

}
