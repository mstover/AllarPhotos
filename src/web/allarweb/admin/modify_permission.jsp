<%@ include file="include.txt"%>
<%
	request.setAttribute("title","Group Administration");
	String group = request.getParameter("group");
	String resource = request.getParameter("resource");
	String type = request.getParameter("type");
	String right = request.getParameter("right");
	String checked;
	SecurityModel security = admin.getGroupRights(group);	
%>
<jsp:include  page="header.jsp"/>
<TABLE CELLPADDING=5 CELLASPACING=5 BORDER=0>
<TR VALIGN="TOP"><td colspan="2" align="CENTER" class="navigation">
<% if(admin.listGroups().length > 0) {
	 %><a href="groups.jsp?page=add_group">Add Group</a> | <a href="groups.jsp?page=group_membership">Membership</a><%
	}
%></td></tr>
<TR VALIGN="TOP">
	<TD><jsp:include page="group_list.jsp"/></td>
	<TD>
<div align="center"><H2>For Group <%=group%></h2></div>
<FORM ACTION="modify_permission_action.jsp" METHOD="POST">
<input type="hidden" name="group" value="<%=group%>">
<input type="hidden" name="resource" value="<%=resource%>">
<input type="hidden" name="type" value="<%=type%>">
<% if(right == null)
   { 
   		String[] resources = Functions.split(resource,"."); 
		if(resources.length == 3) 
		{%>
			Database: <%=resources[0] %><BR>
			Field: <%=resources[1] %><BR>
			Value:<%=resources[2] %><BR>
			<HR>
			Right: admin
			<%
if(security.getPermission(resource,Resource.PROTECTED_FIELD,Rights.ADMIN))
						checked = " CHECKED";
 
				else
 				
	checked = "";
 				%><input type="Checkbox"
name="<%=Rights.ADMIN%>"
value="yes"<%=checked%>> 
			Expiration Date (Optional): <INPUT type="text" name="exp_date_<%=Rights.ADMIN%>">
			<BR>
			Right: download
			<%
if(security.getPermission(resource,Resource.PROTECTED_FIELD,Rights.DOWNLOAD))
						checked = " CHECKED"; 
				else 				
	checked = "";
 				%><input type="checkbox"
name="<%=Rights.DOWNLOAD%>"
value="yes"<%=checked%>> 
			Expiration Date (Optional): <INPUT type="text" name="exp_date_<%=Rights.DOWNLOAD%>">
			<BR>
			Right: order
			<%
if(security.getPermission(resource,Resource.PROTECTED_FIELD,Rights.ORDER))
						checked = " CHECKED";
 
				else
 				
	checked = "";
 				%><input type="checkbox"
name="<%=Rights.ORDER%>"
value="yes"<%=checked%>> 
			Expiration Date (Optional): <INPUT type="text" name="exp_date_<%=Rights.ORDER%>">
			<BR>
			Right: read
			<%
if(security.getPermission(resource,Resource.PROTECTED_FIELD,Rights.READ))
						checked = " CHECKED";
 
				else
 				
	checked = "";
 				%><input type="checkbox"
name="<%=Rights.READ%>"
value="yes"<%=checked%>> 
			Expiration Date (Optional): <INPUT type="text" name="exp_date_<%=Rights.READ%>">
		<%}
	}
	else
	{%>
		Resource: <%=resource%><BR>
		Right: <%=right %><%
if(security.getPermission(resource,Integer.parseInt(type),right))
						checked = " CHECKED";
 
				else
 				
	checked = "";
 				%><input type="Checkbox"
name="<%=right%>"
value="yes"<%=checked%>><BR>
		Expiration Date: <INPUT type="text" name="exp_date_<%=right%>">
		
	<%}%>
	<P>
<div align="center"><INPUT type="submit" name="submit" value="Submit"></div>
</FORM>
	

</td>
</tr></table>

<%@ include file="footer.txt" %>