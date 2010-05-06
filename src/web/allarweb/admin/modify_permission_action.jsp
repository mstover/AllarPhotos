<%@ include file="include.txt"%>
<%
	String group = request.getParameter("group");
	Enumeration enum = request.getParameterNames();
	String temp,tempRight;
	Rights right;
	GregorianCalendar cal;
	Resource resource = new Resource(request.getParameter("resource"),Integer.parseInt(request.getParameter("type")));
	right = new Rights(resource);
	while(enum.hasMoreElements())
	{
		temp = (String)enum.nextElement();
		if(temp.startsWith("exp_date_"))
		{
			tempRight = temp.substring("exp_date_".length());
			if(request.getParameter(tempRight) == null)
				admin.updateGroupRights(group,resource,tempRight,false,null);
			else
			{
				if(!request.getParameter(temp).equals(""))
				{	
					cal = Functions.getDate(request.getParameter(temp));
					admin.updateGroupRights(group,resource,tempRight,true,cal);
				}
				else
					admin.updateGroupRights(group,resource,tempRight,true,null);
			}
		}
	}
	response.sendRedirect("groups.jsp");
%>