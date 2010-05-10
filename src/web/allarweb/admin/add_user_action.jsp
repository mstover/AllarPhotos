<%@ include file="include.txt"%>
<%
if(null != admin.getUser(request.getParameter("username")) && !admin.getUser(request.getParameter("username")).getUsername().equals("N/A")){
	response.sendRedirect("users.jsp?success=oldUser&oldName="+request.getParameter("username")+"&page=add_user");
}else if(null == request.getParameter("groupsList") || request.getParameter("groupsList").equals("")){
	response.sendRedirect("users.jsp?success=noGroup&page=add_user");
}else{
	String expDate;
	String[] groups = request.getParameterValues("groupsList");
	WebBean.setValues(admin,request);
	CommerceUser user = new CommerceUser();
	WebBean.setValues(user,request);	
	com.allarphoto.beans.Company company = new com.allarphoto.beans.Company();
	company.setName("N/A");
	company.setIndustry("N/A");
	user.setShipAddress1("N/A");
	user.setShipAddress2("N/A");
	user.setShipCity("N/A");
	user.setShipState("N/A");
	user.setShipZip("N/A");
	user.setShipCountry("N/A");
	user.setBillAddress1("N/A");
	user.setBillAddress2("N/A");
	user.setBillCity("N/A");
	user.setBillState("N/A");
	user.setBillZip("N/A");
	user.setBillCountry("N/A");
	user.setReferrer("N/A");
	user.setEmailAddress("N/A");
	user.setFirstName("N/A");
	user.setLastName("N/A");
	user.setMiddleInitial("N/A");
	user.setPhone("N/A");
	user.setFax("N/A");
	user.setCompany(company);
	if(((expDate = request.getParameter("expiration_date")) != null) && !expDate.equals(""))
	{
		GregorianCalendar cal;
		try{
			cal = Functions.getDate(expDate);
			if(cal != null)
				user.setExpDate(cal);
		}catch(Exception e){}
	}
	if(user.getPassword().equals(user.getPasswordConfirm()) && null != groups && groups.length > 0){
		admin.addUser(user,groups);
		response.sendRedirect("users.jsp?success=true&page=add_user");
	}else{
		response.sendRedirect("users.jsp?success=false&page=add_user");
	}
}
%>


	
	
	