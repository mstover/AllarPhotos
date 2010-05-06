<%@page import="java.lang.Math,strategiclibrary.service.webaction.beans.*" %>
<%@ taglib prefix="act" uri="/WEB-INF/webacttags.tld" %>
<%@ taglib uri="/WEB-INF/veltag.tld" prefix="vel" %>
<%@taglib uri="http://www.lazerinc-image.com/" prefix="laz" %>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<act:request action="check_for_login_cookie"/>
<act:onError error="LoginCookieExists">../admin.jsp</act:onError>
<act:executeActions/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<%
	String[] randBkd = {
		"KS9622.jpg"
	};
	String[] randBlockPos = {
		"UL"
	};
	int randIndex = (int)Math.round(Math.random()*(randBkd.length - 1));
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>YIHL Splash Mockup</title>
<link rel="stylesheet" type="text/css" href="splash-style.css"/>
</head>

<body>

<table id='mainFrame' cellpadding="0" cellspacing="0">
  <tr>
  	<td id='mastHead'>
		<!--img src='images/edu_demo-logo-tag.jpg' style='float: right;' alt="" /--><img src='images/EDU-logo.gif' style='' alt="" />
	</td>
  </tr>
  <tr>
  <td id="mainContent" style="background-image:url(bkds/<%= randBkd[randIndex] %>);">
	
		<div class='ghostedBkd' style="filter: alpha(opacity=90); text-align: left; line-height: 150%; font-size: 1em;">&nbsp;Welcome to Your Institute of Higher Learning Online Photo Library</div>
		<!-- LOGIN FORM AREA -->
		<div id='loginBlock' class='floating<%= randBlockPos[randIndex] %> ghostedBkd'>
			<form action="/lazerweb/admin.jsp" method="post">
				<input type="hidden" name="redirectUrl" value="admin.jsp">
				<input type="hidden" name="actiona" value="action_login"/>
				<input type="hidden" name="InvalidLogInError" value="edu_demo/index.jsp">
			<input type="hidden" name="request_history_index" value="0"/>
			<% if(session.getAttribute("errorBean") != null && ((ErrorsBean)session.getAttribute("errorBean")).getErrors().size() > 0) {%>
				<div style="color:red;">Invalid Username/Password</div>
				<% } %>
			<div><strong>Please Login:</strong></div>
			<div style='margin-left: 7px;'>
			  <table cellspacing='0' style='padding: 5px; border-width: 0;'>
			  <tr>
				<td>Username:&nbsp;</td>
			
				<td><input type="text" name="request_username" style='width: 175px; background: none;'/></td>
			  </tr>
			  <tr>
				<td>Password:&nbsp;</td>
				<td><input type="password" name="request_password" style='width: 175px; background: none;'/></td>
			  </tr>
			  </table><input type="checkbox" name="remember-me" value="yes" style='background-color: transparent;'/>Remember Me 
			  <input type="submit" name="go" value="Login"/>
			  </div>
			
			</form>
		  <div style='margin-left: 7px;'>
		  <p><b>Forgot your password? ...</b><br/>
			<a href="../password_help/index.jsp?actiona=mark_calling_page">Click here</a> to request an email reminder.</p>
		
		  <p><b>Don't have access? ...</b><br/>
			<a href="../password_help/request.jsp?actiona=mark_calling_page">Click here</a> to request an account.</p>
		  </div>
		</div>
	</td><!-- END of MAIN CONTENT -->
  </tr>
  <tr>
  	<td id='footer' class='center'>
		LazerWeb&trade; All Rights Reserved - &copy; 1998-2008 Lazer Incorporated.
	</td>
  </tr>
</table><!-- END of MAINFRAME -->

</body>
</html>
