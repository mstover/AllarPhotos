<%@ page import = "java.io.*,com.lazerinc.application.*,com.lazerinc.dbtools.*, com.lazerinc.ecommerce.*, com.lazerinc.utils.*, java.net.*, com.lazerinc.ecommerce.impl.*, java.util.*, com.lazerinc.beans.*"%>
<%
		DatabaseApplicationController controller = (DatabaseApplicationController)application.getAttribute("lazerweb.admin.controller");
	if(controller == null)
			response.sendRedirect("index.jsp");
	CommerceAdminWebInterface admin = (CommerceAdminWebInterface)session.getValue("lazerweb.admin");
	String context = (String)session.getValue("lazerweb.context");
	if(admin == null)
	{
		admin = new Administrator();
		admin.setController(controller);
		session.putValue("lazerweb.admin",admin);
		session.putValue("lazerweb.context","lazerweb.admin");
	}
	else if(context == null || !context.equals("lazerweb.admin"))
	{
		session.putValue("lazerweb.context","lazerweb.admin"); 
	}
	CommerceWebInterface commerce = (CommerceWebInterface)session.getValue("lazerweb.commerce");
	if(commerce != null)
			{
				admin.setUser(commerce.getUser());
				if(!admin.checkUser())
					response.sendRedirect("index.jsp");
			}	
			else{
				response.sendRedirect("index.jsp");
		}
		
	int id = Integer.parseInt(request.getParameter("product_id"));
	String table = request.getParameter("data-family");
	Product p = admin.getProduct(table,id);
	ProductFamily family = admin.getProductFamily(table);
	ProductField[] fields = family.getFields();
	for(int x = 0;x < fields.length;x++)
	{
		String name = fields[x].getName();
		String value = request.getParameter(name);
		if(value != null)
		{
			if(null==value || value.equals("") || value.equals("N/A"))
				value = request.getParameter(name+"_select");
			if(value != null && !value.equals("") && !value.equals("null") && !value.equals("N/A"))
			{
				switch(Math.abs(fields[x].getType()))
				{
					case ProductField.CATEGORY : p.setValue(name,value);break;
					case ProductField.PRIMARY : p.setPrimary(value);break;
					case ProductField.DESCRIPTION : p.setValue(name,value);break;
					case ProductField.NUMERICAL : p.setValue(name,new Float(value));break;
				}
			}
		}
	}
	admin.updateProduct(p);
	response.sendRedirect(request.getParameter("return"));%>
	<b>Your request has been processed, please click <a href='<%=(String)request.getParameter("return")%>'>here</a> to return.</b>