<%@ include file="include.txt"%>
<%
	String[] libraries = admin.listFamilies();
%>
Select a Library<BR>
<%
	for(int x = 0;x < libraries.length;x++)
	{
			%><a href="libraries.jsp?page=modify_library&library=<%=libraries[x]%>"><%=libraries[x]%></a><BR><%
	}
%> 