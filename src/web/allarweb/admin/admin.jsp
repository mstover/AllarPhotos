<%@ page import = "com.lazerinc.application.*,com.lazerinc.dbtools.*, com.lazerinc.ecommerce.*, com.lazerinc.utils.*, java.net.*, com.lazerinc.ecommerce.impl.*, java.util.*" %>

<%try{
	String title = "Administration"; 
%>
<%@ include file="header.txt"%>		
<A HREF="admin_import.jsp">Import images</A>
  
<%@ include file="footer.txt"%>
<%
	}catch(Exception e){e.printStackTrace();}
%>