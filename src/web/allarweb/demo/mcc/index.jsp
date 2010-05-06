<%@ taglib prefix="act" uri="/WEB-INF/webacttags.tld" %>
<%@ taglib uri="/WEB-INF/veltag.tld" prefix="vel" %>
<%@taglib uri="http://www.lazerinc-image.com/" prefix="laz" %>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<act:request action="check_for_login_cookie"/>
<act:onError error="LoginCookieExists">whats_new.jsp</act:onError>
<act:executeActions/>
<c:set var="noNav" value="true" />
<% 	/******* Login Page - gateway to the Online Library ********/
	response.setHeader("Cache-Control","no-store"); //HTTP 1.1
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setDateHeader ("Expires", 0); //prevents caching at theproxy server
%>
<vel:velocity>
	#macro(navigation)
		<td>&nbsp;</td></tr><tr>
	#end
	
#macro(mainContent)
<h2 class='center' style='clear: both;'>Welcome to the Monroe Community College <BR/>Digital Online Library Main Portal</h2>
	<!-- LOGIN FORM AREA -->
<div style='margin: 10px auto;'>
<FORM action="login.redirect" METHOD="POST">
	#if($preErrorUrl)
		<input type="hidden" name="redirectUrl" value="$preErrorUrl">
	#else
		<input type="hidden" name="redirectUrl" value="catsearch.jsp">
	#end
<input type="hidden" name="actiona" value="action_login"/>
<input type="hidden" name="request_history_index" value="0"/>

<div><STRONG>Please Login:</STRONG></div>
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
  </TABLE><input type="checkbox" name="remember-me" value="yes">Remember Me 
  <INPUT class='btnBkd' style='' TYPE="Submit" NAME="go" VALUE="Login">
  </div>
</FORM>
  <div style='margin-left: 7px;'>
  <p><b>Forgot your password? ...</b><br>
	<a href="password_help.jsp?actiona=mark_calling_page">Click here</a> to request an email reminder.</p>

  <p><b>Don't have access? ...</b><br>
	<a href="../../password_help/request.jsp?actiona=mark_calling_page">Click here</a> to request an account or access to a library.</p>
<!--
  <p><b>Not a registered user?</b><br>
	Request a username and password by emailing us at: 
	<a href='mailTo:webmaster@lazerinc.com'>webmaster@lazerinc.com</a></p>
-->
  </div>
</div>

#end
#page("" "mcc")



</vel:velocity>