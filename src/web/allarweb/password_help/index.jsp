<%@ include file="/include/global.inc" %>
<act:executeSession/>
<vel:velocity>

<html>
<head>
<LINK REL="STYLESHEET" TYPE="text/css" HREF="/${coinjemaContext.name}request_style.css" media="screen">
	<title>Login Help</title>
</head>

<body>
	#if($configuration.logoTop)
		<div id="top_logo"><img src="/lazerweb/$configuration.logoTop"></div>
	#end
		<div id="page">#messages()

<h3>NEED HELP LOGGING IN TO AN ON-LINE LIBRARY?</h3>
<div id="request">Have you already used the library, but forgot your password?
	Fill out the following form and we will e-mail your password to you as soon 
            as possible. 
            <form action="send_password.redirect" method="POST">
				<input type="hidden" name="redirectUrl" value="$callingUrl">
				<input type="hidden" name="actiond" value="action_password_request">
				<input type="hidden" name="IncompleteUserInfoError" value="/lazerweb/password_help/index.jsp">
				<input type="hidden" name="IncompleteUserInfoError_msg" value="Sorry, could not find a valid user with the information provided.">
              
              <table class="form_table" cellspacing=6 cellpadding=0 border=0 align="center">
                <tr valign="TOP"> 
                  <td> 
                    <h4>Username:</h4>
                  </td>
                  <td> 
                    <input type="Text" name="username" size="25">
                  </td>
                </tr>
				<tr valign="TOP"><td colspan="2" alig="center"><h3>OR</h3></td></tr>
                <tr valign="TOP"> 
                  <td> 
                    <h4>E-mail:</h4>
                  </td>
                  <td> 
                    <input type="Text" name="request_email" size="25">
                  </td>
                </tr>
				<tr><td colspan="2" align="center"><input type="Submit" name="go" value="Submit"></td></tr>
              </table>
            </form>
</div>
 <div class="notes"><b>NOTES:</b> 
            <ul>
              <li> For security purposes, your password will be forwarded to e-mail 
                address that we have on file. This will happen through an auto-reply 
                system, and should be almost instantaneous.</li>
              <li>If you have not entered your password in our system prior, your 
                identity must be identified by the site administrator prior to 
                the release of your password.</li>
              <li>If you have not been granted access to the online library and 
                wish to, please contact the site administrator.</li>
            </ul></div>
</div>
</body>
</html>

</vel:velocity>