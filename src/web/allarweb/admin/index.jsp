<%@ include file="/include/global.inc" %>
<act:executeActions/>

<vel:velocity>
	
#macro(mainContent)
	#if(!$user)
		<FORM METHOD="POST">
        <input type="hidden" name="actiona" value="action_login"/>
        <DIV ALIGN="center"><H3>Login:</H3>
        <TABLE CELLSPACING=0 CELLPADDING=5 BORDER=0>
        <TR VALIGN="TOP"><TD><H4>Username:</H4></TD><TD><INPUT TYPE="Text" NAME="request_username" SIZE="25"></TD></TR>
        <TR VALIGN="TOP"><TD><H4>Password:</H4></TD><TD><INPUT TYPE="Password" NAME="request_password" SIZE="25"></TD></TR>
        </TABLE>
        <INPUT TYPE="Submit" NAME="go" VALUE="Login"></DIV></FORM>
	#else
		You are already logged in - some snazzy navigational features to go here.
	#end
#end
	
	#page("Lazerweb Administration" "admin")
</vel:velocity>