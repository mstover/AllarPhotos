<%@ include file="include.txt"%>
<%
	request.setAttribute("title","Library Administration");
%>
<jsp:include page="header.jsp"/>
<TABLE CELLPADDING=5 CELLSPACING=5 BORDER=0>
<TR VALIGN="TOP"><td colspan="2" align="CENTER" class="navigation">
<% if(admin.listFamilies().length > 0) {
	 %><a href="libraries.jsp?page=add_library">Add Library</a> | <a href="libraries.jsp?page=import_data">Import Data</a> | <a href="libraries.jsp?page=export_data">Export Data</a> | <a href="re_initialize.jsp">Re-Initialize Lazerweb</a><%
	}
%></td></tr>
<TR VALIGN="TOP">
	<TD><jsp:include page="library_list.jsp"/></td>
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