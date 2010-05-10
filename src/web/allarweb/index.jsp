<%@ page import="com.allarphoto.application.*,com.allarphoto.beans.*,com.allarphoto.dbtools.*,com.allarphoto.ecommerce.*,com.allarphoto.ecommerce.impl.*,com.allarphoto.utils.*,java.util.*,java.net.*,java.io.*,java.text.*"%>
<h1>Hey, here's some html</h1>
<%
	String returnPage = (String)session.getValue("returnpage");
	if(application.getAttribute("lazerweb.controller") == null)		
		startup();
	if(returnPage != null)
	{
		%><%=returnPage %><%
		response.sendRedirect(returnPage);
	}
 %>
<%@ include file="global.txt"%>