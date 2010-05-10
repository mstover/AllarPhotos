<%@ include file="/zXZx-LIBRARY-xZXz/include/global.inc" %>
<%@ include file="/zXZx-LIBRARY-xZXz/include/handleActions.inc" %>
<jsp:useBean id="user" scope="session" class="com.allarphoto.client.beans.UserBean" />
<jsp:useBean id="feedbackAlert" scope="request" class="com.allarphoto.client.beans.StringBean"/>
<jsp:useBean id="feedbackSuccess" scope="request" class="com.allarphoto.client.beans.StringBean"/>
<jsp:useBean id="feedbackMessage" scope="request" class="com.allarphoto.client.beans.StringBean"/>
<jsp:useBean id="feedbackEmail" scope="request" class="com.allarphoto.client.beans.StringBean"/>
<jsp:useBean id="feedbackFullName" scope="request" class="com.allarphoto.client.beans.StringBean"/>
<jsp:useBean id="feedbackPhoneNo" scope="request" class="com.allarphoto.client.beans.StringBean"/>

<% String title = "Library Feedback Form", banner="help"; %>
<%@ include file="header.txt"%>
<c:choose>
	<c:when test='${null != param.request_email_to || param.request_email_to == ""}'>
		<c:set var='send_to' value='${param.request_email_to}'/>
	</c:when>
	<c:otherwise>
		<c:set var='send_to' value='${configuration.email_webmaster}'/>
	</c:otherwise>
</c:choose>

<c:if test="${!empty errors.errors}">
	<h3>An Error Occurred</h3>
	<c:forEach var="err" items="${errors.errors}">
		<c:out value="${err.message}"/><BR>
	</c:forEach>
</c:if>

<c:if test="${!empty feedbackAlert.value}">
<p>
<table align='center' width='60%'><tr><td align='center'>
	<c:out value='${feedbackAlert.value}'/>
</tr></td></table>
<p>
</c:if>

<c:if test='${feedbackSuccess.value != "true"}'>
	<DIV ALIGN="center"><H2>Send a question or comment to the Webmaster</H2></DIV>
	<FORM ACTION="lazerweb_feedback.jsp" METHOD="POST">
	<input type="hidden" name="actiona" value="action_lazerweb_feedback">

	<DIV ALIGN="center"><TABLE>
	<c:choose>
		<c:when test="${!empty user.username}">
			<TR VALIGN="TOP"><TD>Your Name:</TD><TD><INPUT TYPE="Text" NAME="request_full_name" value="<c:out value='${user.firstName}'/>&nbsp;<c:if test='${ null != user.middleInitial && user.middleInitial != "N/A"}'><c:out value='${user.middleInitial}' /></c:if>&nbsp;<c:out value='${user.lastName}'/>" ></TD></TR>
		</c:when>
		<c:when test='${!empty feedbackFullName}'>
			<TR VALIGN="TOP"><TD>Your Name:</TD><TD><INPUT TYPE="Text" NAME="request_full_name" value="<c:out value='${feedbackFullName.value}'/>" ></TD></TR>
		</c:when>
		<c:otherwise>
			<TR VALIGN="TOP"><TD>Your Name:</TD><TD><INPUT TYPE="Text" NAME="request_full_name"></TD></TR>
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${!empty user.username}">
			<TR VALIGN="TOP"><TD>Your Email address:</TD> <TD><INPUT TYPE="Text" NAME="request_email" value="<c:out value='${user.emailAddress}' />"></TD></TR>
		</c:when>
		<c:when test='${!empty feedbackEmail}'>
			<TR VALIGN="TOP"><TD>Your Email address:</TD> <TD><INPUT TYPE="Text" NAME="request_email" value="<c:out value='${feedbackEmail.value}' />"></TD></TR>
		</c:when>
		<c:otherwise>
			<TR VALIGN="TOP"><TD>Your Email address:</TD> <TD><INPUT TYPE="Text" NAME="request_email"></TD></TR>	
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test='${!empty feedbackPhoneNo}'>
			<TR VALIGN="TOP"><TD>Your phone number (optional):</TD><TD><INPUT TYPE="Text" NAME="request_phone_no" value="<c:out value='${feedbackPhoneNo.value}'/>"></TD></TR>
		</c:when>
		<c:otherwise>
			<TR VALIGN="TOP"><TD>Your phone number (optional):</TD><TD><INPUT TYPE="Text" NAME="request_phone_no"></TD></TR>
		</c:otherwise>
	</c:choose>
	<INPUT TYPE="Hidden" NAME="request_email_to" VALUE="<c:out value='${send_to}' />">
		<c:choose>
		<c:when test='${!empty feedbackMessage}'>
			<TR VALIGN="TOP"><TD>Your comments or questions:</TD><TD><TEXTAREA NAME="request_message" COLS="40" ROWS="6" WRAP="VIRTUAL" ><c:out value='${feedbackMessage.value}'/></TEXTAREA></TD></TR>
		</c:when>
		<c:otherwise>
			<TR VALIGN="TOP"><TD>Your comments or questions:</TD><TD><TEXTAREA NAME="request_message" COLS="40" ROWS="6" WRAP="VIRTUAL"></TEXTAREA></TD></TR>
	</c:otherwise>
	</c:choose>
</TABLE>
	<INPUT TYPE="Submit"></DIV>
	</FORM>
</c:if>

<%@ include file="footer.jsp"%>