<%@ page import="com.lazerinc.application.*,com.lazerinc.beans.*,com.lazerinc.dbtools.*,com.lazerinc.ecommerce.*,com.lazerinc.ecommerce.impl.*,com.lazerinc.utils.*,java.util.*,java.net.*,java.io.*,java.text.*"%>
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