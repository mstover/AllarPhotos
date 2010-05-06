<%@ include file="include.txt"%>
<%
	String[] groups = admin.listGroups();
	Functions.sortAsc(groups, 0, groups.length);
%>
<FORM ACTION="delete_group_action.jsp" METHOD="POST">
<TABLE CELLSPACING=0 CELLPADDING=3 BORDER=1>
<TR><TH>Click Group to modify</TH><TH><INPUT TYPE="SUBMIT" VALUE="Delete"></TH></TR>
<%
	for(int x = 0;x < groups.length;x++) {
		%><TR VALIGN="TOP"><TD><a href="groups.jsp?page=modify_group&group=<%=URLEncoder.encode(groups[x])%>"><%=groups[x]%></a></TD><TD><INPUT type="checkbox" name="<%=groups[x]%>" value="delete"></TD></TR><%
	}
%>
</TABLE></FORM>