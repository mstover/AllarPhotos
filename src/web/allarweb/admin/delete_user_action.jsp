<%@ include file="include.txt"%>
<%
	Enumeration inputs = request.getParameterNames();
  while(inputs.hasMoreElements())
  {
      String name = (String)inputs.nextElement();
      String value = request.getParameter(name);
      if(value.equals("delete"))
        admin.deleteUser(name);
  }

  response.sendRedirect("users.jsp");
%>




