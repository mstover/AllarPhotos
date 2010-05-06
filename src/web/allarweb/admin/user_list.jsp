<%@ include file="include.txt"%>
<%
	String groupChoice = (String)request.getAttribute("group_choice");
	String[] userList;
	if(groupChoice.equals("All")){
		userList = admin.listUsers();
		Functions.sortAsc(userList, 0, userList.length);
	}else{
		userList = admin.listUsers(groupChoice);
		Functions.sortAsc(userList, 0, userList.length);
	}
%>
<FORM ACTION="delete_user_action.jsp" METHOD="POST">
<TABLE CELLSPACING=0 CELLPADDING=3 BORDER=1>
<TR><TH>Click Username to modify</TH><TH><INPUT TYPE="SUBMIT" VALUE="Delete"></TH></TR>
<%
	for(int x = 0;x < userList.length;x++)
	{
		%><TR VALIGN="TOP"><TD><a href="users.jsp?page=modify_user&user=<%=URLEncoder.encode(userList[x])%>&source_group=<%=URLEncoder.encode(groupChoice)%>"><%=userList[x]%></a></TD><TD><INPUT type="checkbox" name="<%=userList[x]%>" value="delete"></TD></TR><%
	}
%>
</TABLE></FORM>
