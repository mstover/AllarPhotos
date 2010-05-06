<%
/*
Initially started 08.17.01

- Will want to get a placeholder for the site in which this request was 
directed from in order to get back to that site. This utility, should be able
to handle a request from any of the sites.
*/

String bounceBack = request.getParameter("goBack");
if(null != bounceBack){
	session.putValue("bounceBack", bounceBack);
}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
	<title>Login Help</title>
</head>

<body bgcolor="#FFFFFF" align="left" valign="top"
	leftmargin="20" marginwidth="0" topmargin="20" marginheight="0" rightmargin="0">

  <table width="300" bgcolor="#CCCCCC" cellspacing="6">
  	<tr colspan="6">
	  <td ALIGN="center">
		<H3>NEED HELP LOGGING IN?</H3>
	  </td>
	<tr>
	  <td>Have you already used the library, but forgot your password? Fill out the 
		following form and we will email you your password as soon as possible.<BR>
	  </td>
	</tr> 
	<tr>
	  <td bgcolor="#999999"><BR>
		<form action="loginhelp_action.jsp" method="POST">
		<input type="hidden" name="recipient" value="tomc@lazerinc.com">
		<input type="hidden" name="subject" value="Password Help Requested (Form)">
        <table cellspacing=6 cellpadding=0 border=0>
		  <tr valign="TOP"> 
			<td>
			  <h4>Name:</h4>
			</td>
			<td>
			  <input type="Text" name="name" size="25">
			</td>
		  </tr>
		  <tr valign="TOP"> 
			<td>
			  <h4>Username:</h4>
			</td>
			<td>
			  <input type="Text" name="username" size="25">
			</td>
		  </tr>
		  <tr valign="TOP"> 
			<td>
			  <h4>Email:</h4>
			</td>
			<td>
			  <input type="Text" name="email" size="25">
			</td>
		  </tr>
		  <tr valign="TOP"> 
			<td>
			  <h4>Phone Number:</h4>
			</td>
			<td>
			  <input type="Text" name="phone" size="25">
			</td>
		  </tr>
		</table>
		  <div align="center"><input type="Submit" name="go" value="Submit"></div>
        </form>
	  </td>
	</tr>
	<tr valign="TOP"> 
	  <td colspan="2">
	  	NOTE: Your password will be forwarded to email address that we have on file. 
		This is for security reasons. If you have not entered your password in our 
		system prior, your identity must be identified by the site administrator 
		prior to the release of your password.</td>
	</tr> 
  </table>
</body>
</html>
