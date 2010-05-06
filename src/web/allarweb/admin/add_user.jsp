<%@ include file="include.txt"%>

<FORM action="add_user_action.jsp" METHOD="POST"><%
if(null != request.getParameter("success") && !request.getParameter("success").equals("true")){
	if(request.getParameter("success").equals("noGroup")){%>
		<div align="center">
		<h2>There was an error with your user input.</h2>
		<b>You must specify a group.<br>
		Please try again.</b><br>&nbsp;<br></div><%
	}else if(request.getParameter("success").equals("oldUser")){%>
		<div align="center">
		<h2>There was an error with your user input.</h2>
		<b>The &quot;<%if(null != request.getParameter("oldName")){%><%=request.getParameter("oldName")%><%}%>&quot; username already exists.<br>
		Please try again with a different username.</b><br>&nbsp;<br></div><%
	}else{%>
		<div align="center">
		<h2>There was an error with your user input.</h2>
		<b>Please enter the password and password confirmation carefully.<br>
		Please try again.</b><br>&nbsp;<br></div><%
	}
}%>
<DIV ALIGN="center">Please fill in the following information for the user. <BR>
* - indicates a required field.

<table><tr valign="TOP">
	<td>Choose user's group membership<BR>
	<% String[] groupList = admin.listGroups(); 
		int z = groupList.length;
		Functions.sortAsc(groupList, 0, z);
		if(z > 25)
			z = 25;
	%>
	<SELECT name="groupsList" multiple size="<%=z%>">
	<% for(int y = 0;y < groupList.length;y++) {
		%><OPTION value="<%=groupList[y]%>"><%=groupList[y]%><%
		}
	%></SELECT>
	</td>
<td><TABLE CELLPADDING=5 CELLSPACING=5 BORDER=0>
<TR><TD>*Username:</TD><TD><INPUT TYPE="Text" NAME="username" SIZE="25"></TD></TR>
<TR><TD>*Password:</TD><TD><INPUT TYPE="Password" NAME="password" SIZE="25"></TD></TR>
<TR><TD>*Confirm Password:</TD><TD><INPUT TYPE="Password" NAME="passwordConfirm" SIZE="25"></TD></TR>
<TR><TD>Expiration Date:<br>(mm/dd/yy - Leave blank if none wanted)</TD>
<TD><INPUT TYPE="text" NAME="expiration_date" SIZE="25"></TD></TR>
	

</TABLE></td></tr></table>
<INPUT TYPE="Submit" NAME="submit" VALUE="Add User">
</FORM>