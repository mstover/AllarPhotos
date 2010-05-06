<%@ include file="include.txt"%>
<%
	String[] merchants = admin.listMerchants();
%>
Select a Merchant<BR>
<%
	for(int x = 0;x < merchants.length;x++)
	{
		%><a href="merchants.jsp?page=modify_merchant&merchant=<%=URLEncoder.encode(merchants[x])%>"><%=merchants[x]%></a><BR><%
	}
%>