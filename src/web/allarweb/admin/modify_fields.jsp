<%@ include file="include.txt"%>
<%
	ProductFamily family = admin.getProductFamily(request.getParameter("library"));
	ProductField[] fields = family.getFields();
%>
<div align="center"><h3>Modify <%=family.getDescriptiveName()%> fields</h3></div><div align="center"><form action="modify_fields_action.jsp" method="POST">
<input type="Hidden" name="tableName" value="<%=family.getTableName()%>">
<table>
<tr>
<tH>Field Name</tH><tH>Search Order</th><th>Display Order</th><TH>Protected Field</TH><TH>Delete Field</TH>
</tr>
<%for(int i=0;i<fields.length;i++)
{ 
	String checked = "";
	if(fields[i].getType() < 0)
		checked = " CHECKED";
	%>
	<tr>
	<td><input type="text" name="fieldName<%=i%>" value="<%=fields[i].getName()%>"></td>
	<td><input type="text" size="15" name="searchOrder<%=i%>" value="<%=fields[i].getSearchOrder()%>"></td>
	<td><input type="text" size="15" name="displayOrder<%=i%>" value="<%=fields[i].getDisplayOrder()%>"></td>
	<TD><%
	if(Math.abs(fields[i].getType()) == ProductField.CATEGORY)
	{
		%>Protected: <INPUT TYPE="checkbox" NAME="protected<%=i%>" VALUE="yes"<%=checked%>><%
	}
	else
	{
		%>&nbsp;<%
	}%>
	</TD><TD>
	<%
	if(Math.abs(fields[i].getType()) == ProductField.PRIMARY)
	{
		%>&nbsp;<%
	}
	else
	{
		%>Delete: <INPUT TYPE="checkbox" NAME="delete<%=i%>" VALUE="yes"><%
	}%>
	</TD></tr>
<%}%>
<tr><td align="center" colspan="3"><input type="submit" name="submit" value="Submit"></td></tr>
</table>
</form>
</div>

<%@ include file="footer.txt" %>
