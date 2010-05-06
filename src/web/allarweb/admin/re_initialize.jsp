<%@ include file="include.txt"%>
<%
	String username = admin.getUser().getUsername();
	String password = admin.getUser().getPassword();
	admin.reInit();
	String temp;
	Enumeration reinit = application.getAttributeNames();
	for(;reinit.hasMoreElements();)
	{
		temp = (String)reinit.nextElement();
		if(temp.startsWith("lazerweb."))
			application.removeAttribute(temp);
	}
	response.sendRedirect("login.jsp?username="+username+"&password="+password);
%>
<a href="index.jsp">Home</a>