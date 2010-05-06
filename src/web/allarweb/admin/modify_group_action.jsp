<%@ include file="include.txt"%>
<%
	String group = request.getParameter("newName");
	String oldGroup = request.getParameter("name");
	Enumeration enum = request.getParameterNames();
	String[] parts;
	Map rightMap = new HashMap();
	String temp;
	Rights right;
	Resource resource;
	admin.updateGroup(oldGroup,group);
	SecurityModel security = admin.getGroupRights(group);
	while(enum.hasMoreElements())
	{
		temp = (String)enum.nextElement();
		if(temp.indexOf("|") > -1)
		{
			parts = Functions.split(temp,"|");
			resource = new Resource(parts[1],Integer.parseInt(parts[2]));
			if((right = (Rights)rightMap.get(resource)) == null)
			{
				right = new Rights(resource);
				rightMap.put(resource,right);
			}
			if(!parts[0].equals("none"))
			{
				if(resource.getType() == Resource.PROTECTED_FIELD)
				{
					if(!security.getPermission(resource,Rights.READ) &&
						!security.getPermission(resource,Rights.DOWNLOAD) &&
						!security.getPermission(resource,Rights.ORDER))
					{
						right.setRight(Rights.READ,true);
						right.setRight(Rights.DOWNLOAD,true);
						right.setRight(Rights.ORDER,true);
					}
					else
					{
						right.setRight(Rights.READ,security.getPermission(resource,Rights.READ));
						right.setRight(Rights.DOWNLOAD,security.getPermission(resource,Rights.DOWNLOAD));
						right.setRight(Rights.ORDER,security.getPermission(resource,Rights.ORDER));
					}
				}
				else
					right.setRight(parts[0],true);			
			}
		}
	}
	Iterator it = rightMap.keySet().iterator();
	while(it.hasNext())
	{
		resource = (Resource)it.next();
		admin.updateGroupRights(group,(Rights)rightMap.get(resource));
	}
	response.sendRedirect("groups.jsp");
%>
