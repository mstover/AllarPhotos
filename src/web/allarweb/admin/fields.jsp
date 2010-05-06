

<%@ include file="include.txt"%>
<%
	request.setAttribute("title","Library Administration");
	String[] libraries = admin.listFamilies();
	
%>
<jsp:include page="header.jsp"/>
<input type="hidden" name="listReturn" value="fields">
<TABLE CELLPADDING=5 CELLSPACING=5 BORDER=0>
<TR VALIGN="TOP">
	<TD>
	Select a Library<BR>
<%
	for(int x = 0;x < libraries.length;x++)
	{
		%><a href="fields.jsp?library=<%=libraries[x]%>"><%=libraries[x]%></a><BR><%
	}
%>
</td>	
<td>
<%	String library = request.getParameter("library");
	if(library != null){ %>
		<div align="CENTER" class="navigation">
		<a href="fields.jsp?page=add_field&library=<%=library%>">Add Field</a> | <a href="fields.jsp?page=modify_fields&library=<%=library%>">Modify Field</a> | <a href="re_initialize.jsp">Re-Initialize Lazerweb</a>
		</div><%

		String view = request.getParameter("page");
		if(view != null)
		{
			view += ".jsp";
			%><jsp:include page="<%=view%>"/><%
		}else{
			%><h3><%=library%> Library</h3><%
		}
	}
%>
	</td>
</tr></table> 

<%@ include file="footer.txt" %>