<%@ include file="include.txt"%>
<%
	String group = request.getParameter("group");
	String[] families = admin.listFamilies();
	Functions.sortAsc(families, 0, families.length);
	String[] groups = admin.listGroups();
	Functions.sortAsc(groups, 0, groups.length);
	String[] merchants = admin.listMerchants();
	Functions.sortAsc(merchants, 0, merchants.length);	
	String[] belongingGroups = admin.getUser().getGroups();
	Functions.sortAsc(belongingGroups, 0, belongingGroups.length);
	HashTree protectedFields = admin.mapFields();
	Set allGroups = new HashSet();
	for(int x = 0;x < groups.length;allGroups.add(groups[x++]));
	for(int x = 0;x < belongingGroups.length;allGroups.add(belongingGroups[x++]));
	SecurityModel security = admin.getGroupRights(group);
	
	String checked;
%>

<form action="modify_group_action.jsp" method="POST">
<table><tr valign="TOP">
	<td colspan="3" align="CENTER">Group Name:<BR><input type="Text" name="newName" VALUE="<%=group%>"><input type="Hidden" name="name" value="<%=group%>"></td>
</tr></table>
<div align="center"><table border="0" cellspacing="0" cellpadding="5">
<tr><td colspan="3" align="CENTER"><h3>Group Permissions</h3></td></tr>
<tr>
	<td align="CENTER" class="tableheader">Libraries</td>
	<td align="CENTER" class="tableheader">Groups</td>
	<td align="CENTER" class="tableheader">Merchants</td>
</tr>
<tr valign="TOP">
	<td><table border="1" cellspacing="0" cellpadding="3">
	<%
		for(int x = 0;x < families.length;x++)
		{
			int size = 1;
			if(protectedFields.containsKey(families[x]))
				size = protectedFields.list(families[x]).size()+1;
			%><TR VALIGN="middle">
			<td ROWSPAN=<%=size%> align="CENTER"><%=families[x]%><input type="Hidden" name="none|<%=families[x]%>|<%=Resource.DATATABLE%>" value="yes"></td>
			<TD COLSPAN=2><%
if(security.getPermission(families[x],Resource.DATATABLE,Rights.ADMIN))
						checked = " CHECKED";
 
				else
 				
	checked = "";
 				%><input type="Checkbox"
name="<%=Rights.ADMIN%>|<%=families[x]%>|<%=Resource.DATATABLE%>"
value="yes"<%=checked%>><A HREF="modify_permission.jsp?group=<%=group%>&resource=<%=families[x]%>&type=<%=Resource.DATATABLE%>&right=<%=Rights.ADMIN%>"><%=Rights.ADMIN%></A>
 			<%
if(security.getPermission(families[x],Resource.DATATABLE,Rights.DOWNLOAD))
						checked = " CHECKED";
 
				else
 				
	checked = "";
 				%><input type="Checkbox"
name="<%=Rights.DOWNLOAD%>|<%=families[x]%>|<%=Resource.DATATABLE%>"
value="yes"<%=checked%>><A HREF="modify_permission.jsp?group=<%=group%>&resource=<%=families[x]%>&type=<%=Resource.DATATABLE%>&right=<%=Rights.DOWNLOAD%>"><%=Rights.DOWNLOAD%><BR>
 			<%
if(security.getPermission(families[x],Resource.DATATABLE,Rights.ORDER))
						checked = " CHECKED";
 
				else
 				
	checked = "";
 				%><input type="Checkbox"
name="<%=Rights.ORDER%>|<%=families[x]%>|<%=Resource.DATATABLE%>"
value="yes"<%=checked%>><A HREF="modify_permission.jsp?group=<%=group%>&resource=<%=families[x]%>&type=<%=Resource.DATATABLE%>&right=<%=Rights.ORDER%>"><%=Rights.ORDER%>
 			<%
if(security.getPermission(families[x],Resource.DATATABLE,Rights.READ))
						checked = " CHECKED";
 
				else
 				
	checked = "";
 				%><input type="Checkbox"
name="<%=Rights.READ%>|<%=families[x]%>|<%=Resource.DATATABLE%>"
value="yes"<%=checked%>><A HREF="modify_permission.jsp?group=<%=group%>&resource=<%=families[x]%>&type=<%=Resource.DATATABLE%>&right=<%=Rights.READ%>"><%=Rights.READ%></td></TR>

		<% 	
		if(protectedFields.containsKey(families[x]))
		{
			for(Iterator y =
protectedFields.list(families[x]).iterator();y.hasNext();)
			{
				String fieldName = (String)y.next();
				%><TR VALIGN="middle"><TD><%=fieldName%></TD>
				<TD nowrap><%
				for(Iterator z = protectedFields.get(families[x]).list(fieldName).iterator();z.hasNext();)
				{
					String valueName = (String)z.next();if(security.getPermission(families[x]+"."+fieldName+"."+valueName,Resource.PROTECTED_FIELD,Rights.READ)) 
						checked = " CHECKED";  
					else  				
						checked = "";
					%><INPUT TYPE="checkbox" name="<%=Rights.READ%>|<%=families[x]+"."+fieldName+"."+valueName%>|<%=Resource.PROTECTED_FIELD%>" VALUE="yes"<%=checked%>><input type="Hidden" name="none|<%=families[x]+"."+fieldName+"."+valueName%>|<%=Resource.PROTECTED_FIELD%>" value="yes">
					<A HREF="modify_permission.jsp?group=<%=group%>&resource=<%=families[x]+"."+fieldName+"."+valueName%>&type=<%=Resource.PROTECTED_FIELD%>"><%=valueName%>
					<BR><%
				} %></TD></TR><%
			} 
		} 
	}
 
%>			
 	</table></td>
 	<td><table border="1"
cellspacing="0" cellpadding="3">
 	<%
 		for(int x = 0;x <
groups.length;x++)
 		{
 			%><TR VALIGN="middle">
			<td align="CENTER"><%=groups[x]%><input type="Hidden" name="none|<%=groups[x]%>|<%=Resource.GROUP%>" value="yes"></td>
			<TD><% if(security.getPermission(groups[x],Resource.GROUP,Rights.ADMIN))
						checked = " CHECKED";
					else
						checked = "";
				%><input type="Checkbox" name="<%=Rights.ADMIN%>|<%=groups[x]%>|<%=Resource.GROUP%>" value="yes"<%=checked%>><%=Rights.ADMIN%></td></tr><%
		}
	%>			
	</table></td>
	<td><table border="1" cellspacing="0" cellpadding="3">
	<%
		for(int x = 0;x < merchants.length;x++)
		{
			%><TR VALIGN="middle">
			<td align="CENTER"><%=merchants[x]%><input type="Hidden" name="none|<%=merchants[x]%>|<%=Resource.MERCHANT%>" value="yes"></td>
			<TD><% if(security.getPermission(merchants[x],Resource.MERCHANT,Rights.ADMIN))
						checked = " CHECKED";
					else
						checked = "";
				%><input type="Checkbox" name="<%=Rights.ADMIN%>|<%=merchants[x]%>|<%=Resource.MERCHANT%>" value="yes"<%=checked%>><%=Rights.ADMIN%></td></tr><%
		}
	%>			
	</table></td>
</tr>


</table><input type="Submit"></div>
</form>
