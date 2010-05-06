<%@page import="java.util.*,java.lang.Math,strategiclibrary.service.webaction.beans.*,strategiclibrary.service.webaction.*" %>
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
		"bkd_clone.jpg", "bkd_gennyfalls.jpg", "bkd_salsa.jpg", 
		"bkd_landscape2.jpg", "bkd_landscape3.jpg", "bkd_lily5.jpg",
		"bkd_scan06.jpg", "bkd_sculp1.jpg", "bkd_sodgom.jpg"
	};
	int randIndex = (int)Math.round(Math.random()*(randBkd.length - 1));
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Welcome to the Image Library</title>
<link rel="stylesheet" type="text/css" href="splash-style.css"/>
<script language="javascript" src="../../lazerweb/javascript/fix_png.js" type="text/javascript"></script>

	<!--<script src="/javascripts/global.js" type="text/javascript"></script>-->
	<script src="../../lazerweb/javascript/prototype.js" type="text/javascript"></script>
	<script src="../../lazerweb/javascript/scriptaculous.js?load=effects" type="text/javascript"></script>

<script type="text/JavaScript" language="javascript">
<!--

function validate_terms() {
	if(!document.form.agree.checked) {
		alert('You must agree to the term of the Usage Agreement in order to use this site.');
		return false;
	}
}
//-->
</script>

</head>
<!--  onload="new Effect.Fade('usageAgreement')" -->
<body>

