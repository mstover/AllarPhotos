<%@ include file="/zXZx-LIBRARY-xZXz/include/global.inc" %>
<%@ include file="/zXZx-LIBRARY-xZXz/include/handleActions.inc" %>
<jsp:useBean id="errors" scope="request" class="com.lazerinc.client.beans.ErrorsBean"/>

<% 	/******* Login Page - gateway to the Online Library ********/
	// insert a session variable as a passthrough to the search page
	//session.putValue("placeholder","catsearch.jsp?historyIndex=0");
%>
<c:set var="noNav" value="true" />
<% 	/******* Login Page - gateway to the Online Library ********/

	// Variables for page title and the banner image, if necessary
	String title = "zXZx-CUSTOMER-xZXz Digital Online Library"; 
	String help = "login";
	
	// insert a session value to use in redirecting to the search page
	session.putValue("placeholder","whats_new.jsp");

	response.setHeader("Cache-Control","no-store"); //HTTP 1.1
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setDateHeader ("Expires", 0); //prevents caching at theproxy server
%>

<HTML>
<HEAD>
<TITLE>zXZx-CUSTOMER-xZXz Digital Online Library</TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=iso-8859-1">
<LINK REL="STYLESHEET" TYPE="text/css" HREF="style_zXZx-LIBRARY-xZXz.css" media='screen'>
<!-- ImageReady Slices (zXZx-LIBRARY-xZXz-splash.psd) -->
<style media='screen'>
body, td, div, p {font: 12px Verdana, Arial, sans-serif;}
td {padding: 0;}

a {color: #369; text-decoration: none; 
	font-weight: bold;}
a:hover {color: #9777C1;}

</style>

<body style='margin: 10px; background-color: #FFFFFF;'>
<TABLE style='padding: 0; width: 731px; border: 5px solid #999;' CELLSPACING=0>
	<TR>
		<TD><IMG SRC="splash/images/library-splash_01.gif" WIDTH=318 HEIGHT=103 ALT=""></TD>
		<TD><IMG SRC="splash/images/library-splash_02.gif" WIDTH=413 HEIGHT=103 ALT=""></TD>
	</TR>
	<TR>
		<TD><IMG SRC="splash/images/library-splash_03.gif" WIDTH=318 HEIGHT=92 ALT=""></TD>
		<TD><IMG SRC="splash/images/library-splash_04.gif" WIDTH=413 HEIGHT=92 ALT=""></TD>
	</TR>
	<TR>
		<TD><IMG SRC="splash/images/library-splash_05.gif" WIDTH=318 HEIGHT=73 ALT=""></TD>
		<TD><IMG SRC="splash/images/library-splash_06.gif" WIDTH=413 HEIGHT=73 ALT=""></TD>
	</TR>
	<TR>
		<TD><IMG SRC="splash/images/library-splash_07.gif" WIDTH=318 HEIGHT=81 ALT=""></TD>
		<TD><IMG SRC="splash/images/library-splash_08.gif" WIDTH=413 HEIGHT=81 ALT=""></TD>
	</TR>
	<TR>
		<TD><IMG SRC="splash/images/library-splash_09.gif" WIDTH=318 HEIGHT=188 ALT=""></TD>
		<TD><IMG SRC="splash/images/library-splash_10.gif" WIDTH=413 HEIGHT=188 ALT=""></TD>
	</TR>
	<TR>
		<TD><IMG SRC="splash/images/library-splash_11.gif" WIDTH=318 HEIGHT=41 ALT=""></TD>
		<TD><IMG SRC="splash/images/library-splash_12.gif" WIDTH=413 HEIGHT=41 ALT=""></TD>
	</TR>
</TABLE>
<!-- End ImageReady Slices -->

<!-- LOGIN FORM AREA -->
<div style='position: absolute; top: 300px; left: 26px; width: 300px; text-align: center;'>
<FORM action="whats_new.jsp" METHOD="POST">
<input type="hidden" name="actiona" value="action_login"/>
<input type="hidden" name="request_history_index" value="0"/>
<c:choose>
  <c:when test="${!empty errors.errors}">
	<h3>An Error Occurred</h3>
	<c:forEach var="err" items="${errors.errors}">
		<c:if test='${err.message == "InvalidLogInError"}'>
			<c:set var="passwordHelp" value="true"/>
		</c:if>
	</c:forEach>
	<c:choose>
		<c:when test='${passwordHelp == "true"}'>
			<div><STRONG>Your login information was incorrect.<br>
				Please try again:</STRONG></div>
		</c:when>
		<c:otherwise>
			<div><STRONG>Your session may have timed out.<br>
				Please login and try again:</STRONG></div>
		</c:otherwise>
	</c:choose>
  </c:when>
  <c:otherwise>
	<div><STRONG>Please Login:</STRONG></div>
  </c:otherwise>
</c:choose>
  <div style='margin-left: 7px;'>
  <TABLE CELLSPACING=0 style='padding: 5px; border-width: 0;'>
  <TR>
	<TD>Username:&nbsp;</TD>
	<TD><INPUT TYPE="Text" NAME="request_username" SIZE="25"></TD>
  </TR>
  <TR>
	<TD>Password:&nbsp;</TD>
	<TD><INPUT TYPE="Password" NAME="request_password" SIZE="25"></TD>
  </TR>
  </TABLE>
  <INPUT class='btnBkd' style='' TYPE="Submit" NAME="go" VALUE="Login" onClick="doPost('whats_new.jsp')">
  </div>
</FORM>
  <div style='margin-left: 7px;'>
  <p><b>Forgot your password? ...</b><br>
	<a href="password_request.jsp">Click here</a> to request an email reminder.</p>
  <p><b>Not a registered user?</b><br>
	Request a username and password by emailing us at: 
	<a href='mailTo:webmaster@lazerinc.com'>webmaster@lazerinc.com</a></p>
  </div>
</div>

</body>
</html>

	