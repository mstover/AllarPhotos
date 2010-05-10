<%@ page import="com.allarphoto.application.*,com.allarphoto.beans.*,com.allarphoto.dbtools.*,com.allarphoto.ecommerce.*,com.allarphoto.ecommerce.impl.*,com.allarphoto.utils.*,java.util.*,java.net.*,java.io.*,java.text.*"%>
<%
	String returnPage = (String)session.getValue("returnpage");
	if(returnPage == null)
		returnPage = "/lazerweb/password_help/index.jsp";//default page for this family
	//checks for available lazerweb controller
	if(application.getAttribute("lazerweb.controller") == null)
	{
		session.putValue("returnpage",returnPage);
		response.sendRedirect("/lazerweb/index.jsp");//returns to lazerweb to get the controller
	}
	else if(application.getAttribute("lazerweb.password_help.controller") == null)//set demo controller
	{
		startup();
		response.sendRedirect(returnPage);
	}
	response.sendRedirect(returnPage);
 %>
<%@ include file="global.txt"%>