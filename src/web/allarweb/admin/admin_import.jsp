<%@ page import = "com.allarphoto.application.*,com.allarphoto.dbtools.*, com.allarphoto.ecommerce.*, com.allarphoto.utils.*, java.net.*, com.allarphoto.ecommerce.impl.*, java.util.*" %>
Importing!
<%try{
	out.flush();
	out.close();
	DatabaseApplicationController controller = (DatabaseApplicationController)application.getAttribute("lazerweb.controller");
	String title = "Administration"; 
%>
<%@ include file="header.txt"%>
<%	
	ProductAdministrator admin = new ProductAdministrator();
	admin.setController(controller);
	admin.importImages();
%>		

  
<%@ include file="footer.txt"%>
<%
	}catch(Exception e){e.printStackTrace();}
%>