<div id='wrapper'>
<table id='mainFrame' cellpadding="0" cellspacing="0">
  <tr>
  	<td class='midColor' style='width: 300px;'>&nbsp;</td>
  	<td class='midColor' style='width: 655px;'>
  		<div id='poweredBy'>Powered by LazerWeb&trade;</div>
  	</td>
  </tr>
  <tr>
  	<td id='mastHead' colspan='2'>
		<img src='images/fineart_logo.gif' style='padding: 10px 20px 10px 0; width: 200px; height: 50px;' alt="Fine Art Library from Lazer Inc." />
		
	</td>
  </tr>
  <tr>
  <td id="mainContent" style=''>
		<h2 style="color: #996; padding: 8px 35px; text-align: center;">Welcome to the Image Library</h2>
		<!-- LOGIN FORM AREA -->
		<div id='loginBlock' class=''>
			<form name="form" action="/lazerweb/admin.jsp" method="post" 
			  onSubmit="return validate_terms();">
				<input type="hidden" name="redirectUrl" value="admin.jsp">
				<input type="hidden" name="actiona" value="action_login"/>
				<input type="hidden" name="InvalidLogInError" value="/lazerweb/fineart/index.jsp">
			<input type="hidden" name="request_history_index" value="0"/>
			<% if(session.getAttribute("errorBean") != null && ((ErrorsBean)session.getAttribute("errorBean")).getErrors().size() > 0) {%>
				<div style="color:red;">Invalid Username/Password</div>
			<% } %>
			<div style='margin-left: 7px;'>
				<div><strong style='color: #666;'>Please log in to begin:</strong></div>
			  <table cellspacing='0' style='padding: 5px; border-width: 0;'>
			  <tr>
				<td>Username:&nbsp;</td>
			
				<td><input type="text" name="request_username" style='width: 175px; background: none;'/></td>
			  </tr>
			  <tr>
				<td>Password:&nbsp;</td>
				<td><input type="password" name="request_password" style='width: 175px; background: none;'/></td>
			  </tr>
			  </table>
			  <div>
				<div style='font-size:0.9em; display: none;'>
				    <input type="checkbox" name="agree" value="yes" style='background-color: transparent;' checked/>I agree to the terms of the usage agreement
				</div>
			  </div>
			  <div style='clear:both;'>
				  <input type="submit" name="go" value="Login" style='float: right; margin: 2px 35px;' />
				  <input type="checkbox" name="remember-me" value="yes" style='background-color: transparent;'/>Remember Me
			  </div>
			  	<div style='display:none; clear:both; margin: 2px 0 6px 0; padding: 4px; border:1px solid #600; color:#600; text-align:center; font-weight:800; width: 150px;' 
			  		onmouseover="new Effect.BlindDown('usageAgreement',1)">Show Usage Agreement</div>
			</div>
			
			</form>
		  <hr />
		  <div style='margin-left: 7px;'>
		  <p><b>Forgot your password? ...</b><br/>
			<a class="linkRichColor" href="../password_help/index.jsp?actiona=mark_calling_page">Click here</a> to request an email reminder.</p>
		
		  <p><b>Don't have access? ...</b><br/>
			<a class="linkRichColor" href="register.jsp">Click here</a> to request an account.</p>
			
			<div style='bookmarkAlert'>Please ensure that all bookmarks go
				to <span class="linkRichColor">http://fineart.scanbank.com</span></div>
		  </div>
		</div>
	</td><!-- END of MAIN CONTENT -->
	<td style="background-image:url(bkds/<%= randBkd[randIndex] %>); background-repeat: no-repeat;">&nbsp;
	
	<!-- ********* USAGE AGREEMENT ********* -->
		<div id='usageAgreement' style='display:none; margin: 20px auto; padding 20px; max-width: 550px; font-size:0.8em; background-color:#FFF; opacity:0.9;' onclick="new Effect.BlindUp(this)">
		  <div style='padding: 12px;'>
			<h1 align='center' style='margin-top:0;'>Permission to Reprint Images</h1>
			
			<p>Fine Art provides access to an image database to distributors and other companies with which 
			Fine Art conducts business for the purpose of furnishing photographs and other images useful for 
			marketing Fine Art products.  The photographs and images are subject to US copyright law and provisions 
			of international copyright treaties.  Fine Art grants license rights to use the photographs and images in the 
			database, subject to the following conditions.  You are indicating your agreement to these conditions if you use 
			your password to enter the database and download a photograph or image. If you do not agree to these conditions, 
			you should 
			<a href='mailto:webmaster@lazerinc.com?subject=I_do_not_agree_with_the terms_for_Library_Image_Usage&body=Please_deactive_my_account:'>
			notify Fine Art and your password will be inactivated
			</a>.
			</p>
			
			<p>Conditions to use of photographs and images in the Fine Art database:
			</p>
				<ol style='font-size:0.9em;'>
					<li>The password issued to you will be used only by personnel of your company for the purposes described in item 2 below; the password will not be disclosed to any third parties.</li>
					<li>Use of the photographs and images is restricted to marketing Fine Art products for sale.  You will verify and ensure that any photograph or image you utilize depicts the model or version of the Fine Art product to be sold by you in connection with the marketing piece that incorporates the photograph or image.</li>
					<li>You will not alter the photograph or image without first obtaining written consent from Lazer Inc.</li>
					<li>You will acknowledge the Fine Art copyright in the photograph or image on the page or screen on which the photograph appears, in a footnote or other suitable location.  A suggested form of acknowledgment is:  Photo is &copy; Artist Name, Fine Art Library, Lazer Inc., All Rights Reserved.</li>
				</ol>
			<p>Reprinting or use of a photograph from the Fine Art database constitutes your agreement to the foregoing conditions.
			</p>
				
				<div style='width:100px; margin: 0 auto; padding: 6px auto; border:1px solid #006; color:#006; text-align:center; font-weight:800;' onclick="new Effect.Fade('usageAgreement')">Close Usage Agreement</div>

		  </div>
		</div>
	<!-- ********* endOf USAGE AGREEMENT ********* -->
	</td>
  </tr>
  <tr>
  	<td id='footer' class='center midBlue' colspan='2'>&copy; 2008 Lazer Inc.</td>
  </tr>
</table><!-- END of MAINFRAME -->
</div><!-- END of Wrapper -->
<div id='wrapper_base'>&nbsp;</div>

</body>
</html>
