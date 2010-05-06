<%@ page import = "java.io.*,com.lazerinc.application.*,com.lazerinc.dbtools.*, com.lazerinc.ecommerce.*, com.lazerinc.utils.*, java.net.*, com.lazerinc.ecommerce.impl.*, java.util.*, com.lazerinc.beans.*"%>
<%
	if(request.getParameter("oldUsername").equals(request.getParameter("username"))){
		response.sendRedirect("/lazerweb/kodak/dai/relogin.jsp?success=false");
	}else{
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
		String expDate;
	 	String[] groups = request.getParameterValues("groupsList");
		WebBean.setValues(admin,request);
		CommerceUser user = new CommerceUser();
		WebBean.setValues(user,request);
		com.lazerinc.beans.Company company = new com.lazerinc.beans.Company();
		company.setName(request.getParameter("name"));
			String temp;
		company.setIndustry(request.getParameter("industry_text"));
		user.setShipAddress1(request.getParameter("shipAddress1"));
		user.setShipAddress2(request.getParameter("shipAddress2"));
		user.setShipCity(request.getParameter("shipCity"));
		if(null != request.getParameter("shipState")&& !request.getParameter("shipState").equals("choose one")){
			user.setShipState(request.getParameter("shipState"));
		}else{
			user.setShipState("N/A");
		}
		user.setShipZip(request.getParameter("shipZip"));
		if(null != request.getParameter("shipCountry") && !request.getParameter("shipCountry").equals("choose one")){
			user.setShipCountry(request.getParameter("shipCountry"));
		}else{
			user.setShipCountry("N/A");
		}
		user.setBillAddress1("N/A");
		user.setBillAddress2("N/A");
		user.setBillCity("N/A");
		user.setBillState("N/A");
		user.setBillZip("N/A");
		user.setBillCountry("N/A");
		user.setReferrer(request.getParameter("referrer"));
		user.setEmailAddress(request.getParameter("emailAddress"));
		user.setFirstName(request.getParameter("firstName"));
		user.setLastName(request.getParameter("lastName"));
		user.setMiddleInitial(request.getParameter("middleInitial"));
		user.setPhone(request.getParameter("phone"));
		user.setFax(request.getParameter("fax"));
		user.setCompany(company);
		admin.addUser(user,groups);
		commerce.setUser(user);
		commerce.updateUser();
		if(user.getUsername() != null && user.getUsername().length() > 2 &&
				user.getPassword().equals(user.getPasswordConfirm())){
			Functions.email(controller.getConfigValue("email_host"), "webmaster@lazerinc.com", request.getParameter("emailAddress"),
					 "Your new D&AI account", "Group: "+request.getParameter("groupsList")+" A new account has been created in your name for the Kodak D&AI Online Digital Library.\n\r"+
					 "Please maintain this information in your records.\n\r"+
					 "The username is \""+ request.getParameter("username") + "\" and the password is \""+
					 request.getParameter("password")+ ".\"\n\r\n\rIf you believe you received this message in error, "+
					 "please reply to this message with your explanation.\n\r\n\rThank You.\n\r-Kodak D&AI Online Digital Library Webmaster");
			response.sendRedirect("/lazerweb/kodak/dai/relogin.jsp?success=true");
		}else{
			response.sendRedirect("/lazerweb/kodak/dai/relogin.jsp?success=false");
		}
	}
%>

	
	
	