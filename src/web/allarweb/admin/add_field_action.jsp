<%@ include file="include.txt"%>
<%
	DatabaseApplicationController topController = (DatabaseApplicationController)application.getAttribute("lazerweb.controller");

synchronized(syncObj)
{

	ProductFamily family = admin.getProductFamily(request.getParameter("tableName"));
	ProductField field = new ProductField();
	String newName = request.getParameter("fieldName");
	if(!newName.equals(""))
	{
		field.setName(newName);
		try{
			field.setSearchOrder(Integer.parseInt(request.getParameter("searchOrder")));
			field.setDisplayOrder(Integer.parseInt(request.getParameter("displayOrder")));
			field.setType(Integer.parseInt(request.getParameter("type")));
			if(request.getParameter("protected") != null)
				field.setType(field.getType() * (-1));
			admin.addProductField(family, field);
		}catch(NumberFormatException e) {
			response.sendRedirect("error.jsp?error="+URLEncoder.encode(e.toString()));
		}
	}
	
	response.sendRedirect("fields.jsp?page=add_field&library="+family.getTableName());
}
admin.reInit();
%>

<%! Object syncObj = new Object(); %>
