<%@ include file="include.txt"%>
<%
	String[] families = admin.listFamilies();
	Functions.sortAsc(families, 0, families.length);
	String[] groups = admin.listGroups();
	Functions.sortAsc(groups, 0, groups.length);
	String[] merchants = admin.listMerchants();
	Functions.sortAsc(merchants, 0, merchants.length);	
	String[] belongingGroups = admin.getUser().getGroups();
	Functions.sortAsc(belongingGroups, 0, belongingGroups.length);
	Set allGroups = new HashSet();
	for(int x = 0;x < groups.length;allGroups.add(groups[x++]));
	for(int x = 0;x < belongingGroups.length;allGroups.add(belongingGroups[x++]));
%>

<form action="add_group_action.jsp" method="POST">
<table><tr valign="TOP">
	<td align="CENTER">Group Name:<BR><input type="Text" name="name"></td>
	<td align="CENTER">Administrating Group<BR><select name="admin_group" size="1">
	<OPTION VALUE="Choose One">Choose One
	<%
		Iterator it = allGroups.iterator();
		while(it.hasNext())
		{
			String temp = (String)it.next();
			%><OPTION VALUE="<%=temp%>"><%=temp%><%
		}
	 %>
	 </select></td>
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
			%><TR VALIGN="middle">
			<td align="CENTER"><%=families[x]%></td>
			<TD><input type="Checkbox" name="<%=Rights.ADMIN%>|<%=families[x]%>|<%=Resource.DATATABLE%>" value="yes"><%=Rights.ADMIN%>
			<input type="Checkbox" name="<%=Rights.DOWNLOAD%>|<%=families[x]%>|<%=Resource.DATATABLE%>" value="yes"><%=Rights.DOWNLOAD%><BR>
			<input type="Checkbox" name="<%=Rights.ORDER%>|<%=families[x]%>|<%=Resource.DATATABLE%>" value="yes"><%=Rights.ORDER%>
			<input type="Checkbox" name="<%=Rights.READ%>|<%=families[x]%>|<%=Resource.DATATABLE%>" value="yes"><%=Rights.READ%></td></tr><%
		}
	%>			
	</table></td>
	<td><table border="1" cellspacing="0" cellpadding="3">
	<%
		for(int x = 0;x < groups.length;x++)
		{
			%><TR VALIGN="middle">
			<td align="CENTER"><%=groups[x]%></td>
			<TD><input type="Checkbox" name="<%=Rights.ADMIN%>|<%=groups[x]%>|<%=Resource.GROUP%>" value="yes"><%=Rights.ADMIN%></td></tr><%
		}
	%>			
	</table></td>
	<td><table border="1" cellspacing="0" cellpadding="3">
	<%
		for(int x = 0;x < merchants.length;x++)
		{
			%><TR VALIGN="middle">
			<td align="CENTER"><%=merchants[x]%></td>
			<TD><input type="Checkbox" name="<%=Rights.ADMIN%>|<%=merchants[x]%>|<%=Resource.MERCHANT%>" value="yes"><%=Rights.ADMIN%></td></tr><%
		}
	%>			
	</table></td>
</tr>


</table><input type="Submit"></div>
</form>