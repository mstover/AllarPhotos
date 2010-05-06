<%@ include file="include.txt"%>
<%
	boolean exp = false;
%>
<FORM action="modify_user_action.jsp" METHOD="POST">
<DIV ALIGN="center">Please fill in the following information for the user. <BR>
* - indicates a required field.
<% 
	CommerceUser user;
	user = admin.getUser(request.getParameter("user"));
	String[] states = admin.getStates();
	String[] countries = admin.getCountries();
	String[] referrers = admin.getReferrers();
	String[] industries = admin.getIndustries();
	Company comp = user.getCompany();
	String[] gList = user.getGroups();
	Set list = new HashSet();
	for(int y = 0;y < gList.length;list.add(gList[y++]));
%>

<table><tr valign="TOP">
	<td>Choose user's group membership<BR>
	<% String[] groupList = admin.listGroups(); 
		int z = groupList.length;
		Functions.sortAsc(groupList, 0, z);
		if(z > 25)
			z = 25;
	%>
	<SELECT name="groups" multiple size="<%=z%>">
	<% for(int y = 0;y < groupList.length;y++) {
		if(list.contains(groupList[y])) {
		exp = true;
		%><OPTION value="<%=groupList[y]%>" SELECTED><%=groupList[y]%><%
		}
		else {
		%><OPTION value="<%=groupList[y]%>"><%=groupList[y]%><%
		}
	}
	%></SELECT>
	</td>
<td><TABLE CELLPADDING=5 CELLSPACING=5 BORDER=0>
<TR><TD>*Username:</TD><TD><%=user.getUsername()%><INPUT TYPE="hidden" NAME="username" VALUE="<%=user.getUsername()%>"></TD></TR>
<TR><TD>*Password:</TD><TD><INPUT TYPE="Password" NAME="password" VALUE="<%=user.getPassword()%>" SIZE="25"></TD></TR>
<TR><TD>*Confirm Password:</TD><TD><INPUT TYPE="Password" NAME="passwordConfirm" VALUE="<%=user.getPassword()%>" SIZE="25"></TD></TR>
<%
if(exp) {
%><TR><TD>Expiration Date (Leave blank if none):</TD>
		<TD>
		<% GregorianCalendar cal = user.getExpDate();
			String dateValue = new String();
			if(cal != null)
				dateValue = (cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.DAY_OF_MONTH)+"/"+
								cal.get(Calendar.YEAR);
		%><INPUT TYPE="text" NAME="expiration_date" VALUE="<%=dateValue%>"></TD></TR><% 
}
%>
</TABLE></td></tr></table>
<INPUT TYPE="Submit" NAME="submit" VALUE="Modify User">
</FORM>