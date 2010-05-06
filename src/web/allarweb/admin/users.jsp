<%@ include file="include.txt"%>
<%
	String groupChoice = request.getParameter("source_group");
	String[] groupList;
	if(groupChoice == null)
		groupChoice = (String)session.getValue("group");
        if(groupChoice == null)
          groupChoice = "All";
        session.putValue("group",groupChoice);
	groupList = admin.listGroups();
	Functions.sortAsc(groupList, 0, groupList.length);
	request.setAttribute("title","User Administration");
	
	//sets flag for the show user details link
	boolean showDetails = false;
	for(int i=0;i<groupList.length;i++){
		if(admin.getGroupRights(groupList[i]).getPermission("admin",Resource.GROUP,Rights.ADMIN)){
			showDetails=true;
		}	
	}
	
	
%>
<jsp:include page="header.jsp"/>
<TABLE CELLPADDING=5 CELLASPACING=5 BORDER=0>
<TR VALIGN="TOP"><td colspan="2" align="CENTER" class="navigation">
<% if(admin.listGroups().length > 0) {
		 %><a href="users.jsp?page=add_user&source_group=<%=URLEncoder.encode(groupChoice)%>">Add user</a> | <a href="users.jsp?page=group_membership&source_group=<%=URLEncoder.encode(groupChoice)%>">Group Membership</a><%if(showDetails){%> | <a href="users.jsp?page=user_details&source_group=<%=URLEncoder.encode(groupChoice)%>">User Details</a><%}%><%
	}
%></td></tr>
<TR VALIGN="TOP">
	<TD><form action="users.jsp" method="POST">Select a group<BR>
	<select name="source_group" size=1 onChange="submit();">
<% if(groupChoice.equals("All")) {
	 %><OPTION value="All" SELECTED>All<%
	}
	else {
	 %><OPTION value="All">All<%
	}
	for(int x = 0;x < groupList.length;x++)
	{
		if(groupChoice.equals(groupList[x])) {
			%><OPTION VALUE="<%=groupList[x]%>" SELECTED><%=groupList[x]%>
			<%
		}
		else {
			%><OPTION VALUE="<%=groupList[x]%>"><%=groupList[x]%>
			<%
		}
	}
%>
</select></form>
	<% request.setAttribute("group_choice",groupChoice); %>
	<jsp:include page="user_list.jsp"/></td>
	<TD>
	<%
		String view = request.getParameter("page");
		if(view != null)
		{
			view += ".jsp";
			%><jsp:include page="<%=view%>"/><%
		}
	%>
	</td>
</tr></table>

<%@ include file="footer.txt" %>