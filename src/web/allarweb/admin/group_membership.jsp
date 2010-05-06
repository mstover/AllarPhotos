<%@ include file="include.txt"%>
<%
	String groupChoice = request.getParameter("source_group");
	Set selectUsers = new HashSet();
	String[] tempList;
	if(groupChoice != null)
		tempList = admin.listUsers(groupChoice);
	else
	{
		groupChoice = "ALL";
		tempList = new String[0];
	}
	for(int x = 0;x < tempList.length;selectUsers.add(tempList[x++]));
	String[] userList = admin.listUsers();
	Functions.sortAsc(userList, 0, userList.length);
	String[] groupList = admin.listGroups();
	Functions.sortAsc(groupList, 0, groupList.length);
	int userListSize = userList.length, groupListSize = groupList.length;
	if(userListSize > 25)
		userListSize = 25;
	if(groupListSize > 25)
		groupListSize = 25;
%>

<form action="group_membership_action.jsp" method="POST">
<table cellspacing="0" cellpadding="5" BORDER=0>
<tr>
	<td align="CENTER" class="tableheader">User List</td>
	<td align="CENTER" class="tableheader">Group List</td>
</tr>
<tr valign="TOP">
	<td align="CENTER"><select name="userlist" multiple size="<%=userListSize%>">
	<%
		for(int x = 0;x < userList.length;x++)
		{
			if(selectUsers.contains(userList[x])) {
				%><OPTION VALUE="<%=userList[x]%>" SELECTED><%=userList[x]%><%
			}
			else {
				%><OPTION VALUE="<%=userList[x]%>"><%=userList[x]%><%
			} 
		}
	%>
	</select></td>
	<td align="CENTER"><select name="grouplist" multiple size="<%=groupListSize%>">
	<%
		for(int x = 0;x < groupList.length;x++)
		{
			if(groupChoice.equals(groupList[x])) {
				%><OPTION VALUE="<%=groupList[x]%>" SELECTED><%=groupList[x]%><%
			}
			else {
				%><OPTION VALUE="<%=groupList[x]%>"><%=groupList[x]%><%
			}
		} 
	%>
	</select></td>
</tr>
<tr valign="TOP">
	<td colspan="2" align="CENTER"><input type="Submit" name="submit" value="Remove Selected Users from Selected Groups"><BR><input type="Submit" name="submit" value="Add Selected Users to Selected Groups"></td>
</tr></table>

</form>