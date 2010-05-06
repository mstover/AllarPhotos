<%@ include file="/include/global.inc" %>
<act:request action="get_libraries"/>
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
		<div id="page">

<h3>NEED ACCESS TO  AN ON-LINE LIBRARY?</h3>
<div id="request">Did you expect to have permission to use a library? 
	Do you not have an account with the system? 
	Fill out the following form and an admin will give you access to the library or explain why you can't be 
	given access.
            <form action="send_password.redirect" method="POST">
				<input type="hidden" name="IncompleteUserInfoError_msg" value="You must provide either a username of a valid user or your name and email address">
				<input type="hidden" name="IncompleteUserInfoError" value="ignore=true">
				<input type="hidden" name="IncompleteInfoError_msg" value="Please fill in all required fields">
				<input type="hidden" name="IncompleteInfoError" value="request.jsp">
				<input type="hidden" name="redirectUrl" value="$callingUrl">
				<input type="hidden" name="actiond" value="action_user_request">
              
              <table class="form_table" cellspacing=6 cellpadding=0 border=0 align="center">
                <tr valign="TOP"> 
                  <td> 
                    <h4>Your Username (if you have an account):</h4>
                  </td>
                  <td> 
                    <input type="Text" name="username" size="25">
                  </td>
                </tr>
                <tr valign="TOP"> 
                  <td> 
                    <h4>Library You Need Access To (required):</h4>
                  </td>
                  <td> 
                    <select name="library" size="5" multiple="true"><option value=""></option>
					#foreach($library in $libraries)
						<option value="$library.tableName">$!library.descriptiveName</option>
					#end
					</select>
                  </td>
                </tr>
                <tr valign="TOP"> 
                  <td> 
                    <h4>If you have no account:</h4>
                  </td>
                  <td> <table class="inner_table" cellspacing=6 cellpadding=0 border=0 align="center">
						<tr><td colspan="2">New Account Request (all fields required):</td></tr>
						<tr valign="top">
							<td><h4>First Name:</h4></td>
							<td><input type="text" name="firstName" size="25"></td>
						</tr>
						<tr valign="top">
							<td><h4>Last Name:</h4></td>
							<td><input type="text" name="lastName" size="25"></td>
						</tr>
						<tr valign="top">
							<td><h4>Email:</h4></td>
							<td><input type="text" name="email" size="25"></td>
						</tr>
				<tr valign="TOP"> 
                  <td> 
                    <h4>Company:</h4>
                  </td>
                  <td> 
                    <input type="Text" name="company" size="25">
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
				<tr valign="TOP"> 
                  <td> 
                    <h4>Reason for Request:</h4>
                  </td>
                  <td> 
                    <textarea rows="4" cols="25" name="reason"></textarea>
                  </td>
                </tr></table>
                  </td>
                </tr>
				<tr><td colspan="2" align="center"><input type="Submit" name="go" value="Submit"></td></tr>
              </table>
            </form>
</div>

 <table class="notes"><tr><td><b>NOTES:</b> 
            <ul>
              <li>If a new account is created for you, you will receive a generated password via email.</li>
              <li>When the request is approved or rejected, you will receive a notification email.</li>
            </ul></td></tr></table>
</div>
</body>
</html>

</vel:velocity>