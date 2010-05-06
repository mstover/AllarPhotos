<%@ page import = "java.net.*" %>
<html>
<head>
	<title>Untitled</title>
</head>
<% String receiver = request.getParameter("receiver");
	String sendTo = request.getParameter("sendto");
	String subject = request.getParameter("subject");
%>
<body BGColor="#ffffff">
<DIV ALIGN="center"><H2>Send a question or comment to <%=receiver%></H2></DIV>

<FORM ACTION="/feedback_action.jsp" METHOD="POST">
<DIV ALIGN="center"><TABLE><TR VALIGN="TOP"><TD>Your Name:</TD> <TD><INPUT TYPE="Text" NAME="name"></TD></TR>
<TR VALIGN="TOP"><TD>Your Email address:</TD> <TD><INPUT TYPE="Text" NAME="email"></TD></TR>
<TR VALIGN="TOP"><TD>Your phone number (optional):</TD><TD><INPUT TYPE="Text" NAME="phone"></TD></TR>
<TR VALIGN="TOP"><TD>The Image Library you are using:</TD><TD><INPUT TYPE="Text" NAME="Library"></TD></TR>
<TR VALIGN="TOP"><TD>The Image name (if applicable) your question pertains to:</TD><TD><INPUT TYPE="Text" NAME="Image">
<INPUT TYPE="Hidden" NAME="sendto" VALUE="<%=sendTo%>">
<INPUT TYPE="Hidden" NAME="subject" VALUE="<%=subject%>"></TD></TR>
<TR VALIGN="TOP"><TD>If you have a question:</TD><TD><TEXTAREA NAME="Question" COLS="40" ROWS="6" WRAP="VIRTUAL"></TEXTAREA></TD></TR>
<TR VALIGN="TOP"><TD>If you have a comment:</TD><TD><TEXTAREA NAME="Comment" COLS="40" ROWS="6" WRAP="VIRTUAL"></TEXTAREA></TD></TR></TABLE>
<INPUT TYPE="Submit"></DIV>
</FORM>



</body>
</html>
