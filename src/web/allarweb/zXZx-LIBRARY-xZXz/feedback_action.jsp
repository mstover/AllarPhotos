<%@ include file="include.txt"%>
<c:set var='title' value='${title} - Feedback Confirmation' />
<% // **** NEED TO GET THIS PROPERLY FORMATTED ****
	String help = "verify";

	String sendto = request.getParameter("sendto");
	String name = request.getParameter("name");
	String email = request.getParameter("email");
	String subject = request.getParameter("subject");
	String emailHost = controller.getConfigValue("email_host");
	String temp,value;
	Enumeration params = request.getParameterNames();
	StringBuffer msg = new StringBuffer("");
	msg.append("Comment/question from "+name+" ("+email+") regarding "+subject+"\n");
	
	while(params.hasMoreElements())
	{
		temp = (String)params.nextElement();
		if(!temp.equals("name") && !temp.equals("subject") && !temp.equals("email") && !temp.equals("sendto"))
		{
			if(!(value = request.getParameter(temp)).equals(""))
				msg.append(temp+": "+value+"\n\n");
		}
	}
	boolean goodName = true,goodEmail = true;
	if(name.length() == 0)
		goodName = false;
	if(email.indexOf("@")<0 || email.indexOf(".")<0)
		goodEmail = false;
	if(goodName && goodEmail)
		Functions.email(emailHost,email,sendto,subject,msg.toString());
%>
<%@ include file="header.txt"%>
<% if(goodEmail && goodName) 
	{
		%><DIV ALIGN="center"><H3>Your message has been received.</H3>
		You may return to the library.</DIV><%
	}
	else
	{
		%><H1>Your message was not sent.</H1>
		<UL><%
		if(!goodEmail)
		{
			%><LI>You must enter a valid email address<%
		}
		if(!goodName)
		{
			%><LI>You must enter your name<%
		}
		%><UL>Go <A HREF="javascript:history.back()">back</A> and try again<%
	}
%>
<br>&nbsp;<br>
<%@ include file="footer.jsp"%>