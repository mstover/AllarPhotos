<%@ include file="/zXZx-LIBRARY-xZXz/include/global.inc" %>
<%@ include file="/zXZx-LIBRARY-xZXz/include/actionsAndValidate.inc" %>
<jsp:useBean id="user" scope="session" class="com.allarphoto.client.beans.UserBean" />
<jsp:useBean id="cart" scope="session" class="com.allarphoto.client.beans.ShoppingCartBean" />
<%	
	addParams.put(RequestConstants.REQUEST_ACTION+"x",ActionConstants.ACTION_CHECK_USER_INFO); 
	addParams.put("IncompleteUserInformationException","edit_user_info.jsp?badInfo=true"); 
	String title = "Check User Information",
		banner="verify",
		help="verify";		
%>
<jsp:useBean id="data" scope="session" class="com.allarphoto.client.beans.GenericDataBean" />

<%@ include file="header.txt"%>
<div align="center">
<h3>Please verify your user information:</h3>
<table><tr valign="TOP"><td align="right"><b>Name:</b>&nbsp;&nbsp;</td>
<td><c:out value="${user.firstName}"/>
	<c:if test='${user.middleInitial != "N/A"}'>
		<c:out value="${user.middleInitial}"/>
	</c:if>
	<c:out value="${user.lastName}"/></td></tr>
<tr valign="TOP"><td align="right"><b>Email Address:</b>&nbsp;&nbsp;</td><td><c:out value="${user.emailAddress}"/></td></tr>
<tr valign="TOP"><td align="right"><b>Phone:</b>&nbsp;&nbsp;</td><td><c:out value="${user.phone}"/></td></tr><TR>
<tr valign="TOP"><td align="right"><b>Company:</b>&nbsp;&nbsp;</td><td><c:out value="${user.company.name}"/></td></tr>
<tr valign="TOP"><td align="center" colspan="2"><form action="edit_user_info.jsp"><input class='btnBkd' border="0" type="submit" value="Edit User Information"></form></td></tr>
</table>
<laz:if object="cart" method="hasInstruction" arguments="ordering">
	<form action="edit_user_info_action.jsp" method="post">
	<INPUT TYPE="hidden" NAME="name" VALUE='<c:out value="${user.company.name}"/>'>
	<table align="center">
	<TR><TD COLSPAN="2" align="center"><H3>Shipping Address for this Order:</H3></TD></TR>
	<TR valign="top"><TD COLSPAN="2" align="center">Items in <b>bold</b> are required.</TD></TR>
	<TR><td align="right"><b>Company:</b>&nbsp;&nbsp;</TD><TD><INPUT TYPE="Text" NAME="industry" VALUE='<c:out value="${user.company.industry}"/>' SIZE="25"></TD></TR>
	<TR><td align="right">ATTN:</TD><TD><INPUT TYPE="Text" NAME="referrer" VALUE='<c:out value="${user.referrer}"/>' SIZE="25"></TD></TR>
	<TR><td align="right">Phone:</TD><TD><INPUT TYPE="Text" NAME="fax" VALUE='<c:out value="${user.fax}"/>' SIZE="25"></TD></TR>
	<TR><td align="right"><b>Address Line 1:</b>&nbsp;&nbsp;</TD><TD><INPUT TYPE="Text" NAME="shipAddress1" VALUE='<c:out value="${user.shipAddress1}"/>' SIZE="25"></TD></TR>
	<TR><td align="right">Address Line 2:&nbsp;&nbsp;</TD><TD><INPUT TYPE="Text" NAME="shipAddress2" VALUE='<c:out value="${user.shipAddress2}"/>' SIZE="25"></TD></TR>
	<TR><td align="right"><b>City:</b>&nbsp;&nbsp;</TD><TD><INPUT TYPE="Text" NAME="shipCity" VALUE='<c:out value="${user.shipCity}"/>' SIZE="25"></SELECT></TD></TR>
	<TR><td align="right"><b>State:</b>&nbsp;&nbsp;</TD><TD><INPUT TYPE="hidden" NAME="shipState_text" value=""><SELECT NAME="shipState">
	<c:forEach var="state" items="${data.states}">
		<c:choose>
			<c:when test="${state == user.shipState}">
				<OPTION VALUE='<c:out value="${state}"/>' SELECTED><c:out value="${state}"/>
			</c:when>
			<c:otherwise>
				<OPTION VALUE='<c:out value="${state}"/>'><c:out value="${state}"/>
			</c:otherwise>
		</c:choose>
	</c:forEach>
	</SELECT></TD></TR>
	<TR><td align="right"><b>Zip:</b>&nbsp;&nbsp;</TD><TD><INPUT TYPE="Text" NAME="shipZip" VALUE='<c:out value="${user.shipZip}"/>' SIZE="25"></TD></TR>
	<TR><td align="right"><b>Country:</b>&nbsp;&nbsp;</TD><TD><INPUT TYPE="hidden" NAME="shipCountry_text" value=""><SELECT NAME="shipCountry">
	<c:forEach var="country" items="${data.countries}">
		<c:choose>
			<c:when test="${country == user.shipCountry}">
				<OPTION VALUE='<c:out value="${country}"/>' SELECTED><c:out value="${country}"/>
			</c:when>
			<c:otherwise>
				<OPTION VALUE='<c:out value="${country}"/>'><c:out value="${country}"/>
			</c:otherwise>
		</c:choose>
	</c:forEach>
	</SELECT></TD></TR>
	
	</table>
	<input class='btnBkd' type="submit" name="submit" value="Submit New Address">
	</form>
</laz:if>
  <div style='margin-top: 10px;'><form action="verify.jsp">
	<input class='btnBkd' type="submit" border="0" value="Continue with Checkout"></form>
  </div>
</div>

<%@ include file="footer.jsp"%>