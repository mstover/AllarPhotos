<%@ include file="include.txt"%>
<%
	String[] families = admin.listFamilies();
%>
<form action="export_data_action.jsp" method="POST">
<div align="center"><h3>Select a family to export data from:</h3>
<select name="family" size="1">
<%
	for(int x = 0;x < families.length;x++)
	{
		%><OPTION VALUE="<%=families[x]%>"><%=families[x]%><BR><%
	}
%></select><br>&nbsp;<br>
<div class="sm1">
Enter date (mm/dd/yy) to search since<br>
(if left blank, all data is returned):<br></div>
<input type="text" name="since_date"><br>&nbsp;<br>
<input type="Submit" name="submit" value="Export"></div></form>