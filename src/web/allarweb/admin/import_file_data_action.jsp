<%@ include file="include.txt"%>
<%
	admin.importProductData(new FileInputStream(request.getParameter("import_file")));
	response.sendRedirect("libraries.jsp");
%>