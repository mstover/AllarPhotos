<%@ include file="include.txt"%>
<%
	DatabaseApplicationController topController = (DatabaseApplicationController)application.getAttribute("lazerweb.controller");
	
%>
<FORM action="add_user_action.jsp" METHOD="POST">
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
<TR><TD>Expiration Date (Leave blank if non wanted):</TD>
<TD><INPUT TYPE="text" NAME="expiration_date" SIZE="25"></TD></TR>
	

</TABLE></td></tr></table>
<INPUT TYPE="Submit" NAME="submit" VALUE="Add User">
</FORM>