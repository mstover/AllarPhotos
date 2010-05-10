<%@ page import = "com.allarphoto.application.*,com.allarphoto.dbtools.*, com.allarphoto.ecommerce.*, com.allarphoto.utils.*, java.net.*, com.allarphoto.ecommerce.impl.*, java.util.*" %>

<%try{
	String title = "Administration"; 
%>
<%@ include file="header.txt"%>		
<A HREF="admin_import.jsp">Import images</A>
  
<%@ include file="footer.txt"%>
<%
	}catch(Exception e){e.printStackTrace();}
%>