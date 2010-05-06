<%@ include file="include.txt"%>
<%
	String[] su = request.getParameterValues("userlist");
	String[] sg = request.getParameterValues("grouplist");	
	String action = request.getParameter("submit");
	if(su != null && sg != null)
	{
		for(int x = 0;x < su.length;x++)
		{
			for(int y = 0;y < sg.length;y++)
			{
				if(action.startsWith("Remove"))
					admin.removeUserFromGroup(su[x],sg[y]);
				else if(action.startsWith("Add"))
					admin.addUserToGroup(su[x],sg[y]);
			}
		}
	}
	response.sendRedirect("users.jsp?success=true&action=groupmembership");
%>