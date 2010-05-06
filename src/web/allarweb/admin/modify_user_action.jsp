<%@ include file="include.txt"%>
<%
	String expDate;
	CommerceUser user = admin.getUser(request.getParameter("username"));
	WebBean.setValues(user,request);
	String temp;
	if(((expDate = request.getParameter("expiration_date")) != null) && !expDate.equals(""))
	{
		GregorianCalendar cal;
		try{
			cal = Functions.getDate(expDate);
			if(cal != null)
				user.setExpDate(cal);
		}catch(Exception e){}
	}
	else
		user.setExpDate((GregorianCalendar)null);			
	if(!(user.getPassword().equals(user.getPasswordConfirm()))) {
		response.sendRedirect("users.jsp?success=false&action=modifyuser");
	}
	else
	{
		admin.updateUser(user);
		String[] g = request.getParameterValues("groups");
		if(g != null)
		{
			Set newGroups = new HashSet(),oldGroups = new HashSet();
			for(int x = 0;x < g.length;newGroups.add(g[x++]));
			g = user.getGroups();
			for(int x = 0;x < g.length;oldGroups.add(g[x++]));
			Iterator it = newGroups.iterator();
			while(it.hasNext())
			{
				temp = (String)it.next();
				if(!oldGroups.contains(temp))
					admin.addUserToGroup(user.getUsername(),temp);
			}
			it = oldGroups.iterator();
			while(it.hasNext())
			{
				temp = (String)it.next();
				if(!newGroups.contains(temp))
					admin.removeUserFromGroup(user.getUsername(),temp);
			}
		}
		response.sendRedirect("users.jsp?success=true&action=adduser");
	}
%>


	
	
	