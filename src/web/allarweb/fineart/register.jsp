<%@page import="java.util.*,java.lang.Math,strategiclibrary.service.webaction.beans.*,strategiclibrary.service.webaction.*,com.lazerinc.beans.*" %>
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
	String myTitle = "Welch Allyn Online Image Library Access Request";

	String[] randBkd = {
		"search.jpg", "download.jpg", "browse.jpg"
	};
	String[] randBlockPos = {
		"UL"
	};
	int randIndex = (int)Math.round(Math.random()*(randBkd.length - 1));
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title><%= myTitle %></title>
<link rel="stylesheet" type="text/css" href="splash-style.css"/>
<script language="javascript" src="../../lazerweb/javascript/fix_png.js" type="text/javascript"></script>

	<!--<script src="/javascripts/global.js" type="text/javascript"></script>-->
	<script src="../../lazerweb/javascript/prototype.js" type="text/javascript"></script>
	<script src="../../lazerweb/javascript/scriptaculous.js?load=effects" type="text/javascript"></script>

<script type="text/JavaScript" language="javascript">
<!--
function MM_findObj(n, d) {
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_validateForm() { 
  var i,p,q,nm,test,num,min,max,errors='',args=MM_validateForm.arguments;
  for (i=0; i<(args.length-2); i+=3) { test=args[i+2]; 
  	val=MM_findObj(args[i]); errMsg=args[i+1];
    if (val) { nm=val.name; if ((val=val.value)!="") {
      if (test.indexOf('isEmail')!=-1) { p=val.indexOf('@');
        if (p<1 || p==(val.length-1)) {
        	if(errMsg.length > 1) {
        		errors+='- '+errMsg+'\n';
        	}else{
        	 	errors+='- '+nm+' must contain an e-mail address.\n';
        	}
        }
      } else if (test!='R') { num = parseFloat(val);
        if (isNaN(val)) errors+='- '+nm+' must contain a number.\n';
        if (test.indexOf('inRange') != -1) { p=test.indexOf(':');
          min=test.substring(8,p); max=test.substring(p+1);
          if (num<min || max<num) errors+='- '+nm+' must contain a number between '+min+' and '+max+'.\n';
    } } } else if (test.charAt(0) == 'R'){
        	if(errMsg.length > 1) {
        		errors+='- '+errMsg+'\n';
        	}else{ errors += '- '+nm+' is required.\n'; }}}
  } if (errors) alert('The following error(s) occurred:\n'+errors);
  document.MM_returnValue = (errors == '');
}
function validate_access_request() {
	MM_validateForm(
		'firstName','Please enter your first name.','R',
		'lastName','Please enter your last name.','R',
		'email','Please enter your email address \n    and double-check that it is valid.','RisEmail',
		'phone','Please enter your phone number \n    (with country code if outside of the US or Canada).','R',
		'company','Please enter your company.','R',
		'address1','Please enter your street address.','R',
		'city','Please enter your city.','R',
		'state','Please enter your state or province.','R',
		'country','Please enter your country.','R',
		'zip','Please enter your postal code.','R',
		'reason','Please enter your reason for requesting access to the online library.','R'
	);
	return document.MM_returnValue;
}
//-->
</script>

</head>

<body>

<div id='wrapper'>
<table id='mainFrame' cellpadding="0" cellspacing="0">
  <tr>
  	<td class='midBlue' style='width: 300px;'>&nbsp;</td>
  	<td class='ltBlue' style='width: 655px;'>
  		<div id='poweredBy'>Powered by LazerWeb&trade;</div>
  	</td>
  </tr>
  <tr>
  	<td id='mastHead' colspan='2'>
		<img src='images/wa-logo.gif' style='padding: 10px 20px 10px 0; width: 200px; height: 40px;' alt="Welch Allyn Inc." />
		
	</td>
  </tr>
  <tr>
  <td id="mainContent" style=''>
<!-- ********* -->
		<!-- LOGIN FORM AREA -->
		
		<div id='msgBlock' style=''>
		
		  <p><b>Register for access...</b><br/>
			 <form action="index.jsp" method="POST" onSubmit="return validate_access_request();">
				<input type="hidden" name="IncompleteUserInfoError_msg" value="You must provide either a username of a valid user or your name and email address">
				<input type="hidden" name="IncompleteUserInfoError" value="ignore=true">
				<input type="hidden" name="IncompleteInfoError_msg" value="Please fill in all required fields">
				<input type="hidden" name="IncompleteInfoError" value="request.jsp">
				<input type="hidden" name="actiond" value="action_user_request">
				<!-- <input type="hidden" name="NotAuthorizedError" value="register-extra.jsp"> -->
				<input type="hidden" name="library" value="fineart">
              
              <table class="form_table" cellspacing=6 cellpadding=0 border=0 align="center">
						<tr><td colspan="2">New Account Request (all fields required):</td></tr>
						<tr valign="top">
							<td class='input_label'>First Name:</td>
							<td><input type="text" name="firstName" size="22"></td>
						</tr>
						<tr valign="top">
							<td class='input_label'>Last Name:</td>
							<td><input type="text" name="lastName" size="22"></td>
						</tr>
						<tr valign="top">
							<td class='input_label'>Email:</td>
							<td><input type="text" name="email" size="22"></td>
						</tr>
						<tr valign="top">
							<td colspan="2" style="font-weight:bold;">Please provide some additional information:</td>
						</tr>
						<tr valign="top">
							<td class='input_label'>Phone:</td>
							<td><input type="text" name="phone" size="22"></td>
						</tr>
						<tr valign="top">
							<td class='input_label'>Company:</td>
							<td><input type="text" name="company"></td>
						</tr>
						<tr valign="top">
							<td class='input_label'>Address Line 1:</td>
							<td><input type="text" name="address1" size="22"></td>
						</tr>
						<tr valign="top">
							<td class='input_label'>Address Line 2:</td>
							<td><input type="text" name="address2" size="22"></td>
						</tr>
						<tr valign="top">
							<td class='input_label'>City:</td>
							<td><input type="text" name="city" size="22"></td>
						</tr>
						<tr valign="top">
							<td class='input_label'>State/Province:</td>
							<td><vel:velocity><SELECT NAME="state">
    #foreach($state in $data.states)
    		
    			<OPTION class='pinched_select' VALUE='$!state.name'>$!state.name</OPTION>
    		
    	#end
    </SELECT></vel:velocity></td>
						</tr>
						<tr valign="top">
							<td class='input_label'>Country:</td>
							<td><SELECT NAME="country">
<vel:velocity>$data
    #foreach($country in $data.countries)
    		
    			<OPTION class='pinched_select' VALUE='$!country.name'>$!country.name</OPTION>
    		
    	#end
</vel:velocity>
    </SELECT></td>
						</tr>
						<tr valign="top">
							<td class='input_label'>Postal Code (Zip):</td>
							<td><input type="text" name="zip" size="22"></td>
						</tr>
						<tr valign="top">
							<td class='input_label'>Reason for request:</td>
							<td><textarea name="reason" cols="22" rows="3"></textarea></td>
						</tr>
				<tr><td colspan="2" align="center"><input type="Submit" name="go" value="Submit"></td></tr>
              </table>
            </form></p>
		</div>

<!-- ********* -->
	</td><!-- END of MAIN CONTENT -->
	<td style="background-image:url(bkds/<%= randBkd[randIndex] %>); background-repeat: no-repeat;">&nbsp;</td>
  </tr>
  <tr>
  	<td id='footer' class='center midBlue' colspan='2'>&copy; 2007 Welch Allyn</td>
  </tr>
</table><!-- END of MAINFRAME -->
</div><!-- END of Wrapper -->
<div id='wrapper_base'>&nbsp;</div>

</body>
</html>
