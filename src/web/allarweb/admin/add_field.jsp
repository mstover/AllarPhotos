<%@ include file="include.txt"%>
<%
	ProductFamily family = admin.getProductFamily(request.getParameter("library"));
	ProductField[] fields = family.getFields();
	String[] type = {"Numeric","Description","Category"};
	int index = 0;
%>
<div align="center"><h3>Add Field for <%=family.getDescriptiveName()%></h3></div><div align="center"><form action="add_field_action.jsp" method="POST">
<input type="Hidden" name="tableName" value="<%=family.getTableName()%>">
<table>
<tr><TD>&nbsp;</td>
<td>Field Name</td><td>Search Order</td><td>Display Order</td><TD>Category Type(numeric, description, or category)</td>
</tr>
<tr><TD><b>New Field:</b></td>
	<td><input type="text" name="fieldName" value=""></td>
	<td><input type="text" size="15" name="searchOrder" value=""></td>
	<td><input type="text" size="15" name="displayOrder" value=""></td>
	<TD><SELECT NAME="type" SIZE=1>
		<OPTION VALUE="<%=ProductField.CATEGORY%>">Category
		<OPTION VALUE="<%=ProductField.DESCRIPTION%>">DESCRIPTION
		<OPTION VALUE="<%=ProductField.NUMERICAL%>">NUMERICAL
		</select></td>
	<TD>Protected: <INPUT TYPE="checkbox" NAME="protected" VALUE="yes"></TD>
	</tr>
<%for(int i=0;i<fields.length;i++){ %>
	<tr><TD>&nbsp;</td>
	<td><b><%=fields[i].getName()%></b></td>
	<td align="CENTER"><%=fields[i].getSearchOrder()%></td>
	<td align="CENTER"><%=fields[i].getDisplayOrder()%></td>
	<% switch(fields[i].getType())
	   {
	   	case ProductField.NUMERICAL : index = 0;break;
		case ProductField.DESCRIPTION : index = 1; break;
		case ProductField.CATEGORY : index = 2;break;
	   }
	%>
	<TD align="CENTER"><%=type[index]%></td>
	</tr>
<%	}%>
<tr><td align="center" colspan="3"><input type="submit" name="submit" value="Submit"></td></tr>
</table>
</form>
</div>

<%@ include file="footer.txt" %>
