<%@ include file="/include/global.inc" %>
<act:executeActions/>
<vel:velocity>
#macro(mainContent)
	<!-- PASSWORD FORM AREA -->
<div style='margin: 10px auto;'>
	<p><b>Password Request:</b>
	<p>Please enter your username and email address below.
		<div>Your password will be sent to you via email.</div>
	<form action="index.jsp" method="post">
		<input type="hidden" name="actiona" value="action_password_request">
		<input type="hidden" name="IncompleteUserInfoError_msg" value="The username you supplied does not exist in our records. Please try again.">
        <input type="hidden" name="EmailFailureError" value="There was an error sending your confirmation. Please contact the system administrator.">
		<div style='text-align: left; margin-left: 25px; margin-top: 12px;'><div style='float: left;
	width: 120px; padding-right: 6px; text-align: right; font-weight: bolder;'>Username:</div>
			<INPUT TYPE="Text" NAME="username" SIZE="25"></div>
	<div style='text-align: left; margin-left: 25px; margin-top: 12px;'><div style='float: left;
	width: 120px; padding-right: 6px; text-align: right; font-weight: bolder;'>Email Address:</div>
			<INPUT TYPE="Text" NAME="email" SIZE="25"></div>
		<div style='clear: both; margin-top: 12px;'><INPUT class='btnBkd' TYPE="Submit" NAME="go" VALUE="Get Password"></div>
	</form>
</div> 

#midsplit()

<!-- IF Not Active !!! -->
<table class='buttonTable' cellspacing='0'>
	<TR>
		<TD COLSPAN=2><IMG name='barelythere' SRC="buttons/images/buttons_0706-0_01.jpg" WIDTH=307 HEIGHT=56 ALT=""></TD>
	</tr>
	<TR>
		<TD><IMG name='playtex' SRC="buttons/images/buttons_0706-0_02.jpg" WIDTH=159 HEIGHT=74 ALT=""></TD>
		<TD><IMG name='bali' SRC="buttons/images/buttons_0706-0_03.jpg" WIDTH=148 HEIGHT=74 ALT=""></TD>
	</TR>
	<TR>
		<TD><IMG name='jms' SRC="buttons/images/buttons_0706-0_04.jpg" WIDTH=159 HEIGHT=131 ALT=""></TD>
		<TD><IMG name='hanes' SRC="buttons/images/buttons_0706-0_05.jpg" WIDTH=148 HEIGHT=131 ALT=""></TD>
	</TR>
	<TR>
		<TD name='wonderbra' COLSPAN=2><IMG SRC="buttons/images/buttons_0706-0_06.jpg" WIDTH=307 HEIGHT=50 ALT=""></TD>
	</TR>
</table>
#end
#page("Password Reminder" "mcc")

</vel:velocity>