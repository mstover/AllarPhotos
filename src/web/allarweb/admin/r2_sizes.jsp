<%@ page import = "java.io.*,com.lazerinc.application.*,com.lazerinc.dbtools.*, com.lazerinc.ecommerce.*, com.lazerinc.utils.*, java.net.*, com.lazerinc.ecommerce.impl.*, java.util.*, com.lazerinc.beans.*"%>
<%
	String r2Path = "N:/";
	File r2Dir = new File(r2Path);
	String[] r2Libs = r2Dir.list();
	String[] r2Sizes = new String[r2Libs.length];
	File myTemp;
	for(int i=0; i<r2Libs.length; i++)
	{
		myTemp = new File(r2Path+r2Libs[i]);
		r2Sizes[i] = "" + (myTemp.length()/1024/1024);
	}
%>
<html>
<head>
</head>
<body>
	<table border=2>
		<tr>
			<td style='width: 75px; color: #FFF; background-color: #FF9;'>Folder</td>
			<td style='width: 75px; color: #FFF; background-color: #FF9;'>Size-MB</td>
		</tr>
	<% for(int ct=0; ct<r2Libs.length; ct++)
	{ %>
		<tr>
			<td><%= r2Libs[ct] %></td>
			<td><%= r2Sizes[ct] %></td>
		</tr><%
	} %>
	</table>
</body>
</html>