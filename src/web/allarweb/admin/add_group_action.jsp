<%@ include file="include.txt"%>
<%
	String group = request.getParameter("name");
	String adminGroup = request.getParameter("admin_group");
	if(adminGroup == null)
		adminGroup = "admin";
	Enumeration enum = request.getParameterNames();
	String temp;
	String[] parts;
	Map rightMap = new HashMap();
	Rights right;
	admin.addGroup(group,adminGroup);
	while(enum.hasMoreElements())
	{
		temp = (String)enum.nextElement();
		if(temp.indexOf("|") > -1)
		{
			parts = Functions.split(temp,"|");
			if((right = (Rights)rightMap.get(parts[1])) == null)
			{
				right = new Rights(new Resource(parts[1],Integer.parseInt(parts[2])));
				rightMap.put(parts[1],right);
			}
			right.setRight(parts[0],true);			
		}
	}
	Iterator it = rightMap.keySet().iterator();
	while(it.hasNext())
	{
		temp = (String)it.next();
		admin.updateGroupRights(group,(Rights)rightMap.get(temp));
	}
	response.sendRedirect("groups.jsp");
%>