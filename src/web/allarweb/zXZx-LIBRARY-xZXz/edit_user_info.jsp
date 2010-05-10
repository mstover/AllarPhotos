<%@ include file="/zXZx-LIBRARY-xZXz/include/global.inc" %>
<%@ include file="/zXZx-LIBRARY-xZXz/include/actionsAndValidate.inc" %>
<jsp:useBean id="user" scope="session" class="com.allarphoto.client.beans.UserBean" />
<jsp:useBean id="data" scope="session" class="com.allarphoto.client.beans.GenericDataBean" />

<c:set var='title' value='${title} - Edit User Info' />
<c:set var='help' value="edit_user" />
<% // **** NEED TO GET THIS PROPERLY FORMATTED ****
	String help = "edit_user";
%>
<%@ include file="header.txt"%>

<FORM action="check_user.jsp" METHOD="POST">
<input type="hidden" name="<%=RequestConstants.REQUEST_ACTION%>a" value="<%=ActionConstants.ACTION_MODIFY_USER%>">
<DIV ALIGN="center">Please fill in the following information in order to proceed. <BR>
Items in <b>bold</b> are required.

<table><c:if test='${param.badInfo == "true"}'>
	<tr valign="TOP">
		<td><div class="eyecatching">You are missing some required user info.</div></td>
	</tr>
	</c:if>
<tr valign="TOP">
<td><TABLE CELLPADDING=5 CELLSPACING=5 BORDER=0 width="500" align="center">
<TR><TD width="200"><b>Username:</b></TD><TD width="300"><INPUT TYPE="Text" NAME="username" VALUE='<c:out value="${user.username}"/>' SIZE="25"></TD></TR>
<TR><TD align="right"><b>Password:</b></TD><TD><INPUT TYPE="Password" NAME="password" VALUE='<c:out value="${user.password}"/>' SIZE="25"></TD></TR>
<TR><TD align="right"><b>First Name:</b></TD><TD><INPUT TYPE="Text" NAME="firstName" VALUE='<c:out value="${user.firstName}"/>' SIZE="25"></TD></TR>
<TR><td align="right">Middle Initial:</TD><TD><INPUT TYPE="Text" NAME="middleInitial" VALUE='<c:out value="${user.middleInitial}"/>' SIZE="4"></TD></TR>
<TR><td align="right"><b>Last Name:</b></TD><TD><INPUT TYPE="Text" NAME="lastName" VALUE='<c:out value="${user.lastName}"/>' SIZE="25"></TD></TR>
<TR><td align="right"><b>Email Address:</b></TD><TD><INPUT TYPE="Text" NAME="emailAddress" VALUE='<c:out value="${user.emailAddress}"/>' SIZE="25"></TD></TR>
<TR><td align="right"><b>Phone:</b></TD><TD><INPUT TYPE="Text" NAME="phone" VALUE='<c:out value="${user.phone}"/>' SIZE="25"></TD></TR>
<TR><td align="right"><b>Company:</b></TD><TD><INPUT TYPE="Text" NAME="name" VALUE='<c:out value="${user.company.name}"/>' SIZE="25"></TD></TR>
</SELECT></TD></TR>
<TR><TD COLSPAN="2"><H3>Default Address</H3></TD></TR>
<TR><td align="right"><b>Address Line 1:</b></TD><TD><INPUT TYPE="Text" NAME="billAddress1" VALUE='<c:out value="${user.billAddress1}"/>' SIZE="25"></TD></TR>
<TR><td align="right">Address Line 2:</TD><TD><INPUT TYPE="Text" NAME="billAddress2" VALUE='<c:out value="${user.billAddress2}"/>' SIZE="25"></TD></TR>
<TR><td align="right"><b>City:</b></TD><TD><INPUT TYPE="Text" NAME="billCity" VALUE='<c:out value="${user.billCity}"/>' SIZE="25"></TD></TR>
<TR><td align="right"><b>State:</b></TD><TD><SELECT NAME="billState">
<c:forEach var="state" items="${data.states}">
		<c:choose>
			<c:when test="${state == user.billState}">
				<OPTION VALUE='<c:out value="${state}"/>' SELECTED><c:out value="${state}"/>
			</c:when>
			<c:otherwise>
				<OPTION VALUE='<c:out value="${state}"/>'><c:out value="${state}"/>
			</c:otherwise>
		</c:choose>
	</c:forEach>
</SELECT></TD></TR>
<TR><td align="right"><b>Zip:</b></TD><TD><INPUT TYPE="Text" NAME="billZip" VALUE='<c:out value="${user.billZip}"/>' SIZE="25"></TD></TR>
<TR><td align="right"><b>Country:</b></TD><TD><SELECT NAME="billCountry">
<c:forEach var="country" items="${data.countries}">
		<c:choose>
			<c:when test="${country == user.billCountry}">
				<OPTION VALUE='<c:out value="${country}"/>' SELECTED><c:out value="${country}"/>
			</c:when>
			<c:otherwise>
				<OPTION VALUE='<c:out value="${country}"/>'><c:out value="${country}"/>
			</c:otherwise>
		</c:choose>
	</c:forEach>
</SELECT></TD></TR>
</TABLE></td></tr></table>
	<c:if test='${empty user.company.industry || user.company.industry == "N/A" || user.company.industry == "null"}'>
		<INPUT TYPE="hidden" NAME="industry" value='<c:out value="${user.company.name}"/>'>
	</c:if>
<INPUT class='btnBkd' TYPE="Submit" NAME="submit" VALUE="Update User Info">
</FORM>
<%@ include file="footer.jsp"%>