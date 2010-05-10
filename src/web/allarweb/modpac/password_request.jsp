<%@ include file="/modpac/include/global.inc" %>
<%@ include file="/modpac/include/handleActions.inc" %>
<jsp:useBean id="passwordMessage" scope="request" class="com.allarphoto.client.beans.StringBean"/>
<jsp:useBean id="passwordSuccess" scope="request" class="com.allarphoto.client.beans.StringBean"/>

<% String title = "Password Request", banner = "login", help = "password"; // page title and banner image %>
<!--Header that every page contains-->
<%@ include file="header.txt"%>
<c:if test="${!empty errors.errors}">
	<h3>An Error Occurred</h3>
	<c:forEach var="err" items="${errors.errors}">
		<c:out value="${err.message}"/><BR>
	</c:forEach>
</c:if>

<c:if test="${!empty passwordMessage.value}">
	<p><c:out value='${passwordMessage.value}'/><br>
</c:if>

<c:choose>
	<c:when test='${passwordSuccess.value == "true"}'>
		<p><a href="index.jsp">Click here to return to the login page</a>
	</c:when>
	<c:otherwise>
		<p><b>Password Request:</b>
		<p>Please enter your username and email address below.<br>
			Your password will be sent to you via email.
		<form action="password_request.jsp" method="post">
		<input type="hidden" name="actiona" value="action_password_request">
		<TABLE CELLSPACING=0 CELLPADDING=5 BORDER=0>
		<tr align="top">
			<td>
			</td></tr>
		<TR VALIGN="TOP"><TD><H4>Username:</H4></TD><TD><INPUT TYPE="Text" NAME="request_username" SIZE="25"></TD></TR>
		<TR VALIGN="TOP"><TD><H4>Email Address:</H4></TD><TD><INPUT TYPE="text" NAME="request_email" SIZE="25"></TD></TR>
		</TABLE>
		<INPUT TYPE="Submit" NAME="go" VALUE="Get Password">
		<form>
	</c:otherwise>
</c:choose>
<!--Footer that every page contains-->
<%@ include file="footer.jsp"%>