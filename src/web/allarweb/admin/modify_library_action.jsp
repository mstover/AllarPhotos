<%@ include file="include.txt"%>
<%
	
	ProductFamily family = admin.getProductFamily(request.getParameter("tableName"));
	ProductField[] fields = family.getFields();
	/*family.setDescriptiveName(request.getParameter("descriptiveName"));
	family.setDescription(request.getParameter("description"));
	family.setOrderModelClass(request.getParameter("orderModelClass"));
	family.setProductType(new Integer(request.getParameter("productType")).intValue());
	family.setPrimaryLabel(request.getParameter("primaryLabel"));*/
	response.sendRedirect("libraries.jsp");
%>