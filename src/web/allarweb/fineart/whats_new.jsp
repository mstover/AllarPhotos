<%@page import="java.lang.Math,strategiclibrary.service.webaction.beans.*" %>
<%@ taglib prefix="act" uri="/WEB-INF/webacttags.tld" %>
<%@ taglib uri="/WEB-INF/veltag.tld" prefix="vel" %>
<%@taglib uri="http://www.lazerinc-image.com/" prefix="laz" %>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<act:request action="action_validate_login" order="1"/>
<act:onError error="LoginCookieExists">../admin.jsp</act:onError>
<act:executeActions/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<%
	String[] randBkd = {
		"stethoscope.jpg"
	};
	String[] randBlockPos = {
		"UL"
	};
	int randIndex = (int)Math.round(Math.random()*(randBkd.length - 1));
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Welcome to the Image Library - What's New and Help Section</title>
<link rel="stylesheet" type="text/css" href="splash-style.css"/>
<script language="javascript" src="../../lazerweb/javascript/fix_png.js" type="text/javascript"></script>
</head>

<body>

<table id='mainFrame' cellpadding="0" cellspacing="0">
  <tr>
  	<td class='midBlue' style='width: 300px;'>&nbsp;</td>
  	<td class='ltBlue' style='width: 655px;'>
  		<div id='poweredBy'>Powered by LazerWeb&trade;</div>
  	</td>
  </tr>
  <tr>
  	<td id='mastHead' colspan='2'>
		<img src='images/wa-logo.gif' style='padding: 10px 20px; width: 185px; height: 40px;' alt="Welch Allyn Inc." />
	</td>
  </tr>
  <tr>
  <td id="mainContent" style=''>
		<h2 style="color: #6f9abd; padding: 8px 35px; text-align: center;">Welcome to the Image Library</h2>
		<!-- LOGIN FORM AREA -->
		<div id='' class=''>
			<h3>USER TIPS</h3>
			<UL id='sideTips'>
				<li>For Powerpoint Presentations and Web Imagery please download Low Resolution 
				images and graphics. Should you need higher resolution graphics at a later date,
				the images can be easily found by returning to the library.</li>
				<li>Please click buttons only once and allow the system to process your request.  
				This could take several seconds depending on your connection speed and the size 
				of your request.  Errors may occur if processes are interrupted by hitting your 
				"Back" button on your browser or trying to leave the page 
				before your request is complete.</li>
				<li>Orders delivered in the United States are usually received within one to two 
				business days.  International deliveries may take three to four business days.</li>
				<li>Not all model versions of each product have an associated image. In many cases, 
				the main product image should suffice, as extensions relate to battery type 
				(non-rechargable vs. rechargable) which would not be visible in an image. Try 
				your search using the first four digits of the model number.
			</UL>
		</div>
	</td><!-- END of MAIN CONTENT -->
	<td class='ltBlue' style="">
	  <div style='padding: 0 15px;'><!-- BEGIN PADDED CONTENT -->
		<h2>What's New to the Image Library</h2>
		<p>INTRODUCING the addition of our Product Reference Guide Library! 
		Click the link below to be directed to a second library featuring all 
		of the images found in our Product Reference Guide.
		</p>
		
		<div align="center" style='width: 250px;'>
			<form action='../admin.jsp' method='post'>
				<input type="submit" name="submit" value="Continue to the Image Library">
			</form>
		</div>
	  </div><!-- END OF PADDED CONTENT -->
	</td>
  </tr>
  <tr>
  	<td id='footer' class='center midBlue' colspan='2'>
		&copy; 2003-2007 Welch Allyn Inc., All Rights Reserved
	</td>
  </tr>
</table><!-- END of MAINFRAME -->

</body>
</html>
