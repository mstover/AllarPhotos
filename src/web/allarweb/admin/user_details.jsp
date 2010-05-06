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
<div align="center"><h3>User Details for <%=groupChoice%></h3></div>
<TABLE CELLSPACING=0 CELLPADDING=3 BORDER=1 align="center"><%
if(!groupChoice.equals("admin")){%>
	<TR><th>First Name</th><th>Last Name</th><TH>Username</TH><TH><a href="users.jsp?page=user_details_decrypt&source_group=<%=URLEncoder.encode(groupChoice)%>">Password</a></TH><TH>Company</TH><th>Referrer</th></TR><%
}else{%>
	<TR><th>First Name</th><th>Last Name</th><TH>Username</TH><TH>Password</TH><TH>Company</TH><th>Referrer</th></TR><%
}
	for(int x = 0;x < userList.length;x++)
	{
		%><TR VALIGN="TOP">
				<TD><%=admin.getUser(userList[x]).getFirstName()%></TD>
				<TD><%=admin.getUser(userList[x]).getLastName()%></TD>
				<TD><%=userList[x]%></TD>
				<TD><%=Functions.encryptString(admin.getUser(userList[x]).getPassword())%></TD>
				<TD><%=admin.getUser(userList[x]).getCompany().getName()%></TD>
				<TD><%=admin.getUser(userList[x]).getReferrer()%></TD></TR><%
	}
%>
</TABLE>
