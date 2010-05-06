<%@ include file="include.txt"%>
<%
	request.setAttribute("title","Group Administration");
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
	<%
		String view = request.getParameter("page");
		if(view != null)
		{
			view += ".jsp";
			%><jsp:include page="<%=view%>"/><%
		}
	%>
	</td>
</tr></table>

<%@ include file="footer.txt" %>