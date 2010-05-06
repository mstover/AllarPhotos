<%@ include file="include.txt"%>
<%
	CommerceUser user = new CommerceUser();
	WebBean.setValues(user,request);
	admin.setUser(user);
	if(admin.checkUser()){
		response.sendRedirect("start.jsp");
	}
	else{
		response.sendRedirect("bad_login.jsp");
	}
%>