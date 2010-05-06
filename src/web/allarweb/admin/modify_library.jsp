<%@ include file="include.txt"%>
<%
	ProductFamily family = admin.getProductFamily(request.getParameter("library"));
	ProductField[] fields = family.getFields();
	String elect = "",phys = "";
	switch(family.getProductType())
	{
		case CommerceProduct.PHYSICAL : phys = " CHECKED"; break;
		case CommerceProduct.ELECTRONIC : elect = " CHECKED"; break;
	}
%>
<div align="center"><h3>Modify <%=family.getTableName()%> properties</h3></div><div align="center"><form action="modify_library_action.jsp" method="POST">
<input type="Hidden" name="tableName" value="<%=family.getTableName()%>">
<table>
<tr valign="TOP">
	<td>Descriptive Name</td>	
	<td colspan="2"><input type="Text" name="descriptiveName" size=40 value="<%=family.getDescriptiveName()%>"></td>
</tr>
<tr valign="TOP">
	<td>Description</td>		
	<td colspan="2"><textarea name="description" cols=40 rows=2 wrap="VIRTUAL"><%=family.getDescription()%></textarea></td>
</tr>
<tr valign="TOP">
	<td>Order Model</td>		
	<td colspan="2"><input type="Text" size=40 name="orderModelClass" value="<%=family.getOrderModelClass()%>"></td>
</tr>
<tr valign="TOP">
	<td>Product Type</td> 	
	<td colspan="2">Physical<input type="Radio" name="productType" value="<%=CommerceProduct.PHYSICAL%>"<%=phys%>><BR>
		Electronic<input type="Radio" name="productType" value="<%=CommerceProduct.ELECTRONIC%>"<%=elect%>></td>
</tr>
<tr valign="TOP">
	<td>Primary Label</td> 	
	<td colspan="2"><input type="Text" size=40 name="primaryLabel" value="<%=family.getPrimaryLabel()%>"></td>
</tr>
<tr><td align="center" colspan="3"><input type="submit" name="submit" value="Submit"></td></tr>
</table>
</form>
</div>