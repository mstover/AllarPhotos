<%@ include file="include.txt"%>
<%
	request.setAttribute("title","Thomasville Library Administration");
%>
<jsp:include page="header.jsp"/>
<TABLE CELLPADDING=5 CELLASPACING=5 BORDER=0>
<TR VALIGN="TOP"><td colspan="2" align="CENTER" class="navigation">
<% if(admin.listFamilies().length > 0) {
	 %><a href="merchants.jsp?page=add_merchant">Add Merchant</a><%
	}
%></td></tr>
<TR VALIGN="TOP">
	<TD><jsp:include page="merchant_list.jsp"/></td>
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