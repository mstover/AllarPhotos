<%@ page import = "java.io.*,com.lazerinc.application.*,com.lazerinc.dbtools.*, com.lazerinc.ecommerce.*, com.lazerinc.utils.*, java.net.*, com.lazerinc.ecommerce.impl.*, java.util.*, com.lazerinc.beans.*"%>
<%
	StringBuffer emailMessage = new StringBuffer("The following list include images that need to be processed for the Fisher-Price B2B site:\n");
	boolean emailFlag = false;
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
		
	String table,id,field,name;
	String[] parts = {"","","","",""};
	Enumeration enum = request.getParameterNames();
	while(enum.hasMoreElements()){
		name = (String)enum.nextElement();
		parts = Functions.split(name, "|");
		if(parts[0].equals("modify")){
			table = parts[1];
			id = parts[2];
			field = parts[3];		
			Product p = admin.getProduct(table,Integer.parseInt(id));
			ProductFamily family = admin.getProductFamily(table);
			ProductField[] fields = family.getFields();
			String value = request.getParameter(name);
			for(int x = 0;x < fields.length;x++)
			{
				if(field.equals(fields[x].getName()))
				{
					if(value != null && !value.equals("null"))
					{
						switch(Math.abs(fields[x].getType()))
						{
							case ProductField.CATEGORY : {
								p.setValue(field,value);
								if (field.equals("Approval Status") && value.equals("Yes")){
									emailMessage.append(p.getPrimary()+"\n");
									emailFlag = true;
								}
								break;
							}
							case ProductField.PRIMARY : p.setPrimary(value);break;
							case ProductField.DESCRIPTION : p.setValue(field,value);break;
							case ProductField.NUMERICAL : p.setValue(field,new Float(value));break;
						}
					}
				}
			}
			admin.updateProduct(p); 
		}
	}
	if(emailFlag){
		emailB2BAlert(emailMessage.toString());
	}
	
	response.sendRedirect(request.getParameter("return"));%>
	
Click <a href='<%=(String)request.getParameter("return")%>'>here</a> to return to <%=(String)request.getParameter("return")%>.

<%!
	void emailB2BAlert(String message){
		Functions.email("mail.lazerinc-image.com","webmaster@lazerinc.com","paulr@lazerinc.com","FP B2B Admin Alert", message);
	}
%>