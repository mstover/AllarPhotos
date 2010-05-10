<%@page import="java.util.*,java.lang.Math,strategiclibrary.service.webaction.beans.*,strategiclibrary.service.webaction.*,com.allarphoto.beans.*" %>
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
	String myTitle = "Welch Allyn User Access Request";
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title><%= myTitle %></title>
<link rel="stylesheet" type="text/css" href="splash-style.css"/>
</head>
<body>
<table id='mainFrame' cellpadding="0" cellspacing="0">
  <tr>
  	<td id='mastHead' class='clientColors'><img src='images/irwin-industrial-logo.gif' alt="<%= myTitle %>" style='float: left; padding-right: 6px;' />
	<img src='images/poweredby_lazerweb.gif' alt="Powered by LazerWeb" width="150" height="14" style='float: right;' />
	<div class='' style='margin-top: 12px; font-size: 1.4em; font-weight:100; padding-top: 0px;'>Hand Tools &amp; PTA Image Toolbox</div>	</td>
  </tr>
  <tr>
  <td id="mainContent" style="text-align: center;">
  	<div class='ghostedBkd' style='border: solid #F2C633; border-width: 0; padding: 25px 15px; margin: 125px auto 0 auto; width: 800px;'>
		<!-- LOGIN FORM AREA -->
		
		<div id='msgBlock' style=''>
		  <p><b>Forgot your password? ...</b><br/>
			<a href="../password_help/index.jsp?actiona=mark_calling_page">Click here</a> to request an email reminder.</p>
		
		  <p><b>Don't have access? ...</b><br/>
			 <form action="index.jsp" method="POST">
				<input type="hidden" name="IncompleteUserInfoError_msg" value="You must provide either a username of a valid user or your name and email address">
				<input type="hidden" name="IncompleteUserInfoError" value="ignore=true">
				<input type="hidden" name="IncompleteInfoError_msg" value="Please fill in all required fields">
				<input type="hidden" name="IncompleteInfoError" value="register.jsp">
				<input type="hidden" name="actiond" value="action_user_request">
				<input type="hidden" name="library" value="fineart">
              
              <table class="form_table" cellspacing=6 cellpadding=0 border=0 align="center">
						<tr><td colspan="2">New Account Request (all fields required):</td></tr>
						<tr valign="top">
							<td>First Name:</td>
							<td><input type="text" name="firstName" size="25" value="<%=((UserRequest)session.getAttribute("request")).getFirstName()%>"></td>
						</tr>
						<tr valign="top">
							<td>Last Name:</td>
							<td><input type="text" name="lastName" size="25" value="<%=((UserRequest)session.getAttribute("request")).getLastName()%>"></td>
						</tr>
						<tr valign="top">
							<td>Email:</td>
							<td><input type="text" name="email" size="25" value="<%=((UserRequest)session.getAttribute("request")).getEmail()%>"></td>
						</tr>
						<tr valign="top">
							<td colspan="2" style="font-weight:bold;">Please provide some additional information:</td>
						</tr>
						<tr valign="top">
							<td>Phone:</td>
							<td><input type="text" name="phone" size="25"></td>
						</tr>
						<tr valign="top">
							<td>Address Line 1:</td>
							<td><input type="text" name="address1" size="25"></td>
						</tr>
						<tr valign="top">
							<td>Address Line 2:</td>
							<td><input type="text" name="address2" size="25"></td>
						</tr>
						<tr valign="top">
							<td>City:</td>
							<td><input type="text" name="city" size="25"></td>
						</tr>
						<tr valign="top">
							<td>State/Province:</td>
							<td><vel:velocity><SELECT NAME="state">
    #foreach($state in $data.states)
    		
    			<OPTION VALUE='$!state.name'>$!state.name</OPTION>
    		
    	#end
    </SELECT></vel:velocity></td>
						</tr>
						<tr valign="top">
							<td>Country:</td>
							<td><SELECT NAME="country">
<vel:velocity>$data
    #foreach($country in $data.countries)
    		
    			<OPTION VALUE='$!country.name'>$!country.name</OPTION>
    		
    	#end
</vel:velocity>
    </SELECT></td>
						</tr>
						<tr valign="top">
							<td>Postal Code (Zip):</td>
							<td><input type="text" name="zip" size="25"></td>
						</tr>
						<tr valign="top">
							<td>Reason for request:</td>
							<td><textarea name="reason" cols="25" rows="5"></textarea></td>
						</tr>
						<tr valign="top">
							<td>Company:</td>
							<td><input type="text" name="company"></td>
						</tr>
				<tr><td colspan="2" align="center"><input type="Submit" name="go" value="Submit"></td></tr>
              </table>
            </form></p>
		</div>
	</div>  </td><!-- END of MAIN CONTENT -->
  </tr>
  <tr>
  	<td id='footer' class='center'>&copy; 2007 Welch Allyn.</td>
  </tr>
</table>
<!-- END of MAINFRAME -->
</body>
</html>